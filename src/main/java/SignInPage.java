import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;

public class SignInPage extends JFrame {
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
    private ArrayList<String> l;

    public SignInPage() throws SQLException {
        frame1 = new JFrame("SignIn");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setPreferredSize(new Dimension(500,450));
        frame1.setResizable(false);
        frame1.add(panel2);
        frame1.pack();
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);

        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "qwerty");
        PreparedStatement stm = con.prepareStatement("SELECT Nome FROM CentriVaccinali");
        PreparedStatement stm2 = con.prepareStatement("SELECT Indirizzo FROM CentriVaccinali");
        ResultSet rs = stm.executeQuery();
        ResultSet rs2 = stm2.executeQuery();
        while(rs.next() && rs2.next()){
            String indir = rs2.getString(1);
            comboBox1.addItem(rs.getString(1)+" ("+indir+")");
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
                    String cen = (String) comboBox1.getSelectedItem();
                    String[] nomeC = cen.split(" ");
                    serverCV sv = new serverCV();
                    ResultSet rs = sv.registraCittadino(n,c,cf,em,user,p,nomeC[0]);
                    if(user.trim().equals("") || p.trim().equals("") || n.trim().equals("")  || c.trim().equals("") || cf.trim().equals("") || em.trim().equals(""))
                    {JOptionPane.showMessageDialog(signInButton,"uno dei campi è vuoto!");}
                    else if(rs.next() == false){
                        JOptionPane.showMessageDialog(signInButton,"Utente Registrato!");
                        frame1.setVisible(false);
                        new LoginPage();
                    }
                    else{
                        JOptionPane.showMessageDialog(signInButton,"Nome utente già esistente!");}
                } catch (SQLException | RemoteException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame1.setVisible(false);
                try {
                    new LoginPage();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}


