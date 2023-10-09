import './App.css';
import {BrowserRouter} from 'react-router-dom';
import MainRoutes from "./MainRoutes";
import {RequestContextProvider} from "./pages/services/RequestContext";
import {UserContextProvider} from "./pages/services/UserContex";

function App() {
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
