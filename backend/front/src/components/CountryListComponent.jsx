import React, { Component } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTrash, faEdit, faPlus } from '@fortawesome/fontawesome-free-solid'
import BackendService from "../services/BackendService";
import Alert from "./Alert";

class CountryListComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            message: undefined, // текст в окне Alert, который будет меняться в зависимости от того одну страну мы удаляем или несколько
            countries: [], // список стран
            selected_countries: [], // список отмеченных для удаления стран, который создается с помощью списка checkedItems и передается в запросе на удаление стран REST сервису
            show_alert: false, // флаг отображения модального окна Alert для подтверждения удаления стран
            // В каждой строке таблицы стран будет чек бокс, с помощью которого можно будет отметить
            // страну, с тем чтобы потом по кнопке удалить сразу все отмеченные страны. Это будет список
            // двоичных флагов, каждый из которых хранит состояние чек бокса одной из строк таблицы
            // стран: true – отмечен, false – не отмечен.
            checkedItems: [],
            hidden: false, //логический флаг для запрета отображения списка стран, если запрос к сервису завершился с ошибкой
        };

        this.refreshCountries = this.refreshCountries.bind(this);
        this.updateCountryClicked = this.updateCountryClicked.bind(this);
        this.addCountryClicked = this.addCountryClicked.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.closeAlert = this.closeAlert.bind(this);
        this.handleCheckChange = this.handleCheckChange.bind(this);
        this.handleGroupCheckChange = this.handleGroupCheckChange.bind(this);
        this.setChecked = this.setChecked.bind(this);
        this.deleteCountriesClicked = this.deleteCountriesClicked.bind(this);
    }

    // Ставит или снимает отметку всех чек боксов.
    // В заголовок таблицы мы вставим чек бокс, который позволит нам устанавливать или сбрасывать все
    // чек боксы в строках таблицы с помощью этой функции. Мы не можем просто поменять значение в
    // поле состояния checkedItems. Поэтому делаем копию этого поля и заменяем его на эту
    // копию с помощью функции this.setState.
    setChecked(v)
    {
        let checkedCopy = Array(this.state.countries.length).fill(v);
        this.setState( { checkedItems : checkedCopy })
    }

    // Здесь используется принцип контролируемого ввода.
    // При изменении состояния чек бокса вызывается эта функция, которая записывает состояние чек бокса
    // в поле состояния checkedItems. Здесь также делаем копию, вносим в нее изменения и сохраняем обратно
    // в состояние с помощью setState.
    handleCheckChange(e)
    {
        const idx = e.target.name;
        const isChecked = e.target.checked;

        let checkedCopy = [...this.state.checkedItems];
        checkedCopy[idx] = isChecked;
        this.setState({ checkedItems: checkedCopy });
    }

    // Обработчик чек бокса в заголовке таблицы. Он устанавливает или сбрасывает все чек боксы
    // в строках таблицы.
    handleGroupCheckChange(e)
    {
        const isChecked = e.target.checked;
        this.setChecked(isChecked);
    }

    // Эта функция вызывается по кнопке Удалить. Она с помощью функции map, которая
    // аналогична циклу, пробегает по всем странам и, одновременно, по всем элементам
    // checkedItems для того, чтобы определить какие страны отмечены для удаления. Эти
    // страны помещаются в массив x. Если массив оказывается пустым, то на этом все заканчивается.
    deleteCountriesClicked() {
        let x = [];
        this.state.countries.map ((t, idx) => {
            if (this.state.checkedItems[idx]) {
                x.push(t)
            }
            return 0
        });
        if (x.length > 0) {
            var msg;
            if (x.length > 1)
            {
                msg = "Пожалуйста подтвердите удаление " + x.length + " стран";
            }
            else
            {
                msg = "Пожалуйста подтвердите удаление страны " + x[0].name;
            }
            this.setState({ show_alert: true, selected_countries: x, message: msg });
        }
    }

    // Вызывает REST сервис для удаления списка стран и, в случае успешного завершения этой операции,
    // обновляет список оставшихся стран на экране.
    onDelete()
    {
        BackendService.deleteCountries(this.state.selected_countries)
            .then( () => this.refreshCountries())
            .catch(()=>{})
    }

    // Закрывает модальное окно Alert.
    closeAlert()
    {
        this.setState({ show_alert : false })
    }

    // Функция обновления списка стран считывает его с помощью запроса к REST сервису и
    // помещает в поле состояния countries. В случае ошибки, устанавливает флаг hidden,
    // который используется в методе render для того, чтобы убрать с экрана изображение таблицы.
    refreshCountries() {
        BackendService.retrieveAllCountries()
            .then(
                resp => {
                    this.setState( { countries: resp.data, hidden: false });
                }
            )
            .catch(()=> { this.setState({ hidden: true })})
            .finally(()=> this.setChecked(false))
    }

    // Метод вызывается при загрузке страницы.
    // Здесь загружаются данные, необходимые для ее прорисовки.
    componentDidMount() {
        this.refreshCountries()
    }

    // Обработчик кнопки для перехода к странице редактирования страны.
    // Такая кнопка есть в каждой строчке таблицы стран.
    updateCountryClicked(id) {
        this.props.history.push(`/countries/${id}`)
    }

    // Кнопка для добавления страны.
    // При добавлении страны мы указываем в URL индекс -1.
    addCountryClicked() {
        this.props.history.push(`/countries/-1`)
    }

    render() {
        if (this.state.hidden) // Если hidden равно false, возвращаем null ничего не рисуя.
            return null;
        return ( // Иначе выводим заголовок и две кнопки для добавления страны и удаления стран.
            <div className="m-4">
                <div className="row my-2 mr-0">
                    <h3>Страны</h3>
                    <button className="btn btn-outline-secondary ml-auto"
                            onClick={this.addCountryClicked}><FontAwesomeIcon icon={faPlus}/>{' '}Добавить</button>
                    <button className="btn btn-outline-secondary ml-2"
                            onClick={this.deleteCountriesClicked}><FontAwesomeIcon icon={faTrash}/>{' '}Удалить</button>
                </div>
                <div className="row my-2 mr-0">
                    <table className="table table-sm">
                        <thead className="thead-light">
                        <tr>
                            <th>Название</th>
                            <th>
                                <div className="btn-toolbar pb-1">
                                    <div className="btn-group  ml-auto">
                                        <input type="checkbox"  onChange={this.handleGroupCheckChange}/>
                                    </div>
                                </div>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                            this.state.countries && this.state.countries.map((country, index) =>
                                <tr key={country.id}>
                                    <td>{country.name}</td>
                                    <td>
                                        <div className="btn-toolbar">
                                            <div className="btn-group  ml-auto">
                                                <button className="btn btn-outline-secondary btn-sm btn-toolbar"
                                                        onClick={() => this.updateCountryClicked(country.id)}>
                                                    <FontAwesomeIcon icon={faEdit} fixedWidth/></button>
                                            </div>
                                            <div className="btn-group  ml-2 mt-1">
                                                <input type="checkbox" name={index}
                                                       checked={this.state.checkedItems.length > index ?  this.state.checkedItems[index] : false}
                                                       onChange={this.handleCheckChange}/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>
                <Alert
                    title="Удаление"
                    message={this.state.message}
                    ok={this.onDelete}
                    close={this.closeAlert}
                    modal={this.state.show_alert}
                    cancelButton={true}
                />
            </div>
        )
    }
}

export default CountryListComponent;