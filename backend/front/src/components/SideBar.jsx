import React from 'react';
import { Link } from 'react-router-dom'
import { Nav } from 'react-bootstrap'
import {
    faCompactDisc,
    faGlobe, faMicrophone,
    faMusic,
    faUser,
    faUsers
} from "@fortawesome/fontawesome-free-solid";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";


class SideBar extends React.Component {

    constructor(props) {
        super(props);
    }

    // Вариант прорисовки выбирается с помощью свойства expanded, которое может принимать значение
    // true или false.
    render() {
        return (
            <>
                { this.props.expanded &&
                <Nav className={"flex-column my-sidebar my-sidebar-expanded"}>
                    <Nav.Item><Nav.Link as={Link} to="/countries"><FontAwesomeIcon icon={faGlobe}/>{' '}Страны</Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/artists"><FontAwesomeIcon icon={faMicrophone}/>{' '}Артисты</Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/songs"><FontAwesomeIcon icon={faMusic}/>{' '}Песни</Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/shops"><FontAwesomeIcon icon={faCompactDisc}/>{' '}Магазины</Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/users"><FontAwesomeIcon icon={faUsers}/>{' '}Пользователи</Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/myaccount"><FontAwesomeIcon icon={faUser}/>{' '}Мой аккаунт</Nav.Link></Nav.Item>
                </Nav>
                }
                { !this.props.expanded &&
                <Nav className={"flex-column my-sidebar my-sidebar-collapsed"}>
                    <Nav.Item><Nav.Link as={Link} to="/countries"><FontAwesomeIcon icon={faGlobe} size="2x"/></Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/artists"><FontAwesomeIcon icon={faMicrophone} size="2x"/></Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/songs"><FontAwesomeIcon icon={faMusic} size="2x"/></Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/shops"><FontAwesomeIcon icon={faCompactDisc} size="2x"/></Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/users"><FontAwesomeIcon icon={faUsers} size="2x"/></Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/myaccount"><FontAwesomeIcon icon={faUser} size="2x"/></Nav.Link></Nav.Item>
                </Nav>
                }
            </>
        );
    }
}

export default SideBar;