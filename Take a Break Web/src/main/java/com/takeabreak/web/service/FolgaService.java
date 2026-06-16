package com.takeabreak.web.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.takeabreak.web.model.Folga;
import com.takeabreak.web.model.Funcionario;
import com.takeabreak.web.repository.FolgaRepository;
import com.takeabreak.web.repository.FuncionarioRepository;

@Service
public class FolgaService {

    private static final double[] SALARIOS_ESPECIALISTAS = {
        1807.04, 2023.89, 2240.74, 2457.57, 2674.43, 2893.81,
        3114.98, 3336.16, 3557.35, 3723.24, 3889.10
    };

    private static final double[] SALARIOS_TECNICOS = {
        1070.19, 1280.72, 1438.62, 1596.52, 1754.41, 1915.46,
        2078.11, 2240.74, 2403.37, 2566.01, 2674.43, 2783.21
    };

    private static final Set<String> DEPARTAMENTOS_ESPECIALISTAS = Set.of(
        "administracao de sistemas",
        "administracao de infra estrutura de rede",
        "administracao de infraestrutura de rede",
        "administracao de infraestrutura da rede",
        "desenvolvimento e implementacao de novos projetos"
    );

    private static final Set<String> DEPARTAMENTOS_TECNICOS = Set.of(
        "suporte aos utilizadores",
        "manutencao de equipamentos e servicos",
        "manutencao de equipamento e servicos"
    );

    private static final Set<String> MOTIVOS_SEM_REMUNERACAO = Set.of(
        "Doença com regime de proteção pela Segurança Social",
        "Assistência à família"
    );

