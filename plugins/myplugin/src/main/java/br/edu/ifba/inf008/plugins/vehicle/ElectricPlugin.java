package br.edu.ifba.inf008.plugins.vehicle;

import br.edu.ifba.inf008.interfaces.IVehiclePlugin;

public class ElectricPlugin implements IVehiclePlugin {
    @Override
    public String getType() { return "ELECTRIC"; }
    @Override
    public double calculatePrice(double basePrice, int days) {
        return (basePrice * 1.2) * days; // 20% a mais
    }
}