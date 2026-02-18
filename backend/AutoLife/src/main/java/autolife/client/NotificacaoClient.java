package autolife.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "notificacao",
        url = "${notificacao.service.url}"
)
public interface NotificacaoClient {

    @PostMapping("/api/consultas")
    void criarConsulta(@RequestBody Object request);

    @DeleteMapping("/api/consultas/{id}")
    void cancelarConsulta(@PathVariable("id") Long id);

    @PostMapping("/api/lembretes")
    void enviarLembretes();

    @PostMapping("/api/medicos")
    void criarCadastroMedico(@RequestBody Object request);

    @PostMapping("/api/pacientes")
    void criarCadastroPaciente(@RequestBody Object request);
}