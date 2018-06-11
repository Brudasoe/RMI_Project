import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class RMIServant
  extends UnicastRemoteObject 
  implements IChat {
	private Map<String, ICallback> present = new HashMap<String, ICallback>();
	
  public RMIServant() throws RemoteException {
  }
  // Metoda implementujaca funkcje save() interfejsu IChat
/*  public boolean save(String nick, String text) throws RemoteException {
    System.out.println("Server.save(): " + text);
    ICallback callback = present.get(nick);
    if(callback != null) {
    	callback.save(nick, text);
		return true;
    }
    return false;
  }*/
  //Metoda implementujaca funkcje register() interfejsu IChat
  public boolean register(String dataSeries, ICallback n) throws RemoteException {
    System.out.println("Server.register(): " + dataSeries);
    if (!present.containsKey(dataSeries)) {
      present.put(dataSeries, n);
      return true;
    }
    return false;
  }
  //Metoda implementujaca funkcje unregister() interfejsu IChat
	public boolean unregister(String dataSeries) throws RemoteException {
		if(present.remove(dataSeries) != null) {
			System.out.println("Server.unregister(): " + dataSeries);
	        return true;
		} 
		return false;
	}
  //Metoda implementujaca funkcje search() interfejsu IChat
	public Vector<String> search(String dataSeries) throws RemoteException {
		Set<String> set = present.keySet();
		Vector<String> v = new Vector<String>();
		for(String s : set)
			if(s.contains(dataSeries))
				v.add(s);
		return v;
	}
}