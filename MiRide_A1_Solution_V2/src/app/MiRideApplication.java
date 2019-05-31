package app;

import cars.Car;
import cars.SilverServiceCar;
import exceptions.InvalidBooking;
import exceptions.InvalidRefreshments;
import utilities.DateTime;
import utilities.MiRidesUtilities;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * Class:			MiRideApplication
 * Description:		The system manager the manages the 
 *              	collection of data. 
 * Author:			Rodney Cocker
 */
public class MiRideApplication
{
	private Car[] cars = new Car[15];
	private int itemCount = 0;
	private String[] availableCars;
	private Car[] sortedCars = new Car[15];

	public MiRideApplication()
	{
		//seedData();
	}
	
	public String createCar(String id, String make, String model, String driverName, int numPassengers) 
	{
		String validId = isValidId(id);
		if(isValidId(id).contains("Error:"))
		{
			return validId;
		}
		if(!checkIfCarExists(id)) {
			cars[itemCount] = new Car(id, make, model, driverName, numPassengers);
			itemCount++;
			return "New Car added successfully for registion number: " + cars[itemCount-1].getRegistrationNumber();
		}
		return "Error: Already exists in the system.";
	}

	public String createSilverCar(String id, String make, String model, String driverName, int numPassengers, 
			double standardFee, String[] refreshments) 
	{
		String validId = isValidId(id);
		if(isValidId(id).contains("Error:"))
		{
			return validId;
		}
		if(!checkIfCarExists(id)) {
			try {
				cars[itemCount] = new SilverServiceCar(id, make, model, driverName, numPassengers, standardFee, refreshments);
				itemCount++;
				return "New Car added successfully for registion number: " + cars[itemCount-1].getRegistrationNumber();
			}
			catch (InvalidRefreshments e) {
				return "Error: Invalid Refreshments List";
			}
		}
		return "Error: Already exists in the system.";
	}
	
	public String[] book(DateTime dateRequired)
	{
		int numberOfAvailableCars = 0;
		// finds number of available cars to determine the size of the array required.
		for(int i=0; i<cars.length; i++)
		{
			if(cars[i] != null)
			{
				if(!cars[i].isCarBookedOnDate(dateRequired))
				{
					numberOfAvailableCars++;
				}
			}
		}
		if(numberOfAvailableCars == 0)
		{
			String[] result = new String[0];
			return result;
		}
		availableCars = new String[numberOfAvailableCars];
		int availableCarsIndex = 0;
		// Populate available cars with registration numbers
		for(int i=0; i<cars.length;i++)
		{
			
			if(cars[i] != null)
			{
				if(!cars[i].isCarBookedOnDate(dateRequired))
				{
					availableCars[availableCarsIndex] = availableCarsIndex + 1 + ". " + cars[i].getRegistrationNumber();
					availableCarsIndex++;
				}
			}
		}
		return availableCars;
	}
	
	public String book(String firstName, String lastName, DateTime required, int numPassengers, String registrationNumber)
	throws InvalidBooking
	{
		Car car = getCarById(registrationNumber);
		if(car != null)
        {
			if(car.book(firstName, lastName, required, numPassengers))
			{

				String message = "Thank you for your booking. \n" + car.getDriverName() 
		        + " will pick you up on " + required.getFormattedDate() + ". \n"
				+ "Your booking reference is: " + car.getBookingID(firstName, lastName, required);
				return message;
			}
			else
			{
				String message = "Booking could not be completed.";
				return message;
			}
        }
        else{
            return "Car with registration number: " + registrationNumber + " was not found.";
        }
	}
	
