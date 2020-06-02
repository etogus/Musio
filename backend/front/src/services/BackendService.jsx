// Реализация интерфейса с REST сервисом на стороне клиента

import axios from 'axios'
import Utils from '../utils/Utils'
import {alertActions, store} from "../utils/Rdx";

const API_URL = 'http://localhost:8081/api/v1'
const AUTH_URL = 'http://localhost:8081/auth'

class BackendService {

    /* Auth */

    login(login, password) {
        return axios.post(`${AUTH_URL}/login`, {login, password})
    }

    logout() {
        /* return axios.get (`${AUTH_URL}/logout`, { headers: {Authorization: Utils.getToken() }}) */
        return axios.get (`${AUTH_URL}/logout` )
    }

    /* Artists */
    /* Методы для работы с бэкэндовским ArtistController */

    retrieveAllArtists(page, limit) {
        return axios.get(`${API_URL}/artists?page=${page}&limit=${limit}`);
    }

    retrieveArtist(id) {
        return axios.get(`${API_URL}/artists/${id}`);
    }

    createArtist(artist) {
        return axios.post(`${API_URL}/artists`, artist);
    }

    updateArtist(artist) {
        return axios.put(`${API_URL}/artists/${artist.id}`, artist);
    }

    deleteArtists(artists) {
        return axios.post(`${API_URL}/deleteartists`, artists);
    }

    /* Countries */

    retrieveAllCountries(page, limit) {
        return axios.get(`${API_URL}/countries?page=${page}&limit=${limit}`);
    }

    retrieveCountry(id) {
        return axios.get(`${API_URL}/countries/${id}`);
    }

    createCountry(country) {
        return axios.post(`${API_URL}/countries`, country);
    }

    updateCountry(country) {
        return axios.put(`${API_URL}/countries/${country.id}`, country);
    }

    deleteCountries(countries) {
        return axios.post(`${API_URL}/deletecountries`, countries);
    }

    /* Shops */

    retrieveAllShops(page, limit) {
        return axios.get(`${API_URL}/shops?page=${page}&limit=${limit}`);
    }

    retrieveShop(id) {
        return axios.get(`${API_URL}/shops/${id}`);
    }

    createShop(shop) {
        return axios.post(`${API_URL}/shops`, shop);
    }

    updateShop(shop) {
        return axios.put(`${API_URL}/shops/${shop.id}`, shop);
    }

    deleteShops(shops) {
        return axios.post(`${API_URL}/deleteshops`, shops);
    }

    /* Songs */

    retrieveAllSongs(page, limit) {
        return axios.get(`${API_URL}/songs?page=${page}&limit=${limit}`);
    }

    retrieveSong(id) {
        return axios.get(`${API_URL}/songs/${id}`);
    }

    createSong(song) {
        return axios.post(`${API_URL}/songs`, song);
    }

    updateSong(song) {
        return axios.put(`${API_URL}/songs/${song.id}`, song);
    }

    deleteSongs(songs) {
        return axios.post(`${API_URL}/deletesongs`, songs);
    }

    /* Users */

    retrieveAllUsers(page, limit) {
        return axios.get(`${API_URL}/users?page=${page}&limit=${limit}`);
    }

    retrieveUser(id) {
        return axios.get(`${API_URL}/users/${id}`);
    }

    createUsers(user) {
        return axios.post(`${API_URL}/users`, user);
    }

    updateUser(user) {
        return axios.put(`${API_URL}/users/${user.id}`, user);
    }

    deleteUsers(users) {
        return axios.post(`${API_URL}/deleteusers`, users);
    }

}

function showError(msg)
{
    store.dispatch(alertActions.error(msg));
}

axios.interceptors.request.use(
    config => {
        store.dispatch(alertActions.clear());
        let token = Utils.getToken();
        if (token)
            config.headers.Authorization = token;
        return config;
    },
    error => {
        showError(error.message);
        return Promise.reject(error);
    });

axios.interceptors.response.use(undefined,
    error => {
        if (error.response && error.response.status && [401, 403].indexOf(error.response.status) !== -1)
            showError("Ошибка авторизации");
        else if (error.response && error.response.data && error.response.data.message)
            showError(error.response.data.message);
        else
            showError(error.message);
        return Promise.reject(error);
    });

export default new BackendService()