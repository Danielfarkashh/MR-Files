package com.example.mr;


import exceptions.UnvalidValueException;
import interfaces.DataAddListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import model.MRObject;
import utilities.Type;
import utilities.Validator;

public class AddDataController {

    @FXML
    public TextArea mVendor, mPower, mVersion, mOs, mComments;

    @FXML
    public CheckBox mVncSupported;

    private DataAddListener mListener;

    public void onAddButtonClicked() {
        String vendor = mVendor.getText();
        String power = mPower.getText();
        String version = mVersion.getText();
        String os = mOs.getText();
        String comments = mComments.getText();
        boolean isVncSupported = mVncSupported.isSelected();
        if (vendor.isEmpty() || power.isEmpty() || version.isEmpty() || os.isEmpty()) {
            showErrorMsg("All inputs must be filled (except comments)");
            return;
        }
        try {
            Validator.validateInput(Type.Vendor, vendor);
            Validator.validateInput(Type.Power, power);
        } catch (UnvalidValueException exception) {
            showErrorMsg(exception.getLocalizedMessage());
            return;
        }
        MRObject newDataToAddToTheTable = new MRObject(vendor, power, version, os, isVncSupported, comments);
        mListener.onAddData(newDataToAddToTheTable);
        mVendor.getScene().getWindow().hide();
    }

    private void showErrorMsg(String msg) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("File is not valid");
        errorAlert.setContentText(String.format("Error - %s", msg));
        errorAlert.showAndWait();
    }


    public void setListener(DataAddListener mrController) {
        mListener = mrController;
    }
}