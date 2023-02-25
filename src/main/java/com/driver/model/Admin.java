package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Admin{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String password;

    // Mapping of Admin(P) and Customer(C)
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Customer> listOfCustomers = new ArrayList<>();

    public List<Customer> getListOfCustomers() {
        return listOfCustomers;
    }

    public void setListOfCustomers(List<Customer> listOfCustomers) {
        this.listOfCustomers = listOfCustomers;
    }
    //

    //Mapping of Admin(P) and Driver(C)
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Driver> listOfDrivers = new ArrayList<>();

    public List<Driver> getListOfDrivers() {
        return listOfDrivers;
    }

    public void setListOfDrivers(List<Driver> listOfDrivers) {
        this.listOfDrivers = listOfDrivers;
    }
    //

    // to get the list of customers and list of drivers we have to do mapping.
    //Admin to customer.
    @OneToMany(mappedBy = "admin")
    private List<Customer> customerList = new ArrayList<>();

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
    //

    //Admin to Driver
    @OneToMany(mappedBy = "admin")
    private List<Driver> driverList = new ArrayList<>();

    public List<Driver> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }
    //

    //


    public Admin() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}