import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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
    private JComboBox comboBox1;

    public SignInPage(){
        frame1 = new JFrame("SignIn");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setPreferredSize(new Dimension(500,450));
        frame1.setResizable(false);
        frame1.add(panel2);
        frame1.pack();
        frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException c) {
                    System.out.println("Class not found " + c);
                }
                try {
                    Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Login", "postgres","admin");
                    String user = userText.getText();
                    String p = password.getText();
                    String n = Name.getText();
                    String c = Surname.getText();
                    String cf = CodF.getText();
                    String em = Email.getText();
                    PreparedStatement stmt = con.prepareStatement("SELECT username FROM Cittadini_Registrati WHERE Username = ?");
                    stmt.setString(1,user);
                    ResultSet rs = stmt.executeQuery();
                    if(user.trim().equals("") || p.trim().equals("") || n.trim().equals("")  || c.trim().equals("") || cf.trim().equals("") || em.trim().equals(""))
                    {JOptionPane.showMessageDialog(signInButton,"uno dei campi è vuoto!");}
                    else if(rs.next() == false){
                        String query = "INSERT INTO Cittadini_Registrati (Nome,Cognome,CodiceFiscale,Email,Username,Pass) VALUES (?,?,?,?,?,?)";
                        PreparedStatement stmt2 = con.prepareStatement(query);
                        stmt2.setString(1, n);
                        stmt2.setString(2, c);
                        stmt2.setString(3, cf);
                        stmt2.setString(4, em);
                        stmt2.setString(5, user);
                        stmt2.setString(6, p);
                        stmt2.executeUpdate();
                        JOptionPane.showMessageDialog(signInButton,"Utente Registrato!");
                        frame1.setVisible(false);
                        new LoginPage();
                    }
                    else{
                        JOptionPane.showMessageDialog(signInButton,"Nome utente già esistente!");}
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }
}
