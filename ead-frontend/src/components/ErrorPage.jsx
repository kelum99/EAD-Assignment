import React from "react";

const ErrorPage = ({errorCode}) => {
    return (
        <div>
            {errorCode === 404 ? (<div>
                <span>page Not Found</span>
            </div>) : errorCode === 401 ? (<div>
                <span>Unauthorized</span>
            </div>) : (<div></div>)}
        </div>
    )
}

export default ErrorPage;
