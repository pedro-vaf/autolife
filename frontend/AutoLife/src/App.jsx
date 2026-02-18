import { useState, useEffect } from 'react'
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";


// Componentes de login
import Login from './components/login/Login.jsx'

// Componentes compartilhados
import Header from './components/shared/Header'
import Navigation from './components/shared/Navigation'
import Modal from './components/shared/Modal'

// Componentes de Médicos
import FormCadastroMedico from './components/medicos/FormCadastroMedico'
import ListagemMedicos from './components/medicos/ListagemMedicos'
import FormAtualizarMedico from './components/medicos/FormAtualizarMedico'
import ExcluirMedico from './components/medicos/ExcluirMedico'

// Componentes de Pacientes
import FormCadastroPaciente from './components/pacientes/FormCadastroPaciente'
import ListagemPacientes from './components/pacientes/ListagemPacientes'
import FormAtualizarPaciente from './components/pacientes/FormAtualizarPaciente'
import ExcluirPaciente from './components/pacientes/ExcluirPaciente'

// Componentes de Consultas
import FormAgendarConsulta from './components/consultas/FormAgendarConsulta'
import CancelarConsulta from './components/consultas/CancelarConsulta'


const FEATURES = {
    medicos: [
        {
            id: 'cadastro-medico',
            title: 'Cadastro de Médicos',
            description: 'Adicione novos médicos ao sistema',
            icon: '➕',
            render: (closeModal) => <FormCadastroMedico onClose={closeModal} />,
            modalTitle: 'Cadastrar Médico',
            modalIcon: '➕'
        },
        {
            id: 'listagem-medicos',
            title: 'Listagem de Médicos',
            description: 'Visualize todos os médicos cadastrados',
            icon: '📋',
            render: (closeModal) => <ListagemMedicos onClose={closeModal} />,
            modalTitle: 'Listagem de Médicos',
            modalIcon: '📋'
        },
        {
            id: 'atualizar-medico',
            title: 'Atualizar Médico',
            description: 'Edite informações de médicos existentes',
            icon: '✏️',
            render: (closeModal) => <FormAtualizarMedico onClose={closeModal} />,
            modalTitle: 'Atualizar Médico',
            modalIcon: '✏️'
        },
        {
            id: 'excluir-medico',
            title: 'Excluir Médico',
            description: 'Remova médicos do sistema',
            icon: '🗑️',
            render: (closeModal) => <ExcluirMedico onClose={closeModal} />,
            modalTitle: 'Excluir Médico',
            modalIcon: '🗑️'
        }
    ],
    pacientes: [
        {
            id: 'cadastro-paciente',
            title: 'Cadastro de Pacientes',
            description: 'Registre novos pacientes no sistema',
            icon: '➕',
            render: (closeModal) => <FormCadastroPaciente onClose={closeModal} />,
            modalTitle: 'Cadastrar Paciente',
            modalIcon: '➕'
        },
        {
            id: 'listagem-pacientes',
            title: 'Listagem de Pacientes',
            description: 'Visualize todos os pacientes cadastrados',
            icon: '📋',
            render: (closeModal) => <ListagemPacientes onClose={closeModal} />,
            modalTitle: 'Listagem de Pacientes',
            modalIcon: '📋'
        },
        {
            id: 'atualizar-paciente',
            title: 'Atualizar Paciente',
            description: 'Edite dados dos pacientes existentes',
            icon: '✏️',
            render: (closeModal) => <FormAtualizarPaciente onClose={closeModal} />,
            modalTitle: 'Atualizar Paciente',
            modalIcon: '✏️'
        },
        {
            id: 'excluir-paciente',
            title: 'Excluir Paciente',
            description: 'Remova pacientes do sistema',
            icon: '🗑️',
            render: (closeModal) => <ExcluirPaciente onClose={closeModal} />,
            modalTitle: 'Excluir Paciente',
            modalIcon: '🗑️'
        }
    ],
    consultas: [
        {
            id: 'agendar-consulta',
            title: 'Agendar Consulta',
            description: 'Agende uma nova consulta para um paciente',
            icon: '📅',
            render: (closeModal) => <FormAgendarConsulta onClose={closeModal} />,
            modalTitle: 'Agendar Consulta',
            modalIcon: '📅'
        },
        {
            id: 'cancelar-consulta',
            title: 'Cancelar Consulta',
            description: 'Cancele consultas agendadas com motivo',
            icon: '❌',
            render: (closeModal) => <CancelarConsulta onClose={closeModal} />,
            modalTitle: 'Cancelar Consulta',
            modalIcon: '❌'
        }
    ]
}

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const [currentUser, setCurrentUser] = useState(null)
    const [token, setToken] = useState(null)

    const [activeTab, setActiveTab] = useState('medicos')

    const [modal, setModal] = useState({
        isOpen: false,
        title: '',
        icon: '',
        feature: null
    })

    useEffect(() => {
        const savedToken = localStorage.getItem('token')
        const savedLogin = localStorage.getItem('login')

        if (savedToken && savedLogin) {
            setToken(savedToken)
            setCurrentUser(savedLogin)
            setIsAuthenticated(true)
        }
    }, [])

    const handleLoginSuccess = (jwtToken, login) => {
        setToken(jwtToken)
        setCurrentUser(login)
        setIsAuthenticated(true)
    }

    const handleLogout = () => {
        localStorage.removeItem('token')
        localStorage.removeItem('login')
        setToken(null)
        setCurrentUser(null)
        setIsAuthenticated(false)
        setActiveTab('medicos')
    }

    const openModal = (feature) => {
        setModal({
            isOpen: true,
            title: feature.modalTitle,
            icon: feature.modalIcon,
            feature: feature
        })
    }

    const closeModal = () => {
        setModal({
            isOpen: false,
            title: '',
            icon: '',
            feature: null
        })
    }

    const getCardColorClass = () => {
        const classes = {
            medicos: 'medico',
            pacientes: 'paciente',
            consultas: 'consulta'
        }
        return classes[activeTab]
    }

    if (!isAuthenticated) {
        return <Login onLoginSuccess={handleLoginSuccess} />
    }

    const features = FEATURES[activeTab];
    const isTwoItems = features.length === 2;

    // Sistema autenticado
    return (
        <div className="app-container">
            <Header currentUser={currentUser} onLogout={handleLogout} />

            <main className="main-content">
                <Navigation activeTab={activeTab} setActiveTab={setActiveTab} />

                <div className="features-grid">
                    {FEATURES[activeTab].map((feature) => (
                        <div
                            key={feature.id}
                            className={`feature-card ${isTwoItems ? 'span-2' : ''}`}
                            onClick={() => openModal(feature)}
                        >
                            <div className="feature-card-header">
                                <div className={`feature-icon ${getCardColorClass()}`}>
                                    <span style={{ fontSize: '24px' }}>{feature.icon}</span>
                                </div>
                                <div className="feature-card-title">
                                    <h3>{feature.title}</h3>
                                </div>
                            </div>
                            <p className="feature-card-description">{feature.description}</p>
                            <div className="feature-card-action">
                                <span>Acessar</span>
                                <span>→</span>
                            </div>
                        </div>
                    ))}
                </div>
            </main>

            <Modal
                isOpen={modal.isOpen}
                onClose={closeModal}
                title={modal.title}
                icon={modal.icon}
            >
                {modal.feature && modal.feature.render(closeModal)}
            </Modal>

            <ToastContainer
                position="top-right"
                autoClose={3000}
                pauseOnHover
                closeOnClick
            />
        </div>
    )
}

export function getAuthToken() {
    return localStorage.getItem('token')
}

export default App