	public String completeBooking(String firstName, String lastName, DateTime dateOfBooking, double kilometers)
	{
		String result = "";
		
		// Search all cars for bookings on a particular date.
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if(cars[i].isCarBookedOnDate(dateOfBooking))
				{
					return cars[i].completeBooking(firstName, lastName, dateOfBooking, kilometers);
				}
//				else
//				{
//					
//				}
//				if(!result.equals("Booking not found"))
//				{
//					return result;
//				}
			}
		}
		return "Booking not found.";
	}
	
	public String completeBooking(String firstName, String lastName, String registrationNumber, double kilometers)
	{
		String carNotFound = "Car not found";
		Car car = null;
		// Search for car with registration number
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(registrationNumber))
				{
					car = cars[i];
					break;
				}
			}
		}

		if (car == null)
		{
			return carNotFound;
		}
		if (car.getBookingByName(firstName, lastName) != -1)
		{
			return car.completeBooking(firstName, lastName, kilometers);
		}
		return "Error: Booking not found.";
	}
	
	public boolean getBookingByName(String firstName, String lastName, String registrationNumber)
	{
		String bookingNotFound = "Error: Booking not found";
		Car car = null;
		// Search for car with registration number
		for(int i = 0; i <cars.length; i++)
		{
			if (cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(registrationNumber))
				{
					car = cars[i];
					break;
				}
			}
		}
		
		if(car == null)
		{
			return false;
		}
		if(car.getBookingByName(firstName, lastName) == -1)
		{
			return false;
		}
		return true;
	}
	public String displaySpecificCar(String regNo)
	{
		for(int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				if(cars[i].getRegistrationNumber().equals(regNo))
				{
					return cars[i].getDetails();
					
				}
			}
		}
		return "Error: The car could not be located.";
	}
	
	public boolean seedData()
	{
		
		boolean valid = true;
		
		for(int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				valid = false;
				return valid;
			}
		}
		try {
			
			if (valid) {
				// 2 cars not booked
				Car honda = new Car("SIM194", "Honda", "Accord Euro", "Henry Cavill", 5);
				cars[itemCount] = honda;
				honda.book("Craig", "Cocker", new DateTime(1), 3);
				itemCount++;
				
				Car lexus = new Car("LEX666", "Lexus", "M1", "Angela Landsbury", 3);
				cars[itemCount] = lexus;
				lexus.book("Craig", "Cocker", new DateTime(1), 3);
				itemCount++;
				
				// 2 cars booked
				Car bmw = new Car("BMW256", "Mini", "Minor", "Barbara Streisand", 4);
				cars[itemCount] = bmw;
				itemCount++;
				bmw.book("Craig", "Cocker", new DateTime(1), 3);
				
				Car audi = new Car("AUD765", "Mazda", "RX7", "Matt Bomer", 6);
				cars[itemCount] = audi;
				itemCount++;
				audi.book("Rodney", "Cocker", new DateTime(1), 4);
				
				// 1 car booked five times (not available)
				Car toyota = new Car("TOY765", "Toyota", "Corola", "Tina Turner", 7);
				cars[itemCount] = toyota;
				itemCount++;
				toyota.book("Rodney", "Cocker", new DateTime(1), 3);
				toyota.book("Craig", "Cocker", new DateTime(2), 7);
				toyota.book("Alan", "Smith", new DateTime(3), 3);
				toyota.book("Carmel", "Brownbill", new DateTime(4), 7);
				toyota.book("Paul", "Scarlett", new DateTime(5), 7);
				
				// 1 car booked five times (not available)
				Car rover = new Car("ROV465", "Honda", "Rover", "Jonathon Ryss Meyers", 7);
				cars[itemCount] = rover;
				itemCount++;
				rover.book("Rodney", "Cocker", new DateTime(1), 3);
				DateTime inTwoDays = new DateTime(2);
				rover.book("Rodney", "Cocker", inTwoDays, 3);
				//rover.completeBooking("Rodney", "Cocker", inTwoDays,75);
				
				// 2 silver service cars not booked
				String[] mazdaRefreshments = {"Cola", "Lemonade", "Water"};
				SilverServiceCar mazda = new SilverServiceCar("SWD543", "Mazda", "CX3", "Joey Vincent", 4, 4.5, mazdaRefreshments);
				cars[itemCount] = mazda;
				itemCount++;
				
				String[] volvoRefreshments = {"Orange Juice", "Lemonade", "Water"};
				SilverServiceCar volvo = new SilverServiceCar("ATG456", "Volvo", "XC40", "Tom Daugherty", 5, 6, volvoRefreshments);
				cars[itemCount] = volvo;
				itemCount++;
				
				
				
				//2 silver service cars booked
				String[] golfRefreshments = {"Water", "Iced Tea", "Apple Juice", "Soda Water"};
				SilverServiceCar volkswagen  = new SilverServiceCar("VWA378", "Volkswagen", "Golf", "Phillip Bowden", 4, 3.5, golfRefreshments);
				cars[itemCount] = volkswagen;
				itemCount++;
				volkswagen.book("Craig", "Cocker", new DateTime(1), 3);
				
				SilverServiceCar mazdaBT50  = new SilverServiceCar("OXZ952", "Mazda", "BT-50", "Paulina Garrett", 5, 6, mazdaRefreshments);
				cars[itemCount] = mazdaBT50;
				itemCount++;
				mazdaBT50.book("Craig", "Cocker", new DateTime(1), 3);
				
				
				//2 silver service cars booked and completed
				SilverServiceCar volkswagenPassat  = new SilverServiceCar("EJK143", "Volkswagen", "Passat", "Emilee Aguirre", 4, 5.5, volvoRefreshments);
				cars[itemCount] = volkswagenPassat;
				itemCount++;
				volkswagenPassat.book("Craig", "Cocker", new DateTime(1), 3);
				volkswagenPassat.completeBooking("Craig", "Cocker", inTwoDays,75);
				
				SilverServiceCar toyotaRAV  = new SilverServiceCar("UEC218", "Toyota", "RAV4", "Hunter Weiss", 6, 4, volvoRefreshments);
				cars[itemCount] = toyotaRAV;
				itemCount++;
				toyotaRAV.book("Craig", "Cocker", new DateTime(1), 3);
				toyotaRAV.completeBooking("Craig", "Cocker", inTwoDays,75);
			}
		}
		catch (InvalidBooking e){
			System.out.println("Error - Invalid Booking Dates");
		}
		catch (InvalidRefreshments e) {
			System.out.println("Error - Invalid Refreshments List");
		}
		return valid;
		
	}

	public String displayAllBookings(String type, String sortOrder)
	{		
		if(itemCount == 0)
		{
			return "No cars have been added to the system.";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Summary of all cars: ");
		sb.append("\n");
		
		int count = cars.length;
		int index = 0;
		
		if (type.equals("SD")) {
			for (int i = 0; i < count; i++) {
				if (cars[i] != null) {
					if (!cars[i].checkSilverServiceCar()) {
						sortedCars[index] = cars[i];
						index++;
					}
				}
			}
			
			selectionSort(sortOrder);
			
		}
		
		if (type.equals("SS")) {
			for (int i = 0; i < count; i++) {
				if (cars[i] != null) {
					if (cars[i].checkSilverServiceCar()) {
						sortedCars[index] = cars[i];
						index++;
					}
				}
			}
			selectionSort(sortOrder);
		}

		for (int i = 0; i < itemCount; i++)
		{
			if (sortedCars[i] != null) {
				sb.append(sortedCars[i].getDetails());
			}
		}
		
		sortedCars = new Car[15];
		
		return sb.toString();
	}

	public String displayBooking(String id, String seatId)
	{
		Car booking = getCarById(id);
		if(booking == null)
		{
			return "Booking not found";
		}
		return booking.getDetails();
	}
	
	public String isValidId(String id)
	{
		return MiRidesUtilities.isRegNoValid(id);
	}
	
	public String isValidPassengerCapacity(int passengerNumber)
	{
		return MiRidesUtilities.isPassengerCapacityValid(passengerNumber);
	}

	public boolean checkIfCarExists(String regNo)
	{
		Car car = null;
		if (regNo.length() != 6)
		{
			return false;
		}
		car = getCarById(regNo);
		if (car == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private Car getCarById(String regNo)
	{
		Car car = null;

		for (int i = 0; i < cars.length; i++)
		{
			if(cars[i] != null)
			{
				if (cars[i].getRegistrationNumber().equals(regNo))
				{
					car = cars[i];
					return car;
				}
			}
		}
		return car;
	}
	
	public String searchAvailable(String type, DateTime required) {
		StringBuilder available = new StringBuilder();
		int count = cars.length;
		Car[] availableCars = new Car[15];
		int index = 0;
		
		if (type.equals("SS")) {
			for (int i = 0; i < count; i++) {
				if (cars[i] != null) {
					if (!cars[i].isCarBookedOnDate(required) && cars[i].checkSilverServiceCar()) {
						availableCars[index] = cars[i];
						index++;
					}
				}
			}
		}
		if (type.equals("SD")) {
			for (int i = 0; i < count; i++) {
				if (cars[i] != null) {
					if (!cars[i].isCarBookedOnDate(required) && !cars[i].checkSilverServiceCar()) {
						availableCars[index] = cars[i];
						index++;
					}
				}
			}
		}
		
		for (int i = 0; i < availableCars.length; i++) {
			if (availableCars[i] != null) {
				available.append(String.format("%-15s", availableCars[i].getDetails()));
			}
		}
		
		return available.toString();
	}
	
	public void selectionSort(String sort) {
		for (int i = 0; i < this.sortedCars.length; i++) {
	        Car min = this.sortedCars[i];
	        int minId = i;
	        if (sort.equals("A")) {
	        	for (int j = i+1; j < this.sortedCars.length; j++) {
		        	if (sortedCars[j] != null) {
		        		 if (this.sortedCars[j].getRegistrationNumber().compareTo(min.getRegistrationNumber()) < 0) {
		 	                min = this.sortedCars[j];
		 	                minId = j;
		        		 }
		            }
		        }
	        }
	        
	        if (sort.equals("D")) {
	        	for (int j = i+1; j < this.sortedCars.length; j++) {
		        	if (sortedCars[j] != null) {
		        		 if (this.sortedCars[j].getRegistrationNumber().compareTo(min.getRegistrationNumber()) > 0) {
		 	                min = this.sortedCars[j];
		 	                minId = j;
		        		 }
		            }
		        }
	        }
	        
	        // swapping
	        Car temp = this.sortedCars[i];
	        this.sortedCars[i] = min;
	        this.sortedCars[minId] = temp;
	    }
	}
	
	public void readFile() {
		File file = new File("cars.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String readCar;
			try {
				while ((readCar = br.readLine()) != null) {
					executeRead(readCar);
				}
				
			}
			catch (IOException e) {
				
			}
			catch (InvalidRefreshments e) {
				System.out.println("Error - Invalid Refreshments List");
			}
			
		}
		catch (FileNotFoundException e) {
			File backupFile = new File("carsBackup.txt");
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(backupFile));
				String readCar;
				try {
					while ((readCar = br.readLine()) != null) {
						executeRead(readCar);
					}
					System.out.println("Error - Booking Data File Not Found! Loading Data From Backup File.");
					
				}
				catch (IOException x) {
					
				}
				catch (InvalidRefreshments x) {
					System.out.println("Error - Invalid Refreshments List");
				}
			}
			catch(FileNotFoundException x) {
				System.out.println("No Booking Data Found");
			}
		}
		
		
		
	}
	
	public void writeFile() {
		String filename = "cars.txt";
		PrintWriter outputStream = null;
		
		try {
			outputStream = new PrintWriter(new FileOutputStream(filename));
			
			
			for (int i = 0; i < cars.length; i++) {
				if (cars[i] != null) {
					outputStream.write(cars[i].toString() + "\n");
				}
			}
			
			outputStream.flush();
			outputStream.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error: File Not Found Exception");
		}
		
		filename = "carsBackup.txt";
		try {
			outputStream = new PrintWriter(new FileOutputStream(filename));
			
			
			for (int i = 0; i < cars.length; i++) {
				if (cars[i] != null) {
					outputStream.write(cars[i].toString() + "\n");
				}
			}
			
			outputStream.flush();
			outputStream.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Error: File Not Found Exception");
		}
		
	}
	
	public void executeRead(String readCar) throws InvalidRefreshments{
		String[] bookingSplit = readCar.split("\\|");
		String[] splitCar = bookingSplit[0].split(":");
		int bookingLength = bookingSplit.length;
		
		String regNo = splitCar[0];
		String make = splitCar[1];
		String model = splitCar[2];
		String driverName = splitCar[3];
		int passengerCapacity = Integer.parseInt(splitCar[4]);
		
		String[] bookingFeeSplit = splitCar[6].split("\\|");
		float bookingFee = Float.parseFloat((bookingFeeSplit[0]));
		
		if (bookingFee > 1.5) {
			int refreshmentsLength = splitCar.length - 7;
			String[] refreshments = new String[refreshmentsLength];
			
			for (int i = 7, x = 0; i<splitCar.length;i++,x++) {
				String [] refreshmentsSplit = splitCar[i].split("\\s+");
				StringBuilder refreshment = new StringBuilder();
				
				for (int y = 2; y<refreshmentsSplit.length;y++) {
					refreshment.append(refreshmentsSplit[y] + " ");
				}
				
				refreshments[x] = refreshment.toString().trim();
			}
			
			SilverServiceCar createCar = new SilverServiceCar(regNo, make, model, driverName, passengerCapacity, bookingFee, refreshments);
			cars[itemCount] = createCar;
			itemCount++;
			
			
			// createSilverCar(regNo, make, model, driverName, passengerCapacity, bookingFee, );
		}
		else {
			
			Car createCar = new Car(regNo, make, model, driverName, passengerCapacity);
			cars[itemCount] = createCar;
			itemCount++;
		}
		
		if (bookingLength > 1) {
			for (int count = 1; count < bookingLength;count++) {
				String[] tempBooking = bookingSplit[count].split(":");
				String date = tempBooking[2];
				int day = Integer.parseInt(date.substring(0,2));
				int month = Integer.parseInt(date.substring(2,4));
				int year = Integer.parseInt(date.substring(4,8));
				DateTime bookingDate = new DateTime(day,month,year);
				
				int passengerCount = Integer.parseInt(tempBooking[4]);
				float distanceTravelled = Float.parseFloat(tempBooking[5]);
				
				String[] nameSplit = tempBooking[3].split("\\s+");
				String firstName = nameSplit[0];
				String lastName = nameSplit[1];
				
				try {
					if (distanceTravelled > 0.0) {
						cars[itemCount-1].book(firstName, lastName, bookingDate, passengerCount);
						cars[itemCount-1].completeBooking(firstName, lastName, bookingDate, distanceTravelled);
					
					}
					else {
						cars[itemCount-1].book(firstName, lastName, bookingDate, passengerCount);
					}
				}
				catch(InvalidBooking e) {
					
				}
				
				
			}
		}
		
	}
}