package centrivaccinali;

import serverCV.serverCV;
import serverCV.serverCVInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class operatoreCV {
    private JFrame oFrame;
    private JButton REGISTRACENTROButton;
    private JPanel panel1;
    private JButton REGISTRAVACCINATOButton;
    private serverCV s;
    private serverCVInterface server;

    public operatoreCV() throws RemoteException {
        try {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
            oFrame = new JFrame("Operatore");
            oFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            oFrame.setPreferredSize(new Dimension(500, 450));
            oFrame.setResizable(false);
            oFrame.add(panel1);
            oFrame.pack();
            oFrame.setLocationRelativeTo(null);
            oFrame.setVisible(true);
            s = new serverCV();

            REGISTRAVACCINATOButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new RegVaccinato();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            REGISTRACENTROButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        new RegCentro();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Client err:"+e.getMessage());
            JOptionPane.showMessageDialog(null,"Server offline");
        }
    }

    public static void main(String[] args) throws RemoteException {
        new operatoreCV();
    }
}

