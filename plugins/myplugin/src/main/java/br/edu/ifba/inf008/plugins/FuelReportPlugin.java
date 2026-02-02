package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IDataProvider;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.model.Vehicle;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuelReportPlugin implements IPlugin {

    @Override
    public boolean init(ICore core) {
        if (core == null) return false;

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Veículos por Tipo de Combustível");

        IDataProvider provider = core.getDataProvider();
        Map<String, Integer> fuelCount = new HashMap<>();

        try {
            // Varre todos os tipos para pegar todos os veículos (idealmente teria um método getAllVehicles no provider)
            for (Vehicle.VehicleType tipo : Vehicle.VehicleType.values()) {
                List<Vehicle> lista = provider.getVehiclesByType(tipo);
                for (Vehicle v : lista) {
                    String fuel = v.getFuelType();
                    fuelCount.put(fuel, fuelCount.getOrDefault(fuel, 0) + 1);
                }
            }

            for (Map.Entry<String, Integer> entry : fuelCount.entrySet()) {
                pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Tab tab = new Tab("Relatório Combustível", pieChart);
        core.getUIController().addTab(tab);

        return true;
    }
}