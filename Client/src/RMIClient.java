import Classes.TimeHistory;
import Classes.Spectrum;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Vector;
import java.util.Scanner;

public class RMIClient {
    private Scanner userInput = new Scanner(System.in);
    private String device;
    private String description;
    private long date;
    private int channelNr;
    private String unit;
    private double resolution;
    private int s;
    private int sensitivity;
    private String scaling;
    private String dataType;
    private String dataFrame;
    private IChat remoteObject;
    private ICallback callback;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: RMIClient <server host name>");
            System.exit(-1);
        }
        new RMIClient(args[0]);
    }

    private RMIClient(String hostname) {
        try {
            loop(hostname);
            remoteObject.unregister(dataFrame);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void search() {
        String line;
        System.out.print("Enter the device name: ");
        if (userInput.hasNextLine()) {
            line = userInput.nextLine();
            Vector<String> vec = null;
            try {
                vec = remoteObject.search(line);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            assert vec != null;
            System.out.println("Present device(s) " + vec.size());
            for (String s : vec)
                System.out.println(" - " + s);
        }
    }

    private String registerData() {
        System.out.println("Enter data type(TH for Time History or S for Spectrum): ");
        if (userInput.hasNextLine())
            dataType = userInput.nextLine();
        if (dataType.equals("TH") || dataType.equals("S")) {
            System.out.println("Enter device name: ");
            if (userInput.hasNextLine())
                device = userInput.nextLine();
            System.out.println("Enter description: ");
            if (userInput.hasNextLine())
                description = userInput.nextLine();
            System.out.println("Enter date: ");
            if (userInput.hasNextLine())
                date = Long.parseLong(userInput.nextLine());
            System.out.println("Enter unit: ");
            if (userInput.hasNextLine())
                unit = userInput.nextLine();
            System.out.println("Enter resolution: ");
            if (userInput.hasNextLine())
                resolution = Double.parseDouble(userInput.nextLine());
            System.out.println("Enter buffer: ");
            if (userInput.hasNextLine())
                s = Integer.parseInt(userInput.nextLine());
            if (dataType.equals("TH")) {
                System.out.println("Enter sensitivity: ");
                if (userInput.hasNextLine())
                    sensitivity = Integer.parseInt(userInput.nextLine());
                TimeHistory th = new TimeHistory(device, description, date, channelNr, unit, resolution, s, sensitivity);
                dataFrame = th.toString();
            } else {
                System.out.println("Enter scaling: ");
                if (userInput.hasNextLine())
                    scaling = userInput.nextLine();
                Spectrum sp = new Spectrum(device, description, date, channelNr, unit, resolution, s, scaling);
                dataFrame = sp.toString();
            }

        } else
            System.out.println("Wrong dataType, please try again");
        System.out.println(dataFrame);
        return dataFrame;
    }

    private void loop(String hostname) throws RemoteException {
        while (true) {
            String line;
            System.out.println("[s]earch, [r]egister, [e]nd: ");
            if (userInput.hasNextLine()) {
                line = userInput.nextLine();
                if (!line.matches("[sre]")) {
                    System.out.println("You entered invalid command !");
                    continue;
                }
                switch (line) {
                    case "s":
                        search();
                        break;
                    case "r":
                        dataFrame = registerData();
                        Registry reg;
                        reg = LocateRegistry.getRegistry(hostname);
                        try {
                            remoteObject = (IChat) reg.lookup("Server");
                        } catch (NotBoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            callback = new ClientCallback();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        remoteObject.register(dataFrame, callback);
                        break;
                    case "e":
                        System.exit(0);
                }
            }
        }
    }
}

