import { useState, useEffect } from 'react'
import { API_BASE_URL, fetchWithAuth } from '../../config/api'
import { mapConsultaToPayload, validarAntecedencia30min } from '../../utils/mapper.jsx'
import { toast } from "react-toastify";

function FormAgendarConsulta({ onClose }) {
    const [formData, setFormData] = useState({
        pacienteId: '',
        medicoId: '',
        data: '',
        hora: ''
    })

    const [pacientes, setPacientes] = useState([])
    const [medicos, setMedicos] = useState([])
    const [loading, setLoading] = useState(true)
    const [submitting, setSubmitting] = useState(false)
    const [errors, setErrors] = useState({})

    useEffect(() => {
        carregarDados()
    }, [])

    const carregarDados = async () => {
        try {
            const resPacientes = await fetchWithAuth(`${API_BASE_URL}/pacientes`)
            const dataPacientes = await resPacientes.json()
            setPacientes(dataPacientes.content)

            const resMedicos = await fetchWithAuth(`${API_BASE_URL}/medicos`)
            const dataMedicos = await resMedicos.json()
            setMedicos(dataMedicos.content)

            setLoading(false)
        } catch (error) {
            toast.error('Erro ao carregar pacientes e médicos')
            setLoading(false)
        }
    }

    const handleChange = (field, value) => {
        setFormData(prev => ({
            ...prev,
            [field]: value
        }))

        if (errors[field]) {
            setErrors(prev => ({ ...prev, [field]: null }))
        }
    }

    const validarFormulario = () => {
        const newErrors = {}

        if (!formData.pacienteId) {
            newErrors.pacienteId = 'Selecione um paciente'
        }

        if (!formData.data) {
            newErrors.data = 'Selecione uma data'
        }

        if (!formData.hora) {
            newErrors.hora = 'Selecione um horário'
        }

        if (formData.data) {
            const dataConsulta = new Date(`${formData.data}T${formData.hora || '10:00'}`)
            const diaSemana = dataConsulta.getDay()

            if (diaSemana === 0) {
                newErrors.data = 'Clínica não funciona aos domingos'
            }
        }

        if (formData.hora) {
            const [hora] = formData.hora.split(':').map(Number)
            if (hora < 7 || hora > 18) {
                newErrors.hora = 'Horário deve ser entre 07:00 e 18:00'
            }
        }

        if (formData.data && formData.hora) {
            if (!validarAntecedencia30min(formData.data, formData.hora)) {
                newErrors.data = 'Consulta deve ser agendada com 30 minutos de antecedência'
            }
        }

        setErrors(newErrors)
        return Object.keys(newErrors).length === 0
    }

    const handleSubmit = async (e) => {
        e.preventDefault()

        if (!validarFormulario()) {
            return
        }

        setSubmitting(true)

        try {
            const payload = mapConsultaToPayload(formData)

            const response = await fetchWithAuth(`${API_BASE_URL}/consultas/marcar`, {
                method: 'POST',
                body: JSON.stringify(payload)
            })

            if (!response.ok) {
                const error = await response.json()
                throw new Error(error.message || 'Erro ao agendar consulta')
            }

            const result = await response.json()

            toast.success(
                `✅ Consulta agendada com sucesso!\n\n` +
                `Paciente: ${result.paciente}\n` +
                `Médico: ${result.medico}\n` +
                `Data: ${new Date(result.inicioConsulta).toLocaleString('pt-BR')}`
            )

            onClose()

        } catch (error) {
            toast.error(`Erro ao agendar consulta:\n${error.message}`)
        } finally {
            setSubmitting(false)
        }
    }

    if (loading) {
        return (
            <div style={{ textAlign: 'center', padding: '40px' }}>
                <div className="loading-spinner"></div>
                <p style={{ marginTop: '16px', color: 'var(--text-secondary)' }}>
                    Carregando...
                </p>
            </div>
        )
    }

    return (
        <form onSubmit={handleSubmit}>
            <div style={{
                background: 'rgba(59, 130, 246, 0.1)',
                border: '1px solid rgba(59, 130, 246, 0.3)',
                borderRadius: '8px',
                padding: '12px',
                marginBottom: '20px'
            }}>
                <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', margin: 0 }}>
                    <strong>📋 Regras de Agendamento:</strong><br/>
                    • Horário: Segunda a Sábado, 07:00 às 18:00<br/>
                    • Duração: 1 hora (automático)<br/>
                    • Antecedência mínima: 30 minutos<br/>
                    • Médico opcional (sistema escolhe se vazio)
                </p>
            </div>

            <div className="form-group">
                <label className="form-label form-label-required">Paciente</label>
                <select
                    className="form-select"
                    value={formData.pacienteId}
                    onChange={(e) => handleChange('pacienteId', e.target.value)}
                    disabled={submitting}
                >
                    <option value="">Selecione um paciente</option>
                    {pacientes.map(paciente => (
                        <option key={paciente.id} value={paciente.id}>
                            {paciente.nome}
                        </option>
                    ))}
                </select>
                {errors.pacienteId && (
                    <span className="form-helper" style={{ color: 'var(--color-danger)' }}>
                        {errors.pacienteId}
                    </span>
                )}
                {pacientes.length === 0 && (
                    <span className="form-helper" style={{ color: 'var(--color-warning)' }}>
                        Nenhum paciente ativo encontrado
                    </span>
                )}
            </div>

            <div className="form-group">
                <label className="form-label">Médico (opcional)</label>
                <select
                    className="form-select"
                    value={formData.medicoId}
                    onChange={(e) => handleChange('medicoId', e.target.value)}
                    disabled={submitting}
                >
                    <option value="">Sistema escolherá automaticamente</option>
                    {medicos.map(medico => (
                        <option key={medico.id} value={medico.id}>
                            {medico.nome} - {medico.especialidade}
                        </option>
                    ))}
                </select>
                <span className="form-helper">
                    Deixe em branco para o sistema escolher um médico disponível
                </span>
            </div>

            <div className="form-group">
                <label className="form-label form-label-required">Data da Consulta</label>
                <input
                    type="date"
                    className="form-input"
                    value={formData.data}
                    onChange={(e) => handleChange('data', e.target.value)}
                    min={new Date().toISOString().split('T')[0]}
                    disabled={submitting}
                />
                {errors.data && (
                    <span className="form-helper" style={{ color: 'var(--color-danger)' }}>
                        {errors.data}
                    </span>
                )}
                <span className="form-helper">
                    Segunda a Sábado (fechado aos domingos)
                </span>
            </div>

            <div className="form-group">
                <label className="form-label form-label-required">Horário de Início</label>
                <select
                    className="form-select"
                    value={formData.hora}
                    onChange={(e) => handleChange('hora', e.target.value)}
                    disabled={submitting}
                >
                    <option value="">Selecione um horário</option>
                    <option value="07:00">07:00 (termina às 08:00)</option>
                    <option value="08:00">08:00 (termina às 09:00)</option>
                    <option value="09:00">09:00 (termina às 10:00)</option>
                    <option value="10:00">10:00 (termina às 11:00)</option>
                    <option value="11:00">11:00 (termina às 12:00)</option>
                    <option value="12:00">12:00 (termina às 13:00)</option>
                    <option value="13:00">13:00 (termina às 14:00)</option>
                    <option value="14:00">14:00 (termina às 15:00)</option>
                    <option value="15:00">15:00 (termina às 16:00)</option>
                    <option value="16:00">16:00 (termina às 17:00)</option>
                    <option value="17:00">17:00 (termina às 18:00)</option>
                    <option value="18:00">18:00 (termina às 19:00) - Último horário</option>
                </select>
                {errors.hora && (
                    <span className="form-helper" style={{ color: 'var(--color-danger)' }}>
                        {errors.hora}
                    </span>
                )}
                <span className="form-helper">
                    Consultas duram 1 hora
                </span>
            </div>

            <div className="button-group">
                <button
                    type="button"
                    className="button button-secondary"
                    onClick={onClose}
                    disabled={submitting}
                >
                    Cancelar
                </button>
                <button
                    type="submit"
                    className="button button-primary"
                    disabled={submitting}
                >
                    {submitting ? 'Agendando...' : 'Agendar Consulta'}
                </button>
            </div>
        </form>
    )
}

export default FormAgendarConsulta