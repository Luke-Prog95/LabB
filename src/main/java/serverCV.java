import org.postgresql.util.PSQLException;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class serverCV extends UnicastRemoteObject implements serverCVInterface {

    private static Scanner scan = new Scanner(System.in);

    public serverCV() throws RemoteException {
    }

    private static void registraCentroVaccinale() throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
        System.out.print("Nome centro: ");
        String n = scan.next();
        System.out.print("Comune: ");
        String c = scan.next();
        System.out.print("Qualificatore (via,piazza,v.le,...): ");
        String q = scan.next();
        System.out.print(q +": ");
        String v = scan.next();
        System.out.print("Numero civico: ");
        String nc = scan.next();
        System.out.print("Provincia (Sigla): ");
        String p = scan.next();
        System.out.print("CAP: ");
        int cap = scan.nextInt();
        System.out.print("Tipo: ");
        String t = scan.next();
        String indirizzo = q+" "+v+" "+nc+" "+c+" "+p+" "+cap;
        PreparedStatement stmt = con.prepareStatement("INSERT INTO CentriVaccinali (Nome,Indirizzo,Tipologia,Identificatore,Nome_via,Num_civico,Comune,Cap,Provincia) VALUES (?,?,?,?,?,?,?,?,?)");
        stmt.setString(1,n);
        stmt.setString(2,indirizzo);
        stmt.setString(3,t);
        stmt.setString(4,q);
        stmt.setString(5,v);
        stmt.setString(6,nc);
        stmt.setString(7,c);
        stmt.setInt(8,cap);
        stmt.setString(9,p);
        stmt.executeUpdate();
        String nTab = "Vaccinati_"+n;
        String query = "CREATE TABLE "+nTab+"(NomeCentro VARCHAR(100),  \n" +
                "                                 Cittadino VARCHAR(100),\n" +
                "                                 CodiceFiscale VARCHAR(16),\n" +
                "                                 Data_Prima_dose VARCHAR(10), \n" +
                "                                 Vaccino VARCHAR(100), \n" +
                "                                 Identificativo NUMERIC(4), \n" +
                "                                 PRIMARY KEY (NomeCentro),\n" +
                "\t                               FOREIGN KEY (CodiceFiscale) REFERENCES Cittadini_Registrati(CodiceFiscale),\n" +
                "                                 FOREIGN KEY (NomeCentro) REFERENCES CentriVaccinali(Nome)) \n";
        System.out.println(nTab);
        PreparedStatement stmt2 = con.prepareStatement(query);
        stmt2.execute();
        System.out.println("Centro vaccinale registrato");
    }

    private static void registraVaccinato() throws RemoteException, SQLException {
        new RegVaccinato();
        // - manca controllo nome centro vaccinale
        /*Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
            System.out.print("Centro vaccinale: ");
            String n = scan.next();
            System.out.print("Nome e Cognome: ");
            String nc = scan.next();
            System.out.print("Codice fiscale: ");
            String cf = scan.next();
            System.out.print("Data somministrazione vaccino (gg/mm/aaaa): ");
            String data = scan.next();
            System.out.print("Vaccino somministrato (Pfizer, AstraZeneca, Moderna, J&J): ");
            String vac = scan.next();
            System.out.print("ID: ");
            int id = scan.nextInt();
            String nTab = "Vaccinati_"+n;
            //String cercaCF = "SELECT codicefiscale FROM cittadini_registrati ";
            //PreparedStatement stmt = con.prepareStatement(cercaCF);
            String query = "UPDATE "+ nTab + " SET Data_Prima_Dose='"+data+"', Vaccino='"+vac+"', Identificativo="+id+" WHERE CodiceFiscale='"+cf+"'";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.executeUpdate();*/
    }


    @Override
    public DefaultListModel<String> cercaCentroVaccinale(String centro) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
        String query = "SELECT Nome,Indirizzo FROM CentriVaccinali WHERE Nome LIKE '"+"'%"+centro+"%'";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet rs = stm.executeQuery();
        DefaultListModel<String> l = new DefaultListModel<String>();
        while (rs.next()){
            l.addElement(rs.getString(1)+" ("+rs.getString(2)+")");
        }
        return l;
    }

    @Override
    public DefaultListModel<String> cercaCentroVaccinale(String comune, String tipo) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
        String query = "SELECT Nome,Indirizzo FROM CentriVaccinali WHERE Tipologia = '"+tipo+"' AND Comune='"+comune+"'";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet rs = stm.executeQuery();
        DefaultListModel<String> l = new DefaultListModel<String>();
        while (rs.next()){
            l.addElement(rs.getString(1)+" ("+rs.getString(2)+")");
        }
        return l;
    }

    @Override
    public String visualizzaInfoCentroVaccinale(String nome) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
        String risultato = "Nome centro: "+nome+"\nIndirizzo: ";
        PreparedStatement stm2 = con.prepareStatement("SELECT Indirizzo FROM CentriVaccinali WHERE Nome = '"+nome+"'");
        ResultSet rs2 = stm2.executeQuery();
        if(rs2.next())
         risultato += rs2.getString(1)+"\n\nEventi avversi:\n\n" +
                 "Mal di testa          -->   Numero segnalazioni:        Severità media:\n" +
                 "Febbre                 -->   Numero segnalazioni:        Severità media:\n" +
                 "Dolori muscolari  -->   Numero segnalazioni:        Severità media:\n" +
                 "Linfoadenopatia   -->   Numero segnalazioni:        Severità media:\n" +
                 "Tachicardia          -->   Numero segnalazioni:        Severità media:\n" +
                 "Crisi ipertensiva   -->   Numero segnalazioni:        Severità media:";
        return risultato;
    }


    @Override
    public Boolean inserisciEventiAvversi(int id,int malt, int febbre, int dolori, int linfo, int tachi, int crisi) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
        PreparedStatement stm2 = con.prepareStatement("SELECT Identità FROM Sintomi WHERE Identità = '"+id+"'");
        ResultSet rs2 = stm2.executeQuery();
        if(rs2.next()){
            String query2 = "UPDATE Sintomi SET Testa = "+malt+" ,Febbre = "+febbre+" ,Dolori = "+dolori+" ,Linfo = "+linfo+" ,Tachicardia = "+tachi+" ,Crisi = "+crisi+" WHERE Identita = "+id;
            PreparedStatement stm3 = con.prepareStatement(query2);
            stm3.executeUpdate();
        }else {
            String query = "INSERT INTO Sintomi VALUES (?,?,?,?,?,?,?)";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, id);
            stm.setInt(2, malt);
            stm.setInt(3, febbre);
            stm.setInt(4, dolori);
            stm.setInt(5, linfo);
            stm.setInt(6, tachi);
            stm.setInt(7, crisi);
            stm.executeUpdate();
        }
        return true;
    }

    public static void main(String[] args) throws SQLException, RemoteException {
        /*System.out.print("Inserire user database: ");
        String userdb = scan.next();
        System.out.print("Inserire password database: ");
        String passdb = scan.next();
        System.out.print("Inserire host database: ");
        String hostdb = scan.next();*/
        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
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
        } catch (PSQLException ex) {
            if(ex.getSQLState().equals("3D000")){
                System.out.println("Database non esistente\n\nCreazione Database");
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "postgres");
                String createDB = "CREATE DATABASE \"LabB\"\n" +
                        "\tWITH \n" +
                        "\tOWNER = postgres\n" +
                        "\tENCODING = 'UTF8'\n" +
                        "\tLC_COLLATE = 'Italian_Italy.1252'\n" +
                        "\tLC_CTYPE = 'Italian_Italy.1252'\n" +
                        "\tTABLESPACE = pg_default\n" +
                        "\tCONNECTION LIMIT = -1;\n";
                String createCitReg = "CREATE TABLE \"Cittadini_Registrati\" (\n" +
                        "\tnome varchar(20),\n" +
                        "\tcognome varchar(20),\n" +
                        "\tcodicefiscale varchar(20) primary key,\n" +
                        "\temail varchar(100),\n" +
                        "\tusername varchar(20),\n" +
                        "\tpass varchar(20),\n" +
                        "\tidvac numeric\n" +
                        ")";
                PreparedStatement stm = con.prepareStatement(createDB);
                stm.execute();
                /*stm = con.prepareStatement(createCitReg);
                stm.execute();*/
                serverCV.main(args);
            }
            else { ex.printStackTrace(); }
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public ResultSet logCittadino(String user) throws RemoteException, SQLException {
            //try {
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
                PreparedStatement stmt = con.prepareStatement("SELECT Username,Pass FROM Cittadini_Registrati WHERE Username = ?");
                stmt.setString(1, user);
                ResultSet rs = stmt.executeQuery();
                return rs;
            /*} catch (PSQLException ex) {
                if (ex.getSQLState().equals("42P01")) {
                    Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres", "postgres");
                    String createCitReg = "CREATE TABLE \"Cittadini_Registrati\" (\n" +
                            "\tnome varchar(20),\n" +
                            "\tcognome varchar(20),\n" +
                            "\tcodicefiscale varchar(20),\n" +
                            "\temail varchar(100),\n" +
                            "\tusername varchar(20) primary key,\n" +
                            "\tpass varchar(20),\n" +
                            "\tidvac numeric\n" +
                            ")";
                    PreparedStatement stm = stm = con.prepareStatement(createCitReg);
                    stm.executeUpdate();
                }
                return logCittadino(user);
            }*/
    }

    @Override
    public ResultSet registraCittadino(String n,String c,String cf, String em, String u,String p, String nCen) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", "postgres","postgres");
        PreparedStatement stmt = con.prepareStatement("SELECT Username FROM Cittadini_Registrati WHERE Username = ?");
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
            String query1 = "INSERT INTO Vaccinati_"+nCen+" (NomeCentro, Cittadino, CodiceFiscale) VALUES (?,?,?)";
            String utente = n+" "+c;
            PreparedStatement stmt3 = con.prepareStatement(query1);
            stmt3.setString(1, nCen);
            stmt3.setString(2, utente);
            stmt3.setString(3, cf);
            stmt3.executeUpdate();
        }
        return rs;
    }
}
