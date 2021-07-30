import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class serverCV extends UnicastRemoteObject implements serverCVInterface {

    private static Scanner scan = new Scanner(System.in);

    public serverCV() throws RemoteException {
    }

    private static void registraCentroVaccinale() throws RemoteException {
        System.out.println("1");
    }

    private static void registraVaccinato() throws RemoteException {
        System.out.println("2");
    }

    @Override
    public List<String> cercaCentroVaccinale(String centro) throws RemoteException{
        return null;
    }

    @Override
    public List<String> cercaCentroVaccinale(String comune, String tipo) throws RemoteException{
        return null;
    }

    @Override
    public String visualizzaInfoCentroVaccinale() throws RemoteException{
        return null;
    }


    @Override
    public Boolean inserisciEventiAvversi() throws RemoteException{
        return null;
    }

    public static void main(String[] args) throws SQLException, RemoteException {
        /*System.out.print("Inserire user database: ");
        String userdb = scan.next();
        System.out.print("Inserire password database: ");
        String passdb = scan.next();
        System.out.print("Inserire host database: ");
        String hostdb = scan.next();*/
        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "admin");
            System.out.println("\nServer connesso al database");
            Thread.sleep(1000);
            System.out.println("\n... Tentativo di connessione al registry ...");
            Thread.sleep(3000);
            serverCV s = new serverCV();
            Registry registro = LocateRegistry.createRegistry(1099);
            registro.bind("serverCV",s);
            System.out.println("\nServer ready in attesa di client");
            Thread.sleep(1000);
            while(true) {
                System.out.println("\nCosa vuoi fare? \n1 - Registra centro vaccinale\n2 - Registra Vaccinato\n3 - Chiudi programma");
                System.out.print("Scelta: ");
                int r = scan.nextInt();
                switch (r) {
                    case 1:
                        serverCV.registraCentroVaccinale();
                        break;
                    case 2:
                        registraVaccinato();
                        break;
                    case 3:
                        System.out.println("Chiusura programma");
                        Thread.sleep(3000);
                        System.exit(0);
                        break;
                    default: System.out.println("Scelta non valida");
                }
            }
        } catch (Exception e) {e.printStackTrace();}

        /*
        Per registrare un cittadino dopo la vaccinazione, tramite la funzione inserire:
          nome centro vaccinale
          nome e cognome del cittadino
          codice fiscale
          data somministrazione vaccino (formato: gg/mm/aaaa)
          vaccino somministrato (Pfizer, AstraZeneca, Moderna, J&J)
          id univoco vaccinazione (id numerico su 16 bit)
          I dati di ogni cittadino vaccinato sono memorizzati su DB in una Tabella denominata Vaccinati_NomeCentroVaccinale dove
          NomeCentroVaccinale deve essere sostituito dinamicamente dal nome del centro vaccinale*/
    }

    @Override
    public ResultSet logCittadino(String user) throws RemoteException, SQLException {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "admin");
            PreparedStatement stmt = con.prepareStatement("SELECT Username,Pass FROM Cittadini_Registrati WHERE username = ?");
            stmt.setString(1,user);
            ResultSet rs = stmt.executeQuery();
            return rs;
    }

    @Override
    public ResultSet registraCittadino(String n,String c,String cf, String em, String u,String p) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres","admin");
        PreparedStatement stmt = con.prepareStatement("SELECT username FROM Cittadini_Registrati WHERE Username = ?");
        stmt.setString(1,u);
        ResultSet rs = stmt.executeQuery();
        if(rs.next() == false){
            String query = "INSERT INTO Cittadini_Registrati (Nome,Cognome,CodiceFiscale,Email,Username,Pass) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt2 = con.prepareStatement(query);
            stmt2.setString(1, n);
            stmt2.setString(2, c);
            stmt2.setString(3, cf);
            stmt2.setString(4, em);
            stmt2.setString(5, u);
            stmt2.setString(6, p);
            stmt2.executeUpdate();
        }
        return rs;
    }
}
