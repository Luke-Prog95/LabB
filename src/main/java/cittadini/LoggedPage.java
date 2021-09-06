/*
    Limiti Luca 738873 (sede VA)
    Zehhaf Ishak 737763 (sede VA)
    Ferro Paolo 737529 (sede VA)
    Casolo Ginelli Loris 737056 (sede VA)
 */

package cittadini;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import serverCV.serverCVInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;

/**
 * Classe per compilare il form degli eventi avversi avvenuti dopo la vaccinazione
 */
public class LoggedPage extends JFrame {
    private JFrame frame3;
    private JPanel panel4;
    private JCheckBox malDiTestaCheckBox;
    private JCheckBox febbreCheckBox;
    private JCheckBox doloriMuscolariCheckBox;
    private JCheckBox linfoadenopatiaCheckBox;
    private JCheckBox tachicardiaCheckBox;
    private JCheckBox crisiIpertensivaCheckBox;
    private JButton confermaButton;
    private JTextField malTesta;
    private JTextField febbre;
    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;
    private JSlider slider4;
    private JSlider slider5;
    private JSlider slider6;
    private JTextField dolori;
    private JTextField linfo;
    private JTextField tachi;
    private JTextField crisi;
    private JButton backButton;
    private JLabel nomeLabel;
    private JLabel IDLabel;
    private String centroVac;
    private String codf;
    private serverCVInterface server;

