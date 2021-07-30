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

    private static void registraCentroVaccinale() throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "admin");
        System.out.print("Nome centro: ");
        String n = scan.next();
        System.out.print("Comune: ");
        String c = scan.next();
        System.out.print("Qualificatore (via,piazza,v.le,...): ");
        String q = scan.next();
        System.out.print("Via: ");
        String v = scan.next();
        System.out.print("Numero civico: ");
        String nc = scan.next();
        System.out.print("Provincia (Sigla): ");
        String p = scan.next();
        System.out.print("CAP: ");
        String cap = scan.next();
        System.out.print("Tipo: ");
        String t = scan.next();
        String indirizzo = q+" "+v+" "+nc+" "+c+" "+p+" "+cap;
        PreparedStatement stmt = con.prepareStatement("INSERT INTO CentriVaccinali (Nome,Indirizzo,Tipologia) VALUES (?,?,?)");
        stmt.setString(1,n);
        stmt.setString(2,indirizzo);
        stmt.setString(3,t);
        stmt.executeUpdate();
        String nTab = "Vaccinati_"+n;
        String query = "CREATE TABLE "+nTab+"(Nome VARCHAR(100), Cittadino VARCHAR(256), Codice_Fiscale VARCHAR(16), Data_prima_dose VARCHAR(100), Vaccino VARCHAR(100), Identificativo VARCHAR(100))";
        System.out.println(nTab);
        PreparedStatement stmt2 = con.prepareStatement(query);
        stmt2.execute();
        System.out.println("Centro vaccinale registrato");
    }

    private static void registraVaccinato() throws RemoteException {
        System.out.println("2");
    }

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
                        s.registraCentroVaccinale();
                        break;
                    case 2:
                        s.registraVaccinato();
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
