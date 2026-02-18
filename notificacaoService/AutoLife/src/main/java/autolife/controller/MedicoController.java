package autolife.controller;

import autolife.dto.medico.MedicoRequest;
import autolife.dto.medico.MedicoResponse;
import autolife.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @PostMapping
    public ResponseEntity<MedicoResponse> criarCadastroMedico(@Valid @RequestBody MedicoRequest request) {
        MedicoResponse response = medicoService.criarCadastroMedico(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}