import { API_BASE_URL, fetchWithAuth, getAuthToken } from "../../config/api.js";
import { useState } from 'react'
import { toast } from "react-toastify";
import { mapMedicoCreatePayload } from "../../utils/mapper.jsx";
import { PatternFormat } from 'react-number-format';

function FormCadastroMedico({ onClose }) {

    const token = getAuthToken();

    const [formData, setFormData] = useState({
        nome: '',
        email: '',
        telefone: '',
        crm: '',
        especialidade: '',
        logradouro: '',
        numero: '',
        complemento: '',
        bairro: '',
        cidade: '',
        uf: '',
        cep: ''
    });

    const handleChange = (field, value) => {
        setFormData(prev => ({
            ...prev,
            [field]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = mapMedicoCreatePayload(formData);

        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/medicos`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                toast.success('Médico cadastrado com sucesso!');
                onClose();
            } else {
                const errorData = await response.json().catch(() => ({}));
                const errorMessage = errorData.message || 'Erro ao cadastrar médico';
                toast.error(`${errorMessage}`);
            }
        } catch (error) {
            toast.error(`Erro na requisição: ${error.message}`);
        }
    };

    return (
        <form onSubmit={ handleSubmit }>
            <div className="form-group">
                <label className="form-label form-label-required">Nome Completo</label>
                <input
                    type="text"
                    className="form-input"
                    value={formData.nome}
                    onChange={(e) => handleChange('nome', e.target.value)}
                    placeholder="Digite o nome completo"
                    required
                />
            </div>

            <div className="form-group">
                <label className="form-label form-label-required">E-mail</label>
                <input
                    type="email"
                    className="form-input"
                    value={formData.email}
                    onChange={(e) => handleChange('email', e.target.value)}
                    placeholder="medico@exemplo.com"
                    required
                />
            </div>

            <div className="form-group">
                <label className="form-label form-label-required">Telefone</label>
                <PatternFormat
                    format="(##) #####-####"
                    type="tel"
                    className="form-input"
                    value={formData.telefone}
                    onChange={(e) => handleChange('telefone', e.target.value)}
                    placeholder="somente número"
                    required
                />
            </div>

            <div className="form-group">
                <label className="form-label form-label-required">CRM</label>
                <PatternFormat
                    format="######"
                    type="text"
                    className="form-input"
                    value={formData.crm}
                    onChange={(e) => handleChange('crm', e.target.value)}
                    placeholder="somente número"
                    required
                />
            </div>

            <div className="form-group">
                <label className="form-label form-label-required">Especialidade</label>
                <select
                    className="form-input"
                    value={formData.especialidade}
                    onChange={(e) => handleChange('especialidade', e.target.value)}
                    required
                >
                    <option value="">Selecione</option>
                    <option value="ORTOPEDIA">Ortopedia</option>
                    <option value="CARDIOLOGIA">Cardiologia</option>
                    <option value="GINECOLOGIA">Ginecologia</option>
                    <option value="DERMATOLOGIA">Dermatologia</option>
                </select>
            </div>

            <fieldset className="form-section">
                <div className="form-group">
                    <label className="form-label form-label-required">Logradouro (Rua, Avenida, etc.)</label>
                    <input
                        type="text"
                        className="form-input"
                        value={formData.logradouro}
                        onChange={(e) => handleChange('logradouro', e.target.value)}
                        placeholder="Rua Catuzinho"
                        required
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
                    <label className="form-label form-label-required">Bairro</label>
                    <input
                        type="text"
                        className="form-input"
                        value={formData.bairro}
                        onChange={(e) => handleChange('bairro', e.target.value)}
                        placeholder="Star"
                        required
                    />
                </div>

                <div className="form-row">
                    <div className="form-group form-group-half">
                        <label className="form-label form-label-required">Cidade</label>
                        <input
                            type="text"
                            className="form-input"
                            value={formData.cidade}
                            onChange={(e) => handleChange('cidade', e.target.value)}
                            placeholder="Pojuca"
                            required
                        />
                    </div>

                    <div className="form-group form-group-half">
                        <label className="form-label form-label-required">UF</label>
                        <select
                            className="form-input"
                            value={formData.uf}
                            onChange={(e) => handleChange('uf', e.target.value)}
                            required
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
                    <label className="form-label form-label-required">CEP</label>
                    <PatternFormat
                        format="#####-###"
                        type="text"
                        className="form-input"
                        value={formData.cep}
                        onChange={(e) => handleChange('cep', e.target.value)}
                        placeholder="somente número"
                        required
                    />
                </div>
            </fieldset>

            <div className="button-group">
                <button type="button" className="button button-secondary" onClick={onClose}>
                    Cancelar
                </button>
                <button type="submit" className="button button-primary">
                    Salvar
                </button>
            </div>
        </form>
    );
}

export default FormCadastroMedico;