import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ClassCreateClient from "../api/class_createClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ClassCreatePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['getUser', 'getUsers', 'renderClass', 'renderLogin', 'renderMenu', 'onCreate'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new ClassCreateClient();
        var username = 'hamza'; //window.localStorage.getItem('userId'); //searches for the userId in localStorage
        var password = '1234'; //window.localStorage.getItem('userId'); //searches for the userId in localStorage
        this.getUser(username, password);
        this.getUsers();
        this.dataStore.addChangeListener(this.renderMenu);
        this.dataStore.addChangeListener(this.renderLogin);
        this.dataStore.addChangeListener(this.renderClass);
        document.getElementById('create-form').addEventListener('submit', this.onCreate);
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderClass() {
        let resultInstructors = document.getElementById("instructor");
        const instructors = this.dataStore.get("users");
        let instructorHTML = "";

        //Instructor elements for the dropdown list
        if (instructors) {
            for (let element of instructors) {
            {
                if((element.userType == "instructor") && (element.status == "active"))
                instructorHTML += `<option id="${element.userId}">${element.firstName} ${element.lastName}</option>`;
             }
        }
        resultInstructors.innerHTML = instructorHTML;}
        //Date should not be in the past
        let dateField = document.getElementById("date");
        dateField.min = new Date().toISOString().slice(0,new Date().toISOString().lastIndexOf(":"));
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

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        // Gathering the values from all the inputs in the form

        let name = document.getElementById("name").value;
        let description = document.getElementById("description").value;
        let classType = document.getElementById("classType").value;
        let instructor = document.getElementById("instructor");
        let userId = instructor.options[instructor.selectedIndex].id;
        let classCapacity = document.getElementById("classCapacity").value;
        let dateTime = document.getElementById("dateTime").value;
        let status = true;

        //Submits all the information in order to create the record
//        this.dataStore.set("eventCreate", null);
        const createdClass = await this.client.createClass(name, description, classType, userId, classCapacity, dateTime, status, this.errorHandler);
        this.dataStore.set("classCreate", createdClass);

        if (createdClass) {
            this.showMessage(`Created class ${createdClass.classId}!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const classPage = new ClassCreatePage();
    await classPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
