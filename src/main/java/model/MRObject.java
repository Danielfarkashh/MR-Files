package model;

import com.google.gson.JsonObject;
import javafx.beans.property.SimpleStringProperty;

public class MRObject {

    public final SimpleStringProperty mVendor;
    public final SimpleStringProperty mPower;
    public final SimpleStringProperty mVersion;
    public final SimpleStringProperty mOs;
    public final SimpleStringProperty mVnc;
    public final SimpleStringProperty mComments;


    public MRObject(String vendor, String power, String version, String os, boolean vnc, String comments) {
        this.mVendor = new SimpleStringProperty(vendor);
        this.mPower = new SimpleStringProperty(power);
        this.mVersion = new SimpleStringProperty(version);
        this.mOs = new SimpleStringProperty(os);
        this.mVnc = new SimpleStringProperty(String.valueOf(vnc));
        this.mComments = new SimpleStringProperty(comments);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MR_VENDOR", mVendor.getValue());
        jsonObject.addProperty("Power", mPower.getValue());
        jsonObject.addProperty("Version", mVersion.getValue());
        jsonObject.addProperty("Os", mOs.getValue());
        jsonObject.addProperty("VNC_Supported", Boolean.valueOf(mVnc.getValue()));
        jsonObject.addProperty("Comments", mComments.getValue());
        return jsonObject;

    }


    public void setVendor(String vendor) {
        mVendor.set(vendor);
    }

    public void setPower(String power) {
        mPower.set(power);
    }

    public void setVnc(String vnc) {
        mVnc.set(vnc);
    }

    public static MRObject convertJsonToMrObject(MrObjectJsonData jsonData) {
        return new MRObject(jsonData.MR_VENDOR, jsonData.Power, jsonData.Version, jsonData.Os, jsonData.VNC_Supported, jsonData.Comments);
    }


}
