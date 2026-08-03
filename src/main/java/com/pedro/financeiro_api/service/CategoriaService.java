package com.pedro.financeiro_api.service;

import com.pedro.financeiro_api.dto.CategoriaDTO;
import com.pedro.financeiro_api.exception.Exceptions;
import com.pedro.financeiro_api.model.Categoria;
import com.pedro.financeiro_api.model.Usuario;
import com.pedro.financeiro_api.repository.CategoriaRepository;
import com.pedro.financeiro_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    // Pega o usuário logado a partir do token JWT
    private Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new Exceptions.RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    @Transactional
    public CategoriaDTO.Response criar(CategoriaDTO.Request request) {
        Usuario usuario = getUsuarioLogado();

        if (categoriaRepository.existsByNomeAndUsuario(request.getNome(), usuario)) {
            throw new Exceptions.RecursoJaExisteException("Categoria '" + request.getNome() + "' já existe");
        }

        Categoria categoria = Categoria.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .usuario(usuario)
                .build();

        Categoria salva = categoriaRepository.save(categoria);
        return toResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO.Response> listar() {
        Usuario usuario = getUsuarioLogado();
        return categoriaRepository.findByUsuario(usuario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CategoriaDTO.Response atualizar(Long id, CategoriaDTO.Request request) {
        Usuario usuario = getUsuarioLogado();

        Categoria categoria = categoriaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new Exceptions.RecursoNaoEncontradoException("Categoria não encontrada"));

        categoria.setNome(request.getNome());
        categoria.setDescricao(request.getDescricao());

        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = getUsuarioLogado();

        Categoria categoria = categoriaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new Exceptions.RecursoNaoEncontradoException("Categoria não encontrada"));

        categoriaRepository.delete(categoria);
    }

    private CategoriaDTO.Response toResponse(Categoria categoria) {
        return CategoriaDTO.Response.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .build();
    }
}