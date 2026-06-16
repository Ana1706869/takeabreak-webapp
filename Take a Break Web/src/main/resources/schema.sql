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

DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'funcionario' AND column_name = 'data_admissao'
    ) THEN
        ALTER TABLE funcionario ADD COLUMN data_admissao DATE;
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'funcionario' AND column_name = 'morada'
    ) THEN
        ALTER TABLE funcionario ADD COLUMN morada VARCHAR(160);
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'funcionario' AND column_name = 'codigo_postal'
    ) THEN
        ALTER TABLE funcionario ADD COLUMN codigo_postal VARCHAR(20);
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'funcionario' AND column_name = 'localidade'
    ) THEN
        ALTER TABLE funcionario ADD COLUMN localidade VARCHAR(120);
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'funcionario' AND column_name = 'concelho'
    ) THEN
        ALTER TABLE funcionario ADD COLUMN concelho VARCHAR(120);
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'funcionario' AND column_name = 'distrito'
    ) THEN
        ALTER TABLE funcionario ADD COLUMN distrito VARCHAR(120);
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'funcionario' AND column_name = 'telefone'
    ) THEN
        ALTER TABLE funcionario ADD COLUMN telefone VARCHAR(20);
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes
        WHERE tablename = 'funcionario' AND indexname = 'ux_funcionario_telefone'
    ) THEN
        CREATE UNIQUE INDEX ux_funcionario_telefone ON funcionario(telefone);
    END IF;
END $$;

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
