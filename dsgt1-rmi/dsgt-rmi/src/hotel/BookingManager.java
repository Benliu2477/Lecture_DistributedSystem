package hotel;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class BookingManager extends UnicastRemoteObject implements BookingManagerInterface {

	private Room[] rooms;

	public BookingManager() throws RemoteException {
		super();
		this.rooms = initializeRooms();
	}

	@Override
	public Set<Integer> getAllRooms() throws RemoteException {
		Set<Integer> allRooms = new HashSet<Integer>();
		Iterable<Room> roomIterator = Arrays.asList(rooms);
		for (Room room : roomIterator) {
			allRooms.add(room.getRoomNumber());
		}
		return allRooms;
	}

	@Override
	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) throws RemoteException {
		for (Room room : rooms) {
			if (room.getRoomNumber().equals(roomNumber)) {
				return room.getBookings().stream()
						.noneMatch(booking -> booking.getDate().equals(date));
			}
		}
		return false; // 如果房间不存在，则默认为不可用
	}

	@Override
	public void addBooking(BookingDetail bookingDetail) throws RemoteException {
		for (Room room : rooms) {
			if (room.getRoomNumber().equals(bookingDetail.getRoomNumber())) {
				// 检查日期是否已经被预订
				if (isRoomAvailable(bookingDetail.getRoomNumber(), bookingDetail.getDate())) {
					room.getBookings().add(bookingDetail);
					return;
				} else {
					throw new RemoteException("Room is not available on this date");
				}
			}
		}
		throw new RemoteException("Room number does not exist");
	}

	@Override
	public Set<Integer> getAvailableRooms(LocalDate date) throws RemoteException {
		Set<Integer> availableRooms = new HashSet<>();
		for (Room room : rooms) {
			if (isRoomAvailable(room.getRoomNumber(), date)) {
				availableRooms.add(room.getRoomNumber());
			}
		}
		return availableRooms;
	}

	private static Room[] initializeRooms() {
		Room[] rooms = new Room[4];
		rooms[0] = new Room(101);
		rooms[1] = new Room(102);
		rooms[2] = new Room(201);
		rooms[3] = new Room(203);
		return rooms;
	}
}
