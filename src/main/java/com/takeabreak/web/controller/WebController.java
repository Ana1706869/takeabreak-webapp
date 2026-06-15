package com.takeabreak.web.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takeabreak.web.model.Folga;
import com.takeabreak.web.model.Funcionario;
import com.takeabreak.web.service.AuthService;
import com.takeabreak.web.service.FolgaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    private final AuthService authService;
    private final FolgaService folgaService;

    public WebController(AuthService authService, FolgaService folgaService) {
        this.authService = authService;
        this.folgaService = folgaService;
    }

    @GetMapping({"/", "/index.html"})
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        String erroLogin = authService.validarLogin(email, password);
        if (erroLogin != null) {
            model.addAttribute("erro", erroLogin);
            return "index";
        }

        Funcionario funcionario = authService.login(email, password).orElse(null);
        if (funcionario == null) {
            model.addAttribute("erro", "Falha na autenticacao. Verifique as credenciais.");
            return "index";
        }

        session.setAttribute("userId", funcionario.funcionarioId());
        session.setAttribute("userNome", funcionario.nome());
        session.setAttribute("userRole", funcionario.role());
        return "redirect:/dashboard";
    }

    @GetMapping("/registo")
    public String registo() {
        return "registo";
    }

    @PostMapping("/registo")
    public String registoSubmit(@RequestParam String nome,
                                @RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String passwordConfirm,
                                @RequestParam String telefone,
                                @RequestParam String departamento,
                                @RequestParam Integer escalao,
                                @RequestParam String morada,
                                @RequestParam String codigoPostal,
                                @RequestParam String localidade,
                                @RequestParam String concelho,
                                @RequestParam String distrito,
                                Model model) {
        try {
            String erro = authService.registar(nome, email, password, passwordConfirm, telefone, departamento, escalao, morada, codigoPostal, localidade, concelho, distrito);
            if (erro != null) {
                model.addAttribute("erro", erro);
                return "registo";
            }
            model.addAttribute("ok", "Conta criada com sucesso. Faca login.");
            return "index";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao criar conta: " + e.getMessage());
            return "registo";
        }
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/index.html";
        }

        Funcionario perfil = authService.obterPerfil(userId);
        if (perfil == null) {
            return "redirect:/dashboard";
        }

        String nomeSessao = (String) session.getAttribute("userNome");
        model.addAttribute("nome", nomeSessao);
        model.addAttribute("perfil", perfil);
        return "perfil";
    }

    @PostMapping("/perfil/atualizar")
    public String atualizarPerfil(@RequestParam String nome,
                                  @RequestParam String email,
                                  @RequestParam(required = false) String password,
                                  @RequestParam(required = false) String passwordConfirm,
                                  @RequestParam String telefone,
                                  @RequestParam String departamento,
                                  @RequestParam Integer escalao,
                                  @RequestParam(required = false) String morada,
                                  @RequestParam(required = false) String codigoPostal,
                                  @RequestParam(required = false) String localidade,
                                  @RequestParam(required = false) String concelho,
                                  @RequestParam(required = false) String distrito,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/index.html";
        }

        String erro = authService.atualizarPerfil(
                userId,
                nome,
                email,
                password,
                passwordConfirm,
                telefone,
                departamento,
                escalao,
                morada,
                codigoPostal,
                localidade,
                concelho,
                distrito
        );

        if (erro != null) {
            redirectAttributes.addFlashAttribute("erro", erro);
            return "redirect:/perfil";
        }

        session.setAttribute("userNome", nome);
        redirectAttributes.addFlashAttribute("ok", "Dados pessoais atualizados com sucesso.");
        return "redirect:/perfil";
    }

    @PostMapping("/perfil/eliminar")
    public String eliminarPerfil(HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/index.html";
        }

        try {
            authService.eliminarPerfil(userId);
            session.invalidate();
            redirectAttributes.addFlashAttribute("ok", "Perfil eliminado com sucesso.");
            return "redirect:/index.html";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao eliminar perfil: " + e.getMessage());
            return "redirect:/perfil";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String estado,
                            @RequestParam(required = false) String funcionario,
                            @RequestParam(required = false) String departamento,
                            @RequestParam(required = false) String dataPedido,
                            @RequestParam(required = false) String dataInicio,
                            @RequestParam(defaultValue = "1") Integer page,
                            HttpSession session,
                            Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");
        String nome = (String) session.getAttribute("userNome");

        if (userId == null) {
            return "redirect:/index.html";
        }

        model.addAttribute("nome", nome);
        boolean manager = "GESTOR".equals(role);
        model.addAttribute("manager", manager);

        String estadoFiltro = (estado == null || estado.isBlank()) ? null : estado.trim().toUpperCase();
        String funcionarioFiltro = (funcionario == null || funcionario.isBlank()) ? null : funcionario.trim();
        String departamentoFiltro = (departamento == null || departamento.isBlank() || "(Todos)".equals(departamento))
            ? null
            : departamento.trim();

        LocalDate dataPedidoFiltro = null;
        if (dataPedido != null && !dataPedido.isBlank()) {
            try {
                dataPedidoFiltro = LocalDate.parse(dataPedido);
            } catch (Exception e) {
                model.addAttribute("erro", "Data de Pedido invalida para pesquisa.");
            }
        }

        LocalDate dataInicioFiltro = null;
        if (dataInicio != null && !dataInicio.isBlank()) {
            try {
                dataInicioFiltro = LocalDate.parse(dataInicio);
            } catch (Exception e) {
                model.addAttribute("erro", "Data de Inicio invalida para pesquisa.");
            }
        }

        final int pageSize = 10;
        long totalItems = manager
            ? folgaService.contarTodasComFiltros(estadoFiltro, funcionarioFiltro, departamentoFiltro)
            : folgaService.contarMinhasComFiltros(userId, estadoFiltro, dataPedidoFiltro, dataInicioFiltro);

        int totalPages = (int) Math.ceil(totalItems / (double) pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }

        int currentPage = page == null ? 1 : page;
        if (currentPage < 1) {
            currentPage = 1;
        }
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * pageSize;

        List<Folga> folgas = manager
            ? folgaService.listarTodasComFiltrosPaginado(estadoFiltro, funcionarioFiltro, departamentoFiltro, pageSize, offset)
            : folgaService.listarMinhasComFiltrosPaginado(userId, estadoFiltro, dataPedidoFiltro, dataInicioFiltro, pageSize, offset);

        int inicioRegistos = totalItems == 0 ? 0 : offset + 1;
        int fimRegistos = totalItems == 0 ? 0 : Math.min(offset + folgas.size(), (int) totalItems);

        model.addAttribute("folgas", folgas);
        model.addAttribute("paginaAtual", currentPage);
        model.addAttribute("totalPaginas", totalPages);
        model.addAttribute("totalRegistos", totalItems);
        model.addAttribute("inicioRegistos", inicioRegistos);
        model.addAttribute("fimRegistos", fimRegistos);
        model.addAttribute("temAnterior", currentPage > 1);
        model.addAttribute("temSeguinte", currentPage < totalPages);
        model.addAttribute("filtroEstado", estadoFiltro == null ? "" : estadoFiltro);
        model.addAttribute("filtroFuncionario", funcionarioFiltro == null ? "" : funcionarioFiltro);
        model.addAttribute("filtroDepartamento", departamentoFiltro == null ? "" : departamentoFiltro);
        model.addAttribute("filtroDataPedido", dataPedidoFiltro == null ? "" : dataPedidoFiltro.toString());
        model.addAttribute("filtroDataInicio", dataInicioFiltro == null ? "" : dataInicioFiltro.toString());
        return "dashboard";
    }

    @PostMapping("/folgas")
    public String criarPedido(@RequestParam(required = false) String dataInicio,
                              @RequestParam(required = false) String dataFim,
                              @RequestParam(required = false) String motivo,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/index.html";
        }

        if (dataInicio == null || dataInicio.isBlank() || dataFim == null || dataFim.isBlank() || motivo == null || motivo.isBlank()) {
            redirectAttributes.addFlashAttribute("erro", "Preencha a Data de Inicio, a Data de Fim e o Motivo antes de submeter.");
            return "redirect:/dashboard";
        }

        LocalDate dataInicioParsed;
        LocalDate dataFimParsed;
        try {
            dataInicioParsed = LocalDate.parse(dataInicio);
            dataFimParsed = LocalDate.parse(dataFim);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro no registo da solicitacao: formato de data invalido.");
            return "redirect:/dashboard";
        }

        String erro = folgaService.criarPedido(userId, LocalDate.now(), dataInicioParsed, dataFimParsed, motivo);
        if (erro != null) {
            redirectAttributes.addFlashAttribute("erro", erro);
        } else {
            redirectAttributes.addFlashAttribute("ok", "Solicitacao de ausencia submetida com sucesso.");
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/folgas/{id}/aprovar")
    public String aprovar(@PathVariable Long id,
                          @RequestParam String dataInicio,
                          @RequestParam String dataFim,
                          HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"GESTOR".equals(role)) {
            return "redirect:/dashboard";
        }
        folgaService.aprovar(id, LocalDate.parse(dataInicio), LocalDate.parse(dataFim));
        return "redirect:/dashboard";
    }

    @PostMapping("/folgas/{id}/rejeitar")
    public String rejeitar(@PathVariable Long id, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"GESTOR".equals(role)) {
            return "redirect:/dashboard";
        }
        folgaService.rejeitar(id);
        return "redirect:/dashboard";
    }

    @GetMapping("/folga/editar")
    public String editarFolga(@RequestParam Long id,
                              HttpSession session,
                              Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/index.html";
        }

        Folga folga = folgaService.obterPorId(id);
        if (folga == null) {
            return "redirect:/dashboard";
        }

        String nome = (String) session.getAttribute("userNome");
        model.addAttribute("nome", nome);
        model.addAttribute("folga", folga);
        return "editar-folga";
    }

    @PostMapping("/folga/atualizar")
    public String atualizarFolga(@RequestParam Long id,
                                 @RequestParam String dataInicio,
                                 @RequestParam String dataFim,
                                 @RequestParam String motivo,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/index.html";
        }

        Folga folga = folgaService.obterPorId(id);
        if (folga == null) {
            redirectAttributes.addFlashAttribute("erro", "Ausencia nao encontrada.");
            return "redirect:/dashboard";
        }

        try {
            LocalDate dataInicioLocal = LocalDate.parse(dataInicio);
            LocalDate dataFimLocal = LocalDate.parse(dataFim);

            String erro = folgaService.atualizar(id, dataInicioLocal, dataFimLocal, motivo);
            if (erro != null) {
                redirectAttributes.addFlashAttribute("erro", erro);
            } else {
                redirectAttributes.addFlashAttribute("ok", "Ausencia atualizada com sucesso.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar ausencia: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/folga/eliminar")
    public String eliminarFolga(@RequestParam Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/index.html";
        }

        Folga folga = folgaService.obterPorId(id);
        if (folga == null) {
            redirectAttributes.addFlashAttribute("erro", "Ausencia nao encontrada.");
            return "redirect:/dashboard";
        }

        folgaService.eliminar(id);
        redirectAttributes.addFlashAttribute("ok", "Ausencia eliminada com sucesso.");
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index.html";
    }
}
