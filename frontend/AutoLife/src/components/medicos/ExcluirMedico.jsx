import { API_BASE_URL, fetchWithAuth } from "../../config/api.js";
import { useState, useEffect } from 'react'
import { toast } from "react-toastify";


function ExcluirMedico({ onClose }) {
    const [medicos, setMedicos] = useState([]);
    const [loading, setLoading] = useState(true);

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

    const handleDelete = async (id) => {
        if (!confirm('Tem certeza que deseja excluir este médico?')) {
            return;
        }

        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/medicos/${id}`, {
                method: 'POST'
            });

            if (response.ok) {
                toast.success('Médico excluido com sucesso!');
                onClose();
            } else {
                const errorData = await response.json().catch(() => ({}));
                const errorMessage = errorData.message || 'Erro ao excluir médico';
                toast.error(`${errorMessage}`);
            }
        } catch (error) {
            toast.error(`Erro na requisição: ${error.message}`);
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
                {medicos.length === 0 ? (
                    <div className="empty-state">
                        <div className="empty-state-icon">📭</div>
                        <h4>Nenhum médico encontrado</h4>
                    </div>
                ) : (
                    <div style={{ maxHeight: '400px', overflowY: 'auto' }}>
                        {medicos.map(medico => (
                            <div
                                key={medico.id}
                                style={{
                                    padding: '16px',
                                    borderBottom: '1px solid var(--border-light)',
                                    display: 'flex',
                                    justifyContent: 'space-between',
                                    alignItems: 'center'
                                }}
                            >
                                <div>
                                    <strong>{medico.nome}</strong>
                                    <p style={{
                                        fontSize: '0.875rem',
                                        color: 'var(--text-secondary)',
                                        marginTop: '4px'
                                    }}>
                                        {medico.crm} • {medico.especialidade}
                                    </p>
                                </div>
                                <button
                                    className="button button-danger"
                                    onClick={() => handleDelete(medico.id)}
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

export default ExcluirMedico;