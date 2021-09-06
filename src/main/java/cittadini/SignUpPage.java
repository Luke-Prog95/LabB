/*
    Limiti Luca 738873 (sede VA)
    Zehhaf Ishak 737763 (sede VA)
    Ferro Paolo 737529 (sede VA)
    Casolo Ginelli Loris 737056 (sede VA)
 */

package cittadini;

import serverCV.serverCVInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.ArrayList;

/**
 * Classe per la registrazione di un nuovo utente con l'inserimento di tutte le sue informazioni e della scelta di un centro vaccinale per eseguire la vaccinazione
 */
public class SignUpPage extends JFrame {
    private JPanel panel2;
    private JFrame frame1;
    private JTextField Name;
    private JTextField Surname;
    private JTextField CodF;
    private JTextField Email;
    private JTextField password;
    private JButton signInButton;
    private JTextField userText;
    private JComboBox comboBox1; //implementare scelta centro dove registrarsi
    private JButton button1;
    private serverCVInterface server;

    /**
     * Classe per gestire la connessione al server e impostare la GUI del form di registrazione di un nuovo utente con controlli sugli inserimenti delle informazioni
     * @throws SQLException
     */
    public SignUpPage() throws SQLException {
        try {
            Registry reg = LocateRegistry.getRegistry();
            server = (serverCVInterface) reg.lookup("serverCV");
            frame1 = new JFrame("SignUp");
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame1.setPreferredSize(new Dimension(500,450));
            frame1.setResizable(false);
            frame1.add(panel2);
            frame1.pack();
            frame1.setLocationRelativeTo(null);
            frame1.setVisible(true);
            var c = server.listaCentriVaccinali().Clone();
            var cobj = (ArrayList<String>)c.getObject(0);
            var cobjadd = (ArrayList<String>)c.getObject(1);
            for(int i = 0; i < cobj.size(); i++)
            {
                comboBox1.addItem(cobj.get(i)+" ("+cobjadd.get(i)+")");
            }


            signInButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Class.forName("org.postgresql.Driver");
                    } catch (ClassNotFoundException c) {
                        System.out.println("Class not found " + c);
                    }
                    try {
                        String user = userText.getText();
                        String p = password.getText();
                        String n = Name.getText();
                        String c = Surname.getText();
                        String cf = CodF.getText();
                        String em = Email.getText();
                        String nomeC = cobj.get(comboBox1.getSelectedIndex());
                        if(user.trim().equals("") || p.trim().equals("") || n.trim().equals("")  || c.trim().equals("") || cf.trim().equals("") || em.trim().equals("")) {
                            JOptionPane.showMessageDialog(signInButton,"Uno dei campi è vuoto!", "Errore!", JOptionPane.ERROR_MESSAGE);
                        }
                        else if(cf.trim().length() != 16)
                        {
                            JOptionPane.showMessageDialog(signInButton,"Codice fiscale errato", "Errore!", JOptionPane.ERROR_MESSAGE);
                        }
                        else if(server.registraCittadino(n,c,cf,em,user,p,nomeC))
                        {
                            JOptionPane.showMessageDialog(signInButton,"Utente Registrato!");
                            frame1.setVisible(false);
                            new utenteCV();
                        }
                        else{
                            JOptionPane.showMessageDialog(signInButton,"Errore nella registrazione!", "Errore!", JOptionPane.ERROR_MESSAGE);}
                    } catch (SQLException | RemoteException throwables) {
                        throwables.printStackTrace();
                    } catch (java.lang.NullPointerException ex) {
                        JOptionPane.showMessageDialog(signInButton,"Username già utilizzato!", "Errore!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame1.setVisible(false);
                    try {
                        new utenteCV();
                    } catch (SQLException | RemoteException ex) {
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


