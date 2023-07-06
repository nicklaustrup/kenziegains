import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ClassesAdministratorClient from "../api/classes_administratorClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ClassesAdministratorPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['getAllClasses', 'getUser', 'renderClasses', 'renderLogin', 'renderMenu'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new ClassesAdministratorClient();
        this.getAllClasses();
        var username = 'hamza'; //window.localStorage.getItem('userId'); //searches for the userId in localStorage
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
        let classHTML = "";

        if (classes) {
            for (let element of classes){
                let dateFormatted = new Date(element.dateTime).toLocaleString();
                classHTML += `<tr>
                    <td>${dateFormatted}</td>
                    <td>${element.name}</td>
                    <td>${element.description}</td>
                    <td>${element.classType}</td>
                    <td>${element.classCapacity}</td>`;
                if (element.status) {
                    classHTML += `<td><em><span style="color:#89b4ff;"><strong>Scheduled</strong></span></em></td>`;
                    }
                else {
                    classHTML += `<td><em><span style="color:#000000;"><strong>Cancelled</strong></span></em></td>`;
                }
                classHTML +=`<td><input type="button" class="button-12" onclick="store_redirect('${classes.classId}')" value="Update" /></td>
                    </tr>`;
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
