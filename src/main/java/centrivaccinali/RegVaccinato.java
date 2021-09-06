/*
    Limiti Luca 738873 (sede VA)
    Zehhaf Ishak 737763 (sede VA)
    Ferro Paolo (sede VA)
 */

package centrivaccinali;

import serverCV.serverCVInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class RegVaccinato extends JFrame{

    private JPanel panel1;
    private JTextField cf;
    private JTextField data;
    private JComboBox centriVacc;
    private JComboBox vaccini;
    private JButton conferma;
    private JCheckBox secondaDose;
    private JFrame frame;
    private Scanner scan;
    private String[] vaxList = {"Pfizer", "AstraZeneca", "Moderna", "J&J"};
    private serverCVInterface server;
    public RegVaccinato() throws SQLException
    {
        try
        {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
            scan = new Scanner(System.in);
            frame = new JFrame("Registra Vaccinato");
            frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            frame.setPreferredSize(new Dimension(500,450));
            frame.setResizable(false);
            frame.add(panel1);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            try
            {
                var c = server.listaCentriVaccinali().Clone();
                var obj = (ArrayList<String>) c.getObject(0);
                for(int i = 0; i < obj.size();i++)
                {
                    centriVacc.addItem(obj.get(i));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            String hint = sdf.format(today);
            data.setText(hint);
            for (int i=0; i<vaxList.length; i++){
                vaccini.addItem(vaxList[i]);
            }

            conferma.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(cf.getText().equals("") || cf.getText().trim().length()!=16 || data.getText().length()!=10) JOptionPane.showMessageDialog(conferma,"Codice fiscale errato o data non corretta");
                    else {
                        try
                        {
                            if (!server.esisteCodiceFiscale(cf.getText().trim()))
                                JOptionPane.showMessageDialog(conferma, "Codice fiscale non trovato!");
                            else if(!server.codiceFiscaleRegistratoInCentro(cf.getText().trim(),centriVacc.getSelectedItem().toString()))
                            {
                                JOptionPane.showMessageDialog(conferma, "Attenzione, l'utente non è registrato in questo centro!");
                            }
                            else
                            {
                                if(!secondaDose.isSelected() || (secondaDose.isSelected() && server.checkData(data.getText(),centriVacc.getSelectedItem().toString(), cf.getText()))){
                                server.registraVaccinato(centriVacc.getSelectedItem().toString(), cf.getText(), data.getText(), vaccini.getSelectedItem().toString(), secondaDose.isSelected());
                                JOptionPane.showMessageDialog(conferma, "Utente vaccinato");
                                frame.setVisible(false);}
                                else JOptionPane.showMessageDialog(null,"Data seconda dose non valida!");
                            }
                        } catch (SQLException | RemoteException | ParseException throwables) {
                            throwables.printStackTrace();

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

