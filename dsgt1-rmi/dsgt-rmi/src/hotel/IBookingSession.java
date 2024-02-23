package hotel;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBookingSession extends Remote {
    void addBookingDetail(BookingDetail bookingDetail) throws RemoteException;
    boolean bookAll() throws RemoteException;
}