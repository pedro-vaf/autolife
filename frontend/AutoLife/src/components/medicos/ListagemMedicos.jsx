import { API_BASE_URL, fetchWithAuth } from "../../config/api.js";
import { useState, useEffect } from 'react'
import { toast } from "react-toastify";

function ListagemMedicos({ onClose }) {
    const [medicos, setMedicos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        fetchMedicos();
    }, []);

    const fetchMedicos = async () => {
        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/medicos`);
            const data = await response.json();
            setMedicos(data.content);
            setLoading(false);
        } catch (error) {
            setLoading(false);
            toast.error(`Erro na requisição: ${error.message}`);
        }
    };

    const filteredMedicos = medicos.filter((medico) =>
        [medico.nome, medico.email, medico.crm, medico.especialidade]
            .some((value) =>
                value?.toString().toLowerCase().includes(searchTerm.toLowerCase())
            )
    );

    if (loading) {
        return (
            <div style={{ textAlign: 'center', padding: '40px' }}>
                <div className="loading-spinner"></div>
                <p style={{ marginTop: '16px', color: 'var(--text-secondary)' }}>
                    Carregando médicos...
                </p>
            </div>
        );
    }

    return (
        <>
            <div className="search-container">
                <span className="search-icon">🔍</span>
                <input
                    type="text"
                    className="search-input"
                    placeholder="Buscar médico..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>

            <div className="table-container">
                {filteredMedicos.length === 0 ? (
                    <div className="empty-state">
                        <div className="empty-state-icon">📭</div>
                        <h4>Nenhum médico encontrado</h4>
                        <p>Não há médicos cadastrados</p>
                    </div>
                ) : (
                    <div className="table-wrapper">
                        <table className="table">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>Email</th>
                                <th>CRM</th>
                                <th>Especialidade</th>
                            </tr>
                            </thead>
                            <tbody>
                            {filteredMedicos.map(medico => (
                                <tr>
                                    <td><strong>{medico.id}</strong></td>
                                    <td><strong>{medico.nome}</strong></td>
                                    <td>{medico.email}</td>
                                    <td>{medico.crm}</td>
                                    <td>{medico.especialidade}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            <div className="button-group">
                <button type="button" className="button button-secondary" onClick={onClose}>
                    Fechar
                </button>
            </div>
        </>
    );
}

export default ListagemMedicos;