import { API_BASE_URL, fetchWithAuth } from "../../config/api.js";
import { useState, useEffect } from 'react'
import { toast } from "react-toastify";


function ExcluirPaciente({ onClose }) {
    const [pacientes, setPacientes] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchPacientes();
    }, []);

    const fetchPacientes = async () => {
        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/pacientes`);
            const data = await response.json();
            setPacientes(data.content);
            setLoading(false);
        } catch (error) {
            setLoading(false);
            toast.error(`❌ Erro na requisição: ${error.message}`);
        }
    };

    const handleDelete = async (id) => {
        if (!confirm('Tem certeza que deseja excluir este paciente?')) {
            return;
        }

        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/pacientes/${id}`, {
                method: 'POST'
            });

            if (response.ok) {
                toast.success('✅ Paciente excluido com sucesso!');
                onClose();
            } else {
                const errorData = await response.json().catch(() => ({}));
                const errorMessage = errorData.message || 'Erro ao excluir paciente';
                toast.error(`❌ ${errorMessage}`);
            }
        } catch (error) {
            toast.error(`❌ Erro na requisição: ${error.message}`);
        }
    };

    if (loading) {
        return (
            <div style={{ textAlign: 'center', padding: '40px' }}>
                <div className="loading-spinner"></div>
            </div>
        );
    }

    return (
        <>
            <div className="table-container">
                {pacientes.length === 0 ? (
                    <div className="empty-state">
                        <div className="empty-state-icon">📭</div>
                        <h4>Nenhum paciente encontrado</h4>
                    </div>
                ) : (
                    <div style={{ maxHeight: '400px', overflowY: 'auto' }}>
                        {pacientes.map(paciente => (
                            <div
                                key={paciente.id}
                                style={{
                                    padding: '16px',
                                    borderBottom: '1px solid var(--border-light)',
                                    display: 'flex',
                                    justifyContent: 'space-between',
                                    alignItems: 'center'
                                }}
                            >
                                <div>
                                    <strong>{paciente.nome}</strong>
                                    <p style={{
                                        fontSize: '0.875rem',
                                        color: 'var(--text-secondary)',
                                        marginTop: '4px'
                                    }}>
                                        {paciente.cpf} • {paciente.email}
                                    </p>
                                </div>
                                <button
                                    className="button button-danger"
                                    onClick={() => handleDelete(paciente.id)}
                                >
                                    Excluir
                                </button>
                            </div>
                        ))}
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

export default ExcluirPaciente;