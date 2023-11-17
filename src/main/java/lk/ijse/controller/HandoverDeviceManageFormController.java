package lk.ijse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dto.CustomerDto;
import lk.ijse.dto.DeviceDto;
import lk.ijse.dto.tm.DeviceTm;
import lk.ijse.model.CustomerModel;
import lk.ijse.model.HandoverDeviceModel;
import lk.ijse.model.OrdersModel;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class HandoverDeviceManageFormController {

    @FXML
    private TableColumn<?, ?> colCost;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDeviceName;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colProblem;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableView<DeviceTm> tblDevice;

    @FXML
    private JFXTextField txtCost;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtProblem;

    @FXML
    private JFXTextField txtStatus;

    @FXML
    private JFXTextField txtSearch;

    public void initialize(){
        setCellValueFactory();
        generateNextDeviceId();
        loadAllItems();
    }

    public void setCellValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("deviceId"));
        colDeviceName.setCellValueFactory(new PropertyValueFactory<>("dName"));
        colProblem.setCellValueFactory(new PropertyValueFactory<>("problem"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    public void loadAllItems(){
        ObservableList<DeviceTm> obList = FXCollections.observableArrayList();

        try {
            List<DeviceDto> dtolist = HandoverDeviceModel.getAllDevices();

            for(DeviceDto dto : dtolist){
                obList.add(
                        new DeviceTm(
                                dto.getDeviceId(),
                                dto.getDName(),
                                dto.getProblem(),
                                dto.getStatus(),
                                dto.getCost(),
                                dto.getDate()
                        )
                );
            }
            tblDevice.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        try {
            String id = txtId.getText();
            String name = txtName.getText();
            String problem = txtProblem.getText();
            String status = txtStatus.getText();
            String cost = txtCost.getText();
            String date = "2023.12.12";
            String cId = "C001";

            if(id.isEmpty() || name.isEmpty() || cost.isEmpty() || problem.isEmpty() || status.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Fill all fields");
                alert.showAndWait();
                return;
            }

            if(!validateDevice()){
                return;
            }

            DeviceDto deviceDto = new DeviceDto(id, name, problem, status, cost, date ,cId);

            boolean isAdded = HandoverDeviceModel.setDevice(deviceDto);
            if (isAdded) {
                Alert alert =new Alert(Alert.AlertType.CONFIRMATION, "Success");
                alert.showAndWait();
                loadAllItems();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Something went wrong");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateDevice(){

        boolean matches = Pattern.matches("[E][0-9]{3,}", txtId.getText());
        if (!matches){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid device id");
            alert.showAndWait();
            return false;
        }

        boolean matches1 = Pattern.matches("[A-Za-z]{3,}", txtName.getText());
        if(!matches1){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid name");
            alert.showAndWait();
            return false;
        }

        boolean matches2 = Pattern.matches("[A-Za-z]{3,}", txtProblem.getText());
        if (!matches2) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid Problem");
            alert.showAndWait();
            return false;
        }

        boolean matches3 = Pattern.matches("\\w\\D", txtStatus.getText());
        if (!matches3) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid status");
            alert.showAndWait();
            return false;
        }

        boolean matches4 = Pattern.matches("[0-9]{0,}\\W", txtCost.getText());
        if (!matches4) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid cost");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            boolean isDeleted = HandoverDeviceModel.deleteDevice(id);
            if (isDeleted) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Success");
                alert.showAndWait();
                loadAllItems();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Something went wrong");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name= txtName.getText();
        String problem = txtProblem.getText();
        String status = txtStatus.getText();
        String cost = txtCost.getText();
        String date = "2023.12.12";
        String cId = "C001";

        if(id.isEmpty() || name.isEmpty() || problem.isEmpty() || status.isEmpty() || cost.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Fill all fields");
            alert.showAndWait();
            return;
        }
        DeviceDto dto = new DeviceDto(id, name, problem, status, cost, date ,cId);

        try {
            boolean isUpdated = HandoverDeviceModel.updateDevice(dto);
            if (isUpdated){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Success");
                alert.showAndWait();
                loadAllItems();
                clearFields();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Something went wrong");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String id = txtSearch.getText();

        if (id!= null &&!id.isEmpty()) {
            try {
                DeviceDto deviceDto = HandoverDeviceModel.getDevice(id);

                if(deviceDto != null){
                    txtId.setText(deviceDto.getCId());
                    txtName.setText(deviceDto.getDName());
                    txtProblem.setText(deviceDto.getProblem());
                    txtStatus.setText(deviceDto.getStatus());
                    txtCost.setText(deviceDto.getCost());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Customer not found");
                    alert.showAndWait();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Customer name is null or empty");
        }
    }

    private void generateNextDeviceId() {
        try {
            String deviceId = HandoverDeviceModel.generateNextDeviceId();
            txtId.setText(deviceId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtProblem.setText("");
        txtStatus.setText("");
        txtCost.setText("");
        txtSearch.setText("");
    }

}
