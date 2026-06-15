package RegistoFolgas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe para aprovação e rejeição de folgas
 * Sistema avançado com validações por motivo
 * Recuperada a partir do bytecode compilado
 */
public class AprovacaoRejeicao extends JDialog {
    
    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    
    private static Connection con;
    
    private String nomeUsuario;
    private JLabel jLabelNome;
    private JLabel jLabelFuncionario;
    private JLabel jLabelAprovacaoRejeicao;
    private JButton okButton;
    private JButton cancelButton;
    
    private int returnStatus;
    public Folgas folgas;
    
    private int folgaId;
    private String motivo;
    private Date dataInicio;
    private Date dataFim;
    private int funcionarioId;
    private Date dataAdmissao;
    
    public AprovacaoRejeicao(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.returnStatus = 0;
        this.jLabelNome = new JLabel();
        this.jLabelFuncionario = new JLabel();
        initComponents();
        setLocationRelativeTo(null);
        
        // Bind ESC key to cancel
        String cancelName = "cancel";
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), cancelName);
        this.getRootPane().getActionMap().put(cancelName, new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                doClose(RET_CANCEL);
            }
        });
    }
    
    public int getReturnStatus() {
        return returnStatus;
    }
    
    private void initComponents() {
        okButton = new JButton();
        jLabelAprovacaoRejeicao = new JLabel();
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                closeDialog(evt);
            }
        });
        
        okButton.setText("OK");
        okButton.addActionListener(evt -> okButtonActionPerformed(evt));
        
        jLabelAprovacaoRejeicao.setText("A sua folga foi aprovada/rejeitada.");
        
        cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(evt -> closeDialog(null));
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(255, Short.MAX_VALUE)
                    .addComponent(okButton, -2, 67, -2)
                    .addGap(84, 84, 84))
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabelAprovacaoRejeicao, -2, 354, -2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(77, 77, 77)
                    .addComponent(jLabelAprovacaoRejeicao)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                    .addComponent(okButton)
                    .addContainerGap())
        );
        
        this.getRootPane().setDefaultButton(okButton);
        pack();
    }
    
    private void okButtonActionPerformed(ActionEvent evt) {
        doClose(1);
        nomeUsuario = jLabelNome.getText().trim();
        folgas = new Folgas(jLabelNome, jLabelFuncionario);
    }
    
    private void closeDialog(WindowEvent evt) {
        doClose(0);
    }
    
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    
    public JLabel getJLabelAprovacaoRejeicao() {
        return jLabelAprovacaoRejeicao;
    }
    
    // ========== LÓGICA DE APROVAÇÃO E REJEIÇÃO ==========
    
    public boolean aprovarFolga(int folgaId, int funcionarioId, String motivo, 
                                Date dataInicio, Date dataFim, Date dataAdmissao) {
        try {
            if (con == null) {
                con = Database.getLocalConnection();
            }
            
            // Validações específicas por motivo
            if (!validarFolga(funcionarioId, motivo, dataInicio, dataFim, dataAdmissao)) {
                return false;
            }
            
            // Calcular remuneração
            double remuneracao = calcularRemuneracao(funcionarioId, dataInicio, dataFim);
            
            // Atualizar estado para Aprovada
            String query = "UPDATE Folgas SET Estado = 'Aprovada', Remuneracao = ? WHERE FolgaId = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setDouble(1, remuneracao);
            stmt.setInt(2, folgaId);
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, 
                "Folga aprovada com sucesso!\nRemuneração: €" + String.format("%.2f", remuneracao), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao aprovar: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean rejeitarFolga(int folgaId, String motivo) {
        try {
            if (con == null) {
                con = Database.getLocalConnection();
            }
            
            String query = "UPDATE Folgas SET Estado = 'Rejeitada', Motivo = ? WHERE FolgaId = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, motivo);
            stmt.setInt(2, folgaId);
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Folga rejeitada!\nMotivo: " + motivo, 
                "Rejeição", JOptionPane.INFORMATION_MESSAGE);
            
            return true;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao rejeitar: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // ========== VALIDAÇÕES POR MOTIVO ==========
    
    private boolean validarFolga(int funcionarioId, String motivo, Date dataInicio, 
                                 Date dataFim, Date dataAdmissao) {
        
        if ("Férias".equals(motivo)) {
            return podeAprovarFerias(dataAdmissao, dataInicio);
        } else if ("Assistência à família".equals(motivo)) {
            int diasUtilizados = contarDiasAnoAssistenciaFamilia(funcionarioId);
            if (diasUtilizados > 30) {
                JOptionPane.showMessageDialog(null, 
                    "Limite de dias para Assistência à família excedido!\nJá utilizou: " + diasUtilizados + " dias", 
                    "Validação", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else if ("Assistência a filho".equals(motivo)) {
            int diasUtilizados = contarDiasAnoAssistenciaFilho(funcionarioId);
            if (diasUtilizados > 10) {
                JOptionPane.showMessageDialog(null, 
                    "Limite de dias para Assistência a filho excedido!\nJá utilizou: " + diasUtilizados + " dias", 
                    "Validação", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
    
    private boolean podeAprovarFerias(Date dataAdmissao, Date dataPedido) {
        Calendar calAdmissao = Calendar.getInstance();
        calAdmissao.setTime(dataAdmissao);
        
        Calendar calPedido = Calendar.getInstance();
        calPedido.setTime(dataPedido);
        
        // Adiciona 6 meses à data de admissão
        calAdmissao.add(Calendar.MONTH, 6);
        
        // Verifica se o pedido é anterior a 6 meses após admissão
        if (calPedido.before(calAdmissao)) {
            JOptionPane.showMessageDialog(null, 
                "Funcionário ainda não completou 6 meses desde admissão!\nNão pode tirar férias.", 
                "Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private int contarDiasAnoAssistenciaFamilia(int funcionarioId) {
        int totalDias = 0;
        try {
            if (con == null) {
                con = Database.getLocalConnection();
            }
            
            String query = "SELECT DataInicio, DataFim FROM Folgas " +
                          "WHERE Estado = 'Aprovada' AND Motivo = 'Assistência à família' " +
                          "AND FuncionarioId = ?";
            
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, funcionarioId);
            ResultSet rs = pstmt.executeQuery();
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            
            while (rs.next()) {
                Date dataInicio = sdf.parse(rs.getString("DataInicio"));
                Date dataFim = sdf.parse(rs.getString("DataFim"));
                totalDias += calcularDiferencaDiasUteis(dataInicio, dataFim);
            }
            
            rs.close();
            pstmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return totalDias;
    }
    
    private int contarDiasAnoAssistenciaFilho(int funcionarioId) {
        int totalDias = 0;
        try {
            if (con == null) {
                con = Database.getLocalConnection();
            }
            
            String query = "SELECT DataInicio, DataFim FROM Folgas " +
                          "WHERE Estado = 'Aprovada' AND Motivo = 'Assistência a filho' " +
                          "AND FuncionarioId = ?";
            
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, funcionarioId);
            ResultSet rs = pstmt.executeQuery();
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            
            while (rs.next()) {
                Date dataInicio = sdf.parse(rs.getString("DataInicio"));
                Date dataFim = sdf.parse(rs.getString("DataFim"));
                totalDias += calcularDiferencaDiasUteis(dataInicio, dataFim);
            }
            
            rs.close();
            pstmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return totalDias;
    }
    
    // ========== UTILITÁRIOS ==========
    
    private int calcularDiferencaDiasUteis(Date dataInicio, Date dataFim) {
        int diasUteis = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataInicio);
        
        while (cal.getTime().compareTo(dataFim) <= 0) {
            int diaSemana = cal.get(Calendar.DAY_OF_WEEK);
            // 2 = Segunda, 3 = Terça, ..., 6 = Sexta
            if (diaSemana >= 2 && diaSemana <= 6) {
                diasUteis++;
            }
            cal.add(Calendar.DATE, 1);
        }
        
        return diasUteis;
    }
    
    private double calcularRemuneracao(int funcionarioId, Date dataInicio, Date dataFim) {
        try {
            if (con == null) {
                con = Database.getLocalConnection();
            }
            
            String query = "SELECT Salario FROM Funcionarios WHERE FuncionarioId = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, funcionarioId);
            ResultSet rs = stmt.executeQuery();
            
            double salarioMensal = 0;
            if (rs.next()) {
                salarioMensal = rs.getDouble("Salario");
            }
            
            rs.close();
            stmt.close();
            
            int diasUteis = calcularDiferencaDiasUteis(dataInicio, dataFim);
            return (salarioMensal / 30) * diasUteis;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(AprovacaoRejeicao.class.getName())
                .log(java.util.logging.Level.SEVERE, null, e);
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            AprovacaoRejeicao dialog = new AprovacaoRejeicao(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }
}