    private final FolgaRepository folgaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public FolgaService(FolgaRepository folgaRepository, FuncionarioRepository funcionarioRepository) {
        this.folgaRepository = folgaRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public String criarPedido(Long funcionarioId, LocalDate dataPedido, LocalDate dataInicio, LocalDate dataFim, String motivo) {
        if (dataPedido == null || dataInicio == null || dataFim == null || motivo == null || motivo.isBlank()) {
            return "Preencha a Data de Pedido, a Data de Inicio, a Data de Fim e o Motivo antes de submeter.";
        }

        LocalDate hoje = LocalDate.now();
        if (dataInicio.isBefore(hoje) || dataFim.isBefore(hoje)) {
            return "A Data de Inicio e a Data de Fim devem ser superiores ou iguais a data atual.";
        }

        if (dataFim.isBefore(dataInicio)) {
            return "A Data de Inicio da ausencia deve ser inferior ou igual a Data de Fim.";
        }

        try {
            // Buscar funcionário para ter a data de admissão
            Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                    .orElse(null);
            if (funcionario == null) {
                return "Funcionario nao encontrado.";
            }

            // Validar segundo Anexo A1 e decidir aprovação/rejeição
            ResultadoValidacao resultado = validarAnexoA1(funcionario, dataInicio, dataFim, motivo);

            // Inserir com estado já definido (APROVADA ou REJEITADA, nunca PENDENTE)
                folgaRepository.criarPedido(funcionarioId, dataPedido, dataInicio, dataFim, motivo,
                    resultado.aprovada ? "APROVADA" : "REJEITADA",
                    resultado.aprovada ? resultado.remuneracao : BigDecimal.ZERO);

        } catch (Exception e) {
            return "Erro ao registar a solicitacao de ausencia: " + e.getMessage();
        }

        return null;
    }

    private ResultadoValidacao validarAnexoA1(Funcionario funcionario, LocalDate dataInicio,
                                               LocalDate dataFim, String motivo) {
        long diasConsecutivos = contarDiasConsecutivos(dataInicio, dataFim);
        long diasUteis = contarDiasUteis(dataInicio, dataFim);
        int ano = dataInicio.getYear();

        // Verificar overlap com outras ausências aprovadas (diferente para férias)
        if (motivo.equals("Férias")) {
            // Férias: não podem sobrepor com QUALQUER ausência aprovada de QUALQUER funcionário
            if (temOverlapComQualquerFuncionario(dataInicio, dataFim)) {
                return new ResultadoValidacao(false, BigDecimal.ZERO);
            }
        } else {
            // Outras ausências: não podem sobrepor com ausências DO MESMO FUNCIONÁRIO
            if (temOverlap(funcionario.funcionarioId(), dataInicio, dataFim)) {
                return new ResultadoValidacao(false, BigDecimal.ZERO);
            }
        }

        return switch (motivo) {
            case "Doença" -> {
                // Max 3 dias consecutivos (calendário)
                yield diasConsecutivos <= 3 ? new ResultadoValidacao(true, calcularRemuneracao(funcionario, dataInicio, dataFim, motivo))
                        : new ResultadoValidacao(false, BigDecimal.ZERO);
            }
            case "Doença com regime de proteção pela Segurança Social" -> {
                // Superior a 4 dias consecutivos (calendário)
                yield diasConsecutivos > 4 ? new ResultadoValidacao(true, calcularRemuneracao(funcionario, dataInicio, dataFim, motivo))
                        : new ResultadoValidacao(false, BigDecimal.ZERO);
            }
            case "Férias" -> validarFerias(funcionario, dataInicio, dataFim, diasUteis, ano);

            case "Falecimento de cônjuge", "Falecimento de pais" -> {
                // Max 5 dias consecutivos (calendário)
                yield diasConsecutivos <= 5 ? new ResultadoValidacao(true, calcularRemuneracao(funcionario, dataInicio, dataFim, motivo))
                        : new ResultadoValidacao(false, BigDecimal.ZERO);
            }
            case "Falecimento de avós", "Falecimento de irmãos" -> {
                // Max 2 dias consecutivos (calendário)
                yield diasConsecutivos <= 2 ? new ResultadoValidacao(true, calcularRemuneracao(funcionario, dataInicio, dataFim, motivo))
                        : new ResultadoValidacao(false, BigDecimal.ZERO);
            }
            case "Falecimento de filhos" -> {
                // Max 20 dias consecutivos (calendário)
                yield diasConsecutivos <= 20 ? new ResultadoValidacao(true, calcularRemuneracao(funcionario, dataInicio, dataFim, motivo))
                        : new ResultadoValidacao(false, BigDecimal.ZERO);
            }
            case "Licença de casamento" -> validarLicencaCasamento(funcionario, dataInicio, dataFim, diasConsecutivos);

            case "Licença de maternidade" -> {
                // 43-92 dias consecutivos (calendário)
                yield diasConsecutivos > 42 && diasConsecutivos <= 92 ? new ResultadoValidacao(true, calcularRemuneracao(funcionario, dataInicio, dataFim, motivo))
                        : new ResultadoValidacao(false, BigDecimal.ZERO);
            }
            case "Licença de paternidade" -> validarLicencaPaternidade(funcionario, dataInicio, dataFim, diasConsecutivos);

            case "Assistência à família" -> {
                // Max 15 dias/ano (dias de calendário)
                long diasNoAno = folgaRepository.contarDiasMotivo(funcionario.funcionarioId(), motivo, ano);
                long totalCom = diasNoAno + diasConsecutivos;
                yield totalCom <= 15 ? new ResultadoValidacao(true, calcularRemuneracao(funcionario, dataInicio, dataFim, motivo))
                        : new ResultadoValidacao(false, BigDecimal.ZERO);
            }
            case "Assistência a filho" -> {
                // Max 30 dias/ano (dias de calendário)
                long diasNoAno = folgaRepository.contarDiasMotivo(funcionario.funcionarioId(), motivo, ano);
                long totalCom = diasNoAno + diasConsecutivos;
                yield totalCom <= 30 ? new ResultadoValidacao(true, calcularRemuneracao(funcionario, dataInicio, dataFim, motivo))
                        : new ResultadoValidacao(false, BigDecimal.ZERO);
            }
            case "Assistência a filho deficiente ou com doença crónica" -> {
                // Max 180 dias consecutivos (calendário)
                yield diasConsecutivos <= 180 ? new ResultadoValidacao(true, calcularRemuneracao(funcionario, dataInicio, dataFim, motivo))
                        : new ResultadoValidacao(false, BigDecimal.ZERO);
            }
            default -> new ResultadoValidacao(false, BigDecimal.ZERO);
        };
    }

    private ResultadoValidacao validarFerias(Funcionario funcionario, LocalDate dataInicio,
                                             LocalDate dataFim, long diasUteis, int ano) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataAdmissao = funcionario.dataAdmissao();

        // 1. Max 30 dias úteis/ano - Contar dias úteis das férias já aprovadas
        long diasFeriasUteisNoAno = contarDiasUteisFerias(funcionario.funcionarioId(), ano);
        if (diasFeriasUteisNoAno + diasUteis > 30) {
            return new ResultadoValidacao(false, BigDecimal.ZERO);
        }

        // 2. Se no ano de admissão: max 15 dias úteis total e max 2 dias úteis/mês
        if (ano == dataAdmissao.getYear()) {
            if (diasFeriasUteisNoAno + diasUteis > 15) {
                return new ResultadoValidacao(false, BigDecimal.ZERO);
            }
            // Validar máximo 2 dias úteis por mês
            if (!validarMaxDiasPorMes(dataInicio, dataFim, 2)) {
                return new ResultadoValidacao(false, BigDecimal.ZERO);
            }
        } else {
            // 3. Exigir MAIS de 6 meses desde admissão (fora do ano de admissão)
            // Requisito: "intervalo superior a 6 meses" = > 6 meses
            long mesesDesdeAdmissao = ChronoUnit.MONTHS.between(dataAdmissao, hoje);
            if (mesesDesdeAdmissao <= 6) {
                return new ResultadoValidacao(false, BigDecimal.ZERO);
            }
        }

        // 4. Validar período (1 maio - 31 outubro): data fim deve ser <= 30 abr do ano seguinte
        if (dataInicio.getMonthValue() >= 5 && dataInicio.getMonthValue() <= 10) {
            LocalDate teto = LocalDate.of(dataInicio.getYear() + 1, 4, 30);
            if (dataFim.isAfter(teto)) {
                return new ResultadoValidacao(false, BigDecimal.ZERO);
            }
        }

        return new ResultadoValidacao(true, calcularRemuneracao(funcionario, dataInicio, dataFim, "Férias"));
    }

