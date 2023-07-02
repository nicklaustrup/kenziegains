import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import UserRegisterClient from "../api/user_registerClient";


class UserRegisterPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onCreate'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new UserRegisterClient();
        document.getElementById('create-user-form').addEventListener('submit', this.onCreate);
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("userCreated", null);

        let firstName = document.getElementById("create-first-name-field").value;
        let lastName = document.getElementById("create-last-name-field").value;
        let username = document.getElementById("create-user-name-field").value;
        let password = document.getElementById("create-password-field").value;
        let membership = "gold";
        let status = "active";
        let userType = "gymMember";

        const createdUser = await this.client.createUser(firstName, lastName, membership, username, password, status, userType, this.errorHandler);
        this.dataStore.set("userCreated", createdUser);

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
    const userRegisterPage = new UserRegisterPage();
    userRegisterPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
