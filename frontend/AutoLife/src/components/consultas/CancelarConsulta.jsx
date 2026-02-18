import { useState, useEffect } from 'react'
import { API_BASE_URL, fetchWithAuth } from '../../config/api'
import { mapCancelamentoToPayload } from '../../utils/mapper.jsx'
import { toast } from 'react-toastify'

function CancelarConsulta({ onClose }) {
    const [consultas, setConsultas] = useState([])
    const [selectedId, setSelectedId] = useState('')
    const [motivo, setMotivo] = useState('')
    const [loading, setLoading] = useState(true)
    const [submitting, setSubmitting] = useState(false)
    const [error, setError] = useState('')

    useEffect(() => {
        console.log("Testando")
        fetchConsultas()
    }, [])

    const fetchConsultas = async () => {
        try {
            const response = await fetchWithAuth(`${API_BASE_URL}/consultas`)
            const data = await response.json()
            setConsultas(data.content)
            setLoading(false);
        } catch (error) {
            setLoading(false);
            toast.error(`Erro na requisição: ${error.message}`);
        }
    }

    const validarAntecedencia24h = (inicioConsulta) => {
        const agora = new Date()
        const inicio = new Date(inicioConsulta)
        const diffHoras = (inicio - agora) / (1000 * 60 * 60)
        return diffHoras >= 24
    }

    const getMotivoTexto = (motivoKey) => {
        const motivos = {
            paciente_desistiu: 'Paciente desistiu',
            medico_cancelou: 'Médico cancelou',
            outros: 'Outros'
        }
        return motivos[motivoKey] || motivoKey
    }

    const handleCancelar = async () => {
        setError('')

        if (!selectedId) {
            setError('Selecione uma consulta para cancelar')
            return
        }

        if (!motivo) {
            setError('Selecione o motivo do cancelamento')
            return
        }

        const consulta = consultas.find(c => c.id === parseInt(selectedId, 10))

        if (!consulta) {
            setError('Consulta não encontrada')
            return
        }

        if (!validarAntecedencia24h(consulta.inicioConsulta)) {
            setError('Consulta só pode ser cancelada com antecedência mínima de 24 horas')
            return
        }

        const confirmou = confirm(
            `Confirma o cancelamento da consulta?\n\n` +
            `Paciente: ${consulta.pacienteNome}\n` +
            `Médico: ${consulta.medicoNome}\n` +
            `Data: ${consulta.data} às ${consulta.horaInicio}\n` +
            `Motivo: ${getMotivoTexto(motivo)}`
        )

        if (!confirmou) return

        setSubmitting(true)

        try {
            const payload = mapCancelamentoToPayload({
                consultaId: consulta.id,
                motivo
            })

            const response = await fetchWithAuth(
                `${API_BASE_URL}/consultas/${consulta.id}/cancelamento`,
                {
                    method: 'POST',
                    body: JSON.stringify(payload)
                }
            )

            if (!response.ok) {
                const errorData = await response.json()
                throw new Error(errorData.message || 'Erro ao cancelar consulta')
            }

            const result = await response.json()

            toast.success(
                `Consulta cancelada com sucesso!\n\n` +
                `Paciente: ${result.nomePaciente}\n` +
                `Motivo: ${getMotivoTexto(motivo)}\n` +
                `Data do cancelamento: ${new Date(result.dataCancelamento).toLocaleString('pt-BR')}`
            )

            setConsultas(prev =>
                prev.filter(c => c.id !== consulta.id)
            )
            setSelectedId('')
            setMotivo('')
        } catch (err) {
            toast.error(err.message || 'Erro ao cancelar consulta')
        } finally {
            setSubmitting(false)
        }
    }

    if (loading) {
        return (
            <div style={{ textAlign: 'center', padding: '40px' }}>
                <div className="loading-spinner" />
                <p style={{ marginTop: '16px', color: 'var(--text-secondary)' }}>
                    Carregando...
                </p>
            </div>
        )
    }

    return (
        <>
            <div style={{
                background: 'rgba(239, 68, 68, 0.1)',
                border: '1px solid rgba(239, 68, 68, 0.3)',
                borderRadius: '8px',
                padding: '12px',
                marginBottom: '16px'
            }}>
                <p style={{ fontSize: '0.875rem', color: '#991b1b', margin: 0 }}>
                    <strong>⚠️ Atenção:</strong> Cancelamento permitido apenas com antecedência mínima de 24 horas
                </p>
            </div>

            {consultas.length === 0 ? (
                <div className="empty-state">
                    <div className="empty-state-icon">📭</div>
                    <h4>Nenhuma consulta agendada</h4>
                    <p>Não há consultas disponíveis para cancelamento</p>
                </div>
            ) : (
                <>
                    <div className="form-group">
                        <label className="form-label form-label-required">
                            Selecione a consulta
                        </label>
                        <select
                            className="form-select"
                            value={selectedId}
                            onChange={(e) => {
                                setSelectedId(e.target.value)
                                setError('')
                            }}
                            disabled={submitting}
                        >
                            <option value="">Escolha uma consulta...</option>
                            {consultas.map(c => (
                                <option key={c.id} value={c.id}>
                                    {new Date(c.inicioConsulta).toLocaleString('pt-BR')} - {c.paciente.nome} com {c.medico.nome}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label className="form-label form-label-required">
                            Motivo do cancelamento
                        </label>
                        <select
                            className="form-select"
                            value={motivo}
                            onChange={(e) => {
                                setMotivo(e.target.value)
                                setError('')
                            }}
                            disabled={submitting}
                        >
                            <option value="">Selecione o motivo...</option>
                            <option value="paciente_desistiu">Paciente desistiu</option>
                            <option value="medico_cancelou">Médico cancelou</option>
                            <option value="outros">Outros</option>
                        </select>
                    </div>

                    {error && (
                        <div style={{
                            background: 'rgba(239, 68, 68, 0.1)',
                            border: '1px solid rgba(239, 68, 68, 0.3)',
                            borderRadius: '8px',
                            padding: '12px',
                            marginBottom: '16px',
                            color: '#991b1b'
                        }}>
                            {error}
                        </div>
                    )}
                </>
            )}

            <div className="button-group">
                <button
                    type="button"
                    className="button button-secondary"
                    onClick={onClose}
                    disabled={submitting}
                >
                    Fechar
                </button>

                {consultas.length > 0 && (
                    <button
                        type="button"
                        className="button button-danger"
                        onClick={handleCancelar}
                        disabled={submitting || !selectedId || !motivo}
                    >
                        {submitting ? 'Cancelando...' : 'Cancelar Consulta'}
                    </button>
                )}
            </div>
        </>
    )
}

export default CancelarConsulta