    private ResultadoValidacao validarLicencaCasamento(Funcionario funcionario, LocalDate dataInicio, LocalDate dataFim, long diasConsecutivos) {
        LocalDate hoje = LocalDate.now();
        long diasAteInicio = ChronoUnit.DAYS.between(hoje, dataInicio);

        // Min 5 dias entre pedido e início, max 15 dias consecutivos
        boolean condicao = diasAteInicio >= 5 && diasConsecutivos <= 15;
        return new ResultadoValidacao(condicao, condicao ? calcularRemuneracao(funcionario, dataInicio, dataFim, "Licença de casamento") : BigDecimal.ZERO);
    }

    private ResultadoValidacao validarLicencaPaternidade(Funcionario funcionario, LocalDate dataInicio, LocalDate dataFim, long diasConsecutivos) {
        LocalDate hoje = LocalDate.now();
        long diasAteInicio = ChronoUnit.DAYS.between(hoje, dataInicio);

        // Min 5 dias entre pedido e início, 8-27 dias consecutivos
        boolean condicao = diasAteInicio >= 5 && diasConsecutivos > 7 && diasConsecutivos < 28;
        return new ResultadoValidacao(condicao, condicao ? calcularRemuneracao(funcionario, dataInicio, dataFim, "Licença de paternidade") : BigDecimal.ZERO);
    }

    private boolean temOverlap(Long funcionarioId, LocalDate dataInicio, LocalDate dataFim) {
        // Verificar se existe overlap com aprovadas do mesmo funcionário
        long count = folgaRepository.contarAusenciasAprovadas(funcionarioId, dataInicio, dataFim);
        return count > 0;
    }

    private boolean temOverlapComQualquerFuncionario(LocalDate dataInicio, LocalDate dataFim) {
        // Verificar se existe overlap com QUALQUER ausência aprovada de QUALQUER funcionário
        long count = folgaRepository.contarAusenciasAprovadosCualquerFuncionario(dataInicio, dataFim);
        return count > 0;
    }

