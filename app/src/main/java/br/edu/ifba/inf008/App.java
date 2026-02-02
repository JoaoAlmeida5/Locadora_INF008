package br.edu.ifba.inf008;

import br.edu.ifba.inf008.interfaces.IVehiclePlugin;
import br.edu.ifba.inf008.shell.Core;
import br.edu.ifba.inf008.model.Customer;
import br.edu.ifba.inf008.model.Rental;
import br.edu.ifba.inf008.model.Vehicle;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        TabPane rootTabPane = new TabPane();
        VBox mainLayout = new VBox(menuBar, rootTabPane);

        Core core = new Core(rootTabPane, menuBar);

        System.out.println(" >>> CARREGANDO PLUGINS... ");
        core.getPluginController().init();
        core.getPluginController().startPlugins();

        createRentalTab(rootTabPane, core);

        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setTitle("Locadora de Veículos - INF008");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createRentalTab(TabPane tabPane, Core core) {
        MariaDBDataProvider dataProvider = core.getDataProvider();

        Label lblCliente = new Label("Selecione o Cliente (Email):");
        ComboBox<Customer> cmbCustomers = new ComboBox<>();
        cmbCustomers.setConverter(new StringConverter<>() {
            @Override public String toString(Customer c) { return c == null ? "" : c.getEmail(); }
            @Override public Customer fromString(String string) { return null; }
        });

        try {
            cmbCustomers.getItems().addAll(dataProvider.getAllClients()); // Use getAllClients corrigido
        } catch (Exception ex) { ex.printStackTrace(); }

        Label lblTipo = new Label("Tipo de Veículo:");
        ComboBox<Vehicle.VehicleType> cmbType = new ComboBox<>();
        cmbType.getItems().addAll(Vehicle.VehicleType.values());

        TableView<Vehicle> tableVehicles = new TableView<>();

        TableColumn<Vehicle, String> colMake = new TableColumn<>("Marca");
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));

        TableColumn<Vehicle, String> colModel = new TableColumn<>("Modelo");
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Vehicle, Integer> colYear = new TableColumn<>("Ano");
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Vehicle, String> colFuel = new TableColumn<>("Combustível");
        colFuel.setCellValueFactory(new PropertyValueFactory<>("fuelType"));

        TableColumn<Vehicle, String> colTrans = new TableColumn<>("Câmbio");
        colTrans.setCellValueFactory(new PropertyValueFactory<>("transmission"));

        TableColumn<Vehicle, Double> colMileage = new TableColumn<>("Km");
        colMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));

        tableVehicles.getColumns().addAll(colMake, colModel, colYear, colFuel, colTrans, colMileage);
        tableVehicles.setPrefHeight(200);

        Button btnBuscar = new Button("Buscar Veículos Disponíveis");
        Label lblResultado = new Label();

        btnBuscar.setOnAction(e -> {
            Vehicle.VehicleType tipo = cmbType.getValue();
            if (tipo != null) {
                try {
                    List<Vehicle> veiculos = dataProvider.getVehiclesByType(tipo);
                    tableVehicles.getItems().setAll(veiculos);
                    lblResultado.setText(veiculos.size() + " veículos encontrados.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        GridPane gridInputs = new GridPane();
        gridInputs.setHgap(10); gridInputs.setVgap(10);

        DatePicker dtStart = new DatePicker();
        DatePicker dtEnd = new DatePicker();
        TextField txtPickup = new TextField();
        TextField txtDailyRate = new TextField(); // Usuário informa, conforme regra
        TextField txtInsurance = new TextField(); // Usuário informa, conforme regra

        gridInputs.addRow(0, new Label("Início:"), dtStart, new Label("Fim:"), dtEnd);
        gridInputs.addRow(1, new Label("Retirada:"), txtPickup);
        gridInputs.addRow(2, new Label("Valor Diária (R$):"), txtDailyRate, new Label("Seguro (R$):"), txtInsurance);

        Button btnAlugar = new Button("CONFIRMAR LOCAÇÃO");
        btnAlugar.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        btnAlugar.setOnAction(e -> {
            Customer cliente = cmbCustomers.getValue();
            Vehicle veiculo = tableVehicles.getSelectionModel().getSelectedItem();

            if (cliente != null && veiculo != null && dtStart.getValue() != null && dtEnd.getValue() != null) {
                try {
                    double userRate = Double.parseDouble(txtDailyRate.getText().replace(",", "."));
                    double insurance = Double.parseDouble(txtInsurance.getText().replace(",", "."));

                    Rental aluguel = new Rental();
                    aluguel.setCustomer(cliente);
                    aluguel.setVehicle(veiculo);
                    aluguel.setStartDate(dtStart.getValue().atStartOfDay());
                    aluguel.setEndDate(dtEnd.getValue().atStartOfDay());

                    long days = ChronoUnit.DAYS.between(aluguel.getStartDate(), aluguel.getEndDate());
                    if (days <= 0) days = 1;

                    var pluginController = core.getPluginController();
                    IVehiclePlugin plugin = pluginController.getVehiclePlugin(veiculo.getType().toString());

                    BigDecimal valorFinal;
                    if (plugin != null) {
                        double preco = plugin.calculatePrice(veiculo, userRate, (int) days);
                        valorFinal = BigDecimal.valueOf(preco + insurance); // Soma seguro aqui ou dentro do plugin? Spec diz "Valor total = ... + taxas". Seguro é separado na UI, vamos somar no final.
                    } else {
                        valorFinal = BigDecimal.valueOf((userRate * days) + insurance);
                    }

                    aluguel.setTotalValue(valorFinal.doubleValue());
                    dataProvider.saveRental(aluguel);

                    new Alert(Alert.AlertType.INFORMATION, String.format("Sucesso! Total: R$ %.2f", valorFinal)).show();
                    btnBuscar.fire(); // Refresh

                } catch (NumberFormatException nfe) {
                    new Alert(Alert.AlertType.ERROR, "Verifique os valores numéricos.").show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Preencha todos os campos e selecione um veículo.").show();
            }
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(lblCliente, cmbCustomers, lblTipo, cmbType, btnBuscar,
                new Label("Selecione um veículo:"), tableVehicles, lblResultado,
                new Separator(), new Label("Dados da Locação:"), gridInputs,
                new Separator(), btnAlugar);

        Tab tab = new Tab("Nova Locação");
        tab.setContent(layout);
        tab.setClosable(false);
        tabPane.getTabs().add(tab);
    }

    public static void main(String[] args) { launch(args); }
}