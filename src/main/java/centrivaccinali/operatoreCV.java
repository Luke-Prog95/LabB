/*
    Limiti Luca 738873 (sede VA)
    Zehhaf Ishak 737763 (sede VA)
    Ferro Paolo 737529 (sede VA)
    Casolo Ginelli Loris 737056 (sede VA)
 */

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

/**
 * Classe per far scegliere all'operatore vaccinale se aggiungere un nuovo centro vaccinale o confermare la vaccinazione di un vittadino
 */
public class operatoreCV {
    private JFrame oFrame;
    private JButton REGISTRACENTROButton;
    private JPanel panel1;
    private JButton REGISTRAVACCINATOButton;
    private serverCV s;
    private serverCVInterface server;

    /**
     * Metodo per impostare la GUI dell'operatore. Gestisce la connessione al server e la scelta di registrare un vaccinato o un centro vaccinale
     * @throws RemoteException
     */
    public operatoreCV() throws RemoteException {
        try {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
            oFrame = new JFrame("Operatore");
            oFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            oFrame.setPreferredSize(new Dimension(400, 350));
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
            JOptionPane.showMessageDialog(null,"Errore nella connessione o nella lettura dei dati dal server");
        }
    }

    /**
     * Metodo principale per istanziare la GUI per l'operatore vaccinale
     * @throws RemoteException
     */
    public static void main(String[] args) throws RemoteException {
        new operatoreCV();
    }
}

