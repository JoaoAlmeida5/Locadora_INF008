package br.edu.ifba.inf008.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Rental {
    private Customer customer;
    private Vehicle vehicle;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double totalPrice;

    public Rental() {}

    public Rental(Customer customer, Vehicle vehicle, LocalDateTime start, LocalDateTime end) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.startDate = start;
        this.endDate = end;
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalValue(double totalPrice) { this.totalPrice = totalPrice; }

    public long getDays() {
        if (startDate == null || endDate == null) return 0;
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return days <= 0 ? 1 : days;
    }
}