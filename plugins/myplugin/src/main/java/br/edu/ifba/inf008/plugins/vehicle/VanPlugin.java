package br.edu.ifba.inf008.plugins.vehicle;

import br.edu.ifba.inf008.interfaces.IVehiclePlugin;

public class VanPlugin implements IVehiclePlugin {
    @Override
    public String getType() { return "VAN"; }
    @Override
    public double calculatePrice(double basePrice, int days) {
        return (basePrice * 1.5) * days; // 50% a mais
    }
}