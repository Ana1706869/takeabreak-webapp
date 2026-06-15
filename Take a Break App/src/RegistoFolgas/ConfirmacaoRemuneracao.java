package RegistoFolgas;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;

public class ConfirmacaoRemuneracao extends JDialog {

    public static final int RET_CANCEL = 0;
    public static final int RET_OK = 1;

    private JLabel jLabelNome;
    private JLabel jLabelFuncionario;
    private JLabel jLabelRemuneracao;
    private JButton okButton;
    private JButton cancelButton;
    private int returnStatus = RET_CANCEL;

    public ConfirmacaoRemuneracao(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        jLabelNome = new JLabel();
        jLabelFuncionario = new JLabel();
    }

    public int getReturnStatus() {
        return returnStatus;
    }

    private void initComponents() {
        okButton = new JButton();
        cancelButton = new JButton();
        jLabelRemuneracao = new JLabel();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabelRemuneracao.setText("");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(250, Short.MAX_VALUE)
                        .addComponent(okButton, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cancelButton)
                    .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                    .addGap(36, 36, 36)
                    .addComponent(jLabelRemuneracao)
                    .addContainerGap(-1, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                    .addComponent(jLabelRemuneracao)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(okButton)
                        .addComponent(cancelButton))
                    .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);
        pack();
    }

    private void okButtonActionPerformed(ActionEvent evt) {
        doClose(RET_OK);
    }

    private void cancelButtonActionPerformed(ActionEvent evt) {
        doClose(RET_CANCEL);
    }

    private void closeDialog(WindowEvent evt) {
        doClose(RET_CANCEL);
    }

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    public JLabel getJLabelRemuneracao() {
        return jLabelRemuneracao;
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo look : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(look.getName())) {
                    UIManager.setLookAndFeel(look.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ConfirmacaoRemuneracao.class.getName()).log(Level.SEVERE, null, e);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConfirmacaoRemuneracao(new Frame(), true).setVisible(true);
            }
        });
    }
}
