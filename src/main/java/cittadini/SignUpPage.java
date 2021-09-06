/*
    Limiti Luca 738873 (sede VA)
    Zehhaf Ishak 737763 (sede VA)
    Ferro Paolo 737529 (sede VA)
    Casolo Ginelli Loris 737056 (sede VA)
 */

package cittadini;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import serverCV.serverCVInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.ArrayList;

/**
 * Classe per la registrazione di un nuovo utente con l'inserimento di tutte le sue informazioni e della scelta di un centro vaccinale per eseguire la vaccinazione
 */
public class SignUpPage extends JFrame {
    private JPanel panel2;
    private JFrame frame1;
    private JTextField Name;
    private JTextField Surname;
    private JTextField CodF;
    private JTextField Email;
    private JTextField password;
    private JButton signInButton;
    private JTextField userText;
    private JComboBox comboBox1; //implementare scelta centro dove registrarsi
    private JButton button1;
    private serverCVInterface server;

    /**
     * Classe per gestire la connessione al server e impostare la GUI del form di registrazione di un nuovo utente con controlli sugli inserimenti delle informazioni
     *
     * @throws SQLException
     */
    public SignUpPage() throws SQLException {
        try {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
            frame1 = new JFrame("SignUp");
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame1.setPreferredSize(new Dimension(500, 450));
            frame1.setResizable(false);
            frame1.add(panel2);
            frame1.pack();
            frame1.setLocationRelativeTo(null);
            frame1.setVisible(true);
            var c = server.listaCentriVaccinali().Clone();
            var cobj = (ArrayList<String>) c.getObject(0);
            var cobjadd = (ArrayList<String>) c.getObject(1);
            for (int i = 0; i < cobj.size(); i++) {
                comboBox1.addItem(cobj.get(i) + " (" + cobjadd.get(i) + ")");
            }


            signInButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Class.forName("org.postgresql.Driver");
                    } catch (ClassNotFoundException c) {
                        System.out.println("Class not found " + c);
                    }
                    try {
                        String user = userText.getText();
                        String p = password.getText();
                        String n = Name.getText();
                        String c = Surname.getText();
                        String cf = CodF.getText();
                        String em = Email.getText();
                        String nomeC = cobj.get(comboBox1.getSelectedIndex());
                        if (user.trim().equals("") || p.trim().equals("") || n.trim().equals("") || c.trim().equals("") || cf.trim().equals("") || em.trim().equals("")) {
                            JOptionPane.showMessageDialog(signInButton, "Uno dei campi è vuoto!", "Errore!", JOptionPane.ERROR_MESSAGE);
                        } else if (cf.trim().length() != 16) {
                            JOptionPane.showMessageDialog(signInButton, "Codice fiscale errato", "Errore!", JOptionPane.ERROR_MESSAGE);
                        } else if (server.registraCittadino(n, c, cf, em, user, p, nomeC)) {
                            JOptionPane.showMessageDialog(signInButton, "Utente Registrato!");
                            frame1.setVisible(false);
                            new utenteCV();
                        } else {
                            JOptionPane.showMessageDialog(signInButton, "Errore nella registrazione!", "Errore!", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException | RemoteException throwables) {
                        throwables.printStackTrace();
                    } catch (NullPointerException ex) {
                        JOptionPane.showMessageDialog(signInButton, "Username già utilizzato!", "Errore!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame1.setVisible(false);
                    try {
                        new utenteCV();
                    } catch (SQLException | RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Client err:" + e.getMessage());
            JOptionPane.showMessageDialog(null, "Errore nella connessione o nella lettura dei dati dal server");
            System.exit(1);
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(8, 5, new Insets(0, 0, 0, 0), -1, -1));
        Name = new JTextField();
        panel2.add(Name, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Nome");
        panel2.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        Surname = new JTextField();
        panel2.add(Surname, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Cognome");
        panel2.add(label2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CodF = new JTextField();
        panel2.add(CodF, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        Email = new JTextField();
        panel2.add(Email, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        password = new JTextField();
        password.setText("");
        panel2.add(password, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Codice Fiscale");
        panel2.add(label3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Email");
        panel2.add(label4, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Password");
        panel2.add(label5, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel2.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        signInButton = new JButton();
        signInButton.setText("Sign Up");
        panel2.add(signInButton, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userText = new JTextField();
        panel2.add(userText, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Username");
        panel2.add(label6, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox1 = new JComboBox();
        panel2.add(comboBox1, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Centro Vaccinale");
        panel2.add(label7, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button1 = new JButton();
        button1.setText("Indietro");
        panel2.add(button1, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel2;
    }

}


