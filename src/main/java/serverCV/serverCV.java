package serverCV;

import centrivaccinali.RegVaccinato;
import cittadini.Container;
import org.postgresql.util.PSQLException;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class serverCV extends UnicastRemoteObject implements serverCVInterface {

    private static Scanner scan = new Scanner(System.in).useDelimiter("\\n"); //Aggiunto altrimenti con lo spazio saltava al prossimo scan
    public static String mUsername = "";
    public static String mPassword = "";
    public serverCV() throws RemoteException {
    }

    public void registraCentroVaccinale(String nome, String comune, String qualif, String numCiv, String prov, String via, String tipo, int cap) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        String indirizzo = qualif+" "+via+" "+numCiv+" "+comune+" "+"("+prov+")"+" "+cap;
        PreparedStatement stmt = con.prepareStatement("INSERT INTO CentriVaccinali (Nome,Indirizzo,Tipologia,Identificatore,Nome_via,Num_civico,Comune,Cap,Provincia) VALUES (?,?,?,?,?,?,?,?,?)");
        stmt.setString(1,nome);
        stmt.setString(2,indirizzo);
        stmt.setString(3,tipo);
        stmt.setString(4,qualif);
        stmt.setString(5,via);
        stmt.setString(6,numCiv);
        stmt.setString(7,comune);
        stmt.setInt(8,cap);
        stmt.setString(9,prov);
        stmt.executeUpdate();
        String query = "CREATE TABLE \"Vaccinati_"+nome+"\"(Cittadino VARCHAR(100),\n" +
                "                                 CodiceFiscale VARCHAR(16),\n" +
                "                                 Data_Prima_dose VARCHAR(10), \n" +
                "                                 Data_Seconda_dose VARCHAR(10), \n" +
                "                                 Vaccino VARCHAR(100), \n" +
                "                                 PRIMARY KEY (CodiceFiscale),\n" +
                "                                 FOREIGN KEY (CodiceFiscale) REFERENCES Cittadini_Registrati(CodiceFiscale));\n";
        String query1 = "CREATE TABLE \"Sintomi_"+nome+"\"(CodiceFiscale VARCHAR(16),  \n" +
                "                                 Testa NUMERIC(1),\n" +
                "                                 Febbre NUMERIC(1),\n" +
                "                                 Dolori NUMERIC(1), \n" +
                "                                 Linfo NUMERIC(1), \n" +
                "                                 Tachicardia NUMERIC(1), \n" +
                "                                 Crisi NUMERIC(1), \n" +
                "                                 TestaNote VARCHAR(256) , \n" +
                "                                 FebbreNote VARCHAR(256) , \n" +
                "                                 DoloriNote VARCHAR(256), \n" +
                "                                 LinfoNote VARCHAR(256) , \n" +
                "                                 TachiNote VARCHAR(256), \n" +
                "                                 CrisiNote VARCHAR(256) , \n" +
                "                                 FOREIGN KEY (CodiceFiscale) REFERENCES Cittadini_Registrati(CodiceFiscale)); \n";
        PreparedStatement stmt2 = con.prepareStatement(query);
        stmt2.execute();
        PreparedStatement stmt3 = con.prepareStatement(query1);
        stmt3.execute();
        System.out.println("Centro vaccinale registrato");
    }

    @Override
    public Container verificaVaccinato(String username) throws RemoteException, SQLException
    {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);

        String query = "select centro,codicefiscale,idvac from cittadini_registrati where username = '"+username+"' LIMIT 1";
        PreparedStatement stm2 = con.prepareStatement(query);
        ResultSet rs2 = stm2.executeQuery();
        String centro = "";
        String cf = "";
        var c = new Container();
        if(rs2.next())
        {
            centro = rs2.getString("centro");
            cf = rs2.getString("codicefiscale");
            c.setObject(rs2.getString("idvac"));
        }
        if(centro.equals("") || cf.equals(""))
        {

            c.setObject(false);
            return c;
        }

        String s = "select 1 from public.\"Vaccinati_"+centro+"\" where codicefiscale = '"+cf+"' AND data_prima_dose IS NOT NULL";
        PreparedStatement stm3 = con.prepareStatement(s);
        ResultSet rs3 = stm3.executeQuery();
        if(rs3.next())
        {
            try
            {
                c.setObject(true);
                return c;
            }
            catch (Exception e)
            {
                int x = 0;
            }
        }
        c.setObject(false);
        return c;
    }

    @Override
    public Container listaCentriVaccinali() throws RemoteException, SQLException
    {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        String query = "SELECT Nome,Indirizzo FROM CentriVaccinali ";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet rs = stm.executeQuery();
        var c = new Container();
        var arrId = new ArrayList<String>();
        var arrName = new ArrayList<String>();
        while (rs.next())
        {
            try
            {
                arrName.add(rs.getString("Nome"));
                arrId.add(rs.getString("Indirizzo"));
            }
            catch (Exception e)
            {
                return c;
            }

        }
        c.setObject(arrName);
        c.setObject(arrId);
        return c;
    }

    @Override
    public Container infoCittadinoRegistrato(String username) throws RemoteException, SQLException
    {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        String qcercanome = "SELECT Nome,Cognome,codicefiscale,centro FROM Cittadini_Registrati WHERE Username = '"+username+"' LIMIT 1";
        PreparedStatement stm = con.prepareStatement(qcercanome);
        ResultSet rs = stm.executeQuery();
        var c = new Container();
        if (rs.next())
        {
            c.setObject(rs.getString("Nome"));
            c.setObject(rs.getString("Cognome"));
            c.setObject(rs.getString("codicefiscale"));
            c.setObject(rs.getString("centro"));
        }
        return c;
    }

    @Override
    public int controllaRoomDosi(String cf,String nCent) throws RemoteException, SQLException
    {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        String query =
                "SELECT ABS(DATE_PART('day', CURRENT_DATE::timestamp - \n" +
                        "\t \t(SELECT \n" +
                        "\t\t \tCASE \n" +
                        "\t\t \t\tWHEN data_prima_dose IS NULL AND data_seconda_dose IS NULL \n" +
                        "\t\t \t\t\tTHEN NULL \n" +
                        "\t\t \t\tWHEN data_seconda_dose IS NULL \n" +
                        "\t\t \t\t\tTHEN TO_DATE(data_prima_dose,'DD/MM/YYYY') \n" +
                        "\t\t \t\tWHEN TO_DATE(data_prima_dose,'DD/MM/YYYY') >= TO_DATE(data_seconda_dose,'DD/MM/YYYY') \n" +
                        "\t\t \t\t\tTHEN TO_DATE(data_prima_dose,'DD/MM/YYYY') \n" +
                        "\t\t \t\tELSE TO_DATE(data_seconda_dose,'DD/MM/YYYY') END\n" +
                        "\t\t)::timestamp)) AS DateDiff FROM public.\"Vaccinati_"+nCent+"\" WHERE codicefiscale = '"+cf+"' LIMIT 1";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet rs = stm.executeQuery();
        while (rs.next())
        {
            try
            {
                return rs.getInt("DateDiff");
            }
            catch (Exception e)
            {
                return -1;
            }
        }
        return -2;
    }

    @Override
    public Container infoReportVaccinato(String cf, String nCen) throws RemoteException, SQLException
    {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        String query = "SELECT Testa,Febbre,Dolori,Linfo,Tachicardia,Crisi FROM \"Sintomi_"+nCen+"\" WHERE codicefiscale = '"+cf+"'";
        PreparedStatement stm3 = con.prepareStatement(query);
        ResultSet rs4 = stm3.executeQuery();
        var c = new Container();
        if(rs4.next())
        {
            c.setObject(rs4.getInt("Testa"));
            c.setObject(rs4.getInt("Febbre"));
            c.setObject(rs4.getInt("Dolori"));
            c.setObject(rs4.getInt("Linfo"));
            c.setObject(rs4.getInt("Tachicardia"));
            c.setObject(rs4.getInt("Crisi"));
        }
        else
        {
            c.setObject(0);
            c.setObject(0);
            c.setObject(0);
            c.setObject(0);
            c.setObject(0);
            c.setObject(0);
        }
        return c;
    }

    @Override
    public boolean esisteCodiceFiscale(String cf) throws RemoteException, SQLException
    {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        PreparedStatement ps = con.prepareStatement("SELECT 1 AS Result FROM cittadini_registrati WHERE codicefiscale ='" + cf + "' LIMIT 1");
        ResultSet rs = ps.executeQuery();
        while (rs.next())
        {
            try
            {
                return rs.getInt("Result") == 1;
            }
            catch (Exception e)
            {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean codiceFiscaleRegistratoInCentro(String cf, String nCen) throws RemoteException, SQLException
    {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        PreparedStatement ps = con.prepareStatement("SELECT 1 AS Result FROM cittadini_registrati WHERE codicefiscale ='" + cf + "' AND centro = '"+nCen+"' LIMIT 1");
        ResultSet rs = ps.executeQuery();
        while (rs.next())
        {
            try
            {
                return rs.getInt("Result") == 1;
            }
            catch (Exception e)
            {
                return false;
            }
        }
        return false;
    }

    public void registraVaccinato(String centro, String codf, String data, String vacc, boolean secondaDose) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        System.out.println(centro+codf+data+vacc+secondaDose);
        if (secondaDose){
            PreparedStatement ps1 = con.prepareStatement("UPDATE \"Vaccinati_" + centro + "\" SET Data_Seconda_Dose='" + data + "' WHERE CodiceFiscale='" + codf + "'");
            ps1.executeUpdate();
        } else {
            PreparedStatement ps2 = con.prepareStatement("UPDATE \"Vaccinati_" + centro + "\" SET Data_Prima_Dose='" + data + "', vaccino='" + vacc + "' WHERE CodiceFiscale='" + codf + "'");
            ps2.executeUpdate();
        }
    }


    @Override
    public DefaultListModel<String> cercaCentroVaccinale(String centro) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        String query = "SELECT Nome,Indirizzo FROM CentriVaccinali WHERE Nome LIKE '%"+centro+"%'";
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
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        String query = "SELECT Nome,Indirizzo FROM CentriVaccinali WHERE Tipologia = '"+tipo+"' AND Comune='"+comune+"'";
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet rs = stm.executeQuery();
        DefaultListModel<String> l = new DefaultListModel<String>();
        while (rs.next()){
            l.addElement(rs.getString(1)+" ("+rs.getString(2)+")");
        }
        return l;
    }

    /**
     * metodo per la viduall
     * @param nome nome del centro vaccinale
     * @return
     * @throws RemoteException
     * @throws SQLException
     */
    @Override
    public String visualizzaInfoCentroVaccinale(String nome) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        String risultato = "Nome centro: "+nome+"\nIndirizzo: ";
        PreparedStatement stm2 = con.prepareStatement("SELECT Indirizzo FROM CentriVaccinali WHERE Nome = '"+nome+"'");
        ResultSet rs2 = stm2.executeQuery();
        String querySintomi = "select avg(nullif (testa,0)), avg(nullif (febbre,0)), avg(nullif (dolori,0)), avg(nullif (linfo,0)), avg(nullif (tachicardia,0)), avg(nullif (crisi,0)), " +
                "count (nullif (testa,0)), count (nullif (febbre,0)), count (nullif (dolori,0)), count (nullif (linfo,0)), count (nullif (tachicardia,0)), count (nullif (crisi,0))" +
                "FROM \"Sintomi_" + nome + "\"";
        PreparedStatement stm3 = con.prepareStatement(querySintomi);
        ResultSet rs3 = stm3.executeQuery();
        if(rs2.next()){
            if(rs3.next()) {
                risultato += rs2.getString(1) + "\n\nEventi avversi:\n\n" +
                        "Mal di testa \t   -->   Numero segnalazioni: " + rs3.getInt(7) + "\tSeverità media: " + rs3.getFloat(1) +
                        "\nFebbre \t   -->   Numero segnalazioni: " + rs3.getInt(8) + "\tSeverità media: " + rs3.getFloat(2) +
                        "\nDolori muscolari  -->   Numero segnalazioni: " + rs3.getInt(9) + "\tSeverità media: " + rs3.getFloat(3) +
                        "\nLinfoadenopatia   -->   Numero segnalazioni: " + rs3.getInt(10) + "\tSeverità media: " + rs3.getFloat(4) +
                        "\nTachicardia \t   -->   Numero segnalazioni: " + rs3.getInt(11) + "\tSeverità media: " + rs3.getFloat(5) +
                        "\nCrisi ipertensiva  -->   Numero segnalazioni: " + rs3.getInt(12) + "\tSeverità media: " +rs3.getFloat(6) ;
            }
        }
        return risultato;
    }

    /**
     * descisdfghjkl
     * @param id id del vaccinato
     * @param malt
     * @param febbre
     * @param dolori
     * @param linfo
     * @param tachi
     * @param crisi
     * @param cf
     * @param centro
     * @return
     * @throws RemoteException
     * @throws SQLException
     */
    @Override
    public Boolean inserisciEventiAvversi(int id,int malt, int febbre, int dolori, int linfo, int tachi, int crisi, String cf,String centro) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        PreparedStatement stm2 = con.prepareStatement("SELECT codicefiscale FROM cittadini_registrati WHERE idvac="+id);
        ResultSet rs2 = stm2.executeQuery();
        if(rs2.next()) {
            String codf = rs2.getString(1);
            String query1 = "SELECT codicefiscale FROM \"Sintomi_" + centro + "\" WHERE codicefiscale = '"+cf+"'";
            stm2 = con.prepareStatement(query1);
            ResultSet rs3 = stm2.executeQuery();
            if (rs3.next()) {
                String query2 = "UPDATE \"Sintomi_" + centro + "\" SET Testa = " + malt + " ,Febbre = " + febbre + " ,Dolori = " + dolori + " ," +
                        "Linfo = " + linfo + " ,Tachicardia = " + tachi + " ,Crisi = " + crisi + " WHERE codicefiscale = '" + codf + "'";
                PreparedStatement stm3 = con.prepareStatement(query2);
                stm3.executeUpdate();
            } else {
                String query = "INSERT INTO \"Sintomi_" + centro + "\" VALUES (?,?,?,?,?,?,?)";
                PreparedStatement stm = con.prepareStatement(query);
                stm.setString(1, codf);
                stm.setInt(2, malt);
                stm.setInt(3, febbre);
                stm.setInt(4, dolori);
                stm.setInt(5, linfo);
                stm.setInt(6, tachi);
                stm.setInt(7, crisi);
                stm.executeUpdate();
            }
        }
        return true;
    }

    @Override
    public Container logCittadino(String user) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
        PreparedStatement stmt = con.prepareStatement("SELECT Username,Pass FROM Cittadini_Registrati WHERE Username = ? LIMIT 1");
        stmt.setString(1, user);
        ResultSet rs = stmt.executeQuery();
        var c = new Container();
        if(rs.next() == false) return c;
        c.setObject(rs.getString("Username"));
        c.setObject(rs.getString("Pass"));
        con.close();
        return c;
    }

    @Override
    public Boolean registraCittadino(String n,String c,String cf, String em, String u,String p, String nCen) throws RemoteException, SQLException {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername,mPassword);
        PreparedStatement stmt = con.prepareStatement("SELECT Username FROM Cittadini_Registrati WHERE Username = ?");
        stmt.setString(1,u);
        ResultSet rs = stmt.executeQuery();
        if(rs.next() == false)
        {
            String query = "INSERT INTO Cittadini_Registrati (Nome,Cognome,CodiceFiscale,Email,Username,Pass, centro) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement stmt2 = con.prepareStatement(query);
            stmt2.setString(1, n);
            stmt2.setString(2, c);
            stmt2.setString(3, cf);
            stmt2.setString(4, em);
            stmt2.setString(5, u);
            stmt2.setString(6, p);
            stmt2.setString(7, nCen);
            stmt2.executeUpdate();
            String query1 = "INSERT INTO \"Vaccinati_"+nCen+"\" (Cittadino, CodiceFiscale) VALUES (?,?)";
            String utente = n+" "+c;
            PreparedStatement stmt3 = con.prepareStatement(query1);
            stmt3.setString(1, utente);
            stmt3.setString(2, cf);
            stmt3.executeUpdate();
            con.close();
            return true;
        }
        else
        {
            con.close();
            return false;
        }

    }


    public static void main(String[] args) throws SQLException, RemoteException
    {
        try
        {

            //#region ASK INPUTS
            var conn = false;
            do
            {
                mUsername = JOptionPane.showInputDialog(null,"Inserisci lo username:");
                mPassword = JOptionPane.showInputDialog(null,"Inserisci la password:");
                try
                {
                    conn = true;
                    DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
                }
                catch (PSQLException e)
                {
                    if(!e.getSQLState().equals("3D000"))
                    {
                        conn = false;
                        var x = JOptionPane.showConfirmDialog(null,"Dati errati.\nRiprovare?","Connessione",JOptionPane.ERROR_MESSAGE,JOptionPane.OK_OPTION);
                        if(x == JOptionPane.NO_OPTION) return;
                    }
                    JOptionPane.showConfirmDialog(null,"Il database non esiste.\n");
                }

            }
            while(conn == false);
            //#endregion
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
            System.out.println("\nServer connesso al database");
            Thread.sleep(1000);
            System.out.println("\n... Tentativo di creazione del registry ...");
            Thread.sleep(3000);
            serverCV s = new serverCV();
            Registry registro = LocateRegistry.createRegistry(1099);
            registro.bind("serverCV",s);
            System.out.println("\nServer ready in attesa di client\n");
            while(true)
            {
                System.out.println("Per uscire inserire \"exit\"");
                String cmd = scan.next().toLowerCase();
                if(cmd.equals("exit")) System.exit(1);
            }
        } catch (PSQLException ex) {
            if(ex.getSQLState().equals("3D000")){
                System.out.println("Database non esistente\n\nCreazione Database");
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", mUsername, mPassword);
                String createDB = "CREATE DATABASE \"LabB\"\n" +
                        "\tWITH \n" +
                        "\tOWNER = postgres\n" +
                        "\tENCODING = 'UTF8'\n" +
                        "\tLC_COLLATE = 'Italian_Italy.1252'\n" +
                        "\tLC_CTYPE = 'Italian_Italy.1252'\n" +
                        "\tTABLESPACE = pg_default\n" +
                        "\tCONNECTION LIMIT = -1;\n";
                String createCitReg = "CREATE TABLE cittadini_registrati (\n" +
                        "\tnome varchar(20),\n" +
                        "\tcognome varchar(20),\n" +
                        "\tcodicefiscale varchar(20) primary key,\n" +
                        "\temail varchar(100),\n" +
                        "\tusername varchar(20),\n" +
                        "\tpass varchar(20),\n" +
                        "\tidvac serial,\n" +
                        "\tcentro varchar(100)\n" +  //Serial così il db incrementa in automatico
                        ")";
                String createCentri = "create table CentriVaccinali (\n" +
                        "\tnome varchar(100) primary key,\n" +
                        "\tindirizzo varchar(256),\n" +
                        "\ttipologia varchar(100),\n" +
                        "\tidentificatore varchar(100),\n" +
                        "\tnome_via varchar(100),\n" +
                        "\tnum_civico varchar(10),\n" +
                        "\tcomune varchar(100),\n" +
                        "\tcap numeric(5),\n" +
                        "\tprovincia varchar(2)\n" +
                        ")";
                PreparedStatement stm = con.prepareStatement(createDB);
                stm.execute();
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabB", mUsername, mPassword);
                Statement stm1 = con.createStatement();
                stm1.executeUpdate(createCitReg);
                Statement stm2 = con.createStatement();
                stm2.executeUpdate(createCentri);
                serverCV.main(args);
            }
            else { ex.printStackTrace(); }
        } catch (Exception e) {e.printStackTrace();}
    }
}
