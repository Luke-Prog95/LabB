import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
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
    private ImageIcon vIcon;
    private serverCVInterface server;

    public LoginPage(){
        try {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
            System.out.println("Client connesso");
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

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new SignInPage();
            }
        });

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String user = userText.getText();
                    String p = password.getText();
                    Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "admin");
                    PreparedStatement stmt = con.prepareStatement("SELECT Username,Pass FROM Cittadini_Registrati WHERE username = ?");
                    stmt.setString(1,user);
                    ResultSet rs = stmt.executeQuery();
                    if(user.equals("") || p.equals(""))
                    {JOptionPane.showMessageDialog(signInButton,"Campo user o password vuoti!");}
                    else if(rs.next() == false){
                        JOptionPane.showMessageDialog(logInButton,"Utente Non Registrato!");
                    } else{
                        String pas = rs.getString("Pass");
                        if(pas.equals(p)){
                            JOptionPane.showMessageDialog(logInButton,"Sei Loggato!");
                            frame.setVisible(false);
                            new LoggedPage();
                        }
                        else{JOptionPane.showMessageDialog(logInButton,"Password errata!");}
                    }

                }catch(Exception throwables) {
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
