import React from 'react';
import { Navbar, Nav, NavDropdown } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {faHome } from '@fortawesome/fontawesome-free-solid'
import { withRouter } from "react-router-dom";

// Приложение React состоит из компонентов. NavigationBar, это пример такого компонента.
// Этот класс наследуется от React.Component. props в конструкторе - это параметры
// которые можно при желании передать через атрибуты элемента. Как например, это
// сделано ниже <Navbar bg=”light” … Компонент Navbar у себя в конструкторе получит bg в
// виде поля props. Примерно так props.bg
class NavigationBar extends React.Component {

    constructor(props) {
        super(props);

        this.goHome = this.goHome.bind(this)

    }

    // Добавили функцию goHome к классу, привязали ее в конструкторе к экземпляру объекта
    // NavigationBar и вставили ее в Nav.Link так, что бы она вызывалась по клику на ссылку.
    goHome()
    {
        this.props.history.push("/home")
    }

    // Функция render отображает компонент в окне браузера. По сути, функция возвращает HTML текст,
    // но текст этот формируется динамически из комбинации программного кода и фрагментов HTML разметки.
    // {‘ ‘} просто вставляет пробел между иконкой и надписью. { faHome } вставляет иконку из библиотеки FontAwesome.
    render() {
        return (
            <Navbar bg="light" expand="lg">
                <Navbar.Brand><FontAwesomeIcon icon={ faHome}/>{' '}myRPO</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">
                        <Nav.Link href="/home">Home</Nav.Link>
                        <Nav.Link onClick={this.goHome} >Another home</Nav.Link>
                        <Nav.Link onClick={() => { this.props.history.push("/home")}} >Yet another home</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        );
    }
}

// Класс NavigationBar надо экспортировать, чтобы он был доступен из других компонентов. Нам он нужен в App.js
export default withRouter(NavigationBar);