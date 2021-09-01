import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface serverCVInterface extends Remote {
    public DefaultListModel<String> cercaCentroVaccinale(String centro) throws RemoteException, SQLException;
    public DefaultListModel<String> cercaCentroVaccinale(String comune, String tipo) throws RemoteException, SQLException;
    public String visualizzaInfoCentroVaccinale(String nome) throws RemoteException, SQLException;
    public ResultSet logCittadino(String u) throws RemoteException, SQLException;
    public ResultSet registraCittadino(String n,String c,String cf, String em, String u,String p, String nCen)  throws RemoteException, SQLException;
    public Boolean inserisciEventiAvversi(int id,int malt, int febbre, int dolori, int linfo, int tachi, int crisi, String centro) throws RemoteException, SQLException;
}
