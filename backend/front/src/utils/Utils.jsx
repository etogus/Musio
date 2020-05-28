// По аналогии с backend класс Utils - свалка различных полезных функций
// Методы для работы с текущим пользователем
class Utils {

    saveUser(user) {
        localStorage.setItem('user', JSON.stringify(user))
    }

    removeUser() {
        localStorage.removeItem('user')
    }

    getToken()
    {
        let user = JSON.parse(localStorage.getItem('user'))
        return user && "Bearer " + user.token;
    }

    getUserName()
    {
        let user = JSON.parse(localStorage.getItem('user'))
        return user && user.login;
    }

    getUser()
    {
        console.log("USER")
        return JSON.parse(localStorage.getItem('user'))
    }
}
export default new Utils()