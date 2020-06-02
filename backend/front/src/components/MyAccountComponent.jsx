import React, { Component } from 'react';
import { Formik, Form, Field } from 'formik';
import BackendService from '../services/BackendService';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronLeft} from "@fortawesome/fontawesome-free-solid";
import { connect } from "react-redux";
import {alertActions} from "../utils/Rdx";
import Utils from "../utils/Utils";

class MyAccountComponent extends Component {

    constructor(props) {
        super(props);

        this.state = {
            id: null,
            login: '',
            email: '',
            pwd: '',
            pwd2: '',
            show_pwd: false,
        };

        this.onSubmit = this.onSubmit.bind(this);
        this.validate = this.validate.bind(this);
        this.onSetPasswordClick = this.onSetPasswordClick.bind(this)
    }

    componentDidMount() {
        let uid = Utils.getUser().id;
        BackendService.retrieveUser(uid)
            .then(response => {
                this.setState({
                    id: uid,
                    login: response.data.login,
                    email: response.data.email,
                })})
            .catch(()=>{})
    }

    onSetPasswordClick()
    {
        this.setState({ show_pwd: true, pwd: '' });
    }

    onSubmit(values) {
        let user = {
            id: this.state.id,
            login: values.login,
            email: values.email,
            password: values.pwd,
            np: values.pwd
        };
        console.log("user", user);
        BackendService.updateUser(user)
            .then(() => {
                this.props.history.push("users/");
            })
            .catch(()=>{})
    }

    validate(values) {
        let e = null;
        let errors = {};
        if (values.pwd)
        {
            if (values.pwd2.length < 8)
                e = 'Длина пароля не менее 8 символов';
            else if (!values.pwd2)
                e = 'Пожалуйста, повторите пароль';
            else if (values.pwd !== values.pwd2)
                e = 'Пароли не совпадают'
        }
        if (e != null) {
            errors.error = "error";
            this.props.dispatch(alertActions.error(e))
        }
        return errors
    }

    render() {
        let { id, login, email, pwd, pwd2, admin } = this.state;
        return (
            <div>
                <div className="container">
                    <div className="row my-2 mr-0">
                        <h3>Мой аккаунт</h3>
                        <button className="btn btn-outline-secondary ml-auto" onClick={() => this.props.history.goBack()}>
                            <FontAwesomeIcon icon={faChevronLeft}/>{' '}Назад</button>
                    </div>
                    <Formik
                        initialValues={{id, login, email, pwd, pwd2, admin}}
                        onSubmit={this.onSubmit}
                        validateOnChange={false}
                        validateOnBlur={false}
                        validate={this.validate}
                        enableReinitialize={true}
                    >
                        {
                            (props) => (
                                <Form>
                                    <fieldset className="form-group" disabled >
                                        <label>Логин</label>
                                        <Field className="form-control" type="text" name="login" disabled/>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>EMail</label>
                                        <Field className="form-control" type="text" name="email" validate="validateEmail"/>
                                    </fieldset>
                                    {
                                        this.state.show_pwd &&
                                        <fieldset className="form-group">
                                            <label>Введите пароль</label>
                                            <Field className="form-control" type="password" name="pwd"/>
                                        </fieldset>
                                    }
                                    {
                                        this.state.show_pwd &&
                                        <fieldset className="form-group">
                                            <label>Повторите пароль</label>
                                            <Field className="form-control" type="password" name="pwd2"/>
                                        </fieldset>
                                    }
                                    {
                                        !this.state.show_pwd &&
                                        <fieldset className="form-group">
                                            <button className="btn btn-outline-secondary"
                                                    onClick={this.onSetPasswordClick}>Изменить пароль</button>
                                        </fieldset>
                                    }
                                    <button className="btn btn-outline-secondary" type="submit">Сохранить</button>
                                </Form>
                            )
                        }
                    </Formik>

                </div>
            </div>
        )
    }
}
export default connect()(MyAccountComponent);