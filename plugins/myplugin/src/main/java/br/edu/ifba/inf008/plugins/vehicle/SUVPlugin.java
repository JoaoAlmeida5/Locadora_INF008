package br.edu.ifba.inf008.plugins.vehicle;

import br.edu.ifba.inf008.interfaces.IVehiclePlugin;
import br.edu.ifba.inf008.model.Vehicle;
import java.util.Map;

public class SUVPlugin implements IVehiclePlugin {

    @Override
    public String getType() {
        return "SUV";
    }

    @Override
    public double calculatePrice(Vehicle vehicle, double dailyRate, int days) {
        double total = dailyRate * days;

        Map<String, Double> fees = vehicle.getAdditionalFees();

        if (fees.containsKey("offroad_fee")) {
            total += fees.get("offroad_fee");
        }

        return total;
    }
}