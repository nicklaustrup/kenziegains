import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ClassesGymMemberClient from "../api/classes_gymmemberClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ClassesGymMemberPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['getAllClasses', 'getUsers', 'getAttendance', 'getUser', 'renderClasses', 'renderLogin', 'renderMenu'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new ClassesGymMemberClient();
        this.getAllClasses();
        this.getUsers();
        this.getAttendance();
        var username = window.localStorage.getItem('username'); //searches for the username in localStorage
        var password = window.localStorage.getItem('password'); //searches for the password in localStorage
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
        const gymMember = this.dataStore.get("user");
        const attendance = this.dataStore.get("attendance");
        let classHTML = "";
        let instructor_name = "";
        let attendance_number;
        let availability_status = "";

        if (classes) {
            for (let element of classes){
                let attendance_number = 0;
                let registered = false;
                let response = "";
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
                        if (gymMember)
                            if ((element.classId == attendee.classId) && (gymMember.userId == attendee.userId)) {
                                registered = true;
                                response = attendee.classAttendance;
                            }
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
                classHTML += `<td><em><span style="color:#89b4ff;"><strong>${response}</strong></span></em></td>`;
                if (!registered) {
                    if ((element.classCapacity > attendance_number) && (element.status))
                        classHTML +=`<td><input type="button" class="button-12" onclick="store_redirect_create('${element.classId}')" value="Register"></td></tr>`;
                }
                else {
                    if ((response == "Attending") && (element.status))
                        classHTML +=`<td><input type="button" class="button-12" onclick="store_redirect_update('${element.classId}')" value="Update"></td></tr>`;
                    if ((response == "Not Attending") && (element.classCapacity > attendance_number) && (element.status))
                        classHTML +=`<td><input type="button" class="button-12" onclick="store_redirect_update('${element.classId}')" value="Update"></td></tr>`;
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
                    <li><a href="classes_gymmember.html">Class</a></li>
                    <li><a href="user_update.html" id="update">Update Profile</a></li>
                    <li><a href="user_login.html" id="login"></a></li>
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
    const classesPage = new ClassesGymMemberPage();
    await classesPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
