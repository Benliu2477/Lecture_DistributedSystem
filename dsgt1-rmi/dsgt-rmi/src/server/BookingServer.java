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

            // Export the BookingManager object and specify the communication port
            int communicationPort = 8083; // 这里使用5001作为通信端口
            BookingManagerInterface stub = (BookingManagerInterface) UnicastRemoteObject.exportObject(obj, communicationPort);



            // Bind the remote object's stub in the registry
            //Registry registry = LocateRegistry.getRegistry(); //LocateRegistry.getRegistry()获取默认注册表实例，这通常会尝试连接到本机上默认的1099端口

            // Specify the port for the RMI registry
            int registryPort = 8082; // 示例端口号
            // Create the registry on the specified port
            Registry registry = LocateRegistry.createRegistry(registryPort);
            registry.bind("BookingManager", stub);

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
