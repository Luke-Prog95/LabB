import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class clientCV {
    /*
            //stmt.executeUpdate("INSERT INTO Login VALUES('?','Password')");
            String q = "SELECT * FROM L";
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String User = rs.getString("username");
                String pass = rs.getString("pass");
                System.out.println(User+": "+pass);
            }
*/

    public static void main(String[] args) {
        new LoginPage();
    }

}