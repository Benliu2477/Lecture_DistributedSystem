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
			String host = (args.length<1)?null:args[0];
			//Look up the registered remote instance
			Registry registry = LocateRegistry.getRegistry(host);
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
