package utilities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.collections.ObservableList;
import model.DataHolder;
import model.MRObject;
import model.MrObjectJsonData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JsonUtilities {


    /**
     * @param path path of the given file
     * @return all the parsed json file to java object(DataHolder)
     * @throws FileNotFoundException
     */
    public static DataHolder getJsonModelFromFile(String path) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(path));
        //Given the DataHolder.Class structer -> parse the json file
        return gson.fromJson(reader, DataHolder.class);
    }


    /**
     * @param list of the json data
     * @return list of the "smart" MRobject
     */
    public static List<MRObject> convertListOfJsonToMrObjectlist(List<MrObjectJsonData> list) {
        List<MRObject> res = new ArrayList<>();
        for (MrObjectJsonData data : list) {

            res.add(MRObject.convertJsonToMrObject(data));

        }
        return res;
    }


    /**
     * @param list -
     * @return convert the list to json
     */
    public static String convertDataToJson(ObservableList<MRObject> list) {
        JsonArray jsonArray = new JsonArray();
        for (MRObject object : list) {
            jsonArray.add(object.toJson());
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("data", jsonArray);
        return jsonObject.toString();
    }
}