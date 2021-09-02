import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;

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

    public LoggedPage(String utente, String id) throws SQLException, ParseException {
        frame3 = new JFrame("Form");
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "admin");
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
        String qcercanome = "SELECT Nome,Cognome,codicefiscale,centro FROM Cittadini_Registrati WHERE Username = '"+utente+"'";
        PreparedStatement stm = con.prepareStatement(qcercanome);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            nomeLabel.setText("Nome: " + rs.getString("Nome") + " " + rs.getString("Cognome"));
            codf = rs.getString("codicefiscale");
            centroVac = rs.getString("centro");
        }
        IDLabel.setText("ID: "+id);
        PreparedStatement stm1 = con.prepareStatement("SELECT data_prima_dose, data_seconda_dose FROM \"Vaccinati_"+centroVac+"\" WHERE codicefiscale='"+codf+"'");
        ResultSet rs1 = stm1.executeQuery();
        //ToDo Sistemare
        if (rs1.next()){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            Date vac;
            //System.out.println(rs1.getString("data_prima_dose"));
            //System.out.println(rs1.getString("data_seconda_dose"));
            if(rs1.getString("data_prima_dose") == null) {
                frame3.setVisible(false);
                JOptionPane.showMessageDialog(confermaButton, "Non hai ancora effettuato una vaccinazione!");
                new LoginPage();
            } else {
                if (rs1.getString("data_seconda_dose") == null) {
                    vac = sdf.parse(rs1.getString("data_prima_dose"));
                } else {
                    vac = sdf.parse(rs1.getString("data_seconda_dose"));
                }
                long giorniPassati = (today.getTime() - vac.getTime()) / (1000*60*60*24);
                if (giorniPassati>14) {
                    frame3.setVisible(false);
                    JOptionPane.showMessageDialog(confermaButton, "Passati più di 14 giorni per compilare il form!");
                    new LoginPage();
                } else {
                    frame3.setVisible(true);
                }
            }
        }

        String query2 = "SELECT Testa,Febbre,Dolori,Linfo,Tachicardia,Crisi FROM \"Sintomi_"+centroVac+"\" WHERE codicefiscale = '"+codf+"'";
        PreparedStatement stm3 = con.prepareStatement(query2);
        ResultSet rs4 = stm3.executeQuery();
        if(rs4.next()){
            slider1.setValue(rs4.getInt("Testa"));
            slider2.setValue(rs4.getInt("Febbre"));
            slider3.setValue(rs4.getInt("Dolori"));
            slider4.setValue(rs4.getInt("Linfo"));
            slider5.setValue(rs4.getInt("Tachicardia"));
            slider6.setValue(rs4.getInt("Crisi"));
        } else {
            slider1.setValue(0);
            slider2.setValue(0);
            slider3.setValue(0);
            slider4.setValue(0);
            slider5.setValue(0);
            slider6.setValue(0);
        }

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
                    new LoginPage();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    serverCV s = new serverCV();
                    String label = IDLabel.getText();
                    String[] ident = label.split(" ");
                    s.inserisciEventiAvversi(Integer.parseInt(ident[1]),slider1.getValue(),slider2.getValue(),slider3.getValue(),
                            slider4.getValue(),slider5.getValue(),slider6.getValue(), centroVac);
                    JOptionPane.showMessageDialog(confermaButton, "Report inviato");
                } catch (RemoteException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
