package com.pedro.financeiro_api.controller;

import com.pedro.financeiro_api.dto.TransacaoDTO;
import com.pedro.financeiro_api.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<TransacaoDTO.Response> criar(@Valid @RequestBody TransacaoDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<TransacaoDTO.Response>> listar(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String tipo) {
        return ResponseEntity.ok(transacaoService.listar(mes, ano, tipo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoDTO.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transacaoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoDTO.Response> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody TransacaoDTO.Request request) {
        return ResponseEntity.ok(transacaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumo")
    public ResponseEntity<TransacaoService.ResumoResponse> resumo(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano) {
        return ResponseEntity.ok(transacaoService.resumo(mes, ano));
    }
}