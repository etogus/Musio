import React from 'react';
import './App.css';
import { Router, Switch, Route } from "react-router-dom";
import { createBrowserHistory } from 'history';
import NavigationBar from './components/NavigationBar'
import Home from "./components/Home";

// history - библиотечный класс который позволяет перемещаться (по страницам)
// по компонентам веб приложения. Например так history.push(“/home”).
// Router позволяет привязать компоненты к локальным путям внутри приложения и
// автоматизировать перемещение между компонентами с помощью history. Поэтому мы
// создаем и передаем объект history компоненту React через props.
function App() {
    return (
        <div className="App">
            <Router history = { createBrowserHistory() }>
                <NavigationBar/>
                <div className="container-fluid">
                    <Switch>
                        <Route path="/home/" exact component={Home} />
                    </Switch>
                </div>
            </Router>
        </div>
    );
}

export default App;