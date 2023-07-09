import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import UserLoginClient from "../api/user_loginClient";


class UserLoginPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onLogin'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new UserLoginClient();
        document.getElementById('login-user-form').addEventListener('submit', this.onLogin);
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onLogin(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("user", null);

        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;

        const loginUser = await this.client.getUser(username, password, this.errorHandler);
        this.dataStore.set("user", loginUser);

        if (loginUser) {
            this.showMessage(`Username found ${loginUser.username}!`);
            localStorage.clear(); // this clear whatever is in the window.localStorage
            window.localStorage.setItem('username', loginUser.username);
            window.localStorage.setItem('password', loginUser.password);
            if (loginUser.userType == "gymMember")
                window.location.href="classes_gymmember.html";
            if (loginUser.userType == "instructor")
                window.location.href="classes_instructor.html";
            if (loginUser.userType == "administrator")
                window.location.href="classes_administrator.html";
        } else {
            this.errorHandler("Username/Password Error!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const userLoginPage = new UserLoginPage();
    userLoginPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
