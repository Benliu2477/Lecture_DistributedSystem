package hotel;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import java.rmi.RemoteException;

public class BookingManager implements BookingManagerInterface {

	private Room[] rooms;

	public BookingManager() throws RemoteException {
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
	public synchronized boolean isRoomAvailable(Integer roomNumber, LocalDate date) throws RemoteException {
		for (Room room : rooms) {
			if (room.getRoomNumber().equals(roomNumber)) {
				return room.getBookings().stream()
						.noneMatch(booking -> booking.getDate().equals(date));
			}
		}
		return false; // 如果房间不存在，则默认为不可用
	}

	@Override
	public synchronized void addBooking(BookingDetail bookingDetail) throws RemoteException {
		try {
			// 模拟延迟，假设处理预订需要1000毫秒（1秒）
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// 在实际应用中，应适当处理InterruptedException
			Thread.currentThread().interrupt(); // 重新设置中断状态
			throw new RemoteException("Interrupted while processing booking", e);
		}

		for (Room room : rooms) {
			if (room.getRoomNumber().equals(bookingDetail.getRoomNumber())) {
				// 检查日期是否已经被预订
				if (isRoomAvailable(bookingDetail.getRoomNumber(), bookingDetail.getDate())) {
					room.getBookings().add(bookingDetail);
					System.out.println("Booking success for Guest" + Thread.currentThread().getId() + " in room " + room.getRoomNumber());
					return;
				} else {
					throw new RemoteException("Room is not available on this date");
				}
			}
		}
		throw new RemoteException("Room number does not exist");
	}

	@Override
	public synchronized Set<Integer> getAvailableRooms(LocalDate date) throws RemoteException {
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

	@Override
	public IBookingSession createSession() throws RemoteException {
		BookingSession session = new BookingSession(this);
		// 这里不需要注册 session 对象到 RMI 注册表，因为它将通过引用传递
		return session; // 返回 session 的引用
	}

	public synchronized void cancelBooking(BookingDetail bookingDetail) throws RemoteException {
		for (Room room : rooms) {
			if (room.getRoomNumber().equals(bookingDetail.getRoomNumber())) {
				// 从房间的预订列表中移除匹配的预订
				boolean removed = room.getBookings().removeIf(b ->
						b.getDate().equals(bookingDetail.getDate()) &&
								b.getGuest().equals(bookingDetail.getGuest()) // 假设BookingDetail包含guest字段
				);
				if (removed) {
					System.out.println("Booking cancelled for guest: " + bookingDetail.getGuest() + " in room " + room.getRoomNumber());
				} else {
					System.out.println("No matching booking found to cancel for guest: " + bookingDetail.getGuest() + " in room " + room.getRoomNumber());
				}
				return;
			}
		}
		throw new RemoteException("Room number does not exist");
	}
}
