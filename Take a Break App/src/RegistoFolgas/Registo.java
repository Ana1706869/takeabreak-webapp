/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.toedter.calendar.JDateChooser
 */
package RegistoFolgas;

import RegistoFolgas.Login;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Registo
extends JFrame {
    private static Connection con;
    private static final String driver = "org.sqlite.JDBC";
    private static final String url = "jdbc:sqlite:data/AgendamentoFolgas.db";
    private static PreparedStatement statement;
    private JButton jButtonRegistar;
    private JComboBox<String> jComboBoxDepartamento;
    private JComboBox<String> jComboBoxEscalao;
    private JDateChooser jDateChooserDataAdmissao;
    private JDateChooser jDateChooserDataNascimento;
    private JFormattedTextField jFormattedTextFieldCodigoPostal;
    private JFormattedTextField jFormattedTextFieldEmail;
    private JFormattedTextField jFormattedTextFieldTelefone;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;
    private JLabel jLabel18;
    private JLabel jLabel19;
    private JLabel jLabel2;
    private JLabel jLabel20;
    private JLabel jLabel21;
    private JLabel jLabel22;
    private JLabel jLabel23;
    private JLabel jLabel25;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPasswordField jPasswordField1;
    private JPasswordField jPasswordField2;
    private JTextField jTextFieldConcelho;
    private JTextField jTextFieldDistrito;
    private JTextField jTextFieldEndereco;
    private JTextField jTextFieldLocalidade;
    private JTextField jTextFieldNome;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void connector() {
        con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url);
            if (con == null) return;
        }
        catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro", 0);
        }
    }

    public Registo() {
        this.initComponents();
        JDateChooser jDateChooserdataNascimento = new JDateChooser();
        JDateChooser jDateChooserDataAdmissao = new JDateChooser();
        this.connector();
    }

    private void initComponents() {
        this.jPanel1 = new JPanel();
        this.jLabel1 = new JLabel();
        this.jLabel2 = new JLabel();
        this.jLabel3 = new JLabel();
        this.jComboBoxDepartamento = new JComboBox();
        this.jLabel4 = new JLabel();
        this.jLabel5 = new JLabel();
        this.jTextFieldEndereco = new JTextField();
        this.jLabel6 = new JLabel();
        this.jLabel8 = new JLabel();
        this.jTextFieldLocalidade = new JTextField();
        this.jLabel9 = new JLabel();
        this.jTextFieldConcelho = new JTextField();
        this.jLabel10 = new JLabel();
        this.jTextFieldDistrito = new JTextField();
        this.jLabel11 = new JLabel();
        this.jLabel12 = new JLabel();
        this.jLabel13 = new JLabel();
        this.jLabel14 = new JLabel();
        this.jPasswordField1 = new JPasswordField();
        this.jLabel15 = new JLabel();
        this.jPasswordField2 = new JPasswordField();
        this.jButtonRegistar = new JButton();
        this.jLabel16 = new JLabel();
        this.jLabel17 = new JLabel();
        this.jLabel18 = new JLabel();
        this.jLabel19 = new JLabel();
        this.jLabel21 = new JLabel();
        this.jLabel22 = new JLabel();
        this.jLabel23 = new JLabel();
        this.jTextFieldNome = new JTextField();
        this.jFormattedTextFieldCodigoPostal = new JFormattedTextField();
        this.jFormattedTextFieldEmail = new JFormattedTextField();
        this.jFormattedTextFieldTelefone = new JFormattedTextField();
        this.jDateChooserDataNascimento = new JDateChooser();
        this.jDateChooserDataAdmissao = new JDateChooser();
        this.jLabel7 = new JLabel();
        this.jComboBoxEscalao = new JComboBox();
        this.jLabel25 = new JLabel();
        this.jLabel20 = new JLabel();
        this.setDefaultCloseOperation(3);
        this.jLabel1.setFont(new Font("Segoe UI", 1, 24));
        this.jLabel1.setText("Ficha de Registo");
        this.jLabel2.setText("Nome:");
        this.jLabel3.setText("Departamento:");
        this.jComboBoxDepartamento.setModel(new DefaultComboBoxModel<String>(new String[]{"Administra\u00e7\u00e3o de Sistemas", "Administra\u00e7\u00e3o de Infra-estrutura de Rede", "Manuten\u00e7\u00e3o de Equipamentos e Servi\u00e7os", "Suporte aos Utilizadores", "Desenvolvimento e Implementa\u00e7\u00e3o de Novos Projetos"}));
        this.jComboBoxDepartamento.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Registo.this.jComboBoxDepartamentoActionPerformed(evt);
            }
        });
        this.jLabel4.setText("Data de Nascimento:");
        this.jLabel5.setText("Endere\u00e7o:");
        this.jLabel6.setText("C\u00f3digo Postal");
        this.jLabel8.setText("Localidade");
        this.jTextFieldLocalidade.addKeyListener(new KeyAdapter(){

            @Override
            public void keyTyped(KeyEvent evt) {
                Registo.this.jTextFieldLocalidadeKeyTyped(evt);
            }
        });
        this.jLabel9.setText("Concelho");
        this.jTextFieldConcelho.addKeyListener(new KeyAdapter(){

            @Override
            public void keyTyped(KeyEvent evt) {
                Registo.this.jTextFieldConcelhoKeyTyped(evt);
            }
        });
        this.jLabel10.setText("Distrito");
        this.jTextFieldDistrito.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Registo.this.jTextFieldDistritoActionPerformed(evt);
            }
        });
        this.jTextFieldDistrito.addKeyListener(new KeyAdapter(){

            @Override
            public void keyTyped(KeyEvent evt) {
                Registo.this.jTextFieldDistritoKeyTyped(evt);
            }
        });
        this.jLabel11.setText("E-mail");
        this.jLabel12.setText("Telefone");
        this.jLabel13.setText("Data de Admiss\u00e3o");
        this.jLabel14.setText("Palavra-passe ");
        this.jPasswordField1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Registo.this.jPasswordField1ActionPerformed(evt);
            }
        });
        this.jLabel15.setText("Verifica\u00e7\u00e3o da palavra-passe");
        this.jPasswordField2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Registo.this.jPasswordField2ActionPerformed(evt);
            }
        });
        this.jButtonRegistar.setBackground(new Color(0, 0, 0));
        this.jButtonRegistar.setFont(new Font("Segoe UI", 1, 12));
        this.jButtonRegistar.setForeground(new Color(255, 255, 255));
        this.jButtonRegistar.setText("Registar");
        this.jButtonRegistar.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Registo.this.jButtonRegistarActionPerformed(evt);
            }
        });
        this.jLabel16.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel16.setForeground(new Color(255, 0, 0));
        this.jLabel16.setText("*");
        this.jLabel17.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel17.setForeground(new Color(255, 0, 0));
        this.jLabel17.setText("*");
        this.jLabel18.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel18.setForeground(new Color(255, 0, 0));
        this.jLabel18.setText("*");
        this.jLabel19.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel19.setForeground(new Color(255, 0, 0));
        this.jLabel19.setText("*");
        this.jLabel21.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel21.setForeground(new Color(255, 0, 0));
        this.jLabel21.setText("*");
        this.jLabel22.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel22.setForeground(new Color(255, 0, 0));
        this.jLabel22.setText("* Campos de preenchimento obrigat\u00f3rio ");
        this.jLabel23.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel23.setForeground(new Color(255, 0, 0));
        this.jLabel23.setText("*");
        this.jTextFieldNome.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Registo.this.jTextFieldNomeActionPerformed(evt);
            }
        });
        this.jTextFieldNome.addKeyListener(new KeyAdapter(){

            @Override
            public void keyTyped(KeyEvent evt) {
                Registo.this.jTextFieldNomeKeyTyped(evt);
            }
        });
        this.jFormattedTextFieldCodigoPostal.setColumns(8);
        this.jFormattedTextFieldCodigoPostal.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Registo.this.jFormattedTextFieldCodigoPostalActionPerformed(evt);
            }
        });
        this.jFormattedTextFieldCodigoPostal.addKeyListener(new KeyAdapter(){

            @Override
            public void keyTyped(KeyEvent evt) {
                Registo.this.jFormattedTextFieldCodigoPostalKeyTyped(evt);
            }
        });
        this.jFormattedTextFieldTelefone.addKeyListener(new KeyAdapter(){

            @Override
            public void keyTyped(KeyEvent evt) {
                Registo.this.jFormattedTextFieldTelefoneKeyTyped(evt);
            }
        });
        this.jLabel7.setText("Escal\u00e3o");
        this.jComboBoxEscalao.setModel(new DefaultComboBoxModel<String>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"}));
        this.jComboBoxEscalao.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Registo.this.jComboBoxEscalaoActionPerformed(evt);
            }
        });
        this.jLabel25.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel25.setForeground(new Color(255, 0, 0));
        this.jLabel25.setText("*");
        this.jLabel20.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel20.setForeground(new Color(255, 0, 0));
        this.jLabel20.setText("*");
        GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
        this.jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(219, 219, 219).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel9, -2, 109, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jTextFieldConcelho, -2, 122, -2).addGap(55, 55, 55).addComponent(this.jLabel10, -2, 112, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jTextFieldDistrito, -2, 211, -2)).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel11, -2, 43, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jLabel18, -2, 43, -2)).addComponent(this.jLabel14, -2, 111, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel23, -2, 43, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(6, 6, 6).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jPasswordField1, -2, 320, -2).addComponent(this.jFormattedTextFieldTelefone, -2, 126, -2))).addComponent(this.jFormattedTextFieldEmail, -2, 317, -2)))).addGap(15, 15, 15)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel12).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jLabel19, -2, 43, -2).addGap(523, 523, 523))).addGap(0, 0, Short.MAX_VALUE)).addGroup(jPanel1Layout.createSequentialGroup().addGap(3, 3, 3).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel2, -2, 37, -2).addGap(18, 18, 18).addComponent(this.jLabel16, -2, 43, -2).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jTextFieldNome, -2, 281, -2).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jComboBoxDepartamento, -2, -1, -2).addGap(26, 26, 26).addComponent(this.jLabel7).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jLabel25, -2, 31, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jComboBoxEscalao, -2, -1, -2)))).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel5, -2, 86, -2).addComponent(this.jLabel4, GroupLayout.Alignment.TRAILING, -2, 124, -2)).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(49, 49, 49).addComponent(this.jTextFieldEndereco, -2, 427, -2)).addGroup(jPanel1Layout.createSequentialGroup().addGap(18, 18, 18).addComponent((Component)this.jDateChooserDataNascimento, -2, 173, -2).addGap(18, 18, 18).addComponent(this.jLabel13, -2, 111, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel20, -2, 43, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent((Component)this.jDateChooserDataAdmissao, -2, 187, -2)))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel6, -2, 109, -2).addGap(55, 55, 55).addComponent(this.jFormattedTextFieldCodigoPostal, -2, 126, -2).addGap(79, 79, 79).addComponent(this.jLabel8, -2, 103, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jTextFieldLocalidade, -2, 199, -2))).addContainerGap(440, Short.MAX_VALUE)))).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(352, 352, 352).addComponent(this.jLabel1, -2, 207, -2)).addGroup(jPanel1Layout.createSequentialGroup().addGap(227, 227, 227).addComponent(this.jLabel3, -2, 93, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel17, -2, 43, -2)).addGroup(jPanel1Layout.createSequentialGroup().addGap(181, 181, 181).addComponent(this.jLabel15, -2, 170, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel21, -2, 23, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jPasswordField2, -2, 312, -2))).addGap(0, 0, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGap(170, 170, 170).addComponent(this.jLabel22, -2, 300, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, Short.MAX_VALUE).addComponent(this.jButtonRegistar).addGap(561, 561, 561)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel1).addGap(16, 16, 16).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel2).addComponent(this.jLabel16).addComponent(this.jTextFieldNome, -2, -1, -2)).addGap(21, 21, 21).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel3).addComponent(this.jLabel17).addComponent(this.jComboBoxDepartamento, -2, -1, -2).addComponent(this.jLabel7).addComponent(this.jLabel25).addComponent(this.jComboBoxEscalao, -2, -1, -2)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel4, -2, 24, -2).addComponent((Component)this.jDateChooserDataNascimento, -2, -1, -2).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel13).addComponent(this.jLabel20)))).addGroup(jPanel1Layout.createSequentialGroup().addComponent((Component)this.jDateChooserDataAdmissao, -2, -1, -2).addGap(8, 8, 8))).addGap(32, 32, 32).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel5).addComponent(this.jTextFieldEndereco, -2, -1, -2)).addGap(27, 27, 27).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel6).addComponent(this.jLabel8).addComponent(this.jTextFieldLocalidade, -2, -1, -2).addComponent(this.jFormattedTextFieldCodigoPostal, -2, -1, -2)).addGap(26, 26, 26).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jTextFieldConcelho, -2, -1, -2).addComponent(this.jLabel10).addComponent(this.jTextFieldDistrito, -2, -1, -2).addComponent(this.jLabel9)).addGap(40, 40, 40).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel11).addComponent(this.jLabel18)).addComponent(this.jFormattedTextFieldEmail, GroupLayout.Alignment.LEADING, -2, -1, -2)).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel12).addComponent(this.jLabel19).addComponent(this.jFormattedTextFieldTelefone, -2, -1, -2)).addGap(28, 28, 28).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel14).addComponent(this.jPasswordField1, -2, -1, -2).addComponent(this.jLabel23)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel15).addComponent(this.jLabel21).addComponent(this.jPasswordField2, -2, -1, -2)).addGap(12, 12, 12).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.jButtonRegistar).addComponent(this.jLabel22)).addContainerGap(1574, Short.MAX_VALUE)));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(31, 31, 31).addComponent(this.jPanel1, -1, -1, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jPanel1, -2, -1, -2).addContainerGap(-1, Short.MAX_VALUE)));
        this.pack();
    }

    private void jComboBoxDepartamentoActionPerformed(ActionEvent evt) {
    }

    private void jTextFieldDistritoActionPerformed(ActionEvent evt) {
    }

    private void jButtonRegistarActionPerformed(ActionEvent evt) {
        String nome = this.jTextFieldNome.getText().trim();
        String email = this.jFormattedTextFieldEmail.getText().trim();
        String telefone = this.jFormattedTextFieldTelefone.getText().trim();
        String password = this.jPasswordField1.getText().trim();
        String verificacaoPassword = this.jPasswordField2.getText().trim();
        Date dataAdmissao = this.jDateChooserDataAdmissao.getDate();
        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || dataAdmissao == null || password.isEmpty() || verificacaoPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigat\u00f3rios", "Erro", 0);
            return;
        }
        if (!Registo.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "E-mail inv\u00e1lido", "Erro", 0);
            return;
        }
        String codigoPostal = this.jFormattedTextFieldCodigoPostal.getText().trim();
        if (!Registo.validarCodigoPostal(codigoPostal) && !codigoPostal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "C\u00f3digo postal inv\u00e1lido", "Erro", 0);
            return;
        }
        if (telefone.length() < 9) {
            JOptionPane.showMessageDialog(this, "N\u00famero de telefone inv\u00e1lido! O n\u00famero de telefone tem de ter 9 d\u00edgitos", "Erro", 0);
            return;
        }
        if (!password.equals(verificacaoPassword)) {
            JOptionPane.showMessageDialog(this, "As palavras-passe n\u00e3o coincidem", "Erro", 0);
            return;
        }
        this.inserirFuncionario();
    }

    private void jTextFieldNomeActionPerformed(ActionEvent evt) {
    }

    private void jTextFieldNomeKeyTyped(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ') {
            evt.consume();
        }
    }

    private void jTextFieldLocalidadeKeyTyped(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ') {
            evt.consume();
        }
    }

    private void jTextFieldConcelhoKeyTyped(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ') {
            evt.consume();
        }
    }

    private void jTextFieldDistritoKeyTyped(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ') {
            evt.consume();
        }
    }

    private void jPasswordField2ActionPerformed(ActionEvent evt) {
    }

    private void jPasswordField1ActionPerformed(ActionEvent evt) {
    }

    private void jFormattedTextFieldCodigoPostalActionPerformed(ActionEvent evt) {
    }

    private void jFormattedTextFieldCodigoPostalKeyTyped(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != '-') {
            evt.consume();
        }
        if (this.jFormattedTextFieldCodigoPostal.getText().length() >= 8) {
            evt.consume();
        }
    }

    private void jFormattedTextFieldTelefoneKeyTyped(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
        if (this.jFormattedTextFieldTelefone.getText().length() >= 9) {
            evt.consume();
        }
    }

    private void jComboBoxEscalaoActionPerformed(ActionEvent evt) {
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validarCodigoPostal(String codigoPostal) {
        String regex = "^\\d{4}-\\d{3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(codigoPostal);
        return matcher.matches();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void inserirFuncionario() {
        String sql = "INSERT INTO Funcion\u00e1rio (Nome, Departamento, DataNascimento, Endere\u00e7o, C\u00f3digoPostal, Localidade, Concelho, Distrito, Email, Telefone, DataAdmiss\u00e3o, Password, Escalao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            this.connector();
            statement = con.prepareStatement(sql);
            statement.setString(1, this.jTextFieldNome.getText().trim());
            statement.setString(2, this.jComboBoxDepartamento.getSelectedItem().toString());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dataNascimentoSelecionada = this.jDateChooserDataNascimento.getDate();
            if (dataNascimentoSelecionada != null) {
                String dataNascimento = sdf.format(dataNascimentoSelecionada);
                statement.setString(3, dataNascimento);
            } else {
                statement.setNull(3, 91);
            }
            statement.setString(4, this.jTextFieldEndereco.getText().trim());
            statement.setString(5, this.jFormattedTextFieldCodigoPostal.getText().trim());
            statement.setString(6, this.jTextFieldLocalidade.getText().trim());
            statement.setString(7, this.jTextFieldConcelho.getText().trim());
            statement.setString(8, this.jTextFieldDistrito.getText().trim());
            statement.setString(9, this.jFormattedTextFieldEmail.getText().trim());
            statement.setString(10, this.jFormattedTextFieldTelefone.getText());
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dataAdmissaoSelecionada = this.jDateChooserDataAdmissao.getDate();
            if (dataAdmissaoSelecionada != null) {
                String dataAdmissao = sdf.format(dataAdmissaoSelecionada);
                statement.setString(11, dataAdmissao);
            } else {
                statement.setNull(11, 91);
            }
            statement.setString(12, this.jPasswordField1.getText().trim());
            statement.setString(13, this.jComboBoxEscalao.getSelectedItem().toString());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Parab\u00e9ns! Est\u00e1 registado na nossa aplica\u00e7\u00e3o.", "Sucesso", 1);
            this.setVisible(false);
            Login login = new Login((Frame)new JFrame(), true);
            login.setVisible(true);
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir dados na base de dados: " + e.getMessage(), "Erro", 0);
        }
        finally {
            try {
                statement.close();
                con.close();
            }
            catch (Exception exception) {}
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (!"Nimbus".equals(info.getName())) continue;
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Registo.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            Logger.getLogger(Registo.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(Registo.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Registo.class.getName()).log(Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                new Registo().setVisible(true);
            }
        });
    }

    static {
        statement = null;
    }
}
