package centrivaccinali;

import serverCV.serverCV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class RegVaccinato extends JFrame{

    private JPanel panel1;
    private JTextField cf;
    private JTextField data;
    private JComboBox centriVacc;
    private JComboBox vaccini;
    private JButton conferma;
    private JCheckBox secondaDose;
    private JFrame frame;
    private Scanner scan;
    private String[] vaxList = {"Pfizer", "AstraZeneca", "Moderna", "J&J"};
    private serverCV s;

    public RegVaccinato() throws SQLException {
        scan = new Scanner(System.in);
        frame = new JFrame("Registra Vaccinato");
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,450));
        frame.setResizable(false);
        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
        try {
            PreparedStatement stm = con.prepareStatement("SELECT Nome FROM CentriVaccinali");
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                centriVacc.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        String hint = sdf.format(today);
        data.setText(hint);
        for (int i=0; i<vaxList.length; i++){
            vaccini.addItem(vaxList[i]);
        }

        conferma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cf.getText().equals("") || cf.getText().trim().length()!=16 || data.getText().length()!=10) JOptionPane.showMessageDialog(conferma,"Codice fiscale errato o data non corretta");
                else {
                    try {
                        s = new serverCV();
                        PreparedStatement ps = con.prepareStatement("SELECT * FROM cittadini_registrati WHERE codicefiscale ='" + cf.getText() + "'");
                        ResultSet rs = ps.executeQuery();
                        if (!rs.next())
                            JOptionPane.showMessageDialog(conferma, "Codice fiscale non trovato!");
                        else {
                            s.registraVaccinato(centriVacc.getSelectedItem().toString(), cf.getText(), data.getText(), vaccini.getSelectedItem().toString(), secondaDose.isSelected());
                            JOptionPane.showMessageDialog(conferma, "Utente vaccinato");
                        }
                    } catch (SQLException | RemoteException throwables) {
                        throwables.printStackTrace();

                    }
                }
            }
        });
    }
}

