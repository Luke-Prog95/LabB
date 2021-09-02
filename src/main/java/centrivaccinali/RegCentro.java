package centrivaccinali;

import serverCV.serverCVInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class RegCentro {
    private JFrame cenFrame;
    private JTextField nome;
    private JTextField com;
    private JTextField qual;
    private JTextField ind;
    private JTextField nc;
    private JTextField prov;
    private JTextField capT;
    private JRadioButton hubRadioButton;
    private JRadioButton aziendaleRadioButton;
    private JRadioButton ospedalieroRadioButton;
    private ButtonGroup buttonGroup;
    private JButton registraButton;
    private JPanel panel;
    private String tipo = "";
    private serverCVInterface server;

    public RegCentro() throws RemoteException
    {
        try
        {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
            cenFrame = new JFrame("RegCentro");
            cenFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            cenFrame.setPreferredSize(new Dimension(500, 450));
            cenFrame.setResizable(false);
            cenFrame.add(panel);
            cenFrame.pack();
            cenFrame.setLocationRelativeTo(null);
            cenFrame.setVisible(true);
            buttonGroup = new ButtonGroup();
            buttonGroup.add(hubRadioButton);
            buttonGroup.add(aziendaleRadioButton);
            buttonGroup.add(ospedalieroRadioButton);

            registraButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(nome.equals("") || com.equals("") || qual.equals("")  || nc.equals("") || prov.equals("") || ind.equals("") || capT.equals("")
                            || capT.getText().length()!=5 || prov.getText().length()!=2)
                        JOptionPane.showMessageDialog(registraButton, "Dati mancanti o errati!");
                    else {
                        if (hubRadioButton.isSelected())
                            tipo = hubRadioButton.getText();
                        else if (aziendaleRadioButton.isSelected())
                            tipo = aziendaleRadioButton.getText();
                        else
                            tipo = ospedalieroRadioButton.getText();
                        try {
                            int cap = Integer.parseInt(capT.getText());
                            server.registraCentroVaccinale(nome.getText(), com.getText(), qual.getText(), nc.getText(), prov.getText(), ind.getText(), tipo, cap);
                            JOptionPane.showMessageDialog(registraButton, "Centro registrato");
                        } catch (RemoteException | SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
        catch (Exception e)
        {
            System.out.println("Client err:"+e.getMessage());
            JOptionPane.showMessageDialog(null,"Errore nella connessione o nella lettura dei dati dal server");
            System.exit(1);
        }

    }
}
