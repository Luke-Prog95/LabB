import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface serverCVInterface extends Remote {
    public List<String> cercaCentroVaccinale(String centro) throws RemoteException;
    public List<String> cercaCentroVaccinale(String comune, String tipo) throws RemoteException;
    public String visualizzaInfoCentroVaccinale() throws RemoteException;
    public Boolean registraCittadino() throws RemoteException;
    public Boolean inserisciEventiAvversi() throws RemoteException;
}
