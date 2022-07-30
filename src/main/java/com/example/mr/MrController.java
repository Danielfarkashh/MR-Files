package com.example.mr;

import interfaces.DataAddListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.DataHolder;
import model.MRObject;
import utilities.JsonUtilities;
import utilities.Type;
import utilities.Validator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;


public class MrController implements Initializable, DataAddListener {
    private static final String RIGHT_MOUSE_CLICK = "SECONDARY";


    @FXML
    private Text mFilePath;

    @FXML
    private TableView<MRObject> mTableView;

    private final ObservableList<MRObject> mData = FXCollections.observableArrayList();

    /**
     * When clicking on Load Button , parse the data file and inject it to the table and update the path text view
     */
    public void onLoadButtonClicked() {
        File selectedFile = openChooser();
        if (selectedFile != null) {
            try {
                String path = selectedFile.getAbsolutePath();
                DataHolder dataHolder = JsonUtilities.getJsonModelFromFile(path);
                mData.clear();
                List<MRObject> list = JsonUtilities.convertListOfJsonToMrObjectlist(dataHolder.getData());
                mData.addAll(list);
                mFilePath.setText(path);
            } catch (Exception e) {
                showErrorMsg(e);
            }
        }
    }

    public void onSaveButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json file (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            saveTextToFile(JsonUtilities.convertDataToJson(mData), file);
        }

    }


    /**
     * @param content the json string
     * @param file    where to save
     */
    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ignored) {
        }
    }


    /**
     * @return The selected file , if no file selected the function will return null
     */
    private File openChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json File", "*.json"));
        fileChooser.setInitialDirectory(new File(Paths.get(".").toAbsolutePath().normalize().toString()));
        return fileChooser.showOpenDialog(null);
    }

    /**
     * @param e show the excpetion msg in error window
     */
    private void showErrorMsg(Exception e) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("File is not valid");
        errorAlert.setContentText(String.format("Error - %s", e.getLocalizedMessage()));
        errorAlert.showAndWait();
    }


    /**
     * @param nameOfClumn create column for the table with the given nameOfColumn
     * @return
     */
    private TableColumn<MRObject, String> createColumn(String nameOfClumn) {
        TableColumn<MRObject, String> col = new TableColumn(nameOfClumn);
        col.setCellFactory(TextFieldTableCell.forTableColumn());
        return col;
    }

    /**
     * Create all the columns for the table
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Vendor
        TableColumn<MRObject, String> vendorCol = createColumn("MR VENDOR");
        vendorCol.setCellValueFactory(item -> item.getValue().mVendor);
        vendorCol.setOnEditCommit(event -> {
            try {
                Validator.validateInput(Type.Vendor, event.getNewValue());
                event.getRowValue().setVendor(event.getNewValue());
            } catch (Exception e) {
                showErrorMsg(e);
            }
            mTableView.refresh();
        });


        TableColumn<MRObject, String> powerCol = createColumn("Power");
        powerCol.setCellValueFactory(item -> item.getValue().mPower);
        powerCol.setOnEditCommit(event -> {
            try {
                Validator.validateInput(Type.Power, event.getNewValue());
                event.getRowValue().setPower(event.getNewValue());
            } catch (Exception e) {
                showErrorMsg(e);
            }
            mTableView.refresh();
        });


        TableColumn<MRObject, String> versionCol = createColumn("Version");
        versionCol.setCellValueFactory(item -> item.getValue().mVersion);


        TableColumn<MRObject, String> osCol = createColumn("Os");
        osCol.setCellValueFactory(item -> item.getValue().mOs);

        TableColumn<MRObject, String> vncCol = createColumn("VNC Supported");
        vncCol.setCellValueFactory(item -> item.getValue().mVnc);
        vncCol.setOnEditCommit(event -> {
            try {
                Validator.validateInput(Type.VNC, String.valueOf(event.getNewValue()));
                event.getRowValue().setVnc(event.getNewValue());
            } catch (Exception e) {
                showErrorMsg(e);
            }
            mTableView.refresh();
        });

        TableColumn<MRObject, String> commentsCol = createColumn("Comments");
        commentsCol.setCellValueFactory(item -> item.getValue().mComments);

        mTableView.getColumns().addAll(vendorCol, powerCol, versionCol, osCol, vncCol, commentsCol);
        mTableView.setItems(mData);
        mTableView.setRowFactory(mrObjectTableView -> {
            final TableRow<MRObject> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton().name().equals(RIGHT_MOUSE_CLICK)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        mData.remove(row.getItem());
                    }
                }
            });
            return row;
        });
        mTableView.setEditable(true);
    }

    public void onAddButtonClicked() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("add_data_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 450);
            AddDataController dataController = fxmlLoader.getController();
            dataController.setListener(this);
            stage.setTitle("Add new data");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAddData(MRObject data) {
        mData.add(data);
    }
}