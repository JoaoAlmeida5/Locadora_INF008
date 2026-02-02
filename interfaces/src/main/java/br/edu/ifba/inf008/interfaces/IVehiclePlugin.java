package br.edu.ifba.inf008.interfaces;

import br.edu.ifba.inf008.model.Vehicle;

public interface IVehiclePlugin extends IPlugin {
    String getType();
    double calculatePrice(Vehicle vehicle, double dailyRate, int days);

    @Override
    default boolean init(ICore core) {
        return true;
    }
}