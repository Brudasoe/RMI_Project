import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    public static void main(String[] args) {
        try {
            new RMIServer();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private RMIServer() throws RemoteException {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            RMIServant servant = new RMIServant();
            reg.rebind("Server", servant);
            System.out.println("Server READY");
        } catch (RemoteException e) {
            e.printStackTrace();
            throw e;
        }
    }
}

