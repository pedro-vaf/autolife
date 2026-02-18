import { API_BASE_URL, fetchWithAuth } from "../../config/api.js";
import { useState, useEffect } from 'react'
import { toast } from "react-toastify";

function ListagemPacientes({ onClose }) {
    const [pacientes, setPaciente] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        fetchPaciente();
    }, []);

    const fetchPaciente = async () => {
        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/pacientes`);
            const data = await response.json();
            setPaciente(data.content);
            setLoading(false);
        } catch (error) {
            setLoading(false);
            toast.error(`❌ Erro na requisição: ${error.message}`);
        }
    };

    const filteredPacientes = pacientes.filter(pacientes =>
        Object.values(pacientes).some(value =>
            String(value).toLowerCase().includes(searchTerm.toLowerCase())
        )
    );

    if (loading) {
        return (
            <div style={{ textAlign: 'center', padding: '40px' }}>
                <div className="loading-spinner"></div>
                <p style={{ marginTop: '16px', color: 'var(--text-secondary)' }}>
                    Carregando pacientes...
                </p>
            </div>
        );
    }

    return (<>
        <div className="table-container">
            <table className="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Email</th>
                    <th>CPF</th>
                    <th>Telefone</th></tr></thead>
                <tbody>
                {filteredPacientes.map(pacientes => <tr key={pacientes.id}>
                    <td>{pacientes.id}</td>
                    <td>{pacientes.nome}</td>
                    <td>{pacientes.email}</td>
                    <td>{pacientes.cpf}</td>
                </tr>)}
                </tbody>
            </table>
        </div>
        <div className="button-group">
            <button type="button" className="button button-secondary" onClick={onClose}>
                Fechar
            </button>
        </div>
    </>);
}

export default ListagemPacientes;