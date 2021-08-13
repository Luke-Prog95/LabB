import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface serverCVInterface extends Remote {
    public List<String> cercaCentroVaccinale() throws RemoteException, SQLException;
    public List<String> cercaCentroVaccinale(String centro) throws RemoteException;
    public List<String> cercaCentroVaccinale(String comune, String tipo) throws RemoteException;
    public String visualizzaInfoCentroVaccinale() throws RemoteException;
    public ResultSet logCittadino(String u) throws RemoteException, SQLException;
    public ResultSet registraCittadino(String n,String c,String cf, String em, String u,String p, String nCen)  throws RemoteException, SQLException;
    public Boolean inserisciEventiAvversi() throws RemoteException;
}
