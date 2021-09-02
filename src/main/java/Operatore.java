import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class Operatore {
    private JFrame oFrame;
    private JButton REGISTRACENTROButton;
    private JPanel panel1;
    private JButton REGISTRAVACCINATOButton;
    private serverCV s;

    public Operatore() throws RemoteException {
        oFrame = new JFrame("Operatore");
        oFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        oFrame.setPreferredSize(new Dimension(500,450));
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
                    s.registraVaccinato();
                } catch (SQLException | RemoteException ex) {
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
    }
}

