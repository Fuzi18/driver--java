package hu.petrik.driver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DriverController {
    @FXML
    private TableView<Driver> driverTable;
    @FXML
    private TableColumn<Driver, String> pilotaCol;
    @FXML
    private TableColumn<Driver, Integer> korCol;
    @FXML
    private TableColumn<Driver, String> nemzetisegCol;
    private DriverDB db;
    @FXML
    private TextField pilotaInput;
    @FXML
    private TextField nemzetisegInput;
    @FXML
    private Spinner<Integer> korInput;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    private int updateId;

    @FXML
    private void initialize() {
        pilotaCol.setCellValueFactory(new PropertyValueFactory<>("pilota"));
        korCol.setCellValueFactory(new PropertyValueFactory<>("kor"));
        nemzetisegCol.setCellValueFactory(new PropertyValueFactory<>("nemzetiseg"));
        korInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50));
        try {
            db = new DriverDB();
            readDrivers();
        } catch (SQLException e) {
            Platform.runLater(() -> {
                sqlAlert(e);
                Platform.exit();
            });
        }
    }

    private void sqlAlert(SQLException e) {
        alert(Alert.AlertType.ERROR,
                "Hiba történt az adatbázis kapcsolat kialakításakor",
                e.getMessage());
    }

    private void readDrivers() throws SQLException {
        List<Driver> drivers = db.readDriver();
        driverTable.getItems().clear();
        driverTable.getItems().addAll(drivers);
    }

    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    @FXML
    public void updateClick(ActionEvent event) {
        Driver selected = driverTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            setStateToUpdate();
            pilotaInput.setText(selected.getPilota());
            korInput.getValueFactory().setValue(selected.getKor());
            nemzetisegInput.setText(selected.getNemzetiseg());
            updateId = selected.getId();
        }
    }

    private void setStateToUpdate() {
        submitButton.setText("Update");
        driverTable.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }
    private void setStateToSubmit() {
        submitButton.setText("Submit");
        driverTable.setDisable(false);
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
    }
    @FXML
    public void deleteClick(ActionEvent actionEvent) {
        Driver selected = getSelectedDriver();
        if (selected == null) return;

        Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION,
                "Biztos, hogy törölni szeretné a viláasztott pilotat?", "");
        if (optionalButtonType.isEmpty() || !(optionalButtonType.get().equals(ButtonType.OK)) && !(optionalButtonType.get().equals(ButtonType.YES)))  {
            return;
        }

        try {
            if (db.deleteDriver(selected.getId())) {
                alert(Alert.AlertType.WARNING, "Sikeres törlés", "");
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen törlés", "");
            }
            readDrivers();
        } catch (SQLException e) {
            sqlAlert(e);
        }
        setStateToSubmit();
    }



    private Driver getSelectedDriverNullMatter() {
        int selectedIndex = driverTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return null;
        }
        return driverTable.getSelectionModel().getSelectedItem();

    }

    private Driver getSelectedDriver() {
        int selectedIndex = driverTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert(Alert.AlertType.WARNING,
                    "Előbb válasszon ki pilotat a táblázatból", "");
            return null;
        }
        return driverTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void submitClick(ActionEvent actionEvent) {
        String pilota = pilotaInput.getText().trim();
        int kor = korInput.getValue();
        String nemzetiseg = nemzetisegInput.getText().trim();
        if (pilota.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Pilota megadása kötelező", "");
            return;
        }
        if (nemzetiseg.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Nemzetiseg megadása kötelező", "");
            return;
        }
        if (submitButton.getText().equals("Update")) {
            updateDriver(pilota, kor, nemzetiseg);
        } else {
            createDriver(pilota, kor, nemzetiseg);
        }
    }

    private void updateDriver(String pilota, int kor, String nemzetiseg) {
        Driver driver = new Driver(updateId, pilota, kor, nemzetiseg);
        try {
            if (db.updateDriver(driver)) {
                alert(Alert.AlertType.WARNING, "Sikeres módosítás", "");
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen módosítás", "");
            }
            readDrivers();
            setStateToSubmit();
        } catch (SQLException e) {
            sqlAlert(e);
        }
    }

    


    private void createDriver(String pilota, int kor, String nemzetiseg) {
        Driver driver = new Driver(pilota, kor, nemzetiseg);
        try {
            if (db.createDriver(driver)) {
                alert(Alert.AlertType.WARNING, "Sikeres felvétel", "");
                resetForm();
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen felvétel", "");
            }
            readDrivers();
        } catch (SQLException e) {
            sqlAlert(e);
        }
    }

    private void resetForm() {
        pilotaInput.setText("");
        korInput.getValueFactory().setValue(0);
        nemzetisegInput.setText("");

        submitButton.setText("Submit");
        driverTable.setDisable(false);
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
    }
    @FXML
    public void cancelClick(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}