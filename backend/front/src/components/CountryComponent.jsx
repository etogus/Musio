import React, { Component } from 'react';
import BackendService from '../services/BackendService';
import {alertActions} from "../utils/Rdx";
import {connect} from "react-redux"
import {Form} from "react-bootstrap"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import {faChevronLeft, faSave} from '@fortawesome/fontawesome-free-solid'

class CountryComponent extends Component {

    constructor(props) {
        super(props);

        this.state = {
            // Индекс страны автоматически извлекается из строки URL через поле props.match.params
            id: this.props.match.params.id,
            name: '',
            hidden: false,
        };

        this.onSubmit = this.onSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    // Эта функция нужна для управляемого ввода. Она сохраняет значение поля ввода name
    // в поле состояния name, каждый раз, кода в поле ввода name происходят изменения.
    handleChange({ target}) {
        this.setState({ [target.name]: target.value });
    };


    // Эта функция вызывается при нажатии на кнопку submit в форме. Форма - это тег внутри
    // которого должна быть кнопка с атрибутом type="submit". К ней привязывается функция
    // указанная в свойствах формы. В нашем случае, onSubmit.
    // Проверяем, что название страны не пустое и используем Redux для вывода
    // сообщения об ошибке. Если все в порядке, проверяем id и в зависимости от его значения
    // вызываем createCountry или updateCountry. После успешного завершения запроса,
    // возвращаемся с помощью history обратно в список стран.
    onSubmit(event) {
        event.preventDefault();
        event.stopPropagation();
        let err = null;
        if (!this.state.name) {
            err = "Название страны должно быть указано"
        }
        if (err) {
            this.props.dispatch(alertActions.error(err))
        }
        let country = { id: this.state.id, name: this.state.name }
        if (parseInt(country.id) === -1) {
            BackendService.createCountry(country)
                .then(()=> this.props.history.push('/countries'))
                .catch(()=>{})
        }
        else {
            BackendService.updateCountry(country)
                .then(()=> this.props.history.push('/countries'))
                .catch(()=>{})
        }
    }

    // Если страна не создается, а редактируется, надо ее загрузить из backend
    componentDidMount() {
        if (parseInt(this.state.id) !== -1) {
            BackendService.retrieveCountry(this.state.id)
                .then((resp) => {
                    this.setState({
                        name: resp.data.name
                    })
                })
                .catch(()=> this.setState({ hidden: true }))
        }
    }

    // Сначала рисуем заголовок и кнопку Назад. Затем идет форма с одним управляемым
    // полем ввода. В поле обязательно должны быть указаны атрибуты name и value. Иначе
    // handleChange работать не будет. autoComplete=”off” убирает всплывающие варианты
    // заполнения поля name, которые обычно не помогают, а мешают вводить данные.
    render() {
        if (this.state.hidden)
            return null;
        return (
            <div className="m-4">
                <div className="row my-2 mr-0">
                    <h3>Страна</h3>
                    <button
                        className="btn btn-outline-secondary ml-auto"
                        onClick={() => this.props.history.goBack()}><FontAwesomeIcon
                        icon={faChevronLeft}/>{' '}Назад</button>
                </div>
                <Form onSubmit={this.onSubmit}>
                    <Form.Group>
                        <Form.Label>Название</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Введите название страны"
                            onChange={this.handleChange}
                            value={this.state.name}
                            name="name"
                            autoComplete="off"
                        />
                    </Form.Group>
                    <button
                        className="btn btn-outline-secondary"
                        type="submit"><FontAwesomeIcon
                        icon={faSave}/>{' '}Сохранить</button>
                </Form>
            </div>
        )
    }
}

export default connect()(CountryComponent);