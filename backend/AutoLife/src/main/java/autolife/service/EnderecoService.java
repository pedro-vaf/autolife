package autolife.service;

import autolife.dto.endereco.EnderecoCreateForm;
import autolife.dto.endereco.EnderecoDTO;
import autolife.dto.endereco.EnderecoUpdateForm;
import autolife.entities.Endereco;
import autolife.repositories.EnderecoRepository;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public EnderecoDTO saveEndereco(EnderecoCreateForm dados) {

        Endereco endereco = new Endereco();
        atualizarCampos(endereco, dados);

        enderecoRepository.save(endereco);

        return new EnderecoDTO(endereco);
    }

    public EnderecoDTO updateEndereco(Long id, EnderecoUpdateForm dados) {

        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        atualizarCampos(endereco, dados);

        enderecoRepository.save(endereco);

        return new EnderecoDTO(endereco);
    }

    private void atualizarCampos(Endereco endereco, EnderecoCreateForm dados) {

        if (isValid(dados.logradouro()))
            endereco.setLogradouro(normalizarTexto(dados.logradouro()));

        if (isValid(dados.bairro()))
            endereco.setBairro(normalizarTexto(dados.bairro()));

        if (isValid(dados.cidade()))
            endereco.setCidade(normalizarTexto(dados.cidade()));

        if (isValid(dados.cep()))
            endereco.setCep(dados.cep().replaceAll("\\D", ""));

        if (dados.uf() != null)
            endereco.setUf(dados.uf());
    }

    private void atualizarCampos(Endereco endereco, EnderecoUpdateForm dados) {

        if (isValid(dados.logradouro()))
            endereco.setLogradouro(normalizarTexto(dados.logradouro()));

        if (isValid(dados.bairro()))
            endereco.setBairro(normalizarTexto(dados.bairro()));

        if (isValid(dados.cidade()))
            endereco.setCidade(normalizarTexto(dados.cidade()));

        if (isValid(dados.cep()))
            endereco.setCep(dados.cep().replaceAll("\\D", ""));

        if (dados.uf() != null)
            endereco.setUf(dados.uf());
    }

    private boolean isValid(String valor) {
        return valor != null && !valor.isBlank();
    }

    private String normalizarTexto(String valor) {
        return valor.trim();
    }
}