    private boolean validarMaxDiasPorMes(LocalDate inicio, LocalDate fim, int maxDiasPerMes) {
        // Contar dias úteis por mês e verificar que nenhum mês excede o máximo
        Map<YearMonth, Integer> diasPorMes = new HashMap<>();
        LocalDate atual = inicio;
        
        while (!atual.isAfter(fim)) {
            DayOfWeek dow = atual.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                YearMonth mes = YearMonth.from(atual);
                diasPorMes.put(mes, diasPorMes.getOrDefault(mes, 0) + 1);
            }
            atual = atual.plusDays(1);
        }
        
        // Verificar se algum mês excede o máximo
        return diasPorMes.values().stream().allMatch(d -> d <= maxDiasPerMes);
    }

    private long contarDiasUteisFerias(Long funcionarioId, int ano) {
        // Obter todas as férias aprovadas do funcionário no ano
        List<Folga> feriasAprovadas = folgaRepository.listarFeriasAprovadas(funcionarioId, ano);
        
        long totalDiasUteis = 0;
        for (Folga folga : feriasAprovadas) {
            totalDiasUteis += contarDiasUteis(folga.dataInicio(), folga.dataFim());
        }
        
        return totalDiasUteis;
    }

    public List<Folga> listarMinhas(Long funcionarioId) {
        return folgaRepository.listarMinhas(funcionarioId);
    }

    public List<Folga> listarMinhasComFiltros(Long funcionarioId, String estado, LocalDate dataPedido, LocalDate dataInicio) {
        return folgaRepository.listarMinhasComFiltros(funcionarioId, estado, dataPedido, dataInicio);
    }

    public List<Folga> listarMinhasComFiltrosPaginado(Long funcionarioId, String estado, LocalDate dataPedido, LocalDate dataInicio, int limit, int offset) {
        return folgaRepository.listarMinhasComFiltrosPaginado(funcionarioId, estado, dataPedido, dataInicio, limit, offset);
    }

    public long contarMinhasComFiltros(Long funcionarioId, String estado, LocalDate dataPedido, LocalDate dataInicio) {
        return folgaRepository.contarMinhasComFiltros(funcionarioId, estado, dataPedido, dataInicio);
    }

    public List<Folga> listarTodas() {
        return folgaRepository.listarTodas();
    }

    public List<Folga> listarTodasComFiltros(String estado, String funcionario, String departamento) {
        return folgaRepository.listarTodasComFiltros(estado, funcionario, departamento);
    }

    public List<Folga> listarTodasComFiltrosPaginado(String estado, String funcionario, String departamento, int limit, int offset) {
        return folgaRepository.listarTodasComFiltrosPaginado(estado, funcionario, departamento, limit, offset);
    }

    public long contarTodasComFiltros(String estado, String funcionario, String departamento) {
        return folgaRepository.contarTodasComFiltros(estado, funcionario, departamento);
    }

    public void aprovar(Long folgaId, LocalDate dataInicio, LocalDate dataFim) {
        Folga folga = folgaRepository.findById(folgaId).orElse(null);
        Funcionario funcionario = folga == null
            ? null
            : funcionarioRepository.findById(folga.funcionarioId()).orElse(null);
        String motivo = folga == null ? null : folga.motivo();
        BigDecimal remuneracao = calcularRemuneracao(funcionario, dataInicio, dataFim, motivo);
        folgaRepository.aprovar(folgaId, remuneracao);
    }

    public void rejeitar(Long folgaId) {
        folgaRepository.rejeitar(folgaId);
    }

    private long contarDiasConsecutivos(LocalDate inicio, LocalDate fim) {
        // Conta dias de calendário (seg-dom, incluindo fins de semana)
        return ChronoUnit.DAYS.between(inicio, fim) + 1;
    }

    private long contarDiasUteis(LocalDate inicio, LocalDate fim) {
        long dias = 0;
        LocalDate atual = inicio;
        while (!atual.isAfter(fim)) {
            DayOfWeek dow = atual.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                dias++;
            }
            atual = atual.plusDays(1);
        }
        return dias;
    }

    private BigDecimal calcularRemuneracao(Funcionario funcionario, LocalDate dataInicio, LocalDate dataFim, String motivo) {
        if (funcionario == null || dataInicio == null || dataFim == null || dataFim.isBefore(dataInicio)) {
            return BigDecimal.ZERO;
        }

        if (motivo != null && MOTIVOS_SEM_REMUNERACAO.contains(motivo)) {
            return BigDecimal.ZERO;
        }

        double salarioMensal = obterSalarioMensal(funcionario.departamento(), funcionario.escalao());
        if (salarioMensal <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal remuneracaoTotal = BigDecimal.ZERO;
        LocalDate dataAtual = dataInicio;

        while (!dataAtual.isAfter(dataFim)) {
            int diasMes = dataAtual.lengthOfMonth();
            BigDecimal salarioDiario = BigDecimal.valueOf(salarioMensal)
                    .divide(BigDecimal.valueOf(diasMes), 10, RoundingMode.HALF_UP);

            LocalDate fimMesAtual = dataAtual.withDayOfMonth(diasMes);
            LocalDate fimPeriodoAtual = fimMesAtual.isBefore(dataFim) ? fimMesAtual : dataFim;
            long diasPeriodoAtual = ChronoUnit.DAYS.between(dataAtual, fimPeriodoAtual) + 1;

            remuneracaoTotal = remuneracaoTotal.add(
                    salarioDiario.multiply(BigDecimal.valueOf(diasPeriodoAtual))
            );

            dataAtual = fimPeriodoAtual.plusDays(1);
        }

        return remuneracaoTotal.setScale(2, RoundingMode.HALF_UP);
    }

    private double obterSalarioMensal(String departamento, Integer escalao) {
        if (departamento == null || escalao == null || escalao < 1) {
            return 0.0;
        }

        String departamentoNormalizado = normalizarDepartamento(departamento);
        double[] tabela = null;

        if (DEPARTAMENTOS_ESPECIALISTAS.contains(departamentoNormalizado)) {
            tabela = SALARIOS_ESPECIALISTAS;
        } else if (DEPARTAMENTOS_TECNICOS.contains(departamentoNormalizado)) {
            tabela = SALARIOS_TECNICOS;
        }

        if (tabela == null || escalao > tabela.length) {
            return 0.0;
        }

        return tabela[escalao - 1];
    }

    private String normalizarDepartamento(String departamento) {
        String semAcentos = Normalizer.normalize(departamento, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcentos.toLowerCase()
                .replace('-', ' ')
                .trim()
                .replaceAll("\\s+", " ");
    }

    public Folga obterPorId(Long folgaId) {
        return folgaRepository.findById(folgaId).orElse(null);
    }

    public void eliminar(Long folgaId) {
        folgaRepository.delete(folgaId);
    }

    public String atualizar(Long folgaId, LocalDate dataInicio, LocalDate dataFim, String motivo) {
        if (dataInicio == null || dataFim == null || motivo == null || motivo.isBlank()) {
            return "Preencha a Data de Inicio, a Data de Fim e o Motivo antes de alterar.";
        }

        LocalDate hoje = LocalDate.now();
        if (dataInicio.isBefore(hoje) || dataFim.isBefore(hoje)) {
            return "A Data de Inicio e a Data de Fim devem ser superiores ou iguais a data atual.";
        }

        if (dataFim.isBefore(dataInicio)) {
            return "A Data de Inicio da ausencia deve ser inferior ou igual a Data de Fim.";
        }

        Folga folga = folgaRepository.findById(folgaId).orElse(null);
        if (folga == null) {
            return "Ausencia nao encontrada.";
        }

        Funcionario funcionario = funcionarioRepository.findById(folga.funcionarioId()).orElse(null);
        if (funcionario == null) {
            return "Funcionario nao encontrado.";
        }

        ResultadoValidacao resultado = validarAnexoA1(funcionario, dataInicio, dataFim, motivo);
        folgaRepository.atualizar(
                folgaId,
                LocalDate.now(),
                dataInicio,
                dataFim,
                motivo,
                resultado.aprovada ? "APROVADA" : "REJEITADA",
            resultado.aprovada ? resultado.remuneracao : BigDecimal.ZERO
        );

        return null;
    }

    private record ResultadoValidacao(boolean aprovada, BigDecimal remuneracao) {
    }
}
