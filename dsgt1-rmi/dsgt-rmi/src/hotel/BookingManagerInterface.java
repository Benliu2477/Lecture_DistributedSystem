package hotel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Set;

public interface BookingManagerInterface extends Remote {
    // 获取所有房间的编号
    Set<Integer> getAllRooms() throws RemoteException;

    // 检查特定房间在特定日期是否可用
    boolean isRoomAvailable(Integer roomNumber, LocalDate date) throws RemoteException;

    // 添加新的预订
    void addBooking(BookingDetail bookingDetail) throws RemoteException;

    // 获取特定日期所有可用房间的编号
    Set<Integer> getAvailableRooms(LocalDate date) throws RemoteException;

    // 创建并返回一个新的会话对象
    IBookingSession createSession() throws RemoteException;
}