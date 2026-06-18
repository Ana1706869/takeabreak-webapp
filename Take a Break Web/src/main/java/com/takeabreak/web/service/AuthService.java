package com.takeabreak.web.service;

import java.util.Optional;
import java.util.regex.Pattern;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.takeabreak.web.model.Funcionario;
import com.takeabreak.web.repository.FolgaRepository;
import com.takeabreak.web.repository.FuncionarioRepository;

@Service
public class AuthService {

    private static final String EMAIL_GESTOR_PRODUCAO = "anasilva_pinhel@hotmail.com";

    private final FuncionarioRepository funcionarioRepository;
    private final FolgaRepository folgaRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(FuncionarioRepository funcionarioRepository, FolgaRepository folgaRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.folgaRepository = folgaRepository;
    }

    @PostConstruct
    public void migrarPasswordsEmTextoPlano() {
        for (Funcionario funcionario : funcionarioRepository.findAll()) {
            String passwordAtual = funcionario.password();
            if (passwordAtual != null && !isBCryptHash(passwordAtual)) {
                String passwordHash = passwordEncoder.encode(passwordAtual);
                funcionarioRepository.updatePasswordById(funcionario.funcionarioId(), passwordHash);
            }
        }
    }

    public Optional<Funcionario> login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }

        String emailTrim = email.trim();

        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findByEmail(emailTrim);
        if (funcionarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Funcionario funcionario = funcionarioOpt.get();
        if (!passwordConfere(password, funcionario.password())) {
            return Optional.empty();
        }

        if (EMAIL_GESTOR_PRODUCAO.equalsIgnoreCase(emailTrim) && !"GESTOR".equals(funcionario.role())) {
            funcionarioRepository.updateRoleByEmail(emailTrim, "GESTOR");
            funcionario = funcionarioRepository.findByEmail(emailTrim).orElse(funcionario);
        }

        atualizarParaHashSeNecessario(funcionario, password);
        return Optional.of(funcionario);
    }

    public String validarLogin(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return "Preencha o nome de utilizador e a palavra-passe.";
        }

        if (funcionarioRepository.findByEmail(email).isEmpty()) {
            return "Funcionario nao registado na aplicacao.";
        }

        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findByEmail(email.trim());
        if (funcionarioOpt.isEmpty()) {
            return "Funcionario nao registado na aplicacao.";
        }

        Funcionario funcionario = funcionarioOpt.get();
        if (!passwordConfere(password, funcionario.password())) {
            return "Nome de utilizador ou palavra-passe incorretos.";
        }

        atualizarParaHashSeNecessario(funcionario, password);

        return null;
    }

    public String registar(String nome, String email, String password, String passwordConfirm, String telefone,
                           String departamento, int escalao, String dataAdmissao, String morada, String codigoPostal,
                           String localidade, String concelho, String distrito) {
        String nomeTrim = nome != null ? nome.trim() : "";
        String emailTrim = email != null ? email.trim() : "";
        String telefoneTrim = telefone != null ? telefone.trim() : "";
        String departamentoTrim = departamento != null ? departamento.trim() : "";
        String moradaTrim = morada != null ? morada.trim() : "";
        String codigoPostalTrim = codigoPostal != null ? codigoPostal.trim() : "";
        String localidadeTrim = localidade != null ? localidade.trim() : "";
        String concelhoTrim = concelho != null ? concelho.trim() : "";
        String distritoTrim = distrito != null ? distrito.trim() : "";

        
        // Validar campos obrigatórios
        if (nome == null || nomeTrim.isEmpty()) {
            return "Nome é obrigatório.";
        }
        if (email == null || emailTrim.isEmpty()) {
            return "Email é obrigatório.";
        }
        if (telefone == null || telefoneTrim.isEmpty()) {
            return "Telefone é obrigatório.";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Palavra-passe é obrigatória.";
        }
        if (passwordConfirm == null || passwordConfirm.trim().isEmpty()) {
            return "Confirmação de palavra-passe é obrigatória.";
        }

        if (departamentoTrim.isEmpty()) {
            return "Departamento é obrigatório.";
        }

        if (dataAdmissao == null || dataAdmissao.trim().isEmpty()) {
            return "Data de admissão é obrigatória.";
        }
        java.time.LocalDate dataAdmissaoParsed;
        try {
            dataAdmissaoParsed = java.time.LocalDate.parse(dataAdmissao.trim());
        } catch (Exception e) {
            return "Data de admissão inválida.";
        }

        if (escalao < 1 || escalao > 12) {
            return "Escalão inválido. Selecione um valor entre 1 e 12.";
        }
        
        // Validar se as palavras-passe coincidem
        if (!password.equals(passwordConfirm)) {
            return "As palavras-passe não coincidem. Verifique e tente novamente.";
        }

        // Validar formato de email
        if (!validarEmail(emailTrim)) {
            return "Formato de email inválido.";
        }

        if (!validarNome(nomeTrim)) {
            return "Nome inválido. Use apenas letras e espaços.";
        }

        if (!validarTelefone(telefoneTrim)) {
            return "Formato de telefone inválido. Use 9 dígitos.";
        }

        if (!moradaTrim.isEmpty() && !validarMorada(moradaTrim)) {
            return "Morada inválida. Use letras, números e pontuação simples.";
        }

        if (!localidadeTrim.isEmpty() && !validarTextoLocal(localidadeTrim)) {
            return "Localidade inválida. Use apenas letras e espaços.";
        }

        if (!concelhoTrim.isEmpty() && !validarTextoLocal(concelhoTrim)) {
            return "Concelho inválido. Use apenas letras e espaços.";
        }

        if (!distritoTrim.isEmpty() && !validarTextoLocal(distritoTrim)) {
            return "Distrito inválido. Use apenas letras e espaços.";
        }
        
        // Validar formato do código postal se preenchido
        if (!codigoPostalTrim.isEmpty()) {
            if (!validarCodigoPostal(codigoPostalTrim)) {
                return "Formato de código-postal inválido. Use o formato XXXX-XXX.";
            }
        }
        
        // Validar se email já existe
        if (funcionarioRepository.existsByEmail(emailTrim)) {
            return "Ja existe um utilizador com este email.";
        }

        // Validar se telefone já existe
        if (funcionarioRepository.existsByTelefone(telefoneTrim)) {
            return "Ja existe um utilizador com este telefone.";
        }
        
        try {
            // Registar novo utilizador
                    String passwordHash = passwordEncoder.encode(password);
                    funcionarioRepository.create(nomeTrim, emailTrim, passwordHash, departamentoTrim, escalao,
                    dataAdmissaoParsed,
                    moradaTrim,
                    codigoPostalTrim,
                    localidadeTrim,
                    concelhoTrim,
                    distritoTrim,
                    telefoneTrim);
            return null;
        } catch (Exception e) {
            return "Erro ao criar conta: " + e.getMessage();
        }
    }

    public Funcionario obterPerfil(Long funcionarioId) {
        return funcionarioRepository.findById(funcionarioId).orElse(null);
    }

    public String atualizarPerfil(Long funcionarioId,
                                  String nome,
                                  String email,
                                  String password,
                                  String passwordConfirm,
                                  String telefone,
                                  String departamento,
                                  Integer escalao,
                                  String dataAdmissao,
                                  String morada,
                                  String codigoPostal,
                                  String localidade,
                                  String concelho,
                                  String distrito) {
        Funcionario atual = funcionarioRepository.findById(funcionarioId).orElse(null);
        if (atual == null) {
            return "Perfil nao encontrado.";
        }

        if (nome == null || nome.trim().isEmpty()) {
            return "Nome é obrigatório.";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Email é obrigatório.";
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            return "Telefone é obrigatório.";
        }
        if (departamento == null || departamento.trim().isEmpty()) {
            return "Departamento é obrigatório.";
        }
        if (escalao == null || escalao < 1 || escalao > 12) {
            return "Escalão inválido. Selecione um valor entre 1 e 12.";
        }

        if (codigoPostal != null && !codigoPostal.trim().isEmpty() && !validarCodigoPostal(codigoPostal.trim())) {
            return "Formato de código-postal inválido. Use o formato XXXX-XXX.";
        }

        if (funcionarioRepository.existsByEmailAndIdNot(email.trim(), funcionarioId)) {
            return "Ja existe um utilizador com este email.";
        }

        if (funcionarioRepository.existsByTelefoneAndIdNot(telefone.trim(), funcionarioId)) {
            return "Ja existe um utilizador com este telefone.";
        }

        java.time.LocalDate dataAdmissaoFinal = atual.dataAdmissao();
        if (dataAdmissao != null && !dataAdmissao.trim().isEmpty()) {
            try {
                dataAdmissaoFinal = java.time.LocalDate.parse(dataAdmissao.trim());
            } catch (Exception e) {
                return "Data de admissão inválida.";
            }
        }

        String passwordFinal = atual.password();
        boolean passwordInformada = password != null && !password.isBlank();
        boolean passwordConfirmInformada = passwordConfirm != null && !passwordConfirm.isBlank();
        if (passwordInformada || passwordConfirmInformada) {
            if (!passwordInformada || !passwordConfirmInformada) {
                return "Para alterar a palavra-passe, preencha os dois campos de palavra-passe.";
            }
            if (!password.equals(passwordConfirm)) {
                return "As palavras-passe não coincidem. Verifique e tente novamente.";
            }
            passwordFinal = passwordEncoder.encode(password);
        }

        funcionarioRepository.updatePerfil(
                funcionarioId,
                nome.trim(),
                email.trim(),
                passwordFinal,
                departamento.trim(),
                escalao,
                dataAdmissaoFinal,
                morada != null ? morada.trim() : "",
                codigoPostal != null ? codigoPostal.trim() : "",
                localidade != null ? localidade.trim() : "",
                concelho != null ? concelho.trim() : "",
                distrito != null ? distrito.trim() : "",
                telefone.trim()
        );

        return null;
    }

    public void eliminarPerfil(Long funcionarioId) {
        folgaRepository.deleteByFuncionarioId(funcionarioId);
        funcionarioRepository.deleteById(funcionarioId);
    }

    /**
     * Valida o formato do código postal (XXXX-XXX)
     */
    private static boolean validarCodigoPostal(String codigoPostal) {
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{3}$");
        return pattern.matcher(codigoPostal).matches();
    }

    private static boolean validarEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        return pattern.matcher(email).matches();
    }

    private static boolean validarNome(String nome) {
        Pattern pattern = Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ ]{2,120}$");
        return pattern.matcher(nome).matches();
    }

    private static boolean validarTelefone(String telefone) {
        Pattern pattern = Pattern.compile("^\\d{9}$");
        return pattern.matcher(telefone).matches();
    }

    private static boolean validarMorada(String morada) {
        Pattern pattern = Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ0-9 .,'/-]{3,160}$");
        return pattern.matcher(morada).matches();
    }

    private static boolean validarTextoLocal(String texto) {
        Pattern pattern = Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ ]{2,120}$");
        return pattern.matcher(texto).matches();
    }

    private boolean passwordConfere(String passwordRaw, String passwordGuardada) {
        if (passwordGuardada == null || passwordGuardada.isBlank()) {
            return false;
        }

        if (isBCryptHash(passwordGuardada)) {
            return passwordEncoder.matches(passwordRaw, passwordGuardada);
        }

        return passwordRaw.equals(passwordGuardada);
    }

    private void atualizarParaHashSeNecessario(Funcionario funcionario, String passwordRaw) {
        String passwordGuardada = funcionario.password();
        if (passwordGuardada != null && !isBCryptHash(passwordGuardada) && passwordRaw.equals(passwordGuardada)) {
            String passwordHash = passwordEncoder.encode(passwordRaw);
            funcionarioRepository.updatePasswordById(funcionario.funcionarioId(), passwordHash);
        }
    }

    private boolean isBCryptHash(String value) {
        return value != null && value.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
    }
}
