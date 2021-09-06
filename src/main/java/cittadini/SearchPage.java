/*
    Limiti Luca 738873 (sede VA)
    Zehhaf Ishak 737763 (sede VA)
    Ferro Paolo (sede VA)
 */

package cittadini;

import serverCV.serverCVInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.ArrayList;

public class SearchPage extends JFrame {
    private JPanel panel1;
    private JTextArea textArea1;
    private JButton backButton;
    private JButton cercaButton;
    private JList lista;
    private JTextField nomeText;
    private JTextField comuneText;
    private JTextField tipoText;
    private JButton infoButton;
    private JFrame frame;
    private serverCVInterface server;

    public SearchPage()
    {
        try
        {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
            frame = new JFrame("Search");
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(500, 450));
            frame.setResizable(false);

            frame.add(panel1);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            lista.setVisibleRowCount(8);
            DefaultListModel<String> l = new DefaultListModel<String>();

            var c = server.listaCentriVaccinali().Clone();
            var cobj = (ArrayList<String>)c.getObject(0);
            var cobjadd = (ArrayList<String>)c.getObject(1);
            for(int i = 0; i < cobj.size(); i++)
            {
                l.addElement(cobj.get(i)+" ("+cobjadd.get(i)+")");
            }
            lista.setModel(l);

            backButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    frame.setVisible(false);
                    try {
                        new utenteCV();
                    } catch (SQLException | RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            cercaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (nomeText.getText().trim().isEmpty() && comuneText.getText().trim().isEmpty() && tipoText.getText().trim().isEmpty()){
                            JOptionPane.showMessageDialog(cercaButton, "Nessun criterio inserito");
                            lista.setModel(l);
                        }
                        else if (!nomeText.getText().trim().isEmpty() && comuneText.getText().trim().isEmpty() && tipoText.getText().trim().isEmpty())
                            lista.setModel(server.cercaCentroVaccinale(nomeText.getText().trim()));
                        else if (nomeText.getText().trim().isEmpty() && !comuneText.getText().trim().isEmpty() && !tipoText.getText().trim().isEmpty())
                            lista.setModel(server.cercaCentroVaccinale(comuneText.getText().trim(), tipoText.getText().trim()));
                        else JOptionPane.showMessageDialog(cercaButton, "Criteri non adatti");
                    } catch (RemoteException | SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            infoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String[] valore = lista.getSelectedValue().toString().split("\\s\\(");
                        String nome = valore[0];
                        textArea1.setText(server.visualizzaInfoCentroVaccinale(nome));
                    } catch (RemoteException | SQLException ex) {
                        ex.printStackTrace();
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

