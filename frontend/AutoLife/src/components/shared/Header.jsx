import iconAutolife from "../../assets/images/hospital.svg"

function Header({ currentUser, onLogout }) {
    return (
        <header className="header">
            <div className="container">
                <div className="header-content">
                    <div className="header-text">
                        <span>
                            <img className="img-icon" src={iconAutolife}  alt="Simbolo AutoLife"/>
                        </span>
                        <h1>AutoLife</h1>
                        <h3>Gestão completa de médicos, pacientes e consultas</h3>
                    </div>
                </div>
                {currentUser && onLogout && (
                    <div className="header-user">
                        <button
                            className="logout-button"
                            onClick={onLogout}
                            title="Sair do sistema"
                        >
                            Sair
                        </button>
                    </div>
                )}
            </div>
        </header>
    );
}

export default Header;