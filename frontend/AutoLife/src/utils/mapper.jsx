/* Converte dados do frontend para o formato
   esperado pelo backend (payload) e vice-versa */

/* Paciente */
export const mapPacienteCreatePayload = (formData) => {
    return {
        nome: formData.nome?.trim(),
        email: formData.email?.trim().toLowerCase(),
        telefone: formData.telefone,
        cpf: formData.cpf,
        endereco: {
            logradouro: formData.logradouro?.trim(),
            numero: formData.numero?.trim(),
            complemento: formData.complemento?.trim(),
            bairro: formData.bairro?.trim(),
            cidade: formData.cidade?.trim(),
            uf: formData.uf?.trim().toUpperCase(),
            cep: formData.cep
        }
    };
};

export const mapPacienteUpdatePayload = (formData) => ({
    nome: formData.nome?.trim(),
    telefone: formData.telefone,
    endereco: {
        logradouro: formData.logradouro?.trim(),
        numero: formData.numero?.trim(),
        complemento: formData.complemento?.trim(),
        bairro: formData.bairro?.trim(),
        cidade: formData.cidade?.trim(),
        uf: formData.uf?.trim().toUpperCase(),
        cep: formData.cep
    }
});

/* Médico */
export const mapMedicoCreatePayload = (formData) => {
    return {
        nome: formData.nome?.trim(),
        email: formData.email?.trim().toLowerCase(),
        telefone: formData.telefone,
        crm: formData.crm,
        especialidade: formData.especialidade,
        endereco: {
            logradouro: formData.logradouro?.trim(),
            numero: formData.numero?.trim(),
            complemento: formData.complemento?.trim(),
            bairro: formData.bairro?.trim(),
            cidade: formData.cidade?.trim(),
            uf: formData.uf?.trim().toUpperCase(),
            cep: formData.cep
        }
    };
};

export const mapMedicoUpdatePayload = (formData) => ({
    nome: formData.nome?.trim(),
    telefone: formData.telefone,
    endereco: {
        logradouro: formData.logradouro?.trim(),
        numero: formData.numero?.trim(),
        complemento: formData.complemento?.trim(),
        bairro: formData.bairro?.trim(),
        cidade: formData.cidade?.trim(),
        uf: formData.uf?.trim().toUpperCase(),
        cep: formData.cep
    }
});


/* Consulta */
export const mapConsultaToPayload = (formData) => {
    return {
        pacienteId: parseInt(formData.pacienteId),
        medicoId: formData.medicoId ? parseInt(formData.medicoId) : null,
        inicioConsulta: `${formData.data}T${formData.hora}:00.000Z`
    }
}


export const mapCancelamentoToPayload = (motivo) => {
    const motivosMap = {
        'paciente_desistiu': 'DESISTENCIA_PACIENTE',
        'medico_cancelou': 'CANCELAMENTO_MEDICO',
        'outros': 'OUTROS'
    }

    return {
        motivoCancelamento: motivosMap[motivo] || 'OUTROS'
    }
}

export const validarAntecedencia30min = (data, hora) => {
    const dataConsulta = new Date(`${data}T${hora}:00`)
    const agora = new Date()
    const diferencaMinutos = (dataConsulta - agora) / 1000 / 60

    return diferencaMinutos >= 30
}