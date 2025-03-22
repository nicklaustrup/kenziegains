import BaseClass from "../util/baseClass";
import axios from 'axios'
import config from '../config';

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class ClassCreateClient extends BaseClass {

    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'getUser', 'createClass'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        // Create axios instance with base URL
        const axiosInstance = axios.create({
            baseURL: config.apiUrl
        });

        this.clientLoaded(axiosInstance);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    /**
     * Gets the concert for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */

    async getUser(username, password, errorCallback) {
        try {
            const response = await this.client.get(`/user/${username}_${password}`);
            return response.data;
        } catch (error) {
            this.handleError("getUser", error, errorCallback)
        }
    }

    async createClass(name, description, classType, userId, classCapacity, dateTime, status, errorCallback) {
        try {
            const response = await this.client.post(`/instructorleadclass`, {
                name: name,
                description: description,
                classType: classType,
                userId: userId,
                classCapacity: classCapacity,
                dateTime: dateTime,
                status: status
            });
            return response.data;
        } catch (error) {
            this.handleError("createClass", error, errorCallback);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
