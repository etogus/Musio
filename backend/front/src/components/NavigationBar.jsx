import React from 'react';
import { Navbar, Nav } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUser, faBars } from '@fortawesome/fontawesome-free-solid'
import { withRouter, Link } from "react-router-dom";
import BackendService from "../services/BackendService";
import { connect } from "react-redux";
import {userActions} from "../utils/Rdx";

// Приложение React состоит из компонентов. NavigationBar, это пример такого компонента.
// Этот класс наследуется от React.Component. props в конструкторе - это параметры
// которые можно при желании передать через атрибуты элемента. Как например, это
// сделано ниже <Navbar bg=”light” … Компонент Navbar у себя в конструкторе получит bg в
// виде поля props. Примерно так props.bg
class NavigationBar extends React.Component {

    constructor(props) {
        super(props);

        this.goHome = this.goHome.bind(this);
        this.logout = this.logout.bind(this);

    }

    // Добавили функцию goHome к классу, привязали ее в конструкторе к экземпляру объекта
    // NavigationBar и вставили ее в Nav.Link так, что бы она вызывалась по клику на ссылку.
    goHome()
    {
        this.props.history.push("/home");
    }

    logout() {
        BackendService.logout().finally(() => {
            this.props.dispatch(userActions.logout())
            this.props.history.push('/login')
        })
    }


    // Функция render отображает компонент в окне браузера. По сути, функция возвращает HTML текст,
    // но текст этот формируется динамически из комбинации программного кода и фрагментов HTML разметки.
    // {‘ ‘} просто вставляет пробел между иконкой и надписью. { faHome } вставляет иконку из библиотеки FontAwesome.
    render() {
        return (
            <Navbar bg="light" expand="lg">
                {/*кнопка нужна нам для переключения состояния sidebar с expanded на collapsed и обратно*/}
                <button type="button"
                        className="btn btn-outline-secondary mr-2"
                        onClick={this.props.toggleSideBar}>
                    <FontAwesomeIcon icon={ faBars}/>
                </button>
                <Navbar.Brand>myRPO</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">
                        {/*Nav.Link href="/home">Home</Nav.Link>*/}
                        <Nav.Link as={Link} to="/home">Home</Nav.Link>
                        <Nav.Link onClick={this.goHome} >Another home</Nav.Link>
                        <Nav.Link onClick={() => { this.props.history.push("/home")}} >Yet another home</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
                { this.props.user &&
                <Nav.Link onClick={this.logout}><FontAwesomeIcon icon={faUser} fixedWidth/>{' '}Выход</Nav.Link>
                }
                { !this.props.user &&
                <Nav.Link as={Link} to="/login"><FontAwesomeIcon icon={faUser} fixedWidth/>{' '}Вход</Nav.Link>
                }
            </Navbar>
        );
    }
}

function mapStateToProps(state) {
    const { user } = state.authentication;
    return { user };
}

// Класс NavigationBar надо экспортировать, чтобы он был доступен из других компонентов. Нам он нужен в App.js
export default withRouter(connect(mapStateToProps)(NavigationBar));