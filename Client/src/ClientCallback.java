import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientCallback 
    extends UnicastRemoteObject 
    implements ICallback {
	ClientCallback() throws RemoteException {
		super();
	}
	public void save(String dataFrame, String text) throws RemoteException {
		// placeholder
	}
}
