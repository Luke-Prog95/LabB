import org.postgresql.util.PSQLException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.Scanner;

public class LoginPage extends JFrame {
    private JTextField userText;
    private JPanel panel1;
    private JPasswordField password;
    private JButton signInButton;
    private JButton logInButton;
    private JButton cercaButton;
    private JFrame frame;
    private Scanner scan;
    private serverCVInterface server;

    public LoginPage() throws SQLException {
        try {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
        }  catch (Exception m) {
            System.out.println("Client err:"+m.getMessage());}
        scan = new Scanner(System.in);
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,450));
        frame.setResizable(false);
        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                try {
                    new SignInPage();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    serverCV s = new serverCV();
                    String user = userText.getText();
                    String p = password.getText();
                    ResultSet rs = s.logCittadino(user);
                    if (user.equals("") || p.equals("")) {
                        JOptionPane.showMessageDialog(signInButton, "Campo user o password vuoti!");
                    } else if (rs.next() == false) {
                        JOptionPane.showMessageDialog(logInButton, "Utente Non Registrato!");
                    } else {
                        String pas = rs.getString("Pass");
                        if (pas.equals(p)) {
                            PreparedStatement stm1 = con.prepareStatement("SELECT Idvac FROM Cittadini_Registrati WHERE Username ='"+user+"'");
                            ResultSet rs1 = stm1.executeQuery();
                            if(rs1.next() && (Integer.toString(rs1.getInt(1))!=null)) {
                                JOptionPane.showMessageDialog(logInButton, "Sei Loggato!");
                                frame.setVisible(false);
                                new LoggedPage(user,Integer.toString(rs1.getInt(1)));
                            } else {  //ToDo cambiare ui dopo il log
                                JOptionPane.showMessageDialog(logInButton, "Utente: "+user+"\nNon hai ancora effettuato il vaccino!\nNon è possibile compilare il report!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(logInButton, "Password errata!");
                        }
                    }
                } /*catch (SQLException ex){
                    if (ex.getSQLState().equals("42P01")) {
                        String createCitReg = "CREATE TABLE \"Cittadini_Registrati\" (\n" +
                                "\tnome varchar(20),\n" +
                                "\tcognome varchar(20),\n" +
                                "\tcodicefiscale varchar(20),\n" +
                                "\temail varchar(100),\n" +
                                "\tusername varchar(20) primary key,\n" +
                                "\tpass varchar(20),\n" +
                                "\tidvac numeric\n" +
                                ")";
                        PreparedStatement stm = null;
                        try {
                            stm = con.prepareStatement(createCitReg);
                            stm.executeUpdate();
                        } catch (SQLException ex1) {
                            ex1.printStackTrace();
                        }
                        System.out.println("Tabella creata");
                    }
                } */catch (Exception throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        cercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    frame.setVisible(false);
                    new SearchPage();
            }
        });
    }
}
