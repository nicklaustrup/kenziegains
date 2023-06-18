import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";
import RegisterUserClient from "../api/registerUserClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class RegisterUserPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGet', 'onCreate', 'renderExample'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('get-by-user-id-form').addEventListener('submit', this.onGet);
        document.getElementById('create-user-form').addEventListener('submit', this.onCreate);
        this.client = new RegisterUserClient();

        this.dataStore.addChangeListener(this.renderExample)
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderExample() {
        let resultArea = document.getElementById("result-info");

        const user = this.dataStore.get("user");

        if (user) {
            resultArea.innerHTML = `
                <div>username: ${user.username}</div>
                <div>password: ${user.password}</div>
                <div>First Name: ${user.firstName}</div>
                <div>Last Name: ${user.lastName}</div>
                <div>Member Type: ${user.userType}</div>
                <div>Membership: ${user.membership}</div>
                <div>Status: ${user.status}</div>
                <div>User Id: ${user.userId}</div>
            `
        } else {
            resultArea.innerHTML = "No Item";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let username = document.getElementById("username-field").value;
        let password = document.getElementById("password-field").value;
        this.dataStore.set("user", null);

        let result = await this.client.getUser(username, password, this.errorHandler);
        this.dataStore.set("user", result);
        if (result) {
            this.showMessage(`Got ${result.firstName}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("user", null);

        let username = document.getElementById("create-username-field").value;
        let password = document.getElementById("create-password-field").value;
        let firstName = document.getElementById("create-firstname-field").value;
        let lastName = document.getElementById("create-lastname-field").value;
        let userType = document.getElementById("create-usertype-field").value;
        let membership = document.getElementById("create-membership-field").value;


        const createdUser = await this.client.createUser(username, password, firstName, lastName, userType, membership, this.errorHandler);
        this.dataStore.set("user", createdUser);

        if (createdUser) {
            this.showMessage(`Created ${createdUser.firstName}!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const registerUserPage = new RegisterUserPage();
    registerUserPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
