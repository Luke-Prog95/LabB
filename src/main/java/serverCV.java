import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class serverCV extends UnicastRemoteObject implements serverCVInterface {

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
    public Boolean registraCittadino() throws RemoteException{
        return null;
    }

    @Override
    public Boolean inserisciEventiAvversi() throws RemoteException{
        return null;
    }

    public static void main(String[] args) throws SQLException, RemoteException {
        Scanner scan = new Scanner(System.in);
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
        } catch (Exception e) {e.printStackTrace();}

        /*System.out.println("\nCosa vuoi fare? \n1 - Registra centro vaccinale\n2 - Registra Vaccinato");
        int r = scan.nextInt();
        if (r==1) serverCV.registraCentroVaccinale();
        else if (r==2) registraVaccinato();
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

}
