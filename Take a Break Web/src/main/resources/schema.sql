CREATE TABLE IF NOT EXISTS funcionario (
    funcionario_id BIGSERIAL PRIMARY KEY,
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

CREATE UNIQUE INDEX IF NOT EXISTS ux_funcionario_telefone ON funcionario(telefone);

CREATE TABLE IF NOT EXISTS folga (
    folga_id BIGSERIAL PRIMARY KEY,
    funcionario_id BIGINT NOT NULL,
    data_pedido DATE NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    motivo VARCHAR(180) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    remuneracao DECIMAL(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_folga_funcionario FOREIGN KEY (funcionario_id) REFERENCES funcionario(funcionario_id)
);

UPDATE funcionario
SET role = 'GESTOR'
WHERE lower(email) = lower('gestor@gmail.com');
