package com.pedro.financeiro_api.controller;

import com.pedro.financeiro_api.dto.CategoriaDTO;
import com.pedro.financeiro_api.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaDTO.Response> criar(@Valid @RequestBody CategoriaDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO.Response>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO.Response> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody CategoriaDTO.Request request) {
        return ResponseEntity.ok(categoriaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}