package cittadini;

import serverCV.serverCV;
import serverCV.serverCVInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.Scanner;

public class utenteCV extends JFrame {
    private JTextField userText;
    private JPanel panel1;
    private JPasswordField password;
    private JButton signInButton;
    private JButton logInButton;
    private JButton cercaButton;
    private JFrame frame;
    private Scanner scan;
    private serverCVInterface server;

    public utenteCV() throws SQLException, RemoteException {
        try {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
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
                    } catch (Exception throwables) {
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
        } catch (Exception e) {
            System.out.println("Client err:"+e.getMessage());
            JOptionPane.showMessageDialog(null,"Server offline");
        }
    }

    public static void main(String[] args) throws SQLException, RemoteException {
        new utenteCV();
    }
}
