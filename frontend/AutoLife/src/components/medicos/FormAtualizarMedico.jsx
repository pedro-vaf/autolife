import {API_BASE_URL, fetchWithAuth, getAuthToken} from "../../config/api.js";
import { useState, useEffect } from 'react'
import { toast } from "react-toastify";
import { mapMedicoUpdatePayload } from "../../utils/mapper.jsx";
import { PatternFormat } from 'react-number-format';

function FormAtualizarMedico({ onClose }) {

    const token = getAuthToken()

    const [medicos, setMedicos] = useState([]);
    const [selectedId, setSelectedId] = useState('');
    const [formData, setFormData] = useState({
        nome: '',
        crm: '',
        especialidade: '',
        telefone: '',
        email: '',
        endereco: ''
    });
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchMedicos();
    }, []);

    const fetchMedicos = async () => {
        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/medicos`);
            const data = await response.json();
            setMedicos(data.content);
        } catch (error) {
            setLoading(false);
            toast.error(`Erro na requisição: ${error.message}`);
        } finally {
            setLoading(false);
        }
    };

    const handleSelect = async (id) => {
        if (!id) {
            setSelectedId('');
            setFormData({
                nome: '', crm: '', especialidade: '', telefone: '', email: '', endereco: ''
            });
            return;
        }

        setSelectedId(id);
        setLoading(true);

        try {
            const response = await fetch(`${API_BASE_URL}/medicos`);
            const data = await response.json();
            setFormData({
                nome: data.nome || '',
                crm: data.crm || '',
                especialidade: data.especialidade || '',
                telefone: data.telefone || '',
                email: data.email || '',
                endereco: data.endereco || ''
            });
        } catch (error) {
            setLoading(false);
            toast.error(`Erro na requisição: ${error.message}`);
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (field, value) => {
        setFormData(prev => ({
            ...prev,
            [field]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = mapMedicoUpdatePayload(formData)

        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/medicos/${selectedId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                toast.success('Médico atualizado com sucesso!');
                onClose();
            } else {
                const errorData = await response.json().catch(() => ({}));
                const errorMessage = errorData.message || 'Erro ao atualizar paciente';
                toast.error(`${errorMessage}`);
            }
        } catch (error) {
            toast.error(`Erro na requisição: ${error.message}`);
        }
    };

    return (
        <>
            <div className="form-group">
                <label className="form-label">Selecione o médico para atualizar</label>
                <select
                    className="form-select"
                    value={selectedId}
                    onChange={(e) => handleSelect(e.target.value)}
                >
                    <option value="">Selecione...</option>
                    {medicos.map(medico => (
                        <option key={medico.id} value={medico.id}>
                            {medico.nome} - {medico.crm}
                        </option>
                    ))}
                </select>
            </div>

            {selectedId && !loading && (
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label className="form-label">Nome Completo</label>
                        <input
                            type="text"
                            className="form-input"
                            value={formData.nome}
                            onChange={(e) => handleChange('nome', e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label className="form-label">Telefone</label>
                        <PatternFormat
                            format="(##) #####-####"
                            type="tel"
                            className="form-input"
                            value={formData.telefone}
                            onChange={(e) => handleChange('telefone', e.target.value)}
                            placeholder="somente número"
                        />
                    </div>

                    <fieldset className="form-section">
                        <div className="form-group">
                            <label className="form-label">Logradouro (Rua, Avenida, etc.)</label>
                            <input
                                type="text"
                                className="form-input"
                                value={formData.logradouro}
                                onChange={(e) => handleChange('logradouro', e.target.value)}
                                placeholder="Rua das Flores"
                            />
                        </div>

                        <div className="form-row">
                            <div className="form-group form-group-half">
                                <label className="form-label">Número</label>
                                <input
                                    type="text"
                                    className="form-input"
                                    value={formData.numero}
                                    onChange={(e) => handleChange('numero', e.target.value)}
                                    placeholder="123"
                                />
                            </div>

                            <div className="form-group form-group-half">
                                <label className="form-label">Complemento</label>
                                <input
                                    type="text"
                                    className="form-input"
                                    value={formData.complemento}
                                    onChange={(e) => handleChange('complemento', e.target.value)}
                                    placeholder="Apto 101 / Bloco B"
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label className="form-label">Bairro</label>
                            <input
                                type="text"
                                className="form-input"
                                value={formData.bairro}
                                onChange={(e) => handleChange('bairro', e.target.value)}
                                placeholder="Star"
                            />
                        </div>

                        <div className="form-row">
                            <div className="form-group form-group-half">
                                <label className="form-label">Cidade</label>
                                <input
                                    type="text"
                                    className="form-input"
                                    value={formData.cidade}
                                    onChange={(e) => handleChange('cidade', e.target.value)}
                                    placeholder="Pojuca"
                                />
                            </div>

                            <div className="form-group form-group-half">
                                <label className="form-label">UF</label>
                                <select
                                    className="form-input"
                                    value={formData.uf}
                                    onChange={(e) => handleChange('uf', e.target.value)}
                                >
                                    <option value="">Selecione</option>
                                    {[
                                        'AC','AL','AP','AM','BA','CE','DF','ES','GO','MA',
                                        'MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN',
                                        'RS','RO','RR','SC','SP','SE','TO'
                                    ].map(uf => (
                                        <option key={uf} value={uf}>{uf}</option>
                                    ))}
                                </select>
                            </div>
                        </div>

                        <div className="form-group">
                            <label className="form-label">CEP</label>
                            <PatternFormat
                                format="#####-###"
                                type="text"
                                className="form-input"
                                value={formData.cep}
                                onChange={(e) => handleChange('cep', e.target.value)}
                                placeholder="somente número"
                            />
                        </div>
                    </fieldset>

                    <div className="button-group">
                        <button type="button" className="button button-secondary" onClick={onClose}>
                            Cancelar
                        </button>
                        <button type="submit" className="button button-success">
                            Atualizar
                        </button>
                    </div>
                </form>
            )}

            {loading && (
                <div style={{ textAlign: 'center', padding: '40px' }}>
                    <div className="loading-spinner"></div>
                </div>
            )}

            {!selectedId && !loading && (
                <div className="button-group">
                    <button type="button" className="button button-secondary" onClick={onClose}>
                        Cancelar
                    </button>
                </div>
            )}
        </>
    );
}

export default FormAtualizarMedico;