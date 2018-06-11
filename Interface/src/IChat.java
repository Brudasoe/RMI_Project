import java.rmi.*;
import java.util.Vector;

public interface IChat extends Remote {
   boolean register(String nick, ICallback n) throws RemoteException;
   boolean unregister(String nick) throws RemoteException;
   Vector<String> search(String nick) throws RemoteException;
}
