package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import hotel.BookingManager;
import hotel.BookingManagerInterface;

public class BookingServer {
    public static void main(String[] args) {
        try {
            // Create and export a remote object
            BookingManager obj = new BookingManager();
            // 如果BookingManager已经继承自UnicastRemoteObject，则不需要再次导出对象
//            BookingManagerInterface stub = (BookingManagerInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("BookingManager", obj);

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
