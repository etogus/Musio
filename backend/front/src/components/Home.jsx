import React from 'react';

class Home extends React.Component {

    constructor(props) {
        super(props);
    }

    // className=”mt-5” это из bootstrap. Сдвигает надпись вниз, подальше от панели навигатора
    render() {
        return (
            <div className="mt-5">
                <h2>RPO Art Frontend</h2>
            </div>
        );
    }
}

export default Home;