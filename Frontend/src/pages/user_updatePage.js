import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import UserUpdateClient from "../api/user_updateClient";


class UserUpdatePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'renderLogin', 'renderMenu'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new UserUpdateClient();
        document.getElementById('update-user-form').addEventListener('submit', this.onCreate);
        this.renderLogin();
        this.renderMenu();
        var username = window.localStorage.getItem('username'); //searches for the username in localStorage
        var password = window.localStorage.getItem('password'); //searches for the password in localStorage
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
                    <li><a href="classes_administrator.html">Class +</a>
                      <!-- First Tier Drop Down -->
                      <ul>
                        <li><a href="class_create.html">Create Class</a></li>
                      </ul>
                    </li>
                    <li><a href="user_login.html" id="login"></a></li>
                    <li><a href="user_update.html" id="login"></a></li>
                  </ul>
        `;
    }
    // Event Handlers --------------------------------------------------------------------------------------------------

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("userUpdated", null);

        var username = window.localStorage.getItem('username'); //searches for the username in localStorage
        var password = window.localStorage.getItem('password'); //searches for the password in localStorage

        let membership = document.getElementById("user_update_membership_field").value;
        var activeRadioButtons = document.getElementsByName('active_round');
        let status = activeRadioButtons[0].checked;
        let newPassword = document.getElementById("user_update_password1").value;
        let verifyNewPassword = document.getElementById("user_update_password2").value;
        let oldPassword = document.getElementById("user_update_password3").value;

        if (newPassword !== verifyNewPassword){
            alert("Passwords do not match!")
        }

        if (oldPassword !== password){
            alert("Old password is incorrect.")
        }

        const updatedUser = await this.client.updateUser(username, membership, newPassword, status, this.errorHandler);
        this.dataStore.set("userUpdated", updatedUser);

        if (updatedUser) {
            this.showMessage(`Updated ${updatedUser.firstName}'s Profile!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const userUpdatePage = new UserUpdatePage();
    userUpdatePage.mount();
};

window.addEventListener('DOMContentLoaded', main);