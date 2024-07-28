import {NavLink, Outlet} from "react-router-dom";

export default function Layout(){
    return(
        <div className="p-2">
            <nav>
                <NavLink to="/" className="btn btn-primary me-2">Home</NavLink>
                <NavLink to="/chat" className="btn btn-info">Chat</NavLink>
            </nav>
            <main className="p-3 mt-2">
                <Outlet></Outlet>
            </main>
        </div>
    );
}