    /**
     * Metodo per gestire la connessione al server e impostare la GUI del form degli eventi avversi per un utente vaccinato
     *
     * @param utente username utente che richiede la compilazione del form
     * @param id     ID utente che richiede la compilazione del form
     */
    public LoggedPage(String utente, String id) {
        try {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
            frame3 = new JFrame("Form");
            frame3.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame3.setPreferredSize(new Dimension(550, 450));
            frame3.setResizable(false);
            frame3.add(panel4);
            frame3.pack();
            frame3.setLocationRelativeTo(null);
            frame3.setVisible(true);
            malTesta.setEditable(false);
            febbre.setEditable(false);
            dolori.setEditable(false);
            linfo.setEditable(false);
            tachi.setEditable(false);
            crisi.setEditable(false);
            slider1.setEnabled(false);
            slider2.setEnabled(false);
            slider3.setEnabled(false);
            slider4.setEnabled(false);
            slider5.setEnabled(false);
            slider6.setEnabled(false);
            var c = server.infoCittadinoRegistrato(utente).Clone();
            nomeLabel.setText("Nome: " + c.getString(0) + " " + c.getString(1));
            codf = c.getString(2);
            centroVac = c.getString(3);
            IDLabel.setText("ID: " + id);
            int days = server.controllaRoomDosi(codf, centroVac);

            if (days == -2) {
                frame3.setVisible(false);
                JOptionPane.showMessageDialog(confermaButton, "Non hai ancora effettuato una vaccinazione!");
                new utenteCV();
            }
            if (days >= -1 && days < 14) {
                frame3.setVisible(true);
            } else {
                frame3.setVisible(false);
                JOptionPane.showMessageDialog(confermaButton, "Passati più di 14 giorni per compilare il form!");
                new utenteCV();
            }

            var cx = server.infoReportVaccinato(codf, centroVac);
            slider1.setValue(cx.getInt(0));
            slider2.setValue(cx.getInt(1));
            slider3.setValue(cx.getInt(2));
            slider4.setValue(cx.getInt(3));
            slider5.setValue(cx.getInt(4));
            slider6.setValue(cx.getInt(5));

            var cx1 = server.infoNoteVaccinato(codf, centroVac);
            malTesta.setText(cx1.getString(0));
            febbre.setText(cx1.getString(1));
            dolori.setText(cx1.getString(2));
            linfo.setText(cx1.getString(3));
            tachi.setText(cx1.getString(4));
            crisi.setText(cx1.getString(5));


            malDiTestaCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        slider1.setEnabled(true);
                        malTesta.setEditable(true);
                    } else {
                        slider1.setEnabled(false);
                        malTesta.setEditable(false);
                    }
                    ;
                }
            });

            febbreCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        slider2.setEnabled(true);
                        febbre.setEditable(true);
                    } else {
                        slider2.setEnabled(false);
                        febbre.setEditable(false);
                    }
                    ;
                }
            });

            doloriMuscolariCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        slider3.setEnabled(true);
                        dolori.setEditable(true);
                    } else {
                        slider3.setEnabled(false);
                        dolori.setEditable(false);
                    }
                    ;
                }
            });

            linfoadenopatiaCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        slider4.setEnabled(true);
                        linfo.setEditable(true);
                    } else {
                        slider4.setEnabled(false);
                        linfo.setEditable(false);
                    }
                    ;
                }
            });

            tachicardiaCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        slider5.setEnabled(true);
                        tachi.setEditable(true);
                    } else {
                        slider5.setEnabled(false);
                        tachi.setEditable(false);
                    }
                    ;
                }
            });

            crisiIpertensivaCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        slider6.setEnabled(true);
                        crisi.setEditable(true);
                    } else {
                        slider6.setEnabled(false);
                        crisi.setEditable(false);
                    }
                    ;
                }
            });

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame3.setVisible(false);
                    try {
                        new utenteCV();
                    } catch (SQLException | RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            confermaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String label = IDLabel.getText();
                        String[] ident = label.split(" ");
                        server.inserisciEventiAvversi(Integer.parseInt(ident[1]), slider1.getValue(), slider2.getValue(), slider3.getValue(),
                                slider4.getValue(), slider5.getValue(), slider6.getValue(), codf, centroVac);
                        server.inserisciNote(Integer.parseInt(ident[1]), malTesta.getText(), febbre.getText(), dolori.getText(), linfo.getText(), tachi.getText(), crisi.getText(), codf, centroVac);
                        JOptionPane.showMessageDialog(confermaButton, "Report inviato");
                    } catch (RemoteException | SQLException ex) {
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
        panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(9, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Evento");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Severità (1 to 5)");
        panel1.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Note opzionali (256 caratteri)");
        panel1.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        malDiTestaCheckBox = new JCheckBox();
        malDiTestaCheckBox.setText("Mal di testa");
        panel1.add(malDiTestaCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        febbreCheckBox = new JCheckBox();
        febbreCheckBox.setText(" Febbre");
        panel1.add(febbreCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        doloriMuscolariCheckBox = new JCheckBox();
        doloriMuscolariCheckBox.setText("Dolori muscolari ");
        panel1.add(doloriMuscolariCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        linfoadenopatiaCheckBox = new JCheckBox();
        linfoadenopatiaCheckBox.setText("Linfoadenopatia");
        panel1.add(linfoadenopatiaCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tachicardiaCheckBox = new JCheckBox();
        tachicardiaCheckBox.setText("Tachicardia");
        panel1.add(tachicardiaCheckBox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        crisiIpertensivaCheckBox = new JCheckBox();
        crisiIpertensivaCheckBox.setText("Crisi ipertensiva");
        panel1.add(crisiIpertensivaCheckBox, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        malTesta = new JTextField();
        panel1.add(malTesta, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        febbre = new JTextField();
        panel1.add(febbre, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        slider2 = new JSlider();
        slider2.setMajorTickSpacing(1);
        slider2.setMaximum(5);
        slider2.setMinimum(0);
        slider2.setPaintLabels(true);
        slider2.setPaintTicks(true);
        slider2.setValue(0);
        panel1.add(slider2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slider3 = new JSlider();
        slider3.setMajorTickSpacing(1);
        slider3.setMaximum(5);
        slider3.setMinimum(0);
        slider3.setPaintLabels(true);
        slider3.setPaintTicks(true);
        slider3.setValue(0);
        panel1.add(slider3, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slider4 = new JSlider();
        slider4.setMajorTickSpacing(1);
        slider4.setMaximum(5);
        slider4.setMinimum(0);
        slider4.setPaintLabels(true);
        slider4.setPaintTicks(true);
        slider4.setValue(0);
        panel1.add(slider4, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slider5 = new JSlider();
        slider5.setMajorTickSpacing(1);
        slider5.setMaximum(5);
        slider5.setMinimum(0);
        slider5.setPaintLabels(true);
        slider5.setPaintTicks(true);
        slider5.setPaintTrack(true);
        slider5.setRequestFocusEnabled(true);
        slider5.setValue(0);
        slider5.setVisible(true);
        panel1.add(slider5, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slider6 = new JSlider();
        slider6.setMajorTickSpacing(1);
        slider6.setMaximum(5);
        slider6.setMinimum(0);
        slider6.setPaintLabels(true);
        slider6.setPaintTicks(true);
        slider6.setValue(0);
        panel1.add(slider6, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dolori = new JTextField();
        panel1.add(dolori, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        linfo = new JTextField();
        panel1.add(linfo, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tachi = new JTextField();
        panel1.add(tachi, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        crisi = new JTextField();
        panel1.add(crisi, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        slider1 = new JSlider();
        slider1.setMajorTickSpacing(1);
        slider1.setMaximum(5);
        slider1.setMinimum(0);
        slider1.setMinorTickSpacing(0);
        slider1.setPaintLabels(true);
        slider1.setPaintTicks(true);
        slider1.setValue(0);
        panel1.add(slider1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nomeLabel = new JLabel();
        nomeLabel.setText("           Nome:");
        panel1.add(nomeLabel, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        IDLabel = new JLabel();
        IDLabel.setText("                                          ID:");
        panel1.add(IDLabel, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        confermaButton = new JButton();
        confermaButton.setText("Conferma");
        panel1.add(confermaButton, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Indietro");
        panel1.add(backButton, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel4;
    }

}
