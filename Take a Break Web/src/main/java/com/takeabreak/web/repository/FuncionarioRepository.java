package com.takeabreak.web.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.takeabreak.web.model.Funcionario;

@Repository
public class FuncionarioRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Funcionario> mapper = (rs, rowNum) -> new Funcionario(
            rs.getLong("funcionario_id"),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("departamento"),
            rs.getInt("escalao"),
            rs.getString("role"),
            rs.getDate("data_admissao") != null ? rs.getDate("data_admissao").toLocalDate() : null,
            rs.getString("morada"),
            rs.getString("codigo_postal"),
            rs.getString("localidade"),
            rs.getString("concelho"),
            rs.getString("distrito"),
            rs.getString("telefone")
    );

    public FuncionarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Funcionario> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM funcionario WHERE email = ? AND password = ?";
        List<Funcionario> list = jdbcTemplate.query(sql, mapper, email, password);
        return list.stream().findFirst();
    }

    public Optional<Funcionario> findByEmail(String email) {
        String sql = "SELECT * FROM funcionario WHERE email = ?";
        List<Funcionario> list = jdbcTemplate.query(sql, mapper, email);
        return list.stream().findFirst();
    }

    public Optional<Funcionario> findById(Long id) {
        String sql = "SELECT * FROM funcionario WHERE funcionario_id = ?";
        List<Funcionario> list = jdbcTemplate.query(sql, mapper, id);
        return list.stream().findFirst();
    }

    public List<Funcionario> findAll() {
        String sql = "SELECT * FROM funcionario";
        return jdbcTemplate.query(sql, mapper);
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM funcionario WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public boolean existsByEmailAndIdNot(String email, Long funcionarioId) {
        String sql = "SELECT COUNT(*) FROM funcionario WHERE email = ? AND funcionario_id <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, funcionarioId);
        return count != null && count > 0;
    }

    public boolean existsByTelefone(String telefone) {
        String sql = "SELECT COUNT(*) FROM funcionario WHERE telefone = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, telefone);
        return count != null && count > 0;
    }

    public boolean existsByTelefoneAndIdNot(String telefone, Long funcionarioId) {
        String sql = "SELECT COUNT(*) FROM funcionario WHERE telefone = ? AND funcionario_id <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, telefone, funcionarioId);
        return count != null && count > 0;
    }

    public void create(String nome, String email, String password, String departamento, int escalao,
                       java.time.LocalDate dataAdmissao, String morada, String codigoPostal,
                       String localidade, String concelho, String distrito, String telefone) {
        String sql = "INSERT INTO funcionario (nome, email, password, departamento, escalao, role, data_admissao, morada, codigo_postal, localidade, concelho, distrito, telefone) VALUES (?, ?, ?, ?, ?, 'FUNCIONARIO', ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, nome, email, password, departamento, escalao, dataAdmissao, morada, codigoPostal, localidade, concelho, distrito, telefone);
    }

    public void createComRole(String nome, String email, String password, String departamento, int escalao,
                              String role, LocalDate dataAdmissao, String morada, String codigoPostal,
                              String localidade, String concelho, String distrito, String telefone) {
        String sql = "INSERT INTO funcionario (nome, email, password, departamento, escalao, role, data_admissao, morada, codigo_postal, localidade, concelho, distrito, telefone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, nome, email, password, departamento, escalao, role, dataAdmissao, morada, codigoPostal, localidade, concelho, distrito, telefone);
    }

    public void updatePerfil(Long funcionarioId,
                             String nome,
                             String email,
                             String password,
                             String departamento,
                             int escalao,
                             java.time.LocalDate dataAdmissao,
                             String morada,
                             String codigoPostal,
                             String localidade,
                             String concelho,
                             String distrito,
                             String telefone) {
        String sql = "UPDATE funcionario SET nome = ?, email = ?, password = ?, departamento = ?, escalao = ?, data_admissao = ?, morada = ?, codigo_postal = ?, localidade = ?, concelho = ?, distrito = ?, telefone = ? WHERE funcionario_id = ?";
        jdbcTemplate.update(sql, nome, email, password, departamento, escalao, dataAdmissao, morada, codigoPostal, localidade, concelho, distrito, telefone, funcionarioId);
    }

    public void deleteById(Long funcionarioId) {
        String sql = "DELETE FROM funcionario WHERE funcionario_id = ?";
        jdbcTemplate.update(sql, funcionarioId);
    }

    public void updatePasswordById(Long funcionarioId, String passwordHash) {
        String sql = "UPDATE funcionario SET password = ? WHERE funcionario_id = ?";
        jdbcTemplate.update(sql, passwordHash, funcionarioId);
    }

    public void updateRoleByEmail(String email, String role) {
        String sql = "UPDATE funcionario SET role = ? WHERE email = ?";
        jdbcTemplate.update(sql, role, email);
    }
}
