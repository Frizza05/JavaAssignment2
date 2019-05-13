package cars;

import utilities.DateTime;

public class SilverServiceCar extends Car {
	private String[] refreshments;
	private double bookingFee;

	public SilverServiceCar(String regNo, String make, String model, String driverName, int passengerCapacity, double bookingFee, String[] refreshments) {
		super(regNo,make,model,driverName,passengerCapacity);
		this.bookingFee = bookingFee;
		this.refreshments = refreshments;
		
		//Booking Fee has to be greater than or equal to $3.00
		if (bookingFee < 3.00) {
			System.out.println("Error: Booking Fee Too Small");
		}
		
		
	}
	
	public boolean book(String firstName, String lastName, DateTime required, int numPassengers)
	{
		boolean booked = false;
		
		
		// Does car have five bookings
		available = bookingAvailable();
		boolean dateAvailable = notCurrentlyBookedOnDate(required);
		// Date is within range, not in past and within the next week
		boolean dateValid = dateIsValid(required);
		boolean validPassengerNumber = numberOfPassengersIsValid(numPassengers);

		// Booking is permissible
		if (available && dateAvailable && dateValid && validPassengerNumber)
		{
			tripFee = STANDARD_BOOKING_FEE;
			Booking booking = new Booking(firstName, lastName, required, numPassengers, this);
			currentBookings[bookingSpotAvailable] = booking;
			bookingSpotAvailable++;
			booked = true;
		}
		return booked;
	}
	
	public String getDetails() {
		super.getDetails();
		
	}
	
}
