package com.takeabreak.web.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.takeabreak.web.model.Folga;

@Repository
public class FolgaRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Folga> mapper = (rs, rowNum) -> new Folga(
            rs.getLong("folga_id"),
            rs.getLong("funcionario_id"),
            rs.getString("funcionario_nome"),
            rs.getDate("data_pedido").toLocalDate(),
            rs.getDate("data_inicio").toLocalDate(),
            rs.getDate("data_fim").toLocalDate(),
            rs.getString("motivo"),
            rs.getString("estado"),
            rs.getBigDecimal("remuneracao")
    );

    public FolgaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void criarPedido(Long funcionarioId, LocalDate dataPedido, LocalDate dataInicio, LocalDate dataFim, String motivo, String estado, BigDecimal remuneracao) {
        String sql = "INSERT INTO folga (funcionario_id, data_pedido, data_inicio, data_fim, motivo, estado, remuneracao) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, funcionarioId, dataPedido, dataInicio, dataFim, motivo, estado, remuneracao);
    }

    public List<Folga> listarMinhas(Long funcionarioId) {
        return listarMinhasComFiltrosPaginado(funcionarioId, null, null, null, Integer.MAX_VALUE, 0);
    }

    public List<Folga> listarMinhasComFiltros(Long funcionarioId, String estado, LocalDate dataPedido, LocalDate dataInicio) {
        return listarMinhasComFiltrosPaginado(funcionarioId, estado, dataPedido, dataInicio, Integer.MAX_VALUE, 0);
    }

    public List<Folga> listarMinhasComFiltrosPaginado(Long funcionarioId, String estado, LocalDate dataPedido, LocalDate dataInicio, int limit, int offset) {
        String sql = "SELECT f.*, fu.nome AS funcionario_nome FROM folga f " +
            "INNER JOIN funcionario fu ON fu.funcionario_id = f.funcionario_id " +
            "WHERE f.funcionario_id = ? " +
            "AND (? IS NULL OR f.estado = ?) " +
            "AND (? IS NULL OR f.data_pedido = ?) " +
            "AND (? IS NULL OR f.data_inicio = ?) " +
            "ORDER BY f.folga_id DESC " +
            "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, mapper,
            funcionarioId,
            estado, estado,
            dataPedido, dataPedido,
            dataInicio, dataInicio,
            limit, offset);
    }

    public long contarMinhasComFiltros(Long funcionarioId, String estado, LocalDate dataPedido, LocalDate dataInicio) {
        String sql = "SELECT COUNT(*) FROM folga f " +
            "WHERE f.funcionario_id = ? " +
            "AND (f.estado = COALESCE(?, f.estado)) " +
            "AND (f.data_pedido = COALESCE(?, f.data_pedido)) " +
            "AND (f.data_inicio = COALESCE(?, f.data_inicio))";
        Long count = jdbcTemplate.queryForObject(sql, Long.class,
            funcionarioId,
            estado,
            dataPedido,
            dataInicio);
        return count != null ? count : 0;
    }

    public List<Folga> listarTodas() {
        return listarTodasComFiltrosPaginado(null, null, null, Integer.MAX_VALUE, 0);
    }

    public List<Folga> listarTodasComFiltros(String estado, String funcionario, String departamento) {
        return listarTodasComFiltrosPaginado(estado, funcionario, departamento, Integer.MAX_VALUE, 0);
    }

    public List<Folga> listarTodasComFiltrosPaginado(String estado, String funcionario, String departamento, int limit, int offset) {
        String sql = "SELECT f.*, fu.nome AS funcionario_nome FROM folga f " +
            "INNER JOIN funcionario fu ON fu.funcionario_id = f.funcionario_id " +
            "WHERE (f.estado = COALESCE(?, f.estado)) " +
            "AND (fu.nome ILIKE '%' || COALESCE(?, '') || '%') " +
            "AND (replace(replace(lower(fu.departamento), 'infra-estrutura', 'infraestrutura'), 'equipamentos', 'equipamento') = " +
            "replace(replace(lower(COALESCE(?, fu.departamento)), 'infra-estrutura', 'infraestrutura'), 'equipamentos', 'equipamento')) " +
            "ORDER BY f.folga_id DESC " +
            "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, mapper,
            estado,
            funcionario,
            departamento,
            limit, offset);
    }

    public long contarTodasComFiltros(String estado, String funcionario, String departamento) {
        String sql = "SELECT COUNT(*) FROM folga f " +
            "INNER JOIN funcionario fu ON fu.funcionario_id = f.funcionario_id " +
            "WHERE (f.estado = COALESCE(?, f.estado)) " +
            "AND (fu.nome ILIKE '%' || COALESCE(?, '') || '%') " +
            "AND (replace(replace(lower(fu.departamento), 'infra-estrutura', 'infraestrutura'), 'equipamentos', 'equipamento') = " +
            "replace(replace(lower(COALESCE(?, fu.departamento)), 'infra-estrutura', 'infraestrutura'), 'equipamentos', 'equipamento'))";
        Long count = jdbcTemplate.queryForObject(sql, Long.class,
            estado,
            funcionario,
            departamento);
        return count != null ? count : 0;
    }

    public Optional<Folga> findById(Long folgaId) {
        String sql = "SELECT f.*, fu.nome AS funcionario_nome FROM folga f " +
                "INNER JOIN funcionario fu ON fu.funcionario_id = f.funcionario_id " +
                "WHERE f.folga_id = ?";
        List<Folga> list = jdbcTemplate.query(sql, mapper, folgaId);
        return list.stream().findFirst();
    }

    public void aprovar(Long folgaId, BigDecimal remuneracao) {
        String sql = "UPDATE folga SET estado = 'APROVADA', remuneracao = ? WHERE folga_id = ?";
        jdbcTemplate.update(sql, remuneracao, folgaId);
    }

    public void rejeitar(Long folgaId) {
        String sql = "UPDATE folga SET estado = 'REJEITADA', remuneracao = 0 WHERE folga_id = ?";
        jdbcTemplate.update(sql, folgaId);
    }

    public long contarAusenciasAprovadas(Long funcionarioId, LocalDate dataInicio, LocalDate dataFim) {
        String sql = "SELECT COUNT(*) FROM folga WHERE funcionario_id = ? AND estado = 'APROVADA' " +
                "AND NOT (data_fim < ? OR data_inicio > ?)";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, funcionarioId, dataInicio, dataFim);
        return count != null ? count : 0;
    }

    public long contarAusenciasAprovadosCualquerFuncionario(LocalDate dataInicio, LocalDate dataFim) {
        String sql = "SELECT COUNT(*) FROM folga WHERE estado = 'APROVADA' " +
                "AND NOT (data_fim < ? OR data_inicio > ?)";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, dataInicio, dataFim);
        return count != null ? count : 0;
    }

    public List<Folga> listarFeriasAprovadas(Long funcionarioId, int ano) {
        String sql = "SELECT f.*, fu.nome AS funcionario_nome FROM folga f " +
                "INNER JOIN funcionario fu ON fu.funcionario_id = f.funcionario_id " +
                "WHERE f.funcionario_id = ? AND f.motivo = 'Férias' AND f.estado = 'APROVADA' " +
                "AND YEAR(f.data_inicio) = ? ORDER BY f.data_inicio";
        return jdbcTemplate.query(sql, mapper, funcionarioId, ano);
    }

    public long contarDiasMotivo(Long funcionarioId, String motivo, int ano) {
        String sql = "SELECT SUM(CASE WHEN data_fim > data_inicio " +
                "THEN DATEDIFF(data_fim, data_inicio) + 1 ELSE 1 END) FROM folga " +
                "WHERE funcionario_id = ? AND motivo = ? AND estado = 'APROVADA' " +
                "AND YEAR(data_inicio) = ?";
        Long total = jdbcTemplate.queryForObject(sql, Long.class, funcionarioId, motivo, ano);
        return total != null ? total : 0;
    }

    public void delete(Long folgaId) {
        String sql = "DELETE FROM folga WHERE folga_id = ?";
        jdbcTemplate.update(sql, folgaId);
    }

    public void deleteByFuncionarioId(Long funcionarioId) {
        String sql = "DELETE FROM folga WHERE funcionario_id = ?";
        jdbcTemplate.update(sql, funcionarioId);
    }

    public void atualizar(Long folgaId,
                         LocalDate dataPedido,
                         LocalDate dataInicio,
                         LocalDate dataFim,
                         String motivo,
                         String estado,
                         BigDecimal remuneracao) {
        String sql = "UPDATE folga SET data_pedido = ?, data_inicio = ?, data_fim = ?, motivo = ?, estado = ?, remuneracao = ? WHERE folga_id = ?";
        jdbcTemplate.update(sql, dataPedido, dataInicio, dataFim, motivo, estado, remuneracao, folgaId);
    }
}
