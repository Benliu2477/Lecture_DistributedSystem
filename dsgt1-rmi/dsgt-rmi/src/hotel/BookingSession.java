package hotel;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class BookingSession extends UnicastRemoteObject implements IBookingSession {
    private List<BookingDetail> shoppingCart = new ArrayList<>();
    private final BookingManager bookingManager;

    protected BookingSession(BookingManager bookingManager) throws RemoteException {
        super();
        this.bookingManager = bookingManager;
    }

    @Override
    public void addBookingDetail(BookingDetail bookingDetail) throws RemoteException {
        synchronized (shoppingCart) {
            shoppingCart.add(bookingDetail);
        }
    }

    @Override
    public boolean bookAll() throws RemoteException {
        List<BookingDetail> successfullyBooked = new ArrayList<>();
        synchronized (shoppingCart) {
            try {
                for (BookingDetail detail : shoppingCart) {
                    // 假设BookingManager有一个方法可以检查并添加预订
                    if (bookingManager.isRoomAvailable(detail.getRoomNumber(), detail.getDate())) {
                        bookingManager.addBooking(detail);
                        successfullyBooked.add(detail);
                    } else {
                        // 如果任何一个预订失败，则回滚之前的预订
                        rollback(successfullyBooked);
                        return false;
                    }
                }
                shoppingCart.clear(); // 清空购物车
                return true; // 所有预订都成功
            } catch (Exception e) {
                // 发生异常时回滚
                rollback(successfullyBooked);
                throw new RemoteException("Failed to book all: " + e.getMessage(), e);
            }
        }
    }

    private void rollback(List<BookingDetail> successfullyBooked) {
        // 这里添加回滚逻辑，取消成功的预订
        for (BookingDetail detail : successfullyBooked) {
            try {
                bookingManager.cancelBooking(detail);
            } catch (Exception e) {
                System.err.println("Failed to cancel booking for " + detail.getGuest());
            }
        }
    }
}