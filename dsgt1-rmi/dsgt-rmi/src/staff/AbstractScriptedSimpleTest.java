package staff;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import hotel.BookingDetail;

public abstract class AbstractScriptedSimpleTest {

	private final LocalDate today = LocalDate.now();

	/**
	 * Return true if there is no booking for the given room on the date,
	 * otherwise false
	 */
	protected abstract boolean isRoomAvailable(Integer room, LocalDate date) throws Exception;

	/**
	 * Add a booking for the given guest in the given room on the given
	 * date. If the room is not available, throw a suitable Exception.
	 */
	protected abstract void addBooking(BookingDetail bookingDetail) throws Exception;

	/**
	 * Return a list of all the available room numbers for the given date
	 */
	protected abstract Set<Integer> getAvailableRooms(LocalDate date) throws Exception;

	/**
	 * Return a list of all the room numbers
	 */
	protected abstract Set<Integer> getAllRooms() throws Exception;

	public void run() throws Exception {

		//Print all rooms of the hotel
		printAllRooms();

		isRoomAvailable(101, today); //true
		BookingDetail bd1 = new BookingDetail("Ansar", 101, today);
		addBooking(bd1);//booking success

		//Check available rooms after the first booking
		System.out.println("Printing the list of available rooms after the first booking\n");
		Integer[] expectedRoomsAfterFirstBooking = {102, 201, 203};
		checkAvailableRoomsOutput(3, expectedRoomsAfterFirstBooking);

		isRoomAvailable(102, today); //true
		BookingDetail bd2 = new BookingDetail("Smith", 102, today);
		addBooking(bd2);//booking success

		//Check available rooms after the second booking
		System.out.println("Printing the list of available rooms after the second booking\n");
		Integer[] expectedRoomsAfterSecondBooking = {201, 203};
		checkAvailableRoomsOutput(2, expectedRoomsAfterSecondBooking);

		isRoomAvailable(102, today); //false
		BookingDetail bd3 = new BookingDetail("Dimitri", 102, today);
		addBooking(bd3);//booking failure

		//Check available rooms after the booking failure
		System.out.println("Printing the list of available rooms after the third booking failure\n");
		Integer[] expectedRoomsAfterBookingFailure = {201, 203};
		checkAvailableRoomsOutput(2, expectedRoomsAfterBookingFailure);


		// 创建并启动多个线程，每个线程尝试预订房间
		System.out.println("创建并启动多个线程，每个线程尝试预订房间\n");
		Thread[] threads = new Thread[5]; // 假设有5个并发请求
		for (int i = 0; i < threads.length; i++) {
			final int roomNumber = 201; // 模拟不同的房间预订请求
			threads[i] = new Thread(() -> {
				try {
					LocalDate date = LocalDate.now();
					String guestName = "Guest" + Thread.currentThread().getId();
					BookingDetail bd = new BookingDetail(guestName, roomNumber, date);

					if (isRoomAvailable(roomNumber, date)) {
						addBooking(bd); // 尝试添加预订
					} else {
						System.out.println("Room " + roomNumber + " not available for " + guestName);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			threads[i].start();
		}
		// 等待所有线程完成
		for (Thread t : threads) {
			t.join();
		}
		// 在所有预订尝试完成后检查和打印可用房间
		System.out.println("\nPrinting the list of available rooms after all concurrent booking attempts:\n");
		Set<Integer> availableRooms = getAvailableRooms(LocalDate.now());
		Integer[] availableRoomsArray = availableRooms.toArray(new Integer[0]);
		checkAvailableRoomsOutput(1, availableRoomsArray);
	}

	private void checkAvailableRoomsOutput(int expectedSize, Integer[] expectedAvailableRooms) throws Exception {
		Set<Integer> availableRooms = getAvailableRooms(today);
		if (availableRooms != null && availableRooms.size() == expectedSize) {
			if (availableRooms.containsAll(Arrays.asList(expectedAvailableRooms))) {
				System.out.println("List of available rooms (room ID) " + getAvailableRooms(today) + " [CORRECT]\n");
			} else {
				System.out.println("List of available rooms (room ID) " + getAvailableRooms(today) + " [INCORRECT]\n");
			}
		} else {
			System.out.println("List of available rooms (room ID) " + getAvailableRooms(today) + " [INCORRECT]\n");
		}
	}

	private void printAllRooms() throws Exception {
		System.out.println("List of rooms (room ID) in the hotel " + getAllRooms() + "\n");
	}
}