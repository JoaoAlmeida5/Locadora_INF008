package br.edu.ifba.inf008;

import br.edu.ifba.inf008.interfaces.IDataProvider;
import br.edu.ifba.inf008.model.Customer;
import br.edu.ifba.inf008.model.Rental;
import br.edu.ifba.inf008.model.Vehicle;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MariaDBDataProvider implements IDataProvider {

    @Override
    public List<Customer> getAllClients() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT first_name, last_name, tax_id, email, phone FROM customers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                Customer c = new Customer(rs.getString("tax_id"), fullName, rs.getString("email"), rs.getString("phone"));
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Vehicle> getVehiclesByType(Vehicle.VehicleType type) {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT v.license_plate, v.make, v.model, v.year, v.fuel_type, " +
                "v.transmission, v.mileage, vt.daily_rate, vt.type_name, vt.additional_fees " +
                "FROM vehicles v " +
                "JOIN vehicle_types vt ON v.type_id = vt.type_id " +
                "WHERE vt.type_name = ? AND v.status = 'AVAILABLE'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehicle v = new Vehicle(
                        rs.getString("license_plate"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission"),
                        rs.getDouble("mileage"),
                        rs.getDouble("daily_rate"),
                        rs.getString("type_name")
                );

                String jsonFees = rs.getString("additional_fees");
                if (jsonFees != null && !jsonFees.isEmpty()) {
                    Map<String, Double> fees = new HashMap<>();
                    String content = jsonFees.replace("{", "").replace("}", "").replace("\"", "");
                    String[] pairs = content.split(",");
                    for (String pair : pairs) {
                        String[] entry = pair.split(":");
                        if (entry.length == 2) {
                            try {
                                fees.put(entry[0].trim(), Double.parseDouble(entry[1].trim()));
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                    v.setAdditionalFees(fees);
                }
                list.add(v);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void saveRental(Rental rental) {
        String sql = "INSERT INTO rentals (" +
                "   customer_id, vehicle_id, rental_type, start_date, scheduled_end_date, " +
                "   pickup_location, base_rate, insurance_fee, total_amount, rental_status" +
                ") " +
                "SELECT " +
                "   (SELECT customer_id FROM customers WHERE tax_id = ?), " +
                "   (SELECT vehicle_id FROM vehicles WHERE license_plate = ?), " +
                "   'DAILY', " +
                "   ?, " +
                "   ?, " +
                "   'Main Office', " +
                "   (SELECT daily_rate FROM vehicle_types vt JOIN vehicles v ON v.type_id = vt.type_id WHERE v.license_plate = ?), " + // 5. Busca taxa base pela placa
                "   0, " +
                "   ?, " +
                "   'ACTIVE'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rental.getCustomer().getCpf());
            stmt.setString(2, rental.getVehicle().getId());

            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.plusDays(rental.getDays());

            stmt.setTimestamp(3, Timestamp.valueOf(start));
            stmt.setTimestamp(4, Timestamp.valueOf(end));

            stmt.setString(5, rental.getVehicle().getId());
            stmt.setDouble(6, rental.getTotalPrice());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<Rental> getAllRentals(){
        return new ArrayList<>();
    }

    @Override
    public void saveClient(Customer client) {
        String sql = "INSERT INTO customers (first_name, last_name, tax_id, email, customer_type) VALUES (?, ?, ?, ?, 'INDIVIDUAL')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String[] names = client.getName().split(" ", 2);
            String firstName = names[0];
            String lastName = names.length > 1 ? names[1] : "";

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, client.getCpf());
            stmt.setString(4, "email_placeholder_" + client.getCpf() + "@example.com");
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    @Override public void saveVehicle(Vehicle vehicle) {}
    @Override public List<Vehicle> getAllVehicles() { return new ArrayList<>(); }
    @Override public Vehicle findVehicleByPlate(String plate) { return null; }
}