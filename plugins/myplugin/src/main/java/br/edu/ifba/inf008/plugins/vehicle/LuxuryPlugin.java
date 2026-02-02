package br.edu.ifba.inf008.plugins.vehicle;

import br.edu.ifba.inf008.interfaces.IVehiclePlugin;

public class LuxuryPlugin implements IVehiclePlugin {

    @Override
    public String getType() {
        return "LUXURY";
    }

    @Override
    public double calculatePrice(double basePrice, int days) {
        return (basePrice * 1.6) * days;
    }
}