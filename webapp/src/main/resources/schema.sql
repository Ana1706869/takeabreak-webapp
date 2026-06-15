CREATE TABLE IF NOT EXISTS funcionario (
    funcionario_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    departamento VARCHAR(160) NOT NULL,
    escalao INT NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'FUNCIONARIO',
    data_admissao DATE,
    morada VARCHAR(160),
    codigo_postal VARCHAR(20),
    localidade VARCHAR(120),
    concelho VARCHAR(120),
    distrito VARCHAR(120),
    telefone VARCHAR(20)
);

SET @has_data_admissao := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'funcionario' AND COLUMN_NAME = 'data_admissao'
);
SET @sql_data_admissao := IF(@has_data_admissao = 0,
    'ALTER TABLE funcionario ADD COLUMN data_admissao DATE',
    'SELECT 1');
PREPARE stmt_data_admissao FROM @sql_data_admissao;
EXECUTE stmt_data_admissao;
DEALLOCATE PREPARE stmt_data_admissao;

SET @has_morada := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'funcionario' AND COLUMN_NAME = 'morada'
);
SET @sql_morada := IF(@has_morada = 0,
    'ALTER TABLE funcionario ADD COLUMN morada VARCHAR(160)',
    'SELECT 1');
PREPARE stmt_morada FROM @sql_morada;
EXECUTE stmt_morada;
DEALLOCATE PREPARE stmt_morada;

SET @has_codigo_postal := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'funcionario' AND COLUMN_NAME = 'codigo_postal'
);
SET @sql_codigo_postal := IF(@has_codigo_postal = 0,
    'ALTER TABLE funcionario ADD COLUMN codigo_postal VARCHAR(20)',
    'SELECT 1');
PREPARE stmt_codigo_postal FROM @sql_codigo_postal;
EXECUTE stmt_codigo_postal;
DEALLOCATE PREPARE stmt_codigo_postal;

SET @has_localidade := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'funcionario' AND COLUMN_NAME = 'localidade'
);
SET @sql_localidade := IF(@has_localidade = 0,
    'ALTER TABLE funcionario ADD COLUMN localidade VARCHAR(120)',
    'SELECT 1');
PREPARE stmt_localidade FROM @sql_localidade;
EXECUTE stmt_localidade;
DEALLOCATE PREPARE stmt_localidade;

SET @has_concelho := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'funcionario' AND COLUMN_NAME = 'concelho'
);
SET @sql_concelho := IF(@has_concelho = 0,
    'ALTER TABLE funcionario ADD COLUMN concelho VARCHAR(120)',
    'SELECT 1');
PREPARE stmt_concelho FROM @sql_concelho;
EXECUTE stmt_concelho;
DEALLOCATE PREPARE stmt_concelho;

SET @has_distrito := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'funcionario' AND COLUMN_NAME = 'distrito'
);
SET @sql_distrito := IF(@has_distrito = 0,
    'ALTER TABLE funcionario ADD COLUMN distrito VARCHAR(120)',
    'SELECT 1');
PREPARE stmt_distrito FROM @sql_distrito;
EXECUTE stmt_distrito;
DEALLOCATE PREPARE stmt_distrito;

SET @has_telefone := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'funcionario' AND COLUMN_NAME = 'telefone'
);
SET @sql_telefone := IF(@has_telefone = 0,
    'ALTER TABLE funcionario ADD COLUMN telefone VARCHAR(20)',
    'SELECT 1');
PREPARE stmt_telefone FROM @sql_telefone;
EXECUTE stmt_telefone;
DEALLOCATE PREPARE stmt_telefone;

SET @has_unique_telefone := (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'funcionario'
      AND INDEX_NAME = 'ux_funcionario_telefone'
);
SET @sql_unique_telefone := IF(@has_unique_telefone = 0,
    'CREATE UNIQUE INDEX ux_funcionario_telefone ON funcionario(telefone)',
    'SELECT 1');
PREPARE stmt_unique_telefone FROM @sql_unique_telefone;
EXECUTE stmt_unique_telefone;
DEALLOCATE PREPARE stmt_unique_telefone;

CREATE TABLE IF NOT EXISTS folga (
    folga_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    funcionario_id BIGINT NOT NULL,
    data_pedido DATE NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    motivo VARCHAR(180) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    remuneracao DECIMAL(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_folga_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionario(funcionario_id)
);

-- Normalizacao de departamentos ja existentes para valores canonicos usados nos filtros.
UPDATE funcionario
SET departamento = 'Administração de Infra-estrutura de Rede'
WHERE departamento IN (
    'Administração de Infraestrutura de Rede',
    'Administração de infra-estrutura de rede',
    'Administração de infraestrutura de rede',
    'Administracao de Infra-estrutura de Rede',
    'Administracao de Infraestrutura de Rede',
    'Administracao de infraestrutura de rede'
);

UPDATE funcionario
SET departamento = 'Manutenção de Equipamento e Serviços'
WHERE departamento IN (
    'Manutenção de Equipamentos e Serviços',
    'Manutenção de equipamentos e serviços',
    'Manutencao de Equipamentos e Servicos',
    'Manutencao de equipamento e servicos'
);

UPDATE funcionario
SET departamento = 'Administração de Sistemas'
WHERE departamento IN (
    'Administração de sistemas',
    'Administracao de Sistemas',
    'Administracao de sistemas'
);

UPDATE funcionario
SET departamento = 'Suporte aos Utilizadores'
WHERE departamento IN (
    'suporte aos utilizadores',
    'Suporte Aos Utilizadores'
);

UPDATE funcionario
SET departamento = 'Desenvolvimento e Implementação de Novos Projetos'
WHERE departamento IN (
    'Desenvolvimento e implementação de novos projetos',
    'Desenvolvimento e Implementacao de Novos Projetos',
    'desenvolvimento e implementacao de novos projetos'
);

-- Corrige valores com codificacao corrompida (mojibake) encontrados na base atual.
UPDATE funcionario
SET departamento = 'Administração de Sistemas'
WHERE HEX(departamento) = '41646D696E6973747261E2949CC2BAE2949CC3BA6F2064652053697374656D6173';

UPDATE funcionario
SET departamento = 'Desenvolvimento e Implementação de Novos Projetos'
WHERE HEX(departamento) = '446573656E766F6C76696D656E746F206520496D706C656D656E7461E2949CC2BAE2949CC3BA6F206465204E6F766F732050726F6A65746F73';

UPDATE funcionario
SET departamento = 'Administração de Infra-estrutura de Rede'
WHERE HEX(departamento) = '41646D696E6973747261E2949CC2BAE2949CC3BA6F20646520496E6672612D6573747275747572612064652052656465';

UPDATE funcionario
SET departamento = 'Manutenção de Equipamento e Serviços'
WHERE HEX(departamento) = '4D616E7574656EE2949CC2BAE2949CC3BA6F206465204571756970616D656E746F732065205365727669E2949CC2BA6F73';

INSERT INTO funcionario (nome, email, password, departamento, escalao, role)
SELECT 'Rui Vilaca', 'gestor@takeabreak.pt', '1234', 'Recursos Humanos', 11, 'GESTOR'
WHERE NOT EXISTS (SELECT 1 FROM funcionario WHERE email = 'gestor@takeabreak.pt');
