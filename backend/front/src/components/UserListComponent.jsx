import React, { Component } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTrash, faEdit, faPlus } from '@fortawesome/fontawesome-free-solid'
import Alert from './Alert'
import BackendService from "../services/BackendService";
import PaginationComponent from "./PaginationComponent";

class UserListComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            message: undefined,
            items: [],
            selected_items: [],
            show_alert: false,
            checkedItems: [],
            hidden: false,
            page: 0,
            limit: 100,
            totalCount: 0
        };

        this.refreshItems = this.refreshItems.bind(this);
        this.updateItemClicked = this.updateItemClicked.bind(this);
        this.addItemClicked = this.addItemClicked.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.closeAlert = this.closeAlert.bind(this);
        this.handleCheckChange = this.handleCheckChange.bind(this);
        this.handleGroupCheckChange = this.handleGroupCheckChange.bind(this);
        this.setChecked = this.setChecked.bind(this);
        this.deleteItemsClicked = this.deleteItemsClicked.bind(this);

        this.onPageChanged = this.onPageChanged.bind(this);
    }

    onPageChanged(cp) {
        this.refreshItems(cp - 1)
    }

    setChecked(v)
    {
        let checkedCopy = Array(this.state.items.length).fill(v);
        this.setState( { checkedItems : checkedCopy })
    }

    handleCheckChange(e)
    {
        const idx = e.target.name;
        const isChecked = e.target.checked;

        let checkedCopy = [...this.state.checkedItems];
        checkedCopy[idx] = isChecked;
        this.setState({ checkedItems: checkedCopy });
    }

    handleGroupCheckChange(e)
    {
        const isChecked = e.target.checked;
        this.setChecked(isChecked);
    }

    deleteItemsClicked() {
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
                msg = "Пожалуйста подтвердите удаление " + x.length + " пользователей";
            }
            else
            {
                msg = "Пожалуйста подтвердите удаление пользователя " + x[0].login;
            }
            this.setState({ show_alert: true, selected_countries: x, message: msg });
        }
    }

    refreshItems(cp) {
        BackendService.retrieveAllUsers(cp, this.state.limit)
            .then(
                resp => {
                    this.setState( { items: resp.data.content,
                        totalCount: resp.data.totalElements, page: cp, hidden: false });
                }
            )
            .catch(()=> { this.setState({ totalCount: 0, hidden: true })})
            .finally(()=> this.setChecked(false))
    }

    componentDidMount() {
        this.refreshItems(0)
    }

    updateItemClicked(id) {
        this.props.history.push(`/artists/${id}`)
    }

    onDelete()
    {
        BackendService.deleteUsers(this.state.selected_items)
            .then( () => this.refreshItems())
            .catch(()=>{})
    }

    closeAlert()
    {
        this.setState({ show_alert : false })
    }

    addItemClicked() {
        this.props.history.push(`/countries/-1`)
    }

    render() {
        if (this.state.hidden)
            return null;
        return (
            <div className="m-4">
                <div className="row my-2 mr-0">
                    <h3>Пользователи</h3>
                    <button className="btn btn-outline-secondary ml-auto"
                            onClick={this.addItemClicked}><FontAwesomeIcon icon={faPlus}/>{' '}Добавить</button>
                    <button className="btn btn-outline-secondary ml-2"
                            onClick={this.deleteItemsClicked}><FontAwesomeIcon icon={faTrash}/>{' '}Удалить</button>
                </div>
                <div className="row my-2 mr-0">
                    <PaginationComponent
                        totalRecords={this.state.totalCount}
                        pageLimit={this.state.limit}
                        pageNeighbours={1}
                        onPageChanged={this.onPageChanged} />
                    <table className="table table-sm">
                        <thead className="thead-light">
                        <tr>
                            <th>Логин</th>
                            <th>E-Mail</th>
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
                            this.state.items && this.state.items.map((item, index) =>
                                <tr key={item.id}>
                                    <td>{item.login}</td>
                                    <td>{item.email}</td>
                                    <td>
                                        <div className="btn-toolbar">
                                            <div className="btn-group  ml-auto">
                                                <button className="btn btn-outline-secondary btn-sm btn-toolbar"
                                                        onClick={() => this.updateItemClicked(item.id)}>
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

export default UserListComponent;