package autolife.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ConsultaExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ConsultaExceptionHandler.class);

    /// Gerencia exceções mapeadas para erros de negócio pré-estabelecidos
    @ExceptionHandler(ConsultaException.class)
    public ResponseEntity<ConsultaError> handleConsulta(ConsultaException e) {
        return ResponseEntity
                .badRequest()
                .body(new ConsultaError(e.getMessage()));
    }

    /// Gerencia exceções na validação de DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidacao(MethodArgumentNotValidException e) {
        Map<String, String> erros = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(erro -> erros.put(erro.getField(), erro.getDefaultMessage()));

        return ResponseEntity.badRequest().body(erros);
    }

    /// Gerencia a exceção durante o registro de usuário
    @ExceptionHandler(AutenticacaoException.class)
    public ResponseEntity<ConsultaError> handleAutenticacao(AutenticacaoException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .badRequest()
                .body(new ConsultaError("Erro ao autenticar usuário: " + e.getMessage()));
    }

    /// Gerencia exceções genéricas para erros não mapeados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ConsultaError> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ConsultaError("Erro interno do servidor"));
    }
}