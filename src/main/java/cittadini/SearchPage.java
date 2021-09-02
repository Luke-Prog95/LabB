package cittadini;

import serverCV.serverCV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.sql.*;

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

    public SearchPage(){
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
        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
            PreparedStatement stm = con.prepareStatement("SELECT Nome,Indirizzo FROM CentriVaccinali");
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                l.addElement(rs.getString(1)+" ("+rs.getString(2)+")");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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
                serverCV s = null;
                try {
                    s = new serverCV();
                    if (nomeText.getText().trim().isEmpty() && comuneText.getText().trim().isEmpty() && tipoText.getText().trim().isEmpty()){
                        JOptionPane.showMessageDialog(cercaButton, "Nessun criterio inserito");
                        lista.setModel(l);
                    }
                    else if (!nomeText.getText().trim().isEmpty() && comuneText.getText().trim().isEmpty() && tipoText.getText().trim().isEmpty())
                        lista.setModel(s.cercaCentroVaccinale(nomeText.getText().trim()));
                    else if (nomeText.getText().trim().isEmpty() && !comuneText.getText().trim().isEmpty() && !tipoText.getText().trim().isEmpty())
                        lista.setModel(s.cercaCentroVaccinale(comuneText.getText().trim(), tipoText.getText().trim()));
                    else JOptionPane.showMessageDialog(cercaButton, "Criteri non adatti");
                } catch (RemoteException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverCV s = null;
                try {
                    s = new serverCV();
                    String[] valore = lista.getSelectedValue().toString().split("\\s\\(");
                    String nome = valore[0];
                    textArea1.setText(s.visualizzaInfoCentroVaccinale(nome));
                } catch (RemoteException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}

