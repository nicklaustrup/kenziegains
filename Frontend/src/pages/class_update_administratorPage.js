import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ClassUpdateClient from "../api/class_update_administratorClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ClassUpdatePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['getClass', 'getUser', 'getUsers', 'renderClass', 'renderLogin', 'renderMenu', 'onUpdate'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new ClassUpdateClient();
        //var userId = window.localStorage.getItem('userId'); //searches for the userId in localStorage
        var username = 'hamza'; //window.localStorage.getItem('userId'); //searches for the userId in localStorage
        var password = '1234'; //window.localStorage.getItem('userId'); //searches for the userId in localStorage
        var classId = window.localStorage.getItem('classId'); //searches for the eventId in localStorage
        this.getClass(classId);
        this.getUser(username, password);
        this.getUsers();
        this.dataStore.addChangeListener(this.renderMenu);
        this.dataStore.addChangeListener(this.renderLogin);
        this.dataStore.addChangeListener(this.renderClass);
        document.getElementById('update-form').addEventListener('submit', this.onUpdate);
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderClass() {
        const classUpdate = this.dataStore.get("class");
        let resultInstructors = document.getElementById("instructor");
        const instructors = this.dataStore.get("users");
        let instructorHTML = "";
        if (classUpdate) {
            //Setting the values for all the inputs using the response returned by getClass
            document.getElementById('classId').value= classUpdate.classId;
            document.getElementById('name').value= classUpdate.name;
            document.getElementById('description').value= classUpdate.description;
            document.getElementById('classType').value= classUpdate.classType;

            //Instructor elements for the dropdown list
            if (instructors) {
                for (let element of instructors) {
                    if((element.userType == "instructor") && (element.status == "active")) {
                        if (element.userId === classUpdate.userId)
                            instructorHTML += `<option id="${element.userId}" selected>${element.firstName} ${element.lastName}</option>`
                        else
                            instructorHTML += `<option id="${element.userId}">${element.firstName} ${element.lastName}</option>`
                    }
                 }
            }
            resultInstructors.innerHTML = instructorHTML;
            document.getElementById('classCapacity').value= classUpdate.classCapacity;

            //Date should not be in the past
            document.getElementById('dateTime').value= classUpdate.dateTime;
            let dateField = document.getElementById("dateTime");
            dateField.min = new Date().toISOString().slice(0,new Date().toISOString().lastIndexOf(":"));
            if (classUpdate.status) {
               document.getElementById('active_round_scheduled').checked=true;
            }
            else {
                document.getElementById('active_round_cancelled').checked=true;
            };


//            let resultAttendance = document.getElementById("Attendance");
//            let attendanceHTML = "";
//            for (let element of attendees){
//                attendanceHTML += `<tr><td>${element.firstName} ${element.lastName}</td></tr>`;
//            }
//            resultAttendance.innerHTML = attendanceHTML;
        }

    }

    async renderLogin() {
        let resultArea = document.getElementById("login");
        const user = this.dataStore.get("user");
        let userHTML = "";

        if (user) {
            userHTML += `${user.firstName} ${user.lastName} - Log out`;
            resultArea.innerHTML = userHTML;
        } else {
            resultArea.innerHTML = "No User";
        }
    }

    async renderMenu() {
        document.getElementById("menu").innerHTML = `
                  <ul>
                    <li><a href="classes_administrator.html">Class +</a>
                      <!-- First Tier Drop Down -->
                      <ul>
                        <li><a href="class_create.html">Create Class</a></li>
                      </ul>
                    </li>
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

    async onUpdate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("class", null);

        // Gathering the values from all the inputs in the form

        let classId = document.getElementById("classId").value;
        let name = document.getElementById("name").value;
        let description = document.getElementById("description").value;
        let classType = document.getElementById("classType").value;
        let instructor = document.getElementById("instructor");
        let userId = instructor.options[instructor.selectedIndex].id;
        let classCapacity = document.getElementById("classCapacity").value;
        let dateTime = document.getElementById("dateTime").value;
        var activeRadioButtons = document.getElementsByName('active_round');
        let status = activeRadioButtons[0].checked; //checks the first radio button which is Scheduled

        //Submits all the information in order to update the record
        const updatedClass = await this.client.updateClass(classId, name, description, classType, userId, classCapacity, dateTime, status, this.errorHandler)

        this.dataStore.set("class", updatedClass);

        if (updatedClass) {
            this.showMessage(`Updated class ${updatedClass.classId}!`)
        } else {
            this.errorHandler("Error updating!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const classUpdatePage = new ClassUpdatePage();
    await classUpdatePage.mount();
};

window.addEventListener('DOMContentLoaded', main);
