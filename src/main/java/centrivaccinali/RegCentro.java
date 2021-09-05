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
            cenFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
                    if(nome.getText().trim().equals("") || com.getText().trim().equals("") || qual.getText().trim().equals("")  || nc.getText().trim().equals("") || prov.getText().trim().equals("") || ind.getText().trim().equals("") || capT.getText().trim().equals("")
                            || capT.getText().trim().length()!=5 || prov.getText().trim().length()!=2 || (!hubRadioButton.isSelected() && !aziendaleRadioButton.isSelected() && !ospedalieroRadioButton.isSelected()) || !isNumber(capT.getText().trim(),capT.getText().trim().length()))
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
                            if (server.registraCentroVaccinale(nome.getText(), com.getText(), qual.getText(), nc.getText(), prov.getText(), ind.getText(), tipo, cap)){
                                JOptionPane.showMessageDialog(registraButton, "Centro registrato");
                                cenFrame.setVisible(false);
                            }
                            else JOptionPane.showMessageDialog(registraButton, "Esiste già un centro con questo nome");
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

    public static boolean isNumber(String str, int l)
    {
        boolean res=true;
        for (int i = 0; i < l; i++) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                res = true;
            }
            else {
                res = false;
                break;
            }
        }
        return res;
    }


}
