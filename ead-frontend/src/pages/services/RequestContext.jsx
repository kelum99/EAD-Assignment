import React, {useState, createContext, useContext, useMemo, useEffect} from 'react';
import axios from 'axios';

export const RequestContext = createContext({});

export const RequestContextProvider = (props) => {
    const {children, baseURL} = props;
    const [token, setToken] = useState();

    const UpdateToken = (value) => {
        if (value) {
            setToken(value);
            localStorage.setItem('token', value);
        } else {
            setToken(undefined);
            localStorage.removeItem('token');
        }
    };
    useEffect(() => {
        if (!token) {
            const newToken = localStorage.getItem('token');
            UpdateToken(newToken);
        }
    }, [token]);

    const request = useMemo(() => {
        if (token) {
            return axios.create({
                baseURL,
                timeout: 10000,
                headers: {Authorization: `Bearer ${token}`}
            });
        }
        return axios.create({
            baseURL,
            timeout: 10000
        });
    }, [baseURL, token]);

    return (
        <RequestContext.Provider value={{request, token, UpdateToken}}>
            {children}
        </RequestContext.Provider>
    );
};

const useRequest = () => {
    const context = useContext(RequestContext);
    if (context) {
        return context;
    }
};

export default useRequest;
