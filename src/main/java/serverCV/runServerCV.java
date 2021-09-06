package serverCV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class runServerCV extends JFrame {
    private JButton spegniServer;
    private JPanel panel1;
    private JLabel label;
    private JLabel label1;
    private JFrame frame;

    public runServerCV() throws RemoteException, AlreadyBoundException {
        frame = new JFrame("Server");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(250, 150));
        frame.setResizable(false);
        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        label.setText("Server connesso al database!");
        label1.setText("In attesa di client!");
        serverCV s = new serverCV();
        Registry registro = LocateRegistry.createRegistry(1099);
        registro.bind("serverCV",s);

        spegniServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = JOptionPane.showConfirmDialog(null,"Sicuro di voler chiudere il server?","Chiudere",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
                if (x==0) System.exit(0);
            }
        });
    }

}
