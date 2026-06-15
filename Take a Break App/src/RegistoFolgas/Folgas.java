/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.toedter.calendar.JDateChooser
 */
package RegistoFolgas;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class Folgas
extends JFrame {
    private static Connection con;
    private static final String driver = "org.sqlite.JDBC";
    private static final String url = "jdbc:sqlite:data/AgendamentoFolgas.db";
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    private final int pageSize = 30;
    private int currentPage = 1;
    private int totalRows = 20;
    private String nomeUsuario = null;
    private static final double[] ESPECIALISTAS;
    private static final double[] TECNICOS;
    private static final String GESTOR = "Rui Vila\u00e7a";
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButtonAnterior;
    private JButton jButtonAnterior1;
    private JButton jButtonPesquisarDataFim;
    private JButton jButtonPesquisarDataInicio;
    private JButton jButtonPesquisarDepartamento;
    private JButton jButtonPesquisarFuncionario;
    private JButton jButtonSeguinte;
    private JButton jButtonSeguinte1;
    private JButton jButtonSubmeter;
    private JButton jButton\u00daltimo;
    private JButton jButton\u00daltimo1;
    private JComboBox<String> jComboBoxDepartamento;
    private JComboBox<String> jComboBoxMotivos;
    private JDateChooser jDateChooserDataFim;
    private JDateChooser jDateChooserDataFimPesquisar;
    private JDateChooser jDateChooserDataInicio;
    private JDateChooser jDateChooserDataInicioPesquisar;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel17;
    private JLabel jLabel18;
    private JLabel jLabel19;
    private JLabel jLabel2;
    private JLabel jLabel22;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JLabel jLabelFolgasAprovadas;
    private JLabel jLabelFolgasAprovadasGestorRH;
    private JLabel jLabelFuncionario;
    private JLabel jLabelFuncionario2;
    private JLabel jLabelLogout;
    private JLabel jLabelNome;
    private JLabel jLabelPedidoFolgas;
    private JPanel jPanel4;
    private JPanel jPanel8;
    private JPanel jPanelFolgasAprovadasFuncionario;
    private JPanel jPanelFolgasAprovadasGestorRH;
    private JPanel jPanelPaginacao;
    private JPanel jPanelPaginacao1;
    private JPanel jPanelPedidoFolgas;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JPanel jTabFolgasAprovadas;
    private JPanel jTabFolgasAprovadasGestorRH;
    private JPanel jTabLogout;
    private JPanel jTabPedidoFolgas;
    private JTable jTableFolgasAprovadasFuncionario;
    private JTable jTableFolgasAprovadasGestor;
    private JTextField jTextFieldFuncionario;

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

    public Folgas(JLabel jLabelNome, JLabel jLabelFuncionario) {
        this.jLabelNome = jLabelNome;
        this.jLabelFuncionario = jLabelFuncionario;
        this.initComponents();
        this.jPanelPedidoFolgas.setVisible(true);
        this.jPanelFolgasAprovadasGestorRH.setVisible(true);
        this.listarFolgasGestor(this.currentPage);
        this.listarFolgasFuncionarios(this.currentPage);
        this.connector();
    }

    public void aplicarPrivilegios(String nome) {
        if (this.isGestor(nome)) {
            this.jTabPedidoFolgas.setVisible(false);
            this.jTabFolgasAprovadas.setVisible(false);
            this.jTabFolgasAprovadasGestorRH.setVisible(false);
            this.jPanelFolgasAprovadasGestorRH.setVisible(true);
            this.jPanelPedidoFolgas.setVisible(false);
        } else {
            this.jTabFolgasAprovadasGestorRH.setVisible(false);
        }
    }

    private boolean isGestor(String nome) {
        return GESTOR.equals(nome);
    }

    private void initComponents() {
        this.jPanel4 = new JPanel();
        this.jPanel8 = new JPanel();
        this.jPanelPedidoFolgas = new JPanel();
        this.jLabel1 = new JLabel();
        this.jLabel5 = new JLabel();
        this.jLabel6 = new JLabel();
        this.jLabel7 = new JLabel();
        this.jComboBoxMotivos = new JComboBox();
        this.jLabel4 = new JLabel();
        this.jButtonSubmeter = new JButton();
        this.jLabel17 = new JLabel();
        this.jLabel18 = new JLabel();
        this.jLabel19 = new JLabel();
        this.jLabel22 = new JLabel();
        this.jDateChooserDataInicio = new JDateChooser();
        this.jDateChooserDataFim = new JDateChooser();
        this.jLabelFuncionario = new JLabel();
        this.jPanelFolgasAprovadasGestorRH = new JPanel();
        this.jLabel8 = new JLabel();
        this.jPanelPaginacao = new JPanel();
        this.jButtonAnterior = new JButton();
        this.jButton\u00daltimo = new JButton();
        this.jButton1 = new JButton();
        this.jButtonSeguinte = new JButton();
        this.jScrollPane2 = new JScrollPane();
        this.jTableFolgasAprovadasGestor = new JTable();
        this.jLabel3 = new JLabel();
        this.jTextFieldFuncionario = new JTextField();
        this.jButtonPesquisarDepartamento = new JButton();
        this.jLabel9 = new JLabel();
        this.jComboBoxDepartamento = new JComboBox();
        this.jButtonPesquisarFuncionario = new JButton();
        this.jPanelFolgasAprovadasFuncionario = new JPanel();
        this.jScrollPane3 = new JScrollPane();
        this.jTableFolgasAprovadasFuncionario = new JTable();
        this.jLabel10 = new JLabel();
        this.jLabel11 = new JLabel();
        this.jButtonPesquisarDataInicio = new JButton();
        this.jLabel12 = new JLabel();
        this.jButtonPesquisarDataFim = new JButton();
        this.jLabelFuncionario2 = new JLabel();
        this.jPanelPaginacao1 = new JPanel();
        this.jButtonAnterior1 = new JButton();
        this.jButton\u00daltimo1 = new JButton();
        this.jButton2 = new JButton();
        this.jButtonSeguinte1 = new JButton();
        this.jDateChooserDataInicioPesquisar = new JDateChooser();
        this.jDateChooserDataFimPesquisar = new JDateChooser();
        this.jTabPedidoFolgas = new JPanel();
        this.jLabelPedidoFolgas = new JLabel();
        this.jTabFolgasAprovadasGestorRH = new JPanel();
        this.jLabelFolgasAprovadasGestorRH = new JLabel();
        this.jLabel2 = new JLabel();
        this.jTabLogout = new JPanel();
        this.jLabelLogout = new JLabel();
        this.jTabFolgasAprovadas = new JPanel();
        this.jLabelFolgasAprovadas = new JLabel();
        this.jLabelNome = new JLabel();
        this.setDefaultCloseOperation(3);
        this.setTitle("Take a Break!");
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent evt) {
                Folgas.this.formMouseClicked(evt);
            }
        });
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowOpened(WindowEvent evt) {
                Folgas.this.formWindowOpened(evt);
            }
        });
        this.jPanel4.setBackground(new Color(102, 153, 255));
        this.jLabel1.setText("Funcion\u00e1rio:");
        this.jLabel5.setText("Data de In\u00edcio:");
        this.jLabel6.setText("Data de Fim:");
        this.jLabel7.setText("Motivo:");
        this.jComboBoxMotivos.setModel(new DefaultComboBoxModel<String>(new String[]{"Doen\u00e7a", "Doen\u00e7a com regime de prote\u00e7\u00e3o pela Seguran\u00e7a Social", "F\u00e9rias", "Falecimento de c\u00f4njuge", "Falecimento de av\u00f3s", "Falecimento de pais", "Falecimento de filhos", "Falecimento de irm\u00e3os", "Licen\u00e7a de casamento", "Licen\u00e7a de maternidade", "Licen\u00e7a de paternidade", "Assist\u00eancia \u00e0 fam\u00edlia", "Assist\u00eancia a filho", "Assist\u00eancia a filho deficiente ou com doen\u00e7a cr\u00f3nica", " ", " ", " "}));
        this.jComboBoxMotivos.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jComboBoxMotivosActionPerformed(evt);
            }
        });
        this.jLabel4.setFont(new Font("Segoe UI", 1, 24));
        this.jLabel4.setText("Pedido de Aus\u00eancias");
        this.jButtonSubmeter.setBackground(new Color(0, 0, 0));
        this.jButtonSubmeter.setFont(new Font("Segoe UI", 1, 12));
        this.jButtonSubmeter.setForeground(new Color(255, 255, 255));
        this.jButtonSubmeter.setText("Submeter");
        this.jButtonSubmeter.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButtonSubmeterActionPerformed(evt);
            }
        });
        this.jLabel17.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel17.setForeground(new Color(255, 0, 0));
        this.jLabel17.setText("*");
        this.jLabel18.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel18.setForeground(new Color(255, 0, 0));
        this.jLabel18.setText("*");
        this.jLabel19.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel19.setForeground(new Color(255, 0, 0));
        this.jLabel19.setText("*");
        this.jLabel22.setFont(new Font("Segoe UI", 1, 12));
        this.jLabel22.setForeground(new Color(255, 0, 0));
        this.jLabel22.setText("* Campos de preenchimento obrigat\u00f3rio ");
        GroupLayout jPanelPedidoFolgasLayout = new GroupLayout(this.jPanelPedidoFolgas);
        this.jPanelPedidoFolgas.setLayout(jPanelPedidoFolgasLayout);
        jPanelPedidoFolgasLayout.setHorizontalGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPedidoFolgasLayout.createSequentialGroup().addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPedidoFolgasLayout.createSequentialGroup().addGap(133, 133, 133).addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPedidoFolgasLayout.createSequentialGroup().addComponent(this.jLabel6, -2, 93, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel18, -2, 43, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent((Component)this.jDateChooserDataFim, -2, 135, -2)).addGroup(jPanelPedidoFolgasLayout.createSequentialGroup().addComponent(this.jLabel7, -2, 73, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel19, -2, 43, -2).addGap(5, 5, 5).addComponent(this.jComboBoxMotivos, -2, -1, -2).addGap(35, 35, 35).addComponent(this.jButtonSubmeter)).addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.jLabel4, -2, 241, -2).addGroup(GroupLayout.Alignment.LEADING, jPanelPedidoFolgasLayout.createSequentialGroup().addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPedidoFolgasLayout.createSequentialGroup().addComponent(this.jLabel5, -2, 83, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel17, -2, 43, -2)).addComponent(this.jLabel1, -2, 81, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent((Component)this.jDateChooserDataInicio, -2, 145, -2).addComponent(this.jLabelFuncionario, -2, 134, -2)))))).addGroup(jPanelPedidoFolgasLayout.createSequentialGroup().addGap(25, 25, 25).addComponent(this.jLabel22, -2, 300, -2))).addContainerGap(6876, Short.MAX_VALUE)));
        jPanelPedidoFolgasLayout.setVerticalGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPedidoFolgasLayout.createSequentialGroup().addGap(20, 20, 20).addComponent(this.jLabel4, -2, 51, -2).addGap(85, 85, 85).addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.jLabelFuncionario, -2, 24, -2)).addGap(29, 29, 29).addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel5).addComponent(this.jLabel17)).addComponent((Component)this.jDateChooserDataInicio, -2, -1, -2)).addGap(44, 44, 44).addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel6).addComponent(this.jLabel18, -2, 22, -2)).addComponent((Component)this.jDateChooserDataFim, -2, -1, -2)).addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPedidoFolgasLayout.createSequentialGroup().addGap(37, 37, 37).addGroup(jPanelPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel7).addComponent(this.jComboBoxMotivos, -2, -1, -2).addComponent(this.jLabel19))).addGroup(jPanelPedidoFolgasLayout.createSequentialGroup().addGap(53, 53, 53).addComponent(this.jButtonSubmeter))).addGap(89, 89, 89).addComponent(this.jLabel22).addContainerGap(1550, Short.MAX_VALUE)));
        this.jLabel8.setFont(new Font("Segoe UI", 1, 24));
        this.jLabel8.setText("Aus\u00eancias Aprovadas");
        this.jButtonAnterior.setBackground(new Color(0, 0, 0));
        this.jButtonAnterior.setFont(new Font("Segoe UI", 1, 12));
        this.jButtonAnterior.setForeground(new Color(255, 255, 255));
        this.jButtonAnterior.setText("Anterior");
        this.jButtonAnterior.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButtonAnteriorActionPerformed(evt);
            }
        });
        this.jButton\u00daltimo.setBackground(new Color(0, 0, 0));
        this.jButton\u00daltimo.setFont(new Font("Segoe UI", 1, 12));
        this.jButton\u00daltimo.setForeground(new Color(255, 255, 255));
        this.jButton\u00daltimo.setText("\u00daltimo");
        this.jButton\u00daltimo.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButton\u00daltimoActionPerformed(evt);
            }
        });
        this.jButton1.setBackground(new Color(0, 0, 0));
        this.jButton1.setFont(new Font("Segoe UI", 1, 12));
        this.jButton1.setForeground(new Color(255, 255, 255));
        this.jButton1.setText("1");
        this.jButton1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButton1ActionPerformed(evt);
            }
        });
        this.jButtonSeguinte.setBackground(new Color(0, 0, 0));
        this.jButtonSeguinte.setFont(new Font("Segoe UI", 1, 12));
        this.jButtonSeguinte.setForeground(new Color(255, 255, 255));
        this.jButtonSeguinte.setText("Seguinte");
        this.jButtonSeguinte.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButtonSeguinteActionPerformed(evt);
            }
        });
        GroupLayout jPanelPaginacaoLayout = new GroupLayout(this.jPanelPaginacao);
        this.jPanelPaginacao.setLayout(jPanelPaginacaoLayout);
        jPanelPaginacaoLayout.setHorizontalGroup(jPanelPaginacaoLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPaginacaoLayout.createSequentialGroup().addGap(173, 173, 173).addComponent(this.jButton1, -2, 37, -2).addGap(18, 18, 18).addComponent(this.jButtonAnterior).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, Short.MAX_VALUE).addComponent(this.jButtonSeguinte).addGap(18, 18, 18).addComponent(this.jButton\u00daltimo).addGap(228, 228, 228)));
        jPanelPaginacaoLayout.setVerticalGroup(jPanelPaginacaoLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPaginacaoLayout.createSequentialGroup().addGroup(jPanelPaginacaoLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPaginacaoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jButtonAnterior).addComponent(this.jButton\u00daltimo).addComponent(this.jButtonSeguinte)).addComponent(this.jButton1, -2, 34, -2)).addGap(0, 56, Short.MAX_VALUE)));
        this.jTableFolgasAprovadasGestor.setAutoCreateRowSorter(true);
        this.jTableFolgasAprovadasGestor.setBackground(new Color(153, 153, 153));
        this.jTableFolgasAprovadasGestor.setFont(new Font("Segoe UI", 1, 12));
        this.jTableFolgasAprovadasGestor.setForeground(new Color(255, 255, 255));
        this.jTableFolgasAprovadasGestor.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null, null, null, null}, {null, null, null, null, null, null, null}, {null, null, null, null, null, null, null}, {null, null, null, null, null, null, null}, {null, null, null, null, null, null, null}, {null, null, null, null, null, null, null}, {null, null, null, null, null, null, null}, {null, null, null, null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"}){
            Class[] types;
            {
                this.types = new Class[]{Integer.class, Object.class, String.class, Object.class, Object.class, Object.class, Double.class};
            }

            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
        });
        this.jTableFolgasAprovadasGestor.setCellSelectionEnabled(true);
        this.jTableFolgasAprovadasGestor.setShowHorizontalLines(true);
        this.jTableFolgasAprovadasGestor.setShowVerticalLines(true);
        this.jScrollPane2.setViewportView(this.jTableFolgasAprovadasGestor);
        this.jLabel3.setText("Funcion\u00e1rio");
        this.jTextFieldFuncionario.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jTextFieldFuncionarioActionPerformed(evt);
            }
        });
        this.jTextFieldFuncionario.addKeyListener(new KeyAdapter(){

            @Override
            public void keyTyped(KeyEvent evt) {
                Folgas.this.jTextFieldFuncionarioKeyTyped(evt);
            }
        });
        this.jButtonPesquisarDepartamento.setBackground(new Color(153, 153, 153));
        this.jButtonPesquisarDepartamento.setFont(new Font("Segoe UI", 1, 12));
        this.jButtonPesquisarDepartamento.setForeground(new Color(255, 255, 255));
        this.jButtonPesquisarDepartamento.setText("Pesquisar");
        this.jButtonPesquisarDepartamento.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButtonPesquisarDepartamentoActionPerformed(evt);
            }
        });
        this.jLabel9.setText("Departamento:");
        this.jComboBoxDepartamento.setModel(new DefaultComboBoxModel<String>(new String[]{"(Todos)", "Administra\u00e7\u00e3o de Sistemas", "Administra\u00e7\u00e3o de Infra-estrutura de Rede", "Manuten\u00e7\u00e3o de Equipamento e Servi\u00e7os", "Suporte aos Utilizadores", "Desenvolvimento e Implementa\u00e7\u00e3o de Novos Projetos"}));
        this.jComboBoxDepartamento.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jComboBoxDepartamentoActionPerformed(evt);
            }
        });
        this.jButtonPesquisarFuncionario.setBackground(new Color(153, 153, 153));
        this.jButtonPesquisarFuncionario.setFont(new Font("Segoe UI", 1, 12));
        this.jButtonPesquisarFuncionario.setForeground(new Color(255, 255, 255));
        this.jButtonPesquisarFuncionario.setText("Pesquisar");
        this.jButtonPesquisarFuncionario.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButtonPesquisarFuncionarioActionPerformed(evt);
            }
        });
        GroupLayout jPanelFolgasAprovadasGestorRHLayout = new GroupLayout(this.jPanelFolgasAprovadasGestorRH);
        this.jPanelFolgasAprovadasGestorRH.setLayout(jPanelFolgasAprovadasGestorRHLayout);
        jPanelFolgasAprovadasGestorRHLayout.setHorizontalGroup(jPanelFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelFolgasAprovadasGestorRHLayout.createSequentialGroup().addGroup(jPanelFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelFolgasAprovadasGestorRHLayout.createSequentialGroup().addGap(69, 69, 69).addGroup(jPanelFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanelFolgasAprovadasGestorRHLayout.createSequentialGroup().addGroup(jPanelFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.jLabel9, -1, 90, Short.MAX_VALUE).addComponent(this.jLabel3, -1, -1, Short.MAX_VALUE)).addGap(32, 32, 32).addGroup(jPanelFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jComboBoxDepartamento, -2, -1, -2).addComponent(this.jTextFieldFuncionario, -2, 202, -2))).addGroup(jPanelFolgasAprovadasGestorRHLayout.createSequentialGroup().addComponent(this.jLabel8, -2, 390, -2).addGap(120, 120, 120))).addGap(50, 50, 50).addGroup(jPanelFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jButtonPesquisarDepartamento).addComponent(this.jButtonPesquisarFuncionario))).addComponent(this.jPanelPaginacao, -2, -1, -2).addComponent(this.jScrollPane2, -2, 916, -2)).addContainerGap(2133, Short.MAX_VALUE)));
        jPanelFolgasAprovadasGestorRHLayout.setVerticalGroup(jPanelFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelFolgasAprovadasGestorRHLayout.createSequentialGroup().addContainerGap().addComponent(this.jLabel8, -2, 49, -2).addGap(34, 34, 34).addGroup(jPanelFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel3).addComponent(this.jTextFieldFuncionario, -2, -1, -2).addComponent(this.jButtonPesquisarFuncionario)).addGap(34, 34, 34).addGroup(jPanelFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel9).addGroup(jPanelFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jComboBoxDepartamento, -2, -1, -2).addComponent(this.jButtonPesquisarDepartamento))).addGap(32, 32, 32).addComponent(this.jScrollPane2, -2, 352, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jPanelPaginacao, -2, -1, -2).addContainerGap(2317, Short.MAX_VALUE)));
        this.jTableFolgasAprovadasFuncionario.setAutoCreateRowSorter(true);
        this.jTableFolgasAprovadasFuncionario.setBackground(new Color(153, 153, 153));
        this.jTableFolgasAprovadasFuncionario.setFont(new Font("Segoe UI", 1, 12));
        this.jTableFolgasAprovadasFuncionario.setForeground(new Color(255, 255, 255));
        this.jTableFolgasAprovadasFuncionario.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}}, new String[]{"Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8"}){
            Class[] types;
            {
                this.types = new Class[]{String.class, Object.class, Object.class, Object.class, String.class, Double.class, Object.class, Object.class};
            }

            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
        });
        this.jTableFolgasAprovadasFuncionario.setCellSelectionEnabled(true);
        this.jTableFolgasAprovadasFuncionario.setShowHorizontalLines(true);
        this.jTableFolgasAprovadasFuncionario.setShowVerticalLines(true);
        this.jScrollPane3.setViewportView(this.jTableFolgasAprovadasFuncionario);
        this.jLabel10.setFont(new Font("Segoe UI", 1, 24));
        this.jLabel10.setText("Aus\u00eancias Aprovadas ");
        this.jLabel11.setText("Data de In\u00edcio");
        this.jButtonPesquisarDataInicio.setBackground(new Color(153, 153, 153));
        this.jButtonPesquisarDataInicio.setFont(new Font("Segoe UI", 1, 12));
        this.jButtonPesquisarDataInicio.setForeground(new Color(255, 255, 255));
        this.jButtonPesquisarDataInicio.setText("Pesquisar");
        this.jButtonPesquisarDataInicio.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButtonPesquisarDataInicioActionPerformed(evt);
            }
        });
        this.jLabel12.setText("Data de Pedido");
        this.jButtonPesquisarDataFim.setBackground(new Color(153, 153, 153));
        this.jButtonPesquisarDataFim.setFont(new Font("Segoe UI", 1, 12));
        this.jButtonPesquisarDataFim.setForeground(new Color(255, 255, 255));
        this.jButtonPesquisarDataFim.setText("Pesquisar");
        this.jButtonPesquisarDataFim.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButtonPesquisarDataFimActionPerformed(evt);
            }
        });
        this.jLabelFuncionario2.setText("jLabel20");
        this.jButtonAnterior1.setBackground(new Color(0, 0, 0));
        this.jButtonAnterior1.setFont(new Font("Segoe UI", 1, 12));
        this.jButtonAnterior1.setForeground(new Color(255, 255, 255));
        this.jButtonAnterior1.setText("Anterior");
        this.jButtonAnterior1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButtonAnterior1ActionPerformed(evt);
            }
        });
        this.jButton\u00daltimo1.setBackground(new Color(0, 0, 0));
        this.jButton\u00daltimo1.setFont(new Font("Segoe UI", 1, 12));
        this.jButton\u00daltimo1.setForeground(new Color(255, 255, 255));
        this.jButton\u00daltimo1.setText("\u00daltimo");
        this.jButton\u00daltimo1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButton\u00daltimo1ActionPerformed(evt);
            }
        });
        this.jButton2.setBackground(new Color(0, 0, 0));
        this.jButton2.setFont(new Font("Segoe UI", 1, 12));
        this.jButton2.setForeground(new Color(255, 255, 255));
        this.jButton2.setText("1");
        this.jButton2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButton2ActionPerformed(evt);
            }
        });
        this.jButtonSeguinte1.setBackground(new Color(0, 0, 0));
        this.jButtonSeguinte1.setFont(new Font("Segoe UI", 1, 12));
        this.jButtonSeguinte1.setForeground(new Color(255, 255, 255));
        this.jButtonSeguinte1.setText("Seguinte");
        this.jButtonSeguinte1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                Folgas.this.jButtonSeguinte1ActionPerformed(evt);
            }
        });
        GroupLayout jPanelPaginacao1Layout = new GroupLayout(this.jPanelPaginacao1);
        this.jPanelPaginacao1.setLayout(jPanelPaginacao1Layout);
        jPanelPaginacao1Layout.setHorizontalGroup(jPanelPaginacao1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPaginacao1Layout.createSequentialGroup().addGap(173, 173, 173).addComponent(this.jButton2, -2, 37, -2).addGap(18, 18, 18).addComponent(this.jButtonAnterior1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, Short.MAX_VALUE).addComponent(this.jButtonSeguinte1).addGap(18, 18, 18).addComponent(this.jButton\u00daltimo1).addGap(228, 228, 228)));
        jPanelPaginacao1Layout.setVerticalGroup(jPanelPaginacao1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPaginacao1Layout.createSequentialGroup().addGroup(jPanelPaginacao1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelPaginacao1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jButtonAnterior1).addComponent(this.jButton\u00daltimo1).addComponent(this.jButtonSeguinte1)).addComponent(this.jButton2, -2, 34, -2)).addGap(0, 56, Short.MAX_VALUE)));
        GroupLayout jPanelFolgasAprovadasFuncionarioLayout = new GroupLayout(this.jPanelFolgasAprovadasFuncionario);
        this.jPanelFolgasAprovadasFuncionario.setLayout(jPanelFolgasAprovadasFuncionarioLayout);
        jPanelFolgasAprovadasFuncionarioLayout.setHorizontalGroup(jPanelFolgasAprovadasFuncionarioLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createSequentialGroup().addGroup(jPanelFolgasAprovadasFuncionarioLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createSequentialGroup().addGap(196, 196, 196).addComponent(this.jLabelFuncionario2)).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createSequentialGroup().addGap(48, 48, 48).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel11, -2, 109, -2).addComponent(this.jLabel12, -2, 83, -2)).addGap(28, 28, 28).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent((Component)this.jDateChooserDataInicioPesquisar, -1, 141, Short.MAX_VALUE).addComponent((Component)this.jDateChooserDataFimPesquisar, -1, -1, Short.MAX_VALUE)).addGap(18, 18, 18).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.jButtonPesquisarDataInicio).addComponent(this.jButtonPesquisarDataFim))).addComponent(this.jScrollPane3, -2, 927, -2).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createSequentialGroup().addGap(70, 70, 70).addComponent(this.jPanelPaginacao1, -2, -1, -2)).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createSequentialGroup().addGap(107, 107, 107).addComponent(this.jLabel10, -2, 410, -2))).addContainerGap(15291, Short.MAX_VALUE)));
        jPanelFolgasAprovadasFuncionarioLayout.setVerticalGroup(jPanelFolgasAprovadasFuncionarioLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createSequentialGroup().addGap(11, 11, 11).addComponent(this.jLabel10, -2, 49, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createSequentialGroup().addComponent(this.jLabelFuncionario2).addGap(18, 18, 18).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel11).addComponent(this.jButtonPesquisarDataInicio))).addComponent((Component)this.jDateChooserDataInicioPesquisar, -2, -1, -2)).addGap(18, 18, 18).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanelFolgasAprovadasFuncionarioLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel12).addComponent(this.jButtonPesquisarDataFim)).addComponent((Component)this.jDateChooserDataFimPesquisar, -2, -1, -2)).addGap(26, 26, 26).addComponent(this.jScrollPane3, -2, 352, -2).addGap(18, 18, 18).addComponent(this.jPanelPaginacao1, -2, -1, -2).addContainerGap(2850, Short.MAX_VALUE)));
        GroupLayout jPanel8Layout = new GroupLayout(this.jPanel8);
        this.jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addComponent(this.jPanelPedidoFolgas, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jPanelFolgasAprovadasGestorRH, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jPanelFolgasAprovadasFuncionario, -2, -1, -2).addGap(0, 5146, Short.MAX_VALUE)));
        jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jPanelFolgasAprovadasGestorRH, -2, -1, -2).addComponent(this.jPanelFolgasAprovadasFuncionario, -2, -1, -2).addGroup(jPanel8Layout.createSequentialGroup().addGap(38, 38, 38).addComponent(this.jPanelPedidoFolgas, -2, -1, -2))).addContainerGap(635, Short.MAX_VALUE)));
        this.jTabPedidoFolgas.setBackground(new Color(153, 153, 153));
        this.jTabPedidoFolgas.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent evt) {
                Folgas.this.jTabPedidoFolgasMouseClicked(evt);
            }
        });
        this.jLabelPedidoFolgas.setFont(new Font("Segoe UI", 1, 12));
        this.jLabelPedidoFolgas.setForeground(new Color(255, 255, 255));
        this.jLabelPedidoFolgas.setText("Pedido de Aus\u00eancias");
        GroupLayout jTabPedidoFolgasLayout = new GroupLayout(this.jTabPedidoFolgas);
        this.jTabPedidoFolgas.setLayout(jTabPedidoFolgasLayout);
        jTabPedidoFolgasLayout.setHorizontalGroup(jTabPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jTabPedidoFolgasLayout.createSequentialGroup().addGap(28, 28, 28).addComponent(this.jLabelPedidoFolgas, -1, 202, Short.MAX_VALUE).addContainerGap()));
        jTabPedidoFolgasLayout.setVerticalGroup(jTabPedidoFolgasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, jTabPedidoFolgasLayout.createSequentialGroup().addContainerGap(10, Short.MAX_VALUE).addComponent(this.jLabelPedidoFolgas).addContainerGap()));
        this.jTabFolgasAprovadasGestorRH.setBackground(new Color(153, 153, 153));
        this.jTabFolgasAprovadasGestorRH.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent evt) {
                Folgas.this.jTabFolgasAprovadasGestorRHMouseClicked(evt);
            }
        });
        this.jLabelFolgasAprovadasGestorRH.setFont(new Font("Segoe UI", 1, 12));
        this.jLabelFolgasAprovadasGestorRH.setForeground(new Color(255, 255, 255));
        this.jLabelFolgasAprovadasGestorRH.setText("Aus\u00eancias Aprovadas ");
        GroupLayout jTabFolgasAprovadasGestorRHLayout = new GroupLayout(this.jTabFolgasAprovadasGestorRH);
        this.jTabFolgasAprovadasGestorRH.setLayout(jTabFolgasAprovadasGestorRHLayout);
        jTabFolgasAprovadasGestorRHLayout.setHorizontalGroup(jTabFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jTabFolgasAprovadasGestorRHLayout.createSequentialGroup().addComponent(this.jLabelFolgasAprovadasGestorRH, -2, 214, -2).addContainerGap(31, Short.MAX_VALUE)));
        jTabFolgasAprovadasGestorRHLayout.setVerticalGroup(jTabFolgasAprovadasGestorRHLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jTabFolgasAprovadasGestorRHLayout.createSequentialGroup().addContainerGap(-1, Short.MAX_VALUE).addComponent(this.jLabelFolgasAprovadasGestorRH, -2, 25, -2)));
        this.jLabel2.setFont(new Font("Segoe UI", 1, 24));
        this.jLabel2.setForeground(new Color(255, 204, 204));
        this.jLabel2.setText("Take a Break!");
        this.jTabLogout.setBackground(new Color(153, 153, 153));
        this.jTabLogout.setForeground(new Color(255, 255, 255));
        this.jTabLogout.setFont(new Font("Segoe UI", 1, 12));
        this.jTabLogout.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent evt) {
                Folgas.this.jTabLogoutMouseClicked(evt);
            }
        });
        this.jLabelLogout.setFont(new Font("Segoe UI", 1, 12));
        this.jLabelLogout.setForeground(new Color(255, 255, 255));
        this.jLabelLogout.setText("Logout");
        GroupLayout jTabLogoutLayout = new GroupLayout(this.jTabLogout);
        this.jTabLogout.setLayout(jTabLogoutLayout);
        jTabLogoutLayout.setHorizontalGroup(jTabLogoutLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jTabLogoutLayout.createSequentialGroup().addGap(96, 96, 96).addComponent(this.jLabelLogout, -1, -1, Short.MAX_VALUE).addGap(90, 90, 90)));
        jTabLogoutLayout.setVerticalGroup(jTabLogoutLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jTabLogoutLayout.createSequentialGroup().addContainerGap().addComponent(this.jLabelLogout).addContainerGap(-1, Short.MAX_VALUE)));
        this.jTabFolgasAprovadas.setBackground(new Color(153, 153, 153));
        this.jTabFolgasAprovadas.setForeground(new Color(255, 255, 255));
        this.jTabFolgasAprovadas.setFont(new Font("Segoe UI", 1, 12));
        this.jTabFolgasAprovadas.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent evt) {
                Folgas.this.jTabFolgasAprovadasMouseClicked(evt);
            }
        });
        this.jLabelFolgasAprovadas.setFont(new Font("Segoe UI", 1, 12));
        this.jLabelFolgasAprovadas.setForeground(new Color(255, 255, 255));
        this.jLabelFolgasAprovadas.setText("Aus\u00eancias Aprovadas ");
        this.jLabelFolgasAprovadas.setToolTipText("");
        GroupLayout jTabFolgasAprovadasLayout = new GroupLayout(this.jTabFolgasAprovadas);
        this.jTabFolgasAprovadas.setLayout(jTabFolgasAprovadasLayout);
        jTabFolgasAprovadasLayout.setHorizontalGroup(jTabFolgasAprovadasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jTabFolgasAprovadasLayout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.jLabelFolgasAprovadas, -1, -1, Short.MAX_VALUE).addGap(14, 14, 14)));
        jTabFolgasAprovadasLayout.setVerticalGroup(jTabFolgasAprovadasLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, jTabFolgasAprovadasLayout.createSequentialGroup().addContainerGap(-1, Short.MAX_VALUE).addComponent(this.jLabelFolgasAprovadas).addContainerGap()));
        this.jLabelNome.setFont(new Font("Segoe UI", 1, 24));
        GroupLayout jPanel4Layout = new GroupLayout(this.jPanel4);
        this.jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGap(23, 23, 23).addComponent(this.jLabel2, -2, 174, -2)).addComponent(this.jLabelNome, -2, 273, -2).addComponent(this.jTabPedidoFolgas, -2, -1, -2).addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.jTabLogout, GroupLayout.Alignment.LEADING, -1, -1, Short.MAX_VALUE).addComponent(this.jTabFolgasAprovadas, GroupLayout.Alignment.LEADING, -1, -1, Short.MAX_VALUE).addComponent(this.jTabFolgasAprovadasGestorRH, GroupLayout.Alignment.LEADING, -1, -1, Short.MAX_VALUE))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE).addComponent(this.jPanel8, -2, -1, -2).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGap(78, 78, 78).addComponent(this.jLabel2).addGap(18, 18, 18).addComponent(this.jLabelNome, -2, 44, -2).addGap(28, 28, 28).addComponent(this.jTabPedidoFolgas, -2, -1, -2).addGap(38, 38, 38).addComponent(this.jTabFolgasAprovadasGestorRH, -2, -1, -2).addGap(60, 60, 60).addComponent(this.jTabFolgasAprovadas, -2, -1, -2).addGap(64, 64, 64).addComponent(this.jTabLogout, -2, -1, -2)).addComponent(this.jPanel8, -2, -1, -2)).addContainerGap(-1, Short.MAX_VALUE)));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jPanel4, -2, -1, -2).addGap(0, 0, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jPanel4, GroupLayout.Alignment.TRAILING, -1, -1, Short.MAX_VALUE));
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void formMouseClicked(MouseEvent evt) {
    }

    private void jTabPedidoFolgasMouseClicked(MouseEvent evt) {
        this.jPanelPedidoFolgas.setVisible(true);
        this.jPanelFolgasAprovadasGestorRH.setVisible(false);
        this.jPanelFolgasAprovadasFuncionario.setVisible(false);
    }

    private void jTabFolgasAprovadasGestorRHMouseClicked(MouseEvent evt) {
        this.jPanelPedidoFolgas.setVisible(false);
        this.jPanelFolgasAprovadasGestorRH.setVisible(true);
        this.jPanelFolgasAprovadasFuncionario.setVisible(false);
    }

    private void jComboBoxMotivosActionPerformed(ActionEvent evt) {
    }

    private void jComboBoxDepartamentoActionPerformed(ActionEvent evt) {
    }

    private void jButtonSubmeterActionPerformed(ActionEvent evt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dataInicio = this.jDateChooserDataInicio.getDate();
        String dataInicioString = sdf.format(dataInicio);
        java.util.Date dataFim = this.jDateChooserDataFim.getDate();
        String dataFimString = sdf.format(dataFim);
        System.out.println("Data de fim" + dataFimString);
        int row = this.jTableFolgasAprovadasFuncionario.getSelectedRow();
        int folgaId = -1;
        if (row != -1) {
            String nome = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 0);
            String dataPedido = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 1);
            String estado = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 2);
            dataInicioString = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 3);
            dataFimString = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 4);
            String motivo = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 5);
            Double remuneracao = (Double)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 6);
            System.out.println("Nome" + nome);
            System.out.println("Data do Pedido" + dataPedido);
            System.out.println("Estado" + estado);
            System.out.println("DataInicio" + dataInicioString);
            System.out.println("DataFim" + dataFimString);
            System.out.println("Motivo" + motivo);
            System.out.println("Remunera\u00e7\u00e3o" + remuneracao);
            folgaId = this.obterFolgaId(nome, dataPedido, estado, dataInicioString, dataFimString, motivo, remuneracao);
        }
        System.out.println("FolgaId" + folgaId);
        java.util.Date dataAtual = new java.util.Date();
        System.out.println("Data inicio: " + dataInicio.toString());
        System.out.println("Data fim: " + dataFim.toString());
        System.out.println("Data atual: " + dataAtual.toString());
        if (dataInicio == null || dataFim == null) {
            JOptionPane.showMessageDialog(this, "Por favor, insira as datas de In\u00edcio e Fim", "Erro", 0);
        } else if (dataFim.compareTo(dataInicio) < 0) {
            JOptionPane.showMessageDialog(this, "A Data de Fim deve ser maior ou igual \u00e0 Data de In\u00edcio", "Erro", 0);
        } else if (dataInicio.compareTo(dataAtual) < 0) {
            JOptionPane.showMessageDialog(this, "A data de in\u00edcio introduzida tem de ser superior ou igual \u00e0 data atual", "Erro", 0);
        } else if (dataFim.compareTo(dataAtual) < 0) {
            JOptionPane.showMessageDialog(this, "A data de fim introduzida tem de ser superior ou igual \u00e0 data atual", "Erro", 0);
        } else if (folgaId == -1) {
            this.InserirDadosBaseDados();
        } else {
            this.atualizarFolga(folgaId);
        }
        this.limparCampos();
    }

    private void jTabFolgasAprovadasMouseClicked(MouseEvent evt) {
        this.jPanelPedidoFolgas.setVisible(false);
        this.jPanelFolgasAprovadasGestorRH.setVisible(false);
        this.jPanelFolgasAprovadasFuncionario.setVisible(true);
        this.listarFolgasFuncionarios(this.currentPage);
    }

    private void jButtonPesquisarDataInicioActionPerformed(ActionEvent evt) {
        java.util.Date dataInicio = this.jDateChooserDataInicioPesquisar.getDate();
        if (dataInicio != null) {
            int funcionarioId = this.obterFuncionarioId(this.jLabelNome.getText().trim());
            System.out.println("Funcion\u00e1rioId" + funcionarioId);
            System.out.println("Texto da jLabelNome" + this.jLabelNome.getText());
            this.listarFolgasFuncionarioDataInicio(this.currentPage, dataInicio, funcionarioId);
        } else {
            this.listarFolgasFuncionarios(this.currentPage);
        }
    }

    private void jButtonPesquisarDataFimActionPerformed(ActionEvent evt) {
        java.util.Date dataPedido = this.jDateChooserDataFimPesquisar.getDate();
        if (dataPedido != null) {
            int funcionarioId = this.obterFuncionarioId(this.jLabelNome.getText().trim());
            System.out.println("Funcion\u00e1rioId" + funcionarioId);
            System.out.println("Texto da jLabelNome" + this.jLabelNome.getText());
            this.listarFolgasFuncionarioDataPedido(this.currentPage, dataPedido, funcionarioId);
        } else {
            this.listarFolgasFuncionarios(this.currentPage);
        }
    }

    private void jButtonPesquisarDepartamentoActionPerformed(ActionEvent evt) {
        String departamento = this.jComboBoxDepartamento.getSelectedItem().toString();
        if (departamento.equals("(Todos)")) {
            this.listarFolgasGestor(this.currentPage);
        } else {
            this.listarFolgasGestorDepartamento(departamento, this.currentPage);
        }
    }

    private void jButtonPesquisarFuncionarioActionPerformed(ActionEvent evt) {
        String funcionario = this.jTextFieldFuncionario.getText().trim();
        if (funcionario.isEmpty()) {
            this.listarFolgasGestor(this.currentPage);
        } else {
            this.listarFolgasGestorFuncionarios(funcionario, this.currentPage);
        }
    }

    private void formWindowOpened(WindowEvent evt) {
    }

    private void jTextFieldFuncionarioActionPerformed(ActionEvent evt) {
    }

    private void jTextFieldFuncionarioKeyTyped(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && c != ' ') {
            evt.consume();
        }
    }

    private void jButtonAnteriorActionPerformed(ActionEvent evt) {
        if (this.currentPage > 1) {
            --this.currentPage;
            String departamento = this.jComboBoxDepartamento.getSelectedItem().toString();
            if (departamento.equals("(Todos)")) {
                this.listarFolgasGestor(this.currentPage);
            } else {
                this.listarFolgasGestorDepartamento(departamento, this.currentPage);
            }
            String funcionario = this.jTextFieldFuncionario.getText().trim();
            if (funcionario.isEmpty()) {
                this.listarFolgasGestor(this.currentPage);
            } else {
                this.listarFolgasGestorFuncionarios(funcionario, this.currentPage);
            }
        }
    }

    private void jButton\u00daltimoActionPerformed(ActionEvent evt) {
        int totalPages = (int)Math.ceil((double)this.totalRows / 30.0);
        this.listarFolgasGestor(totalPages);
        String departamento = this.jComboBoxDepartamento.getSelectedItem().toString();
        if (departamento.equals("(Nenhum)")) {
            this.listarFolgasGestor(totalPages);
        } else {
            this.listarFolgasGestorDepartamento(departamento, totalPages);
        }
        String funcionario = this.jTextFieldFuncionario.getText().trim();
        if (funcionario.isEmpty()) {
            this.listarFolgasGestor(totalPages);
        } else {
            this.listarFolgasGestorFuncionarios(funcionario, totalPages);
        }
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        String departamento = this.jComboBoxDepartamento.getSelectedItem().toString();
        if (departamento.equals("(Nenhum)")) {
            this.listarFolgasGestor(1);
        } else {
            this.listarFolgasGestorDepartamento(departamento, 1);
        }
        String funcionario = this.jTextFieldFuncionario.getText().trim();
        if (funcionario.isEmpty()) {
            this.listarFolgasGestor(1);
        } else {
            this.listarFolgasGestorFuncionarios(funcionario, 1);
        }
    }

    private void jButtonSeguinteActionPerformed(ActionEvent evt) {
        int totalPages = (int)Math.ceil((double)this.totalRows / 30.0);
        if (this.currentPage < totalPages) {
            ++this.currentPage;
            String departamento = this.jComboBoxDepartamento.getSelectedItem().toString();
            if (departamento.equals("(Nenhum)")) {
                this.listarFolgasGestor(this.currentPage);
            } else {
                this.listarFolgasGestorDepartamento(departamento, this.currentPage);
            }
            String funcionario = this.jTextFieldFuncionario.getText().trim();
            if (funcionario.isEmpty()) {
                this.listarFolgasGestor(this.currentPage);
            } else {
                this.listarFolgasGestorFuncionarios(funcionario, this.currentPage);
            }
        }
    }

    private void jButtonAnterior1ActionPerformed(ActionEvent evt) {
        if (this.currentPage > 1) {
            --this.currentPage;
            java.util.Date dataInicio = this.jDateChooserDataInicioPesquisar.getDate();
            java.util.Date dataPedido = this.jDateChooserDataFimPesquisar.getDate();
            int funcionarioId = this.obterFuncionarioId(this.jLabelNome.getText().trim());
            if (dataInicio == null) {
                this.listarFolgasFuncionarios(this.currentPage);
            } else {
                this.listarFolgasFuncionarioDataInicio(this.currentPage, dataInicio, funcionarioId);
            }
            if (dataPedido == null) {
                this.listarFolgasFuncionarios(this.currentPage);
            } else {
                this.listarFolgasFuncionarioDataPedido(this.currentPage, dataPedido, funcionarioId);
            }
        }
    }

    private void jButton\u00daltimo1ActionPerformed(ActionEvent evt) {
        int totalPages = (int)Math.ceil((double)this.totalRows / 30.0);
        java.util.Date dataInicio = this.jDateChooserDataInicio.getDate();
        java.util.Date dataPedido = this.jDateChooserDataFim.getDate();
        int funcionarioId = this.obterFuncionarioId(this.jLabelNome.getText().trim());
        if (dataInicio == null) {
            this.listarFolgasFuncionarios(totalPages);
        } else {
            this.listarFolgasFuncionarioDataInicio(totalPages, dataInicio, funcionarioId);
        }
        if (dataPedido == null) {
            this.listarFolgasFuncionarios(totalPages);
        } else {
            this.listarFolgasFuncionarioDataPedido(totalPages, dataPedido, funcionarioId);
        }
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        java.util.Date dataInicio = this.jDateChooserDataInicio.getDate();
        java.util.Date dataPedido = this.jDateChooserDataFim.getDate();
        int funcionarioId = this.obterFuncionarioId(this.jLabelNome.getText().trim());
        if (dataInicio == null) {
            this.listarFolgasFuncionarios(1);
        } else {
            this.listarFolgasFuncionarioDataInicio(1, dataInicio, funcionarioId);
        }
        if (dataPedido == null) {
            this.listarFolgasFuncionarios(1);
        } else {
            this.listarFolgasFuncionarioDataPedido(1, dataPedido, funcionarioId);
        }
    }

    private void jButtonSeguinte1ActionPerformed(ActionEvent evt) {
        int totalPages = (int)Math.ceil((double)this.totalRows / 30.0);
        java.util.Date dataInicio = this.jDateChooserDataInicio.getDate();
        java.util.Date dataPedido = this.jDateChooserDataFim.getDate();
        int funcionarioId = this.obterFuncionarioId(this.jLabelNome.getText().trim());
        if (this.currentPage < totalPages) {
            ++this.currentPage;
            if (dataInicio == null) {
                this.listarFolgasFuncionarios(this.currentPage);
            } else {
                this.listarFolgasFuncionarioDataInicio(this.currentPage, dataInicio, funcionarioId);
            }
            if (dataPedido == null) {
                this.listarFolgasFuncionarios(this.currentPage);
            } else {
                this.listarFolgasFuncionarioDataPedido(this.currentPage, dataPedido, funcionarioId);
            }
        }
    }

    private void jTabLogoutMouseClicked(MouseEvent evt) {
        this.dispose();
        Login login = new Login((Frame)new JFrame(), true);
        login.setVisible(true);
    }

    private void listarFolgasGestor(int page) {
        String departamento = this.jComboBoxDepartamento.getSelectedItem().toString();
        if (!departamento.equals("(Todos)")) {
            return;
        }
        Object[] colunas = new String[]{"Nome", "Data de Pedido", "Estado", "Data de In\u00edcio", "Data de Fim", "Motivo", "Remunera\u00e7\u00e3o (euros)"};
        try {
            this.connector();
            String sql = "SELECT COUNT(*) FROM Folgas WHERE Estado = 'Aprovada'";
            PreparedStatement countStatement = con.prepareStatement(sql);
            ResultSet resultSetCount = countStatement.executeQuery();
            if (resultSetCount.next()) {
                this.totalRows = resultSetCount.getInt(1);
            }
            int totalPages = (int)Math.ceil((double)this.totalRows / 30.0);
            int offset = (page - 1) * 30;
            sql = "SELECT Func.Nome, F.DataPedido, F.Estado, F.DataInicio, F.DataFim, F.Motivo, F.Remuneracao FROM Folgas F INNER JOIN Funcion\u00e1rio Func ON F.FuncionarioId = Func.FuncionarioId WHERE F.Estado = 'Aprovada' LIMIT ? OFFSET ?";
            PreparedStatement statementAprovada = con.prepareStatement(sql);
            statementAprovada.setInt(1, 30);
            statementAprovada.setInt(2, offset);
            ResultSet resultSetAprovada = statementAprovada.executeQuery();
            DefaultTableModel model = new DefaultTableModel(colunas, 0){

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    switch (columnIndex) {
                        case 0: 
                        case 1: 
                        case 2: 
                        case 3: 
                        case 4: 
                        case 5: {
                            return String.class;
                        }
                        case 6: {
                            return Double.class;
                        }
                    }
                    return Object.class;
                }
            };
            while (resultSetAprovada.next()) {
                String nomeFuncionario = resultSetAprovada.getString("Nome");
                String dataPedido = resultSetAprovada.getString("DataPedido");
                String estado = resultSetAprovada.getString("Estado");
                String dataInicio = resultSetAprovada.getString("DataInicio");
                String dataFim = resultSetAprovada.getString("DataFim");
                String motivo = resultSetAprovada.getString("Motivo");
                double remuneracao = resultSetAprovada.getDouble("Remuneracao");
                model.addRow(new Object[]{nomeFuncionario, dataPedido, estado, dataInicio, dataFim, motivo, remuneracao});
            }
            this.jTableFolgasAprovadasGestor.setModel(model);
            resultSetAprovada.close();
            statementAprovada.close();
            resultSetCount.close();
            countStatement.close();
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void listarFolgasGestorDepartamento(String departamento, int page) {
        try {
            this.connector();
            String sql = "SELECT COUNT(*)FROM Folgas F INNER JOIN Funcion\u00e1rio Func ON F.FuncionarioId=Func.FuncionarioId            WHERE F.Estado='Aprovada' AND Func.Departamento=?";
            PreparedStatement countStatement = con.prepareStatement(sql);
            countStatement.setString(1, departamento);
            ResultSet resultSetCount = countStatement.executeQuery();
            if (resultSetCount.next()) {
                this.totalRows = resultSetCount.getInt(1);
            }
            int totalPages = (int)Math.ceil((double)this.totalRows / 30.0);
            int offset = (page - 1) * 30;
            sql = "SELECT Func.Nome, F.DataPedido, F.Estado, F.DataInicio, F.DataFim, F.Motivo, F.Remuneracao                 FROM Folgas F INNER JOIN Funcion\u00e1rio Func ON F.FuncionarioId=Func.FuncionarioId                 WHERE F.Estado ='Aprovada' AND Func.Departamento=? LIMIT ? OFFSET ?";
            PreparedStatement statementAprovada = con.prepareStatement(sql);
            statementAprovada.setString(1, departamento);
            statementAprovada.setInt(2, 30);
            statementAprovada.setInt(3, offset);
            ResultSet resultSetAprovada = statementAprovada.executeQuery();
            DefaultTableModel model = (DefaultTableModel)this.jTableFolgasAprovadasGestor.getModel();
            model.setRowCount(0);
            while (resultSetAprovada.next()) {
                String nomeFuncionario = resultSetAprovada.getString("Nome");
                String dataPedido = resultSetAprovada.getString("DataPedido");
                String estado = resultSetAprovada.getString("Estado");
                String dataInicio = resultSetAprovada.getString("DataInicio");
                String dataFim = resultSetAprovada.getString("DataFim");
                String motivo = resultSetAprovada.getString("Motivo");
                double remuneracao = resultSetAprovada.getDouble("Remuneracao");
                model.addRow(new Object[]{nomeFuncionario, dataPedido, estado, dataInicio, dataFim, motivo, remuneracao});
            }
            resultSetAprovada.close();
            statementAprovada.close();
            con.close();
            model.fireTableDataChanged();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void listarFolgasGestorFuncionarios(String funcionario, int page) {
        try {
            this.connector();
            String sql = "SELECT COUNT(*)FROM Folgas F INNER JOIN Funcion\u00e1rio Func ON F.FuncionarioId=Func.FuncionarioId            WHERE F.Estado='Aprovada' AND Func.Nome=?";
            PreparedStatement countStatement = con.prepareStatement(sql);
            countStatement.setString(1, funcionario);
            ResultSet resultSetCount = countStatement.executeQuery();
            if (resultSetCount.next()) {
                this.totalRows = resultSetCount.getInt(1);
            }
            int totalPages = (int)Math.ceil((double)this.totalRows / 30.0);
            int offset = (page - 1) * 30;
            sql = "SELECT Func.Nome, F.DataPedido, F.Estado, F.DataInicio, F.DataFim, F.Motivo, F.Remuneracao                 FROM Folgas F INNER JOIN Funcion\u00e1rio Func ON F.FuncionarioId=Func.FuncionarioId                 WHERE F.Estado ='Aprovada' AND Func.Nome=? LIMIT ? OFFSET ?";
            PreparedStatement statementAprovada = con.prepareStatement(sql);
            statementAprovada.setString(1, funcionario);
            statementAprovada.setInt(2, 30);
            statementAprovada.setInt(3, offset);
            ResultSet resultSetAprovada = statementAprovada.executeQuery();
            DefaultTableModel model = (DefaultTableModel)this.jTableFolgasAprovadasGestor.getModel();
            model.setRowCount(0);
            while (resultSetAprovada.next()) {
                String nomeFuncionario = resultSetAprovada.getString("Nome");
                String dataPedido = resultSetAprovada.getString("DataPedido");
                String estado = resultSetAprovada.getString("Estado");
                String dataInicio = resultSetAprovada.getString("DataInicio");
                String dataFim = resultSetAprovada.getString("DataFim");
                String motivo = resultSetAprovada.getString("Motivo");
                double remuneracao = resultSetAprovada.getDouble("Remuneracao");
                model.addRow(new Object[]{nomeFuncionario, dataPedido, estado, dataInicio, dataFim, motivo, remuneracao});
            }
            resultSetAprovada.close();
            statementAprovada.close();
            model.fireTableDataChanged();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listarFolgasFuncionarios(int page) {
        Object[] colunas1 = new String[]{"Nome", "Data de Pedido", "Estado", "Data de In\u00edcio", "Data de Fim", "Motivo", "Remunera\u00e7\u00e3o (euros)", "Alterar", "Eliminar"};
        ArrayList linhas1 = new ArrayList();
        String nomeFuncionario = this.jLabelNome.getText().trim();
        int idFuncionario = this.obterFuncionarioId(nomeFuncionario);
        if (idFuncionario != -1) {
            try {
                this.connector();
                System.out.println("Nome:" + nomeFuncionario);
                System.out.println("Funcion\u00e1rioId encontrado:" + idFuncionario);
                System.out.println("Texto da jLabelNome:" + this.jLabelNome.getText());
                String sql = "SELECT COUNT(*) FROM Folgas WHERE Estado = 'Aprovada' AND FuncionarioId = ?";
                PreparedStatement countFuncionariosStatement = con.prepareStatement(sql);
                countFuncionariosStatement.setInt(1, idFuncionario);
                ResultSet resultSetCountFuncionarios = countFuncionariosStatement.executeQuery();
                int totalRows = 0;
                if (resultSetCountFuncionarios.next()) {
                    totalRows = resultSetCountFuncionarios.getInt(1);
                }
                resultSetCountFuncionarios.close();
                countFuncionariosStatement.close();
                int totalPages = (int)Math.ceil((double)totalRows / 30.0);
                int offset = (page - 1) * 30;
                sql = "SELECT Func.Nome, F.DataPedido, F.Estado, F.DataInicio, F.DataFim, F.Motivo, F.Remuneracao FROM Folgas F INNER JOIN Funcion\u00e1rio Func ON F.FuncionarioId = Func.FuncionarioId WHERE F.Estado = 'Aprovada' AND Func.FuncionarioId = ? LIMIT ? OFFSET ?";
                PreparedStatement statementFuncionario = con.prepareStatement(sql);
                statementFuncionario.setInt(1, idFuncionario);
                statementFuncionario.setInt(2, 30);
                statementFuncionario.setInt(3, offset);
                ResultSet resultSetFuncionario = statementFuncionario.executeQuery();
                DefaultTableModel model1 = new DefaultTableModel(colunas1, 0){

                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        switch (columnIndex) {
                            case 0: 
                            case 1: 
                            case 2: 
                            case 3: 
                            case 4: 
                            case 5: {
                                return String.class;
                            }
                            case 6: {
                                return Double.class;
                            }
                        }
                        return JButton.class;
                    }
                };
                while (resultSetFuncionario.next()) {
                    String nome = resultSetFuncionario.getString("Nome");
                    String dataPedido = resultSetFuncionario.getString("DataPedido");
                    String estado = resultSetFuncionario.getString("Estado");
                    String dataInicio = resultSetFuncionario.getString("DataInicio");
                    String dataFim = resultSetFuncionario.getString("DataFim");
                    String motivo = resultSetFuncionario.getString("Motivo");
                    double remuneracao = resultSetFuncionario.getDouble("Remuneracao");
                    model1.addRow(new Object[]{nome, dataPedido, estado, dataInicio, dataFim, motivo, remuneracao});
                }
                this.jTableFolgasAprovadasFuncionario.setModel(model1);
                this.jTableFolgasAprovadasFuncionario.getColumnModel().getColumn(8).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                    JButton button = new JButton("Eliminar");
                    button.setOpaque(true);
                    button.setBackground(Color.RED);
                    button.setForeground(Color.WHITE);
                    button.setFont(button.getFont().deriveFont(1));
                    return button;
                });
                this.jTableFolgasAprovadasFuncionario.getColumnModel().getColumn(7).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                    JButton button = new JButton("Alterar");
                    button.setOpaque(true);
                    button.setBackground(new Color(0, 128, 0));
                    button.setForeground(Color.WHITE);
                    button.setFont(button.getFont().deriveFont(1));
                    button.addActionListener(e -> {});
                    return button;
                });
                this.jTableFolgasAprovadasFuncionario.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(new JCheckBox()));
                this.jTableFolgasAprovadasFuncionario.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(new JCheckBox()));
                ((JCheckBox)this.jTableFolgasAprovadasFuncionario.getColumnModel().getColumn(7).getCellEditor().getTableCellEditorComponent(this.jTableFolgasAprovadasFuncionario, "Alterar", true, 0, 0)).addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int row = Folgas.this.jTableFolgasAprovadasFuncionario.getSelectedRow();
                        String nome = (String)Folgas.this.jTableFolgasAprovadasFuncionario.getValueAt(row, 0);
                        String dataPedido = (String)Folgas.this.jTableFolgasAprovadasFuncionario.getValueAt(row, 1);
                        String estado = (String)Folgas.this.jTableFolgasAprovadasFuncionario.getValueAt(row, 2);
                        String dataInicio = (String)Folgas.this.jTableFolgasAprovadasFuncionario.getValueAt(row, 3);
                        String dataFim = (String)Folgas.this.jTableFolgasAprovadasFuncionario.getValueAt(row, 4);
                        String motivo = (String)Folgas.this.jTableFolgasAprovadasFuncionario.getValueAt(row, 5);
                        Double remuneracao = (Double)Folgas.this.jTableFolgasAprovadasFuncionario.getValueAt(row, 6);
                        int folgaId = Folgas.this.obterFolgaId(nome, dataPedido, estado, dataInicio, dataFim, motivo, remuneracao);
                        Folgas.this.preencherCampos(folgaId);
                    }
                });
                ((JCheckBox)this.jTableFolgasAprovadasFuncionario.getColumnModel().getColumn(8).getCellEditor().getTableCellEditorComponent(this.jTableFolgasAprovadasFuncionario, "Eliminar", true, 0, 0)).addActionListener(e -> {
                    int row = this.jTableFolgasAprovadasFuncionario.getSelectedRow();
                    String nome = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 0);
                    String dataPedido = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 1);
                    String estado = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 2);
                    String dataInicio = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 3);
                    String dataFim = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 4);
                    String motivo = (String)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 5);
                    Double remuneracao = (Double)this.jTableFolgasAprovadasFuncionario.getValueAt(row, 6);
                    int folgaId = this.obterFolgaId(nome, dataPedido, estado, dataInicio, dataFim, motivo, remuneracao);
                    this.preencherCampos(folgaId);
                    UIManager.put("OptionPane.yesButtonText", "Sim");
                    UIManager.put("OptionPane.noButtonText", "N\u00e3o");
                    int confirmar = JOptionPane.showConfirmDialog(this, "Tem a certeza que quer eliminar o pedido de aus\u00eancia?", "Confirma\u00e7\u00e3o", 0);
                    if (confirmar == 0) {
                        this.eliminarFolga(folgaId);
                    }
                    this.limparCampos();
                });
                resultSetFuncionario.close();
                statementFuncionario.close();
            }
            catch (SQLException e2) {
                e2.printStackTrace();
            }
        } else {
            System.out.println("Funcion\u00e1rioId n\u00e3o encontrado");
        }
    }

    private int obterFolgaId(String nome, String dataPedido, String estado, String dataInicio, String dataFim, String motivo, Double remuneracao) {
        int folgaId = -1;
        try {
            this.connector();
            String sql = "SELECT F.FolgaId FROM Folgas F INNER JOIN Funcion\u00e1rio Func ON F.FuncionarioId=Func.FuncionarioId WHERE Func.Nome=? AND F.DataPedido=? AND F.Estado=? AND F.DataInicio=? AND F.DataFim=? AND F.Motivo=?AND Remuneracao=?";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            PreparedStatement folgaIdStatement = con.prepareStatement(sql);
            folgaIdStatement.setString(1, nome);
            folgaIdStatement.setString(2, dataPedido);
            folgaIdStatement.setString(3, estado);
            folgaIdStatement.setString(4, dataInicio);
            folgaIdStatement.setString(5, dataFim);
            folgaIdStatement.setString(6, motivo);
            folgaIdStatement.setDouble(7, remuneracao);
            ResultSet resultSetFolgaId = folgaIdStatement.executeQuery();
            if (resultSetFolgaId.next()) {
                folgaId = resultSetFolgaId.getInt("FolgaId");
                System.out.println("FolgaId encontrada" + folgaId);
                this.abrirInterfacePedidoFolga(nome, dataPedido, estado, dataInicio, dataFim, motivo, remuneracao);
            } else {
                System.out.println("FolgaId n\u00e3o encontrada");
            }
            resultSetFolgaId.close();
            folgaIdStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao encontrar folgaId");
        }
        return folgaId;
    }

    private void abrirInterfacePedidoFolga(String nome, String dataPedido, String estado, String dataInicio, String dataFim, String motivo, Double remuneracao) {
        this.jPanelPedidoFolgas.setVisible(true);
    }

    private void preencherCampos(int folgaId) {
        if (folgaId != -1) {
            try {
                this.connector();
                String sql = "SELECT DataInicio, DataFim, Motivo FROM Folgas WHERE FolgaId=?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setInt(1, folgaId);
                ResultSet rs = pstm.executeQuery();
                if (rs.next()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        this.jDateChooserDataInicio.setDate(sdf.parse(rs.getString("DataInicio")));
                    }
                    catch (ParseException ex) {
                        Logger.getLogger(Folgas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        this.jDateChooserDataFim.setDate(sdf.parse(rs.getString("DataFim")));
                    }
                    catch (ParseException ex) {
                        Logger.getLogger(Folgas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.jComboBoxMotivos.setSelectedItem(rs.getString("Motivo"));
                } else {
                    System.out.println("Folga n\u00e3o encontrada com os crit\u00e9rios fornecidos");
                }
                rs.close();
                pstm.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erro ao preencher campos: " + e.getMessage());
            }
        }
    }

    private void atualizarFolga(int folgaId) {
        if (folgaId != -1) {
            try {
                this.connector();
                String sql = "UPDATE Folgas SET DataPedido=?, DataInicio = ?, DataFim = ?, Motivo = ?,Remuneracao=? WHERE FolgaId = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                pstmt.setString(1, sdf2.format(new java.util.Date()));
                pstmt.setString(2, sdf.format(this.jDateChooserDataInicio.getDate()));
                pstmt.setString(3, sdf.format(this.jDateChooserDataFim.getDate()));
                pstmt.setString(4, (String)this.jComboBoxMotivos.getSelectedItem());
                pstmt.setDouble(5, 0.0);
                pstmt.setInt(6, folgaId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Aus\u00eancia alterada com sucesso", "Sucesso", 1);
                pstmt.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao alterar folga!", "Erro", 0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Folga n\u00e3o encontrada com os crit\u00e9rios fornecidos", "Erro", 0);
        }
        this.agendamento();
    }

    private void eliminarFolga(int folgaId) {
        try {
            this.connector();
            String sql = "DELETE FROM Folgas WHERE FolgaId=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, folgaId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Aus\u00eancia eliminada com sucesso", "Sucesso", 1);
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma aus\u00eancia encontrada para eliminar", "Erro", 0);
            }
            pstmt.close();
            this.atualizarTabelaFolgas();
        }
        catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao eliminar aus\u00eancia!", "Erro", 0);
        }
    }

    private void atualizarTabelaFolgas() {
        DefaultTableModel model = (DefaultTableModel)this.jTableFolgasAprovadasFuncionario.getModel();
        model.setRowCount(0);
        try {
            this.connector();
            String sql = "SELECT Func.Nome,F.DataPedido,F.Estado,F.DataInicio,F.DataFim,F.Motivo,F.Remuneracao FROM Folgas F INNER JOIN Funcion\u00e1rio Func ON F.FuncionarioId=Func.FuncionarioId";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String nome = rs.getString("Nome");
                String dataPedido = rs.getString("DataPedido");
                String estado = rs.getString("Estado");
                String dataInicio = rs.getString("DataInicio");
                String dataFim = rs.getString("DataFim");
                String motivo = rs.getString("Motivo");
                double remuneracao = rs.getDouble("Remuneracao");
                model.addRow(new Object[]{nome, dataPedido, estado, dataInicio, dataFim, motivo, remuneracao});
            }
            rs.close();
            pstmt.close();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar a tabela", "Erro", 0);
        }
    }

    private void listarFolgasFuncionarioDataInicio(int page, java.util.Date dataInicio, int funcionarioId) {
        try {
            this.connector();
            String sql = "SELECT COUNT(*)FROM Folgas WHERE Estado ='Aprovada' AND FuncionarioId=? AND DataInicio=?";
            PreparedStatement countStatement = con.prepareStatement(sql);
            countStatement.setInt(1, funcionarioId);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dataInicioString = sdf.format(dataInicio);
            countStatement.setString(2, dataInicioString);
            ResultSet resultSetCount = countStatement.executeQuery();
            if (resultSetCount.next()) {
                this.totalRows = resultSetCount.getInt(1);
            }
            int totalPages = (int)Math.ceil((double)this.totalRows / 30.0);
            int offset = (page - 1) * 30;
            sql = "SELECT Func.Nome, F.DataPedido, F.Estado, F.DataInicio, F.DataFim, F.Motivo, F.Remuneracao                 FROM Folgas F INNER JOIN Funcion\u00e1rio Func ON F.FuncionarioId=Func.FuncionarioId                 WHERE F.Estado ='Aprovada' AND F.FuncionarioId=? AND F.DataInicio=? LIMIT ? OFFSET ?";
            PreparedStatement statementAprovada = con.prepareStatement(sql);
            statementAprovada.setInt(1, funcionarioId);
            statementAprovada.setString(2, dataInicioString);
            statementAprovada.setInt(3, 30);
            statementAprovada.setInt(4, offset);
            ResultSet resultSetAprovada = statementAprovada.executeQuery();
            DefaultTableModel model = (DefaultTableModel)this.jTableFolgasAprovadasFuncionario.getModel();
            model.setRowCount(0);
            while (resultSetAprovada.next()) {
                String nomeFuncionario = resultSetAprovada.getString("Nome");
                String dataPedido = resultSetAprovada.getString("DataPedido");
                String estado = resultSetAprovada.getString("Estado");
                String dataInicioStr = resultSetAprovada.getString("DataInicio");
                String dataFim = resultSetAprovada.getString("DataFim");
                String motivo = resultSetAprovada.getString("Motivo");
                double remuneracao = resultSetAprovada.getDouble("Remuneracao");
                model.addRow(new Object[]{nomeFuncionario, dataPedido, estado, dataInicioStr, dataFim, motivo, remuneracao});
            }
            resultSetAprovada.close();
            statementAprovada.close();
            model.fireTableDataChanged();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void listarFolgasFuncionarioDataPedido(int page, java.util.Date dataPedido, int funcionarioId) {
        try {
            this.connector();
            String sql = "SELECT COUNT(*)FROM Folgas WHERE Estado ='Aprovada' AND FuncionarioId=? AND DataPedido=?";
            PreparedStatement countStatement = con.prepareStatement(sql);
            countStatement.setInt(1, funcionarioId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataPedidoString = sdf.format(dataPedido);
            countStatement.setString(2, dataPedidoString);
            ResultSet resultSetCount = countStatement.executeQuery();
            if (resultSetCount.next()) {
                this.totalRows = resultSetCount.getInt(1);
            }
            int totalPages = (int)Math.ceil((double)this.totalRows / 30.0);
            int offset = (page - 1) * 30;
            sql = "SELECT Func.Nome, F.DataPedido, F.Estado, F.DataInicio, F.DataFim, F.Motivo, F.Remuneracao                 FROM Folgas F INNER JOIN Funcion\u00e1rio Func ON F.FuncionarioId=Func.FuncionarioId                 WHERE F.Estado ='Aprovada' AND F.FuncionarioId=? AND F.DataPedido=? LIMIT ? OFFSET ?";
            PreparedStatement statementAprovada = con.prepareStatement(sql);
            statementAprovada.setInt(1, funcionarioId);
            statementAprovada.setString(2, dataPedidoString);
            statementAprovada.setInt(3, 30);
            statementAprovada.setInt(4, offset);
            ResultSet resultSetAprovada = statementAprovada.executeQuery();
            DefaultTableModel model = (DefaultTableModel)this.jTableFolgasAprovadasFuncionario.getModel();
            model.setRowCount(0);
            while (resultSetAprovada.next()) {
                String nomeFuncionario = resultSetAprovada.getString("Nome");
                String dataPedidoStr = resultSetAprovada.getString("DataPedido");
                String estado = resultSetAprovada.getString("Estado");
                String dataInicio = resultSetAprovada.getString("DataInicio");
                String dataFim = resultSetAprovada.getString("DataFim");
                String motivo = resultSetAprovada.getString("Motivo");
                double remuneracao = resultSetAprovada.getDouble("Remuneracao");
                model.addRow(new Object[]{nomeFuncionario, dataPedidoStr, estado, dataInicio, dataFim, motivo, remuneracao});
            }
            resultSetAprovada.close();
            statementAprovada.close();
            model.fireTableDataChanged();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JLabel getJLabelNome() {
        return this.jLabelNome;
    }

    public void atualizarLabels(String nomeUsuario) {
        this.jLabelNome.setText(nomeUsuario);
        this.jLabelFuncionario.setText(nomeUsuario);
        this.jLabelFuncionario2.setText(nomeUsuario);
        String nomeFuncionario = this.jLabelFuncionario2.getText().trim();
        int idFuncionario = this.obterFuncionarioId(nomeFuncionario);
        System.out.println("Nome:" + nomeFuncionario);
        System.out.println("Funcion\u00e1rioIdencontrado:" + idFuncionario);
        System.out.println("Texto da jLabelFuncionario2:" + this.jLabelFuncionario2.getText());
    }

    public int obterFuncionarioId(String nome) {
        String sql = "SELECT FuncionarioId FROM Funcion\u00e1rio WHERE Nome = ?";
        try {
            this.connector();
            PreparedStatement funcionarioIdStatement = con.prepareStatement(sql);
            funcionarioIdStatement.setString(1, nome);
            ResultSet funcionarioIdResultSet = funcionarioIdStatement.executeQuery();
            if (funcionarioIdResultSet.next()) {
                int funcionarioId = funcionarioIdResultSet.getInt("FuncionarioId");
                System.out.println("Funcion\u00e1rio Id Encontrado" + funcionarioId);
                funcionarioIdResultSet.close();
                funcionarioIdStatement.close();
                return funcionarioId;
            }
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
        return -1;
    }

    private void InserirDadosBaseDados() {
        int funcionarioId = this.obterFuncionarioId(this.jLabelNome.getText().trim());
        if (funcionarioId != -1) {
            try {
                this.connector();
                java.util.Date dataAtual = new java.util.Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dataPedido = sdf.format(dataAtual);
                String sql = "INSERT INTO Folgas (FuncionarioId, DataPedido, Estado, DataInicio, DataFim, Motivo, Remuneracao) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = con.prepareStatement(sql);
                insertStatement.setInt(1, funcionarioId);
                insertStatement.setString(2, dataPedido);
                insertStatement.setString(3, "Pendente");
                sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dataInicio = sdf.format(this.jDateChooserDataInicio.getDate());
                insertStatement.setString(4, dataInicio);
                sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dataFim = sdf.format(this.jDateChooserDataFim.getDate());
                insertStatement.setString(5, dataFim);
                insertStatement.setString(6, this.jComboBoxMotivos.getSelectedItem().toString());
                insertStatement.setDouble(7, 0.0);
                insertStatement.executeUpdate();
                insertStatement.close();
                JOptionPane.showMessageDialog(this, "Aus\u00eancia submetida com sucesso.", "Sucesso", 1);
            }
            catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao inserir folga na base de dados: " + e.getMessage(), "Erro", 0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao recuperar o ID do funcion\u00e1rio.", "Erro", 0);
        }
        this.agendamento();
    }

    private void limparCampos() {
        this.jDateChooserDataInicio.setDate(null);
        this.jDateChooserDataFim.setDate(null);
        this.jComboBoxMotivos.setSelectedItem("Doen\u00e7a");
    }

    private int contarFolgas(java.util.Date dataInicio, java.util.Date dataFim) {
        int totalFolgas = 0;
        try {
            this.connector();
            SimpleDateFormat sdfIso = new SimpleDateFormat("yyyyMMdd");
            String dataInicioISO = sdfIso.format(dataInicio);
            String dataFimISO = sdfIso.format(dataFim);
            String sql = "SELECT COUNT(*) FROM Folgas WHERE Estado= 'Aprovada' "
                + "AND (substr(DataInicio,7,4)||substr(DataInicio,4,2)||substr(DataInicio,1,2)) <= ? "
                + "AND (substr(DataFim,7,4)||substr(DataFim,4,2)||substr(DataFim,1,2)) >= ?";
            PreparedStatement countStatement = con.prepareStatement(sql);
            countStatement.setString(1, dataFimISO);
            countStatement.setString(2, dataInicioISO);
            ResultSet resultSetFolgas = countStatement.executeQuery();
            if (resultSetFolgas.next()) {
                totalFolgas = resultSetFolgas.getInt(1);
            }
            System.out.println("Data de in\u00edcio" + dataInicio);
            System.out.println("Data de Fim" + dataFim);
            System.out.println("N\u00famero de folgas aprovadas" + totalFolgas);
            resultSetFolgas.close();
            countStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return totalFolgas;
    }

    private java.util.Date calcularPascoa(int ano) {
        int a = ano % 19;
        int b = ano / 100;
        int c = ano % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (19 * a + b - d - f + 15) % 30;
        int h = c / 4;
        int i = c % 4;
        int k = (32 + 2 * e + 2 * h - g - i) % 7;
        int l = (a + 11 * g + 22 * k) / 451;
        int m = g + k - 7 * l + 114;
        int dia = m % 31;
        int mes = m / 31;
        Calendar calendar = Calendar.getInstance();
        calendar.set(ano, mes - 1, dia, 0, 0, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    private boolean isFeriado(java.util.Date data, List<java.util.Date> feriados) {
        Calendar calData = Calendar.getInstance();
        calData.setTime(data);
        calData.set(11, 0);
        calData.set(12, 0);
        calData.set(13, 0);
        calData.set(14, 0);
        Calendar calDataSemHora = Calendar.getInstance();
        calDataSemHora.set(calData.get(1), calData.get(2), calData.get(5));
        return feriados.stream().anyMatch(feriado -> {
            Calendar calFeriado = Calendar.getInstance();
            calFeriado.setTime((java.util.Date)feriado);
            calFeriado.set(11, 0);
            calFeriado.set(12, 0);
            calFeriado.set(13, 0);
            calFeriado.set(14, 0);
            Calendar calFeriadoSemHora = Calendar.getInstance();
            calFeriadoSemHora.set(calFeriado.get(1), calFeriado.get(2), calFeriado.get(5));
            return calDataSemHora.getTime().equals(calFeriadoSemHora.getTime());
        });
    }

    private long calcularDiferencaDias(java.util.Date dataInicio, java.util.Date dataFim) {
        long diferencaMillis = dataFim.getTime() - dataInicio.getTime();
        return TimeUnit.DAYS.convert(diferencaMillis, TimeUnit.MILLISECONDS);
    }

    private long calcularDiferencaDiasUteis(java.util.Date dataInicio, java.util.Date dataFim) {
        long diasUteis = 0L;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataInicio);
        while (cal.getTime().before(dataFim) || cal.getTime().equals(dataFim)) {
            int diaSemana = cal.get(7);
            if (diaSemana != 7 && diaSemana != 1) {
                ++diasUteis;
            }
            cal.add(5, 1);
        }
        return diasUteis;
    }

    private int contarDiasUteisFeriasAno(int ano, int funcionarioId, java.util.Date dataInicioReq, java.util.Date dataFimReq) {
        int totalDiasUteisFerias = 0;
        try {
            this.connector();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar startOfYear = Calendar.getInstance();
            startOfYear.set(ano, Calendar.JANUARY, 1, 0, 0, 0); startOfYear.set(Calendar.MILLISECOND, 0);
            Calendar endOfYear = Calendar.getInstance();
            endOfYear.set(ano, Calendar.DECEMBER, 31, 23, 59, 59); endOfYear.set(Calendar.MILLISECOND, 999);
            String sql = "SELECT DataInicio, DataFim FROM Folgas WHERE Estado = 'Aprovada' AND Motivo='F\u00e9rias' AND FuncionarioId = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, funcionarioId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                java.util.Date di = sdf.parse(rs.getString("DataInicio"));
                java.util.Date df = sdf.parse(rs.getString("DataFim"));
                java.util.Date efStart = di.before(startOfYear.getTime()) ? startOfYear.getTime() : di;
                java.util.Date efEnd   = df.after(endOfYear.getTime())   ? endOfYear.getTime()   : df;
                if (!efStart.after(efEnd)) {
                    totalDiasUteisFerias += (int) this.calcularDiferencaDiasUteis(efStart, efEnd);
                }
            }
            rs.close();
            pstmt.close();
            totalDiasUteisFerias += (int) this.calcularDiferencaDiasUteis(dataInicioReq, dataFimReq);
        }
        catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return totalDiasUteisFerias;
    }

    private java.util.Date obterDataPedido(int funcionarioId) {
        java.util.Date dataPedido = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String dataPedidoStr;
            this.connector();
            String sql = "SELECT DataPedido FROM Folgas WHERE FuncionarioId = ? ORDER BY DataPedido DESC LIMIT 1";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, funcionarioId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && (dataPedidoStr = rs.getString("DataPedido")) != null) {
                try {
                    dataPedido = sdf.parse(dataPedidoStr);
                }
                catch (ParseException e) {
                    System.err.println("Erro ao analisar a data do pedido: " + e.getMessage());
                }
            }
            rs.close();
            pstmt.close();
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return dataPedido;
    }

    private java.util.Date obterDataAdmissao(int funcionarioId) {
        java.util.Date dataAdmissao = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String dataAdmissaoStr;
            this.connector();
            String sql = "SELECT DataAdmiss\u00e3o FROM Funcion\u00e1rio WHERE FuncionarioId = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, funcionarioId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && (dataAdmissaoStr = rs.getString("DataAdmiss\u00e3o")) != null) {
                try {
                    dataAdmissao = sdf.parse(dataAdmissaoStr);
                }
                catch (ParseException e) {
                    System.err.println("Erro ao analisar a data de admiss\u00e3o: " + e.getMessage());
                }
            }
            rs.close();
            pstmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return dataAdmissao;
    }

    private int calcularDireitoFerias(java.util.Date dataAdmissao, java.util.Date dataAtual) {
        Calendar calAdmissao = Calendar.getInstance();
        calAdmissao.setTime(dataAdmissao);
        Calendar calAtual = Calendar.getInstance();
        calAtual.setTime(dataAtual);
        int meses = (calAtual.get(1) - calAdmissao.get(1)) * 12 + calAtual.get(2) - calAdmissao.get(2);
        return Math.min(15, 2 * meses);
    }

    private boolean podeAprovarFerias(java.util.Date dataAdmissao, java.util.Date dataPedido) {
        Calendar calAdmissao = Calendar.getInstance();
        calAdmissao.setTime(dataAdmissao);
        Calendar calPedido = Calendar.getInstance();
        calPedido.setTime(dataPedido);
        calAdmissao.add(2, 6);
        return !calPedido.before(calAdmissao.getTime());
    }

    private int contarDiasAnoAssistenciaFamilia(int ano, int funcionarioId, java.util.Date dataInicioReq, java.util.Date dataFimReq) {
        int totalDiasAssistenciaFamilia = 0;
        try {
            this.connector();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar startOfYear = Calendar.getInstance();
            startOfYear.set(ano, Calendar.JANUARY, 1, 0, 0, 0); startOfYear.set(Calendar.MILLISECOND, 0);
            Calendar endOfYear = Calendar.getInstance();
            endOfYear.set(ano, Calendar.DECEMBER, 31, 23, 59, 59); endOfYear.set(Calendar.MILLISECOND, 999);
            String sql = "SELECT DataInicio, DataFim FROM Folgas WHERE Estado = 'Aprovada' AND Motivo='Assist\u00eancia \u00e0 fam\u00edlia' AND FuncionarioId = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, funcionarioId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                java.util.Date di = sdf.parse(rs.getString("DataInicio"));
                java.util.Date df = sdf.parse(rs.getString("DataFim"));
                java.util.Date efStart = di.before(startOfYear.getTime()) ? startOfYear.getTime() : di;
                java.util.Date efEnd   = df.after(endOfYear.getTime())   ? endOfYear.getTime()   : df;
                if (!efStart.after(efEnd)) {
                    totalDiasAssistenciaFamilia += (int) this.calcularDiferencaDiasDataInicio(efStart, efEnd);
                }
            }
            rs.close();
            pstmt.close();
            totalDiasAssistenciaFamilia += (int) this.calcularDiferencaDiasDataInicio(dataInicioReq, dataFimReq);
        }
        catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return totalDiasAssistenciaFamilia;
    }

    private int contarDiasAnoAssistenciaFilho(int ano, int funcionarioId, java.util.Date dataInicioReq, java.util.Date dataFimReq) {
        int totalDiasAssistenciaFilho = 0;
        try {
            this.connector();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar startOfYear = Calendar.getInstance();
            startOfYear.set(ano, Calendar.JANUARY, 1, 0, 0, 0); startOfYear.set(Calendar.MILLISECOND, 0);
            Calendar endOfYear = Calendar.getInstance();
            endOfYear.set(ano, Calendar.DECEMBER, 31, 23, 59, 59); endOfYear.set(Calendar.MILLISECOND, 999);
            String sql = "SELECT DataInicio, DataFim FROM Folgas WHERE Estado = 'Aprovada' AND Motivo='Assist\u00eancia a filho' AND FuncionarioId = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, funcionarioId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                java.util.Date di = sdf.parse(rs.getString("DataInicio"));
                java.util.Date df = sdf.parse(rs.getString("DataFim"));
                java.util.Date efStart = di.before(startOfYear.getTime()) ? startOfYear.getTime() : di;
                java.util.Date efEnd   = df.after(endOfYear.getTime())   ? endOfYear.getTime()   : df;
                if (!efStart.after(efEnd)) {
                    totalDiasAssistenciaFilho += (int) this.calcularDiferencaDiasDataInicio(efStart, efEnd);
                }
            }
            rs.close();
            pstmt.close();
            totalDiasAssistenciaFilho += (int) this.calcularDiferencaDiasDataInicio(dataInicioReq, dataFimReq);
        }
        catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return totalDiasAssistenciaFilho;
    }

    private LocalDate toLocalDate(java.util.Date date) {
        return new Date(date.getTime()).toLocalDate();
    }

    private String obterDepartamento(int funcionarioId) {
        String departamento = "";
        try {
            this.connector();
            String query = "SELECT Departamento FROM Funcion\u00e1rio WHERE FuncionarioId = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, funcionarioId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                departamento = resultSet.getString("Departamento");
                System.out.println("Departamento=" + departamento);
            } else {
                System.out.println("Departamento n\u00e3o encontrado");
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return departamento;
    }

    private int obterEscalao(int funcionarioId) {
        int escalao = 0;
        try {
            this.connector();
            String query = "SELECT Escalao FROM Funcion\u00e1rio WHERE FuncionarioId = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, funcionarioId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                escalao = resultSet.getInt("Escalao");
                System.out.println("Escal\u00e3o" + escalao);
            } else {
                System.out.println("Escal\u00e3o n\u00e3o encontrado");
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return escalao;
    }

    private double obterSalarioMensal(int funcionarioId) {
        int escalao = this.obterEscalao(funcionarioId);
        String departamento = this.obterDepartamento(funcionarioId);
        double[] salarios = null;
        if (departamento.equals("Administra\u00e7\u00e3o de Sistemas") || departamento.equals("Administra\u00e7\u00e3o de Infra-estrutura de Rede") || departamento.equals("Desenvolvimento e Implementa\u00e7\u00e3o de Novos Projetos")) {
            salarios = ESPECIALISTAS;
        } else if (departamento.equals("Manuten\u00e7\u00e3o de Equipamentos e Servi\u00e7os") || departamento.equals("Suporte aos Utilizadores")) {
            salarios = TECNICOS;
        }
        if (salarios != null && escalao > 0 && escalao <= salarios.length) {
            System.out.println("Sal\u00e1rio mensal:" + salarios[escalao - 1]);
            return salarios[escalao - 1];
        }
        return 0.0;
    }

    private long calcularDiferencaDiasDataInicio(java.util.Date dataInicio, java.util.Date dataFim) {
        long diffInMillies = dataFim.getTime() - dataInicio.getTime();
        System.out.println("Diferen\u00e7a de dias=" + diffInMillies / 86400000L + "1");
        return diffInMillies / 86400000L + 1L;
    }

    private int obterDiasMes(Calendar calendar) {
        System.out.println("N\u00famero de dias" + calendar.getActualMaximum(5));
        return calendar.getActualMaximum(5);
    }

    private double calcularSalarioDiario(double salarioMensal, Calendar calendario) {
        int diasMes = this.obterDiasMes(calendario);
        return salarioMensal / (double)diasMes;
    }

    private double calcularRemuneracaoFolga(double salarioMensal, java.util.Date dataInicio, java.util.Date dataFim) {
        Calendar calDataInicio = Calendar.getInstance();
        Calendar calDataFim = Calendar.getInstance();
        calDataInicio.setTime(dataInicio);
        calDataFim.setTime(dataFim);
        double remuneracaoTotal = 0.0;
        while (calDataInicio.before(calDataFim) || calDataInicio.equals(calDataFim)) {
            double salarioDiario = this.calcularSalarioDiario(salarioMensal, calDataInicio);
            int diasMes = this.obterDiasMes(calDataInicio);
            int restantesDiasMes = diasMes - calDataInicio.get(5) + 1;
            int diasPeriodoAtual = (int)Math.min((long)restantesDiasMes, this.calcularDiferencaDiasDataInicio(calDataInicio.getTime(), calDataFim.getTime()));
            remuneracaoTotal += salarioDiario * (double)diasPeriodoAtual;
            calDataInicio.add(2, 1);
            calDataInicio.set(5, 1);
        }
        return remuneracaoTotal;
    }

    private void atualizarRemuneracaoFolga(int funcionarioId, double remuneracaoFolga, java.util.Date dataInicio, java.util.Date dataFim) {
        funcionarioId = this.obterFuncionarioId(this.jLabelNome.getText().trim());
        try {
            this.connector();
            String updateSqlRemuneracao = "UPDATE Folgas SET Remuneracao = ? WHERE FolgaId=(SELECT MAX (FolgaId) FROM Folgas WHERE FuncionarioId = ? AND DataInicio = ? AND DataFim = ?)";
            PreparedStatement updateStatementRemuneracao = con.prepareStatement(updateSqlRemuneracao);
            updateStatementRemuneracao.setDouble(1, (double)Math.round(remuneracaoFolga * 100.0) / 100.0);
            updateStatementRemuneracao.setInt(2, funcionarioId);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dataInicioString = sdf.format(dataInicio);
            updateStatementRemuneracao.setString(3, dataInicioString);
            String dataFimString = sdf.format(dataFim);
            updateStatementRemuneracao.setString(4, dataFimString);
            int rowsAffected = updateStatementRemuneracao.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Remunera\u00e7\u00e3o atualizada com sucesso");
            } else {
                System.out.println("Nenhuma remunera\u00e7\u00e3o foi atualizada");
            }
            updateStatementRemuneracao.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exibirRemuneracaoFolgaPorEscalao(String departamento, double salarioMensal, java.util.Date dataInicio, java.util.Date dataFim, String remuneracaoFormatada) {
        double remuneracaoFolga = this.calcularRemuneracaoFolga(salarioMensal, dataInicio, dataFim);
        System.out.println("A sua folga por escal\u00e3o \u00e9" + remuneracaoFolga);
        String mensagem = "A sua aus\u00eancia ser\u00e1 remunerada em " + remuneracaoFormatada + " euros.";
        ConfirmacaoRemuneracao confirmacao = new ConfirmacaoRemuneracao((Frame)new JFrame(), true);
        confirmacao.getJLabelRemuneracao().setText(mensagem);
        confirmacao.setVisible(true);
    }

    private boolean atravessarMes(java.util.Date dataInicio, java.util.Date dataFim) {
        Calendar inicioCal = Calendar.getInstance();
        inicioCal.setTime(dataInicio);
        Calendar fimCal = Calendar.getInstance();
        fimCal.setTime(dataFim);
        boolean atravessaMes = inicioCal.get(2) != fimCal.get(2) || inicioCal.get(1) != fimCal.get(1);
        System.out.println("Atravessa m\u00eas" + atravessaMes);
        return atravessaMes;
    }

    private void agendamento() {
        boolean dataPedidoCorreta;
        String motivoSelecionado = this.jComboBoxMotivos.getSelectedItem().toString();
        java.util.Date dataInicio = this.jDateChooserDataInicio.getDate();
        java.util.Date dataFim = this.jDateChooserDataFim.getDate();
        int funcionarioId = this.obterFuncionarioId(this.jLabelNome.getText().trim());
        java.util.Date dataPedido = this.obterDataPedido(funcionarioId);
        String departamento = this.obterDepartamento(funcionarioId);
        double salarioMensal = this.obterSalarioMensal(funcionarioId);
        long diasFolga = this.calcularDiferencaDiasDataInicio(dataInicio, dataFim);
        System.out.println("Dias de folga calculados" + diasFolga);
        double remuneracaoFolga = this.calcularRemuneracaoFolga(salarioMensal, dataInicio, dataFim);
        long diferencaDiasPedidoInicio = this.calcularDiferencaDias(dataPedido, dataInicio);
        System.out.println("Diferen\u00e7a de dias entre data de pedido e data de in\u00edcio" + diferencaDiasPedidoInicio);
        int totalDiasUteisFerias = this.contarDiasUteisFeriasAno(Calendar.getInstance().get(1), funcionarioId, dataInicio, dataFim);
        int totalDiasAnoAssistenciaFamilia = this.contarDiasAnoAssistenciaFamilia(Calendar.getInstance().get(1), funcionarioId, dataInicio, dataFim);
        int totalDiasAnoAssistenciaFilho = this.contarDiasAnoAssistenciaFilho(Calendar.getInstance().get(1), funcionarioId, dataInicio, dataFim);
        String estado = "Pendente";
        String mensagem = "";
        String mensagemRemuneracao = "";
        int anoAtual = Calendar.getInstance().get(1);
        ConfirmacaoRemuneracao confirmacaoRemuneracao = new ConfirmacaoRemuneracao((Frame)new JFrame(), true);
        long diasAssistenciaFilhoDeficiencia = this.calcularDiferencaDias(dataInicio, dataFim);
        AprovacaoRejeicao aprovacaoRejeicao = new AprovacaoRejeicao((Frame)new JFrame(), true);
        long diasUteis = this.calcularDiferencaDiasUteis(dataInicio, dataFim);
        java.util.Date dataAdmissao = this.obterDataAdmissao(funcionarioId);
        int direitoFerias = this.calcularDireitoFerias(dataAdmissao, new java.util.Date());
        Calendar calDataFimEsperada = Calendar.getInstance();
        calDataFimEsperada.setTime(dataInicio);
        calDataFimEsperada.add(1, 1);
        calDataFimEsperada.set(2, 3);
        calDataFimEsperada.set(5, 30);
        java.util.Date dataFimEsperada = calDataFimEsperada.getTime();
        boolean dataFimCorreta = dataFim.before(dataFimEsperada) || dataFim.equals(dataFimEsperada);
        Calendar calDataPedido = Calendar.getInstance();
        calDataPedido.setTime(dataPedido);
        Calendar calInicioIntervalo = Calendar.getInstance();
        calInicioIntervalo.set(calDataPedido.get(1), 4, 1);
        Calendar calFimIntervalo = Calendar.getInstance();
        calFimIntervalo.set(calDataPedido.get(1), 9, 31);
        boolean bl = dataPedidoCorreta = !calDataPedido.before(calInicioIntervalo) && !calDataPedido.after(calFimIntervalo);
        if (motivoSelecionado.equals("Doen\u00e7a") && this.calcularDiferencaDias(dataInicio, dataFim) <= 2L) {
            estado = "Aprovada";
            mensagem = "A sua aus\u00eancia foi aprovada!";
        } else if (motivoSelecionado.equals("Doen\u00e7a com regime de prote\u00e7\u00e3o pela Seguran\u00e7a Social") && this.calcularDiferencaDias(dataInicio, dataFim) >= 4L) {
            estado = "Aprovada";
            mensagem = "A sua aus\u00eancia foi aprovada!";
            mensagemRemuneracao = "A sua aus\u00eancia n\u00e3o ser\u00e1 remunerada";
        } else if (motivoSelecionado.equals("F\u00e9rias") && dataFimCorreta && dataPedidoCorreta) {
            Calendar calAdmAno = Calendar.getInstance();
            calAdmAno.setTime(dataAdmissao);
            int limiteAnualFerias = (calAdmAno.get(Calendar.YEAR) == anoAtual) ? direitoFerias : 30;
            if (this.contarFolgas(dataInicio, dataFim) <= 0 && this.podeAprovarFerias(dataAdmissao, dataPedido) && totalDiasUteisFerias <= limiteAnualFerias) {
                estado = "Aprovada";
                mensagem = "A sua aus\u00eancia foi aprovada!";
            } else {
                estado = "Rejeitada";
                mensagem = "A sua aus\u00eancia foi rejeitada!";
            }
        } else if ((motivoSelecionado.equals("Falecimento de c\u00f4njuge") || motivoSelecionado.equals("Falecimento de pais")) && this.calcularDiferencaDias(dataInicio, dataFim) <= 4L) {
            estado = "Aprovada";
            mensagem = "A sua aus\u00eancia foi aprovada!";
        } else if ((motivoSelecionado.equals("Falecimento de av\u00f3s") || motivoSelecionado.equals("Falecimento de irm\u00e3os")) && this.calcularDiferencaDias(dataInicio, dataFim) <= 1L) {
            estado = "Aprovada";
            mensagem = "A sua aus\u00eancia foi aprovada!";
        } else if (motivoSelecionado.equals("Falecimento de filhos") && this.calcularDiferencaDias(dataInicio, dataFim) <= 19L) {
            estado = "Aprovada";
            mensagem = "A sua aus\u00eancia foi aprovada!";
        } else if (motivoSelecionado.equals("Licen\u00e7a de casamento")) {
            if (this.calcularDiferencaDias(dataInicio, dataFim) <= 14L && diferencaDiasPedidoInicio >= 5L) {
                estado = "Aprovada";
                mensagem = "A sua aus\u00eancia foi aprovada!";
            } else {
                estado = "Rejeitada";
                mensagem = "A sua aus\u00eancia foi rejeitada!";
            }
        } else if (motivoSelecionado.equals("Licen\u00e7a de maternidade") && this.calcularDiferencaDias(dataInicio, dataFim) >= 42L && this.calcularDiferencaDias(dataInicio, dataFim) <= 91L) {
            estado = "Aprovada";
            mensagem = "A sua aus\u00eancia foi aprovada!";
        } else if (motivoSelecionado.equals("Licen\u00e7a de paternidade")) {
            if (this.calcularDiferencaDias(dataInicio, dataFim) >= 7L && this.calcularDiferencaDias(dataInicio, dataFim) <= 26L && diferencaDiasPedidoInicio >= 5L) {
                estado = "Aprovada";
                mensagem = "A sua aus\u00eancia foi aprovada!";
            } else {
                estado = "Rejeitada";
                mensagem = "A sua aus\u00eancia foi rejeitada!";
            }
        } else if (motivoSelecionado.equals("Assist\u00eancia \u00e0 fam\u00edlia") && totalDiasAnoAssistenciaFamilia <= 15) {
            estado = "Aprovada";
            mensagem = "A sua aus\u00eancia foi aprovada!";
            mensagemRemuneracao = "A sua aus\u00eancia n\u00e3o ser\u00e1 remunerada";
        } else if (motivoSelecionado.equals("Assist\u00eancia a filho") && totalDiasAnoAssistenciaFilho <= 30) {
            estado = "Aprovada";
            mensagem = "A sua aus\u00eancia foi aprovada!";
        } else if (motivoSelecionado.equals("Assist\u00eancia a filho deficiente ou com doen\u00e7a cr\u00f3nica")) {
            if (diasAssistenciaFilhoDeficiencia <= 179L) {
                estado = "Aprovada";
                mensagem = "A sua aus\u00eancia foi aprovada!";
            } else {
                estado = "Rejeitada";
                mensagem = "A sua aus\u00eancia foi rejeitada!";
            }
        } else {
            estado = "Rejeitada";
            mensagem = "A sua aus\u00eancia foi rejeitada!";
        }
        boolean folgaRepetida = false;
        boolean folgaAtravessaMes = this.atravessarMes(dataInicio, dataFim);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfIso = new SimpleDateFormat("yyyyMMdd");
        try {
            String checkSql = "SELECT COUNT(*) FROM Folgas WHERE FuncionarioId =? AND Estado='Aprovada' "
                + "AND (substr(DataInicio,7,4)||substr(DataInicio,4,2)||substr(DataInicio,1,2)) <= ? "
                + "AND (substr(DataFim,7,4)||substr(DataFim,4,2)||substr(DataFim,1,2)) >= ?";
            PreparedStatement checkStatement = con.prepareStatement(checkSql);
            checkStatement.setInt(1, funcionarioId);
            checkStatement.setString(2, sdfIso.format(dataFim));
            checkStatement.setString(3, sdfIso.format(dataInicio));
            ResultSet rs = checkStatement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Count" + count);
                if (count > 0) {
                    folgaRepetida = true;
                    System.out.println("Folga repetida" + folgaRepetida);
                } else {
                    System.out.println("N\u00e3o existem folgas repetidas");
                }
            }
            checkStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        if (folgaRepetida) {
            estado = "Rejeitada";
            mensagem = "A sua aus\u00eancia foi rejeitada!";
        }
        try {
            String updateSql = "UPDATE Folgas SET Estado = ? WHERE FolgaId=(SELECT MAX (FolgaId) FROM Folgas WHERE FuncionarioId = ? AND DataInicio = ? AND DataFim = ? );";
            String dataInicioString = sdf.format(this.jDateChooserDataInicio.getDate());
            String dataFimString = sdf.format(this.jDateChooserDataFim.getDate());
            PreparedStatement updateStatement = con.prepareStatement(updateSql);
            updateStatement.setString(1, estado);
            updateStatement.setInt(2, funcionarioId);
            updateStatement.setString(3, dataInicioString);
            updateStatement.setString(4, dataFimString);
            updateStatement.executeUpdate();
            System.out.println("Estado" + estado);
            System.out.println("Mensagem" + mensagem);
            updateStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        Font fonteNegrito = new Font("Arial", 1, 12);
        DecimalFormat df = new DecimalFormat("#.00");
        String remuneracaoFormatada = df.format(remuneracaoFolga);
        confirmacaoRemuneracao.setVisible(false);
        if (estado.equals("Aprovada")) {
            Color cor = new Color(0, 128, 0);
            aprovacaoRejeicao.getJLabelAprovacaoRejeicao().setFont(fonteNegrito);
            aprovacaoRejeicao.getJLabelAprovacaoRejeicao().setText(mensagem);
            aprovacaoRejeicao.getJLabelAprovacaoRejeicao().setForeground(cor);
            aprovacaoRejeicao.setVisible(true);
            if (motivoSelecionado.equals("Doen\u00e7a com regime de prote\u00e7\u00e3o pela Seguran\u00e7a Social") || motivoSelecionado.equals("Assist\u00eancia \u00e0 fam\u00edlia")) {
                confirmacaoRemuneracao.getJLabelRemuneracao().setText(mensagemRemuneracao);
                confirmacaoRemuneracao.setVisible(true);
            } else {
                this.atualizarRemuneracaoFolga(funcionarioId, remuneracaoFolga, dataInicio, dataFim);
                this.exibirRemuneracaoFolgaPorEscalao(departamento, salarioMensal, dataInicio, dataFim, remuneracaoFormatada);
            }
        } else {
            Color cor = Color.RED;
            aprovacaoRejeicao.getJLabelAprovacaoRejeicao().setFont(fonteNegrito);
            aprovacaoRejeicao.getJLabelAprovacaoRejeicao().setText(mensagem);
            aprovacaoRejeicao.getJLabelAprovacaoRejeicao().setForeground(cor);
            aprovacaoRejeicao.setVisible(true);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
            }
        });
    }

    static {
        statement = null;
        ESPECIALISTAS = new double[]{1807.04, 2023.89, 2240.74, 2457.57, 2674.43, 2893.81, 3114.98, 3336.16, 3557.35, 3723.24, 3889.1};
        TECNICOS = new double[]{1070.19, 1280.72, 1438.62, 1596.52, 1754.41, 1915.46, 2078.11, 2240.74, 2403.37, 2566.01, 2674.43};
    }
}
