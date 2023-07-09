import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import RegistrationCreateClient from "../api/class_update_registrationClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class RegistrationCreatePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['getClass', 'getUser', 'getUsers', 'getAttendance', 'renderClass', 'renderLogin', 'renderMenu', 'onUpdate'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new RegistrationCreateClient();
        var username = 'jacob'; //window.localStorage.getItem('username'); //searches for the userId in localStorage
        var password = '1234'; //window.localStorage.getItem('password'); //searches for the userId in localStorage
        var classId = window.localStorage.getItem('classId'); //searches for the eventId in localStorage
        this.getUsers();
        this.getAttendance();
        this.getClass(classId);
        this.getUser(username, password);
        this.dataStore.addChangeListener(this.renderMenu);
        this.dataStore.addChangeListener(this.renderLogin);
        this.dataStore.addChangeListener(this.renderClass);
        document.getElementById('update-form').addEventListener('submit', this.onUpdate);
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderClass() {
        const classRecord = this.dataStore.get("class");
        let resultInstructors = document.getElementById("instructor");
        const users = this.dataStore.get("users");
        const gymMember = this.dataStore.get("user");
        const attendees = this.dataStore.get("attendance");
        let registration_attendance = false;
        let instructorHTML = "";
        if (classRecord) {
            //Posting all the class information using the response returned by getClass
            document.getElementById('classId').value= window.localStorage.getItem('classId');
            if (gymMember)
                document.getElementById('userId').value= gymMember.userId;
            document.getElementById('name').innerHTML = `Name: ${classRecord.name}`;
            document.getElementById('description').innerHTML = `Description: ${classRecord.description}`;
            document.getElementById('classType').innerHTML = `Class Type: ${classRecord.classType}`;
            //Instructor information
            if (users) {
                for (let element of users) {
                   if (element.userId == classRecord.userId)
                            document.getElementById('instructor').innerHTML = `Instructor: ${element.firstName} ${element.lastName}`
                 }
            }
            document.getElementById('classCapacity').innerHTML = `Class Capacity: ${classRecord.classCapacity}`;
            document.getElementById('dateTime').innerHTML = `Date-Time: ${classRecord.dateTime}`;
            if (classRecord.status)
                document.getElementById('status').innerHTML = `Status: Scheduled`
            else
                document.getElementById('status').innerHTML = `Status: Cancelled`;

            //Attendance list
            let resultAttendance = document.getElementById("Attendance");
            let attendanceHTML = "";
            if (attendees) {
                for (let element of attendees){
                    if (classRecord.classId == element.classId) {
                        if (users) {
                            for (let user of users) {
                                if ((element.userId == user.userId) && (element.classAttendance == "Attending"))
                                    attendanceHTML += `<tr><td>${user.firstName} ${user.lastName}</td></tr>`;
                            }
                        }
                        if (gymMember)
                            if ((gymMember.userId == element.userId) && (element.classAttendance == "Attending"))
                                registration_attendance = true;
                    }
                }
                resultAttendance.innerHTML = attendanceHTML;
            }
            if (registration_attendance) {
               document.getElementById('active_round_yes').checked=true;
            }
            else {
                document.getElementById('active_round_no').checked=true;
            };
        }
    }

    async renderLogin() {
        let resultArea = document.getElementById("login");
        const user = this.dataStore.get("user");
        let userHTML = "";

        if (user) {
            let userType = "";
            if (user.userType == "administrator")
                userType = "(Admin)";
            if (user.userType == "instructor")
                userType = "(Instructor)";
            if (user.userType == "gymMember")
                userType = "(Gym Member)";

            userHTML += `${user.firstName} ${user.lastName} ${userType} - Log out`;
            resultArea.innerHTML = userHTML;
        } else {
            resultArea.innerHTML = "No User";
        }
    }

    async renderMenu() {
        document.getElementById("menu").innerHTML = `
                  <ul>
                    <li><a href="classes_gymmember.html">Class</a></li>
                    <li><a href="index.html" id="login"></a></li>
                  </ul>
        `;
    }


    // Class Handlers --------------------------------------------------------------------------------------------------

    async getClass(classId) {
        let result = await this.client.getClass(classId, this.errorHandler);
        this.dataStore.set("class", result);
        if (result) {
            this.showMessage(`Got Class!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async getUser(username,password) {
        let result = await this.client.getUser(username, password, this.errorHandler);
        this.dataStore.set("user", result);
        if (result) {
            this.showMessage(`Got User!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async getUsers() {
        let result = await this.client.getUsers(this.errorHandler);
        this.dataStore.set("users", result);
        if (result) {
            this.showMessage(`Got All Users!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async getAttendance() {
        let result = await this.client.getAttendance(this.errorHandler);
        this.dataStore.set("attendance", result);
        if (result) {
            this.showMessage(`Got All Users Attending!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onUpdate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("class", null);

        // Gathering the values from all the inputs in the form

        let classId = document.getElementById("classId").value;
        let userId = document.getElementById("userId").value;
        let classAttendance = "Attending";
        var activeRadioButtons = document.getElementsByName('active_round');
        let status = activeRadioButtons[0].checked; //checks the first radio button which is Yes
        if (!status)
            classAttendance = "Not Attending";
        //Submits all the information in order to update the record
        const updatedRegistration = await this.client.updateRegistration(classId, userId, classAttendance, this.errorHandler)

        this.dataStore.set("registrationUpdate", updatedRegistration);

        if (updatedRegistration) {
            this.showMessage(`Updated registration ${updatedRegistration.userId} - ${updatedRegistration.classId}!`)
        } else {
            this.errorHandler("Error updating!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const registrationCreatePage = new RegistrationCreatePage();
    await registrationCreatePage.mount();
};

window.addEventListener('DOMContentLoaded', main);
