package autolife.controller;

import autolife.dto.consulta.ConsultaRequest;
import autolife.dto.consulta.ConsultaResponse;
import autolife.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ConsultaResponse> criarConsulta(@Valid @RequestBody ConsultaRequest request) {
        ConsultaResponse response = consultaService.criarConsulta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ConsultaResponse> cancelarConsulta(@PathVariable Long id) {
        ConsultaResponse response = consultaService.cancelarConsulta(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/lembretes")
    public ResponseEntity<Void> enviarLembretes() {
        consultaService.enviarLembretes();
        return ResponseEntity.ok().build();
    }
}