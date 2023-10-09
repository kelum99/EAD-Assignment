import './App.css';
import {BrowserRouter} from 'react-router-dom';
import MainRoutes from "./MainRoutes";
import {RequestContextProvider} from "./pages/services/RequestContext";
import {UserContextProvider} from "./pages/services/UserContex";
import {useEffect} from "react";
function App() {
    useEffect(() => {
        console.log('aaa',process.env.REACT_APP_API_URL)
    })
    return (
        <RequestContextProvider baseURL={process.env.REACT_APP_API_URL}>
            <UserContextProvider>
                <BrowserRouter>
                    <MainRoutes/>
                </BrowserRouter>
            </UserContextProvider>
        </RequestContextProvider>
    );
}

export default App;
