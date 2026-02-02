package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.ICore;
import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.model.Rental;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.time.format.DateTimeFormatter;

public class RentalHistoryPlugin implements IPlugin {

    @Override
    public boolean init(ICore core) {
        TableView<Rental> table = new TableView<>();

        TableColumn<Rental, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCustomer().getName()));

        TableColumn<Rental, String> colVeiculo = new TableColumn<>("Veículo");
        colVeiculo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getVehicle().getModel()));

        TableColumn<Rental, String> colData = new TableColumn<>("Início");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colData.setCellValueFactory(data -> {
            if(data.getValue().getStartDate() != null)
                return new SimpleStringProperty(data.getValue().getStartDate().format(dtf));
            return new SimpleStringProperty("-");
        });

        TableColumn<Rental, Double> colValor = new TableColumn<>("Total (R$)");
        colValor.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        table.getColumns().addAll(colCliente, colVeiculo, colData, colValor);

        try {
            if (core.getDataProvider() != null) {
                table.getItems().addAll(core.getDataProvider().getAllRentals());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox content = new VBox(table);
        Tab tab = new Tab("Histórico Geral");
        tab.setContent(content);

        core.getUIController().addTab(tab);
        return true;
    }
}