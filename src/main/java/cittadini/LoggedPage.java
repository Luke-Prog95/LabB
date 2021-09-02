package cittadini;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private JTextField textField1;
    private JTextField textField2;
    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;
    private JSlider slider4;
    private JSlider slider5;
    private JSlider slider6;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton backButton;
    private JLabel nomeLabel;
    private JLabel IDLabel;
    private String centroVac;
    private String codf;
    private serverCVInterface server;
    public LoggedPage(String utente, String id)
    {
        try
        {
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
            textField1.setEditable(false);
            textField2.setEditable(false);
            textField3.setEditable(false);
            textField4.setEditable(false);
            textField5.setEditable(false);
            textField6.setEditable(false);
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
            IDLabel.setText("ID: "+id);
            int days = server.controllaRoomDosi(codf,centroVac);

            if(days == -2)
            {
                frame3.setVisible(false);
                JOptionPane.showMessageDialog(confermaButton, "Non hai ancora effettuato una vaccinazione!");
                new utenteCV();
            }
            if(days >= -1 && days < 14)
            {
                frame3.setVisible(true);
            }
            else
            {
                frame3.setVisible(false);
                JOptionPane.showMessageDialog(confermaButton, "Passati più di 14 giorni per compilare il form!");
                new utenteCV();
            }

            var cx = server.infoReportVaccinato(codf,centroVac);
            slider1.setValue(cx.getInt(0));
            slider2.setValue(cx.getInt(1));
            slider3.setValue(cx.getInt(2));
            slider4.setValue(cx.getInt(3));
            slider5.setValue(cx.getInt(4));
            slider6.setValue(cx.getInt(5));


            malDiTestaCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED) {
                        slider1.setEnabled(true);
                        textField1.setEditable(true);
                    } else {
                        slider1.setEnabled(false);
                        textField1.setEditable(false);
                    };
                }
            });

            febbreCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED) {
                        slider2.setEnabled(true);
                        textField2.setEditable(true);
                    } else {
                        slider2.setEnabled(false);
                        textField2.setEditable(false);
                    };
                }
            });

            doloriMuscolariCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED) {
                        slider3.setEnabled(true);
                        textField3.setEditable(true);
                    } else {
                        slider3.setEnabled(false);
                        textField3.setEditable(false);
                    };
                }
            });

            linfoadenopatiaCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED) {
                        slider4.setEnabled(true);
                        textField4.setEditable(true);
                    } else {
                        slider4.setEnabled(false);
                        textField4.setEditable(false);
                    };
                }
            });

            tachicardiaCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED) {
                        slider5.setEnabled(true);
                        textField5.setEditable(true);
                    } else {
                        slider5.setEnabled(false);
                        textField5.setEditable(false);
                    };
                }
            });

            crisiIpertensivaCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED) {
                        slider6.setEnabled(true);
                        textField6.setEditable(true);
                    } else {
                        slider6.setEnabled(false);
                        textField6.setEditable(false);
                    };
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
                        server.inserisciEventiAvversi(Integer.parseInt(ident[1]),slider1.getValue(),slider2.getValue(),slider3.getValue(),
                                slider4.getValue(),slider5.getValue(),slider6.getValue(),codf,centroVac);
                        JOptionPane.showMessageDialog(confermaButton, "Report inviato");
                    } catch (RemoteException | SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            System.out.println("Client err:"+e.getMessage());
            JOptionPane.showMessageDialog(null,"Errore nella connessione o nella lettura dei dati dal server");
            System.exit(1);
        }

    }
}
