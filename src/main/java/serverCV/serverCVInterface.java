package serverCV;

import cittadini.Container;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface serverCVInterface extends Remote
{
    public DefaultListModel<String> cercaCentroVaccinale(String centro) throws RemoteException, SQLException;
    public DefaultListModel<String> cercaCentroVaccinale(String comune, String tipo) throws RemoteException, SQLException;
    public String visualizzaInfoCentroVaccinale(String nome) throws RemoteException, SQLException;
    public Container logCittadino(String u) throws RemoteException, SQLException;
    public Boolean registraCittadino(String n,String c,String cf, String em, String u,String p, String nCen)  throws RemoteException, SQLException;
    public Boolean inserisciEventiAvversi(int id,int malt, int febbre, int dolori, int linfo, int tachi, int crisi, String cf,String centro) throws RemoteException, SQLException;
    public void registraVaccinato(String centro, String codf, String data, String vacc, boolean secondaDose) throws RemoteException, SQLException;
    public void registraCentroVaccinale(String nome, String comune, String qualif, String numCiv, String prov, String via, String tipo, int cap) throws RemoteException, SQLException;
    public Container verificaVaccinato(String username) throws RemoteException, SQLException;
    public Container listaCentriVaccinali()throws RemoteException, SQLException;
    public Container infoCittadinoRegistrato(String username) throws RemoteException, SQLException;
    public int controllaRoomDosi(String cf,String nCen) throws RemoteException, SQLException;
    public Container infoReportVaccinato(String cf,String nCen) throws RemoteException, SQLException;
    public boolean esisteCodiceFiscale(String cf) throws RemoteException, SQLException;
    public boolean codiceFiscaleRegistratoInCentro(String cf,String nCen) throws RemoteException, SQLException;
}
