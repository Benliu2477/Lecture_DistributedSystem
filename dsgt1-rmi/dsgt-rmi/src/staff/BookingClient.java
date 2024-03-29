package staff;

import java.time.LocalDate;
import java.util.Set;

import hotel.BookingDetail;
import hotel.BookingManager;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import hotel.BookingManagerInterface;

public class BookingClient extends AbstractScriptedSimpleTest {

	private BookingManagerInterface bm = null;

	public static void main(String[] args) throws Exception {
		BookingClient client = new BookingClient(args);
		client.run();
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/
	public BookingClient(String[] args) {
		try {
			int port = 8083; // 默认的 RMI 注册表端口号
			String host = (args.length<1)?null:args[0];
			//Look up the registered remote instance
			if (args.length >= 2) {
				port = Integer.parseInt(args[1]); // 如果命令行参数中提供了第二个参数，则使用它作为端口号
			}
			// 获取 RMI 注册表，并指定主机名和端口号
			Registry registry = LocateRegistry.getRegistry(host, port);
			bm = (BookingManagerInterface) registry.lookup("BookingManager");
		} catch (Exception exp) {
			System.err.println("Client Exception: "+ exp.toString());
			exp.printStackTrace();
		}
	}

	public BookingClient(String host, int port) {
		try {
			// 获取 RMI 注册表，并指定主机名和端口号
			Registry registry = LocateRegistry.getRegistry(host, port);
			bm = (BookingManagerInterface) registry.lookup("BookingManager");
		} catch (Exception exp) {
			System.err.println("Client Exception: "+ exp.toString());
			exp.printStackTrace();
		}
	}

	@Override
	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) {
		try {
			return bm.isRoomAvailable(roomNumber, date);
		} catch (Exception e) {
			System.err.println("Client Exception: "+ e.toString());
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void addBooking(BookingDetail bookingDetail) throws Exception {
		try {
			bm.addBooking(bookingDetail);
		} catch (Exception e) {
			System.err.println("Client Exception: "+ e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public Set<Integer> getAvailableRooms(LocalDate date) {
		try {
			return bm.getAvailableRooms(date);
		} catch (Exception e) {
			System.err.println("Client Exception: "+ e.toString());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Set<Integer> getAllRooms() {
		try {
			return bm.getAllRooms();
		} catch (Exception e) {
			System.err.println("Client Exception: "+ e.toString());
			e.printStackTrace();
			return null;
		}
	}
}
