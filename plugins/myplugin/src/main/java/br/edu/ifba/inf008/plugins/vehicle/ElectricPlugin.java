package br.edu.ifba.inf008.plugins.vehicle;

import br.edu.ifba.inf008.interfaces.IVehiclePlugin;
import br.edu.ifba.inf008.model.Vehicle;
import java.util.Map;

public class ElectricPlugin implements IVehiclePlugin {
    @Override
    public String getType() { return "ELECTRIC"; }

    @Override
    public double calculatePrice(Vehicle vehicle, double dailyRate, int days) {
        double total = dailyRate * days;

        Map<String, Double> fees = vehicle.getAdditionalFees();
        for (Map.Entry<String, Double> entry : fees.entrySet()) {
            if (entry.getKey().endsWith("_fee")) {
                total += entry.getValue();
            }
        }
        return total;
    }
}