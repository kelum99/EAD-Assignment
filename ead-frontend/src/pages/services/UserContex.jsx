import React, {createContext, useContext, useEffect, useState} from 'react';
import jwt_decode from 'jwt-decode';

export const UserContext = createContext({});

export const UserContextProvider = ({ children }) => {
    const [user, setUser] = useState();

    const decodeToken = (token) => {
        if (token) {
            const userData = jwt_decode(token);
            setUser(userData);
        } else {
            const localStorageToken = localStorage.getItem('token');
            if (localStorageToken) {
                decodeToken(localStorageToken);
            }
        }
    };
    useEffect(() => {
        decodeToken();
    }, []);

    return (
        <UserContext.Provider value={{ user, setUser, decodeToken }}>{children}</UserContext.Provider>
    );
};

const useUser = () => {
    const context = useContext(UserContext);
    if (context) {
        return context;
    }
};

export default useUser;
