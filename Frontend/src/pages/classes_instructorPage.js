import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ClassesAdministratorClient from "../api/classes_instructorClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ClassesAdministratorPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['getAllClasses', 'getUsers', 'getAttendance', 'getUser', 'renderClasses', 'renderLogin', 'renderMenu'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new ClassesAdministratorClient();
        this.getAllClasses();
        this.getUsers();
        this.getAttendance();
        var username = 'nick'; //window.localStorage.getItem('userId'); //searches for the userId in localStorage
        var password = '1234'; //window.localStorage.getItem('userId'); //searches for the userId in localStorage
        this.getUser(username, password);
        this.dataStore.addChangeListener(this.renderMenu);
        this.dataStore.addChangeListener(this.renderClasses);
        this.dataStore.addChangeListener(this.renderLogin)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderClasses() {
        let resultArea = document.getElementById("classes-info");
        const classes = this.dataStore.get("classes");
        const users = this.dataStore.get("users");
        const attendance = this.dataStore.get("attendance");
        const instructor = this.dataStore.get("user");
        let classHTML = "";
        let instructor_name = "";
        let attendance_number;
        let availability_status = "";

        if (classes) {
            for (let element of classes){
                if (instructor) {
                    if (instructor.userId == element.userId) {
                        let attendance_number = 0;
                        if (users) {
                            for (let user of users) {
                                if (element.userId == user.userId)
                                    instructor_name = user.firstName + ` ` + user.lastName;
                            }
                        }
                        if (attendance) {
                            for (let attendee of attendance) {
                                if ((element.classId == attendee.classId) && (attendee.classAttendance == "Attending"))
                                    attendance_number += 1;
                            }
                        }
                        let dateFormatted = new Date(element.dateTime).toLocaleString();
                        classHTML += `<tr>
                            <td>${dateFormatted}</td>
                            <td>${element.name}</td>
                            <td>${element.description}</td>
                            <td>${element.classType}</td>
                            <td>${instructor_name}</td>
                            <td>${element.classCapacity}</td>
                            <td>${attendance_number}</td>`;
                        if (attendance_number == element.classCapacity)
                            classHTML += `<td><em><span style="color:#000000;"><strong>Class Full</strong></span></em></td>`
                        else
                            classHTML += `<td><em><span style="color:#89b4ff;"><strong>Available</strong></span></em></td>`;
                        if (element.status)
                            classHTML += `<td><em><span style="color:#89b4ff;"><strong>Scheduled</strong></span></em></td>`
                        else
                            classHTML += `<td><em><span style="color:#000000;"><strong>Cancelled</strong></span></em></td>`;
                        classHTML +=`<td><input type="button" class="button-12" onclick="store_redirect('${element.classId}')" value="Update" /></td>
                            </tr>`;
                    }
                }
            }
            resultArea.innerHTML = classHTML;
        } else {
            resultArea.innerHTML = "No Classes";
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
                    <li><a href="classes_instructor.html">Class +</a>
                      <!-- First Tier Drop Down -->
                      <ul>
                        <li><a href="class_create_instructor.html">Create Class</a></li>
                      </ul>
                    </li>
                    <li><a href="index.html" id="login"></a></li>
                  </ul>
        `;
    }

    // Classes Handlers --------------------------------------------------------------------------------------------------

    async getAllClasses() {
        let result = await this.client.getAllClasses(this.errorHandler);
        this.dataStore.set("classes", result);
        if (result) {
            this.showMessage(`Got All Classes!`)
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
    async getUser(username,password) {
        let result = await this.client.getUser(username, password, this.errorHandler);
        this.dataStore.set("user", result);
        if (result) {
            this.showMessage(`Got User!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const classesPage = new ClassesAdministratorPage();
    await classesPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
