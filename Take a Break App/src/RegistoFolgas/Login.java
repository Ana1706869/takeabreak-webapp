package RegistoFolgas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe de Login para a aplicação de Gestão de Folgas
 * Recuperada a partir do bytecode compilado (JDialog Modal)
 */
public class Login extends JDialog {
    
    private static Connection con;
    private static final String driver = "org.sqlite.JDBC";
    private static final String url = "jdbc:sqlite:data/AgendamentoFolgas.db";
    
    private PreparedStatement statement;
    private ResultSet resultSet;
    
    private JLabel jLabelNome;
    private JLabel jLabelFuncionario;
    private static final String GESTOR = "Gestor";
    
    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;
    
    private JButton jButtonEntrar;
    public JFormattedTextField jFormattedTextFieldUtilizador;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabelRegisto;
    private JPasswordField jPasswordField;
    
    public Login(Frame parent, boolean modal) {
        super(parent, modal);
        statement = null;
        resultSet = null;
        initComponents();
        jLabelNome = new JLabel();
        jLabelFuncionario = new JLabel();
        connector();
        
        // ESC para fechar
        String cancelName = "cancel";
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(27, 0), cancelName);
        getRootPane().getActionMap().put(cancelName, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }
    
    public void connector() {
        try {
            con = null;
            Class.forName(driver);
            con = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initComponents() {
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jPasswordField = new JPasswordField();
        jButtonEntrar = new JButton();
        jLabelRegisto = new JLabel();
        jFormattedTextFieldUtilizador = new JFormattedTextField();
        jLabel6 = new JLabel();
        
        setBackground(new Color(255, 255, 255));
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                closeDialog(evt);
            }
        });
        
        // Configurar jLabel1 - Título
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 24));
        jLabel1.setForeground(new Color(255, 102, 204));
        jLabel1.setText("Take a Break!");
        
        // Configurar jLabel2 - Descrição 1
        jLabel2.setText("Com o Take a Break você pode marcar as suas ausências, receber a notificação de aprovação ou rejeição, visulizar as ausências aprovadas e ainda poder saber a remuneração que irá receber pela ausência!");
        
        // Configurar jLabel3 - Instrução
        jLabel3.setText("Para aceder a Take a Break! tem de fazer o seu login ou registar-se na aplicação caso não tenha uma conta associada.");
        
        // Configurar jLabel4 - Label Utilizador
        jLabel4.setText("Nome de Utilizador");
        
        // Configurar jLabel5 - Label Senha
        jLabel5.setText("Palavra-Passe");
        
        // Configurar jPasswordField
        jPasswordField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jPasswordFieldActionPerformed(evt);
            }
        });
        
        // Configurar jButtonEntrar
        jButtonEntrar.setBackground(new Color(0, 0, 0));
        jButtonEntrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jButtonEntrar.setForeground(new Color(255, 255, 255));
        jButtonEntrar.setText("Entrar");
        jButtonEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonEntrarActionPerformed(evt);
            }
        });
        
        // Configurar jLabelRegisto
        jLabelRegisto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelRegisto.setForeground(new Color(102, 153, 255));
        jLabelRegisto.setText("Caso não tenha uma conta associada, clique aqui para fazer seu registo!");
        jLabelRegisto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabelRegisto.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                jLabelRegistoMouseClicked(evt);
            }
        });
        
        // Configurar jLabel6 - Descrição 2
        jLabel6.setText("O Take a Break faz isso automaticamente sem precisar do seu chefe!");
        
        // Configurar Layout com GroupLayout - Exatamente igual ao compilado
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addGap(98, 98, 98)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(jPasswordField, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jFormattedTextFieldUtilizador))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabelRegisto, GroupLayout.PREFERRED_SIZE, 447, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, Short.MAX_VALUE)
                        .addComponent(jButtonEntrar)
                        .addGap(283, 283, 283)))
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(238, 238, 238)
                            .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 881, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 803, GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(293, Short.MAX_VALUE))
        );
        
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                    .addGap(32, 32, 32)
                    .addComponent(jLabel2)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel6)
                    .addGap(8, 8, 8)
                    .addComponent(jLabel3)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jFormattedTextFieldUtilizador, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(jLabel5))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(32, 32, 32)
                            .addComponent(jPasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jButtonEntrar)
                            .addGap(92, 92, 92))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabelRegisto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(77, 77, 77)))
        )
        );
        
        // Definir tamanho da janela com pack(), como no dist
        pack();
        setLocationRelativeTo(null);
    }
    
    private void closeDialog(WindowEvent evt) {
        setVisible(false);
        new Folgas(jLabelNome, jLabelFuncionario).setVisible(false);
        new Registo().setVisible(false);
    }
    
    private void jLabelRegistoMouseClicked(MouseEvent evt) {
        setVisible(false);
        Registo registo = new Registo();
        registo.setVisible(true);
        new Folgas(jLabelNome, jLabelFuncionario).setVisible(false);
    }
    
    private void jButtonEntrarActionPerformed(ActionEvent evt) {
        if (autenticarUsuario()) {
            setVisible(false);
            Folgas folgas = new Folgas(jLabelNome, jLabelFuncionario);
            autenticarUsuario1(folgas);
            folgas.setVisible(true);
            new Registo().setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "Credenciais inválidas!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void jPasswordFieldActionPerformed(ActionEvent evt) {
    }
    
    public boolean autenticarUsuario() {
        try {
            String utilizador = jFormattedTextFieldUtilizador.getText().trim();
            String password = String.valueOf(jPasswordField.getPassword());
            
            String sql = "SELECT * FROM Funcionário WHERE Email = ? AND Password = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, utilizador);
            pstmt.setString(2, password);
            
            resultSet = pstmt.executeQuery();
            boolean autenticado = resultSet.next();
            
            resultSet.close();
            pstmt.close();
            
            return autenticado;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }
    
    public String autenticarUsuario1(Folgas folgas) {
        try {
            String utilizador = jFormattedTextFieldUtilizador.getText().trim();
            String password = String.valueOf(jPasswordField.getPassword());
            
            String sql = "SELECT Nome FROM Funcionário WHERE Email = ? AND Password = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, utilizador);
            pstmt.setString(2, password);
            
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                String nome = resultSet.getString("Nome");
                folgas.aplicarPrivilegios(nome);
                folgas.atualizarLabels(nome);
                int funcionarioId = folgas.obterFuncionarioId(nome);
                
                if (funcionarioId != -1) {
                    System.out.println("Funcionário ID: " + funcionarioId);
                    System.out.println("Nome: " + nome);
                    System.out.println("Label Nome: " + folgas.getJLabelNome().getText());
                } else {
                    System.out.println("Funcionário Id não encontrado");
                }
            }
            
            resultSet.close();
            pstmt.close();
            
            return null;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }
    
    public JFormattedTextField getJFormattedTextFieldUtilizador() {
        return jFormattedTextFieldUtilizador;
    }
    
    public JPasswordField getJPasswordField() {
        return jPasswordField;
    }
    
    public static void main(String[] args) {
        // Aplicar Look and Feel Nimbus
        try {
            UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
            for (UIManager.LookAndFeelInfo look : looks) {
                if ("Nimbus".equals(look.getName())) {
                    UIManager.setLookAndFeel(look.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
                 UnsupportedLookAndFeelException e) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        
        // Criar janela
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login(new Frame(), true).setVisible(true);
            }
        });
    }
}
