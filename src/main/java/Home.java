import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Home {
    private JFrame HFrame;
    private JPanel panel1;
    private JButton OPERATOREButton;
    private JButton UTENTEButton;
    private JPanel panel;
    private JLabel label;
    private JPasswordField pass;
    private serverCVInterface server;

    public Home() {
        try {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
            HFrame = new JFrame("Home");
            HFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            HFrame.setPreferredSize(new Dimension(500,450));
            HFrame.setResizable(false);
            HFrame.add(panel1);
            HFrame.pack();
            HFrame.setLocationRelativeTo(null);
            HFrame.setVisible(true);
            panel = new JPanel();
            label = new JLabel("Enter a password:");
            pass = new JPasswordField(10);
            panel.add(label);
            panel.add(pass);
            String[] options = new String[]{"OK", "Cancel"};

            UTENTEButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new LoginPage();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            OPERATOREButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int option = JOptionPane.showOptionDialog(null, panel, "The title",
                                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                                null, options, options[1]);
                        if (option == 0) // pressing OK button
                        {
                            char[] password = pass.getPassword();
                            String ps = new String(password);
                            if(!ps.equals("admin")){
                                JOptionPane.showMessageDialog(OPERATOREButton,"Password errata");
                            }
                            else
                                new Operatore();
                        }
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception m) {
            System.out.println("Client err:"+m.getMessage());
            JOptionPane.showMessageDialog(null,"Server offline");
        }
    }

    public static void main(String[] args) {
        new Home();
    }
}
