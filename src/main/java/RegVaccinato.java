import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
    private serverCVInterface server;

    public RegVaccinato() throws SQLException {
        try {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
        }  catch (Exception m) {
            System.out.println("Client err:"+m.getMessage());}
        scan = new Scanner(System.in);
        frame = new JFrame("Registra Vaccinato");
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,450));
        frame.setResizable(false);
        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
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
                String codf = cf.getText();
                if(codf.equals("")) JOptionPane.showMessageDialog(conferma,"Inserire codice fiscale!");
                try {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM cittadini_registrati WHERE codicefiscale ='"+codf+"'");
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next())
                        JOptionPane.showMessageDialog(conferma,"Codice fiscale non trovato!");
                    else {
                        String centro = (String) centriVacc.getSelectedItem();
                        String d = data.getText();
                        String vac = (String) vaccini.getSelectedItem();
                        if (secondaDose.isSelected()){
                            PreparedStatement ps1 = con.prepareStatement("UPDATE \"Vaccinati_" + centro + "\" SET Data_Seconda_Dose='" + d + "' WHERE CodiceFiscale='" + codf + "'");
                            ps1.executeUpdate();
                        } else {
                            PreparedStatement ps2 = con.prepareStatement("UPDATE \"Vaccinati_" + centro + "\" SET Data_Prima_Dose='" + d + "', vaccino='" + vac + "' WHERE CodiceFiscale='" + codf + "'");
                            ps2.executeUpdate();
                        }
                        frame.setVisible(false);
                        frame.dispose();
                    }
                } catch (SQLException throwables) {
                    System.out.println(throwables.getSQLState());
                    throwables.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws SQLException {
        new RegVaccinato();
    }
}

