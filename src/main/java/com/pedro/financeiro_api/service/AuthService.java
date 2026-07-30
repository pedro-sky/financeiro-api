package com.pedro.financeiro_api.service;

import com.pedro.financeiro_api.dto.AuthDTO;
import com.pedro.financeiro_api.exception.Exceptions;
import com.pedro.financeiro_api.model.Usuario;
import com.pedro.financeiro_api.repository.UsuarioRepository;
import com.pedro.financeiro_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthDTO.TokenResponse registrar(AuthDTO.RegistroRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new Exceptions.RecursoJaExisteException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha())) // BCrypt aqui
                .build();

        usuarioRepository.save(usuario);

        String token = jwtService.gerarToken(usuario.getEmail());

        return AuthDTO.TokenResponse.builder()
                .token(token)
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }

    public AuthDTO.TokenResponse login(AuthDTO.LoginRequest request) {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exceptions.RecursoNaoEncontradoException("Usuário não encontrado"));

        String token = jwtService.gerarToken(usuario.getEmail());

        return AuthDTO.TokenResponse.builder()
                .token(token)
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }
}