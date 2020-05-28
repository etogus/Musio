// компонент страницы логина
import React from 'react';
import BackendService from '../services/BackendService';
import {connect} from "react-redux";
import {userActions} from "../utils/Rdx";

class Login extends React.Component {
    // state - переменная, в которой хранится состояние компонента. Состояние - это объект, который
    // включает набор полей, влияющих на прорисовку компонента.
    // Здесь присваиваются начальные значения: пустое имя пользователя и пароль для полей формы,
    // флажок для отображения спиннера во время ожидания ответа от сервера и другой флажок для вывода
    // сообщения об ошибке, если пользователь нажал на кнопку Вход, не заполнив поля формы.
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            loggingIn: false,
            submitted: false,
            //error_message: null,
        };

        // Привязываем методы к экзмепляру компонента. Явная привязка (explicit binding)
        // В JavaScript функцию надо привязать к контексту, в котором она вызывается явно или неявно.
        this.handleChange = this.handleChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    // Функция handleChange делает ввод в форме контролируемым. Она вызывается при вводе
    // каждого символа в поле ввода логина или пароля. Контролируемый ввод мы используем
    // для того, чтобы выводить сообщения об ошибке в случае, если пользователь не заполнил
    // нужные для входа поля формы.
    handleChange(e) {
        const { name, value } = e.target;
        this.setState({ [name]: value }); // перерисовка компонента при каждом вызове функции
    }

    // Эта функция вызывается после заполнения формы, по нажатию на кнопку Вход.
    handleSubmit(e) {
        e.preventDefault();

        this.setState({ submitted: true, loggingIn: true, /*error_message : null */ });
        const { username, password } = this.state;
        BackendService.login(username, password)
            .then( resp => {
                this.props.dispatch(userActions.login(resp.data))
                this.props.history.push("/home")
            })
            .catch(
                err => {
                    /* if (err.response && err.response.status === 401)
                        this.setState( { error_message : "Ошибка авторизации", loggingIn: false } );
                    else
                        this.setState( { error_message : err.message, loggingIn: false  } ); */
                    this.setState( { loggingIn: false  } );
                }
            )
    }

    render() {
        console.log("render");
        let { submitted, username, password, loggingIn } = this.state;
        return (
            <div className="col-md-6 mr-0">
                {/*this.state.error_message &&
                    <div className="alert alert-danger mt-1 mr-0 ml-0">{this.state.error_message}</div>
                */}
                <h2>Вход</h2>
                <form name="form" onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="username">Логин</label>
                        <input type="text" className={'form-control' + (submitted && !username ? ' is-invalid' : '')}
                               name="username" value={username}
                               onChange={this.handleChange} />
                        {submitted && !username && <div className="help-block text-danger">Введите имя пользователя</div>}
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Пароль</label>
                        <input type="password" className={'form-control' + (submitted && !password ? ' is-invalid' : '')}
                               name="password" value={password}
                               onChange={this.handleChange} />
                        {submitted && !password &&
                        <div className="help-block text-danger">Введите пароль</div>
                        }
                    </div>
                    <div className="form-group">
                        <button className="btn btn-primary">
                            {loggingIn && <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>}
                            Вход</button>
                    </div>
                </form>
            </div>
        );
    }
}
export default connect()(Login);