package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.repository.CabRepository;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Autowired
	CabRepository cabRepository;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer = customerRepository2.findById(customerId).get();
		List<TripBooking> tripBookingList = customer.getTripBookingList();

		for(TripBooking tripBooking : tripBookingList){
			if(tripBooking.getStatus() == TripStatus.CONFIRMED){
				tripBooking.setStatus(TripStatus.CANCELED);
			}
		}
		customerRepository2.delete(customer);
	}

	@Override
	//make a Dto for all the following parameters
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query

		TripBooking tripBooking = new TripBooking();
		Driver availableDriver = null;
		List<Driver> allDrivers = driverRepository2.findAll();
		for(Driver driver : allDrivers){
			if(driver.getCab().getAvailable() == true){
				if(availableDriver == null || availableDriver.getDriverId() > driver.getDriverId())
					availableDriver = driver;
			}
		}

		if(availableDriver == null){
			throw new Exception("No cab available!");
		}
		// driver id available
		Customer customer = customerRepository2.findById(customerId).get();
		//setting the attributes.
		tripBooking.setCustomer(customer);
		tripBooking.setDriver(availableDriver);
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setStatus(TripStatus.CONFIRMED);
		availableDriver.getCab().setAvailable(false);

		int rates = availableDriver.getCab().getPerKmRate();
		int bill = rates * distanceInKm;
		tripBooking.setBill(bill);

		customer.getTripBookingList().add(tripBooking);
		customerRepository2.save(customer);

		availableDriver.getTripBookingList().add(tripBooking);
		driverRepository2.save(availableDriver);

		return tripBooking;

	}


	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.CANCELED);
		tripBooking.setBill(0);
		tripBooking.getDriver().getCab().setAvailable(true);
		tripBookingRepository2.save(tripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.COMPLETED);

		int distance =tripBooking.getDistanceInKm();
		int rates = tripBooking.getDriver().getCab().getPerKmRate();

		tripBooking.setBill(distance * rates);
		tripBooking.getDriver().getCab().setAvailable(true);
		tripBookingRepository2.save(tripBooking);




	}
}
