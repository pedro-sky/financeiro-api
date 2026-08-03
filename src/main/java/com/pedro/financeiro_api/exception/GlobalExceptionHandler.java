package com.pedro.financeiro_api.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> errosCampos = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensagem = error.getDefaultMessage();
            errosCampos.put(campo, mensagem);
        });

        return ResponseEntity.status(400).body(
                new ErroResponse(400, "Erro de validação nos campos", errosCampos, LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exceptions.RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handleNaoEncontrado(Exceptions.RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(404).body(
                new ErroResponse(404, ex.getMessage(), null, LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exceptions.RecursoJaExisteException.class)
    public ResponseEntity<ErroResponse> handleJaExiste(Exceptions.RecursoJaExisteException ex) {
        return ResponseEntity.status(409).body(
                new ErroResponse(409, ex.getMessage(), null, LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exceptions.RegraDeNegocioException.class)
    public ResponseEntity<ErroResponse> handleRegraDeNegocio(Exceptions.RegraDeNegocioException ex) {
        return ResponseEntity.status(422).body(
                new ErroResponse(422, ex.getMessage(), null, LocalDateTime.now())
        );
    }

    // Captura erro de email/senha incorretos no login
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(401).body(
                new ErroResponse(401, "Email ou senha incorretos", null, LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleErroGenerico(Exception ex) {
        return ResponseEntity.status(500).body(
                new ErroResponse(500, "Ocorreu um erro interno. Tente novamente mais tarde.", null, LocalDateTime.now())
        );
    }

    public record ErroResponse(
            int status,
            String mensagem,
            Map<String, String> errosCampos,
            LocalDateTime timestamp
    ) {}
}