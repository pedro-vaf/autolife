package autolife.controllers;

import autolife.dto.dados.DadosLogin;
import autolife.dto.dados.DadosTokenJWT;
import autolife.dto.usuario.UsuarioCreateForm;
import autolife.dto.usuario.UsuarioDTO;
import autolife.entities.Usuario;
import autolife.exception.ConsultaError;
import autolife.service.JWTokenService;
import autolife.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class AutenticacaoController {
    private final AuthenticationManager authenticationManager;

    private final JWTokenService tokenService;
    private final UsuarioService usuarioService;

    public AutenticacaoController(
            AuthenticationManager authenticationManager,
            JWTokenService tokenService,
            UsuarioService usuarioService
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Realiza login",
            description = "Autentica usuário conforme credenciais fornecidas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token do usuário autenticado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DadosTokenJWT.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosLogin dados) {
        var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var auth = authenticationManager.authenticate(token);

        var jwToken =  tokenService.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(jwToken));
    }

    @Operation(
            summary = "Registra novo usuário",
            description = "Registra novo usuário a partir de usuário e senha fornecidos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login do usuário registrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro decorrente de credenciais erradas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaError.class)
                    )
            )
    })
    @PostMapping()
    public ResponseEntity<UsuarioDTO> efetuarRegistro(@RequestBody @Valid UsuarioCreateForm form) {
        var usuarioDTO = this.usuarioService.createUsuario(form);
        return ResponseEntity.ok(usuarioDTO);
    }
}