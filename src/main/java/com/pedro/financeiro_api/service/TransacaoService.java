package com.pedro.financeiro_api.service;

import com.pedro.financeiro_api.dto.CategoriaDTO;
import com.pedro.financeiro_api.dto.TransacaoDTO;
import com.pedro.financeiro_api.exception.Exceptions;
import com.pedro.financeiro_api.model.Categoria;
import com.pedro.financeiro_api.model.Transacao;
import com.pedro.financeiro_api.model.Usuario;
import com.pedro.financeiro_api.repository.CategoriaRepository;
import com.pedro.financeiro_api.repository.TransacaoRepository;
import com.pedro.financeiro_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new Exceptions.RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    @Transactional
    public TransacaoDTO.Response criar(TransacaoDTO.Request request) {
        Usuario usuario = getUsuarioLogado();

        Categoria categoria = null;
        if (request.getCategoriaId() != null) {
            categoria = categoriaRepository.findByIdAndUsuario(request.getCategoriaId(), usuario)
                    .orElseThrow(() -> new Exceptions.RecursoNaoEncontradoException("Categoria não encontrada"));
        }

        Transacao transacao = Transacao.builder()
                .descricao(request.getDescricao())
                .valor(request.getValor())
                .data(request.getData())
                .tipo(request.getTipo())
                .categoria(categoria)
                .usuario(usuario)
                .build();

        return toResponse(transacaoRepository.save(transacao));
    }

    @Transactional(readOnly = true)
    public Page<TransacaoDTO.Response> listar(Integer mes, Integer ano, String tipo, Pageable pageable) {
        Usuario usuario = getUsuarioLogado();

        if (mes != null && ano != null) {
            List<Transacao> transacoes = transacaoRepository.findByUsuarioAndMesAno(usuario, mes, ano);
            List<TransacaoDTO.Response> response = transacoes.stream().map(this::toResponse).toList();
            return new PageImpl<>(response, pageable, response.size());
        }

        if (tipo != null) {
            Transacao.Tipo tipoEnum = Transacao.Tipo.valueOf(tipo.toUpperCase());
            List<Transacao> transacoes = transacaoRepository.findByUsuarioAndTipoOrderByDataDesc(usuario, tipoEnum);
            List<TransacaoDTO.Response> response = transacoes.stream().map(this::toResponse).toList();
            return new PageImpl<>(response, pageable, response.size());
        }

        return transacaoRepository.findByUsuario(usuario, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public TransacaoDTO.Response buscarPorId(Long id) {
        Usuario usuario = getUsuarioLogado();
        Transacao transacao = transacaoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new Exceptions.RecursoNaoEncontradoException("Transação não encontrada"));
        return toResponse(transacao);
    }

    @Transactional
    public TransacaoDTO.Response atualizar(Long id, TransacaoDTO.Request request) {
        Usuario usuario = getUsuarioLogado();

        Transacao transacao = transacaoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new Exceptions.RecursoNaoEncontradoException("Transação não encontrada"));

        Categoria categoria = null;
        if (request.getCategoriaId() != null) {
            categoria = categoriaRepository.findByIdAndUsuario(request.getCategoriaId(), usuario)
                    .orElseThrow(() -> new Exceptions.RecursoNaoEncontradoException("Categoria não encontrada"));
        }

        transacao.setDescricao(request.getDescricao());
        transacao.setValor(request.getValor());
        transacao.setData(request.getData());
        transacao.setTipo(request.getTipo());
        transacao.setCategoria(categoria);

        return toResponse(transacaoRepository.save(transacao));
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = getUsuarioLogado();
        Transacao transacao = transacaoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new Exceptions.RecursoNaoEncontradoException("Transação não encontrada"));
        transacaoRepository.delete(transacao);
    }

    @Transactional(readOnly = true)
    public ResumoResponse resumo(Integer mes, Integer ano) {
        Usuario usuario = getUsuarioLogado();

        BigDecimal totalReceitas;
        BigDecimal totalDespesas;

        if (mes != null && ano != null) {
            totalReceitas = transacaoRepository.somarPorTipoEMesAno(usuario, Transacao.Tipo.RECEITA, mes, ano);
            totalDespesas = transacaoRepository.somarPorTipoEMesAno(usuario, Transacao.Tipo.DESPESA, mes, ano);
        } else {
            totalReceitas = transacaoRepository.somarPorTipo(usuario, Transacao.Tipo.RECEITA);
            totalDespesas = transacaoRepository.somarPorTipo(usuario, Transacao.Tipo.DESPESA);
        }

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new ResumoResponse(totalReceitas, totalDespesas, saldo);
    }

    private TransacaoDTO.Response toResponse(Transacao transacao) {
        CategoriaDTO.Response categoriaResponse = null;
        if (transacao.getCategoria() != null) {
            categoriaResponse = CategoriaDTO.Response.builder()
                    .id(transacao.getCategoria().getId())
                    .nome(transacao.getCategoria().getNome())
                    .descricao(transacao.getCategoria().getDescricao())
                    .build();
        }

        return TransacaoDTO.Response.builder()
                .id(transacao.getId())
                .descricao(transacao.getDescricao())
                .valor(transacao.getValor())
                .data(transacao.getData())
                .tipo(transacao.getTipo().name())
                .categoria(categoriaResponse)
                .build();
    }

    public record ResumoResponse(
            BigDecimal totalReceitas,
            BigDecimal totalDespesas,
            BigDecimal saldo) {
    }
}