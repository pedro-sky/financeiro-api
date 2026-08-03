package com.pedro.financeiro_api.repository;

import com.pedro.financeiro_api.model.Transacao;
import com.pedro.financeiro_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByUsuarioOrderByDataDesc(Usuario usuario);

    Optional<Transacao> findByIdAndUsuario(Long id, Usuario usuario);

    List<Transacao> findByUsuarioAndTipoOrderByDataDesc(Usuario usuario, Transacao.Tipo tipo);

    @Query("SELECT t FROM Transacao t WHERE t.usuario = :usuario " +
           "AND MONTH(t.data) = :mes AND YEAR(t.data) = :ano " +
           "ORDER BY t.data DESC")
    List<Transacao> findByUsuarioAndMesAno(@Param("usuario") Usuario usuario,
                                           @Param("mes") int mes,
                                           @Param("ano") int ano);

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t " +
           "WHERE t.usuario = :usuario AND t.tipo = :tipo")
    BigDecimal somarPorTipo(@Param("usuario") Usuario usuario,
                            @Param("tipo") Transacao.Tipo tipo);

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t " +
           "WHERE t.usuario = :usuario AND t.tipo = :tipo " +
           "AND MONTH(t.data) = :mes AND YEAR(t.data) = :ano")
    BigDecimal somarPorTipoEMesAno(@Param("usuario") Usuario usuario,
                                   @Param("tipo") Transacao.Tipo tipo,
                                   @Param("mes") int mes,
                                   @Param("ano") int ano);
}