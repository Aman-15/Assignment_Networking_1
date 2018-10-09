package models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("type")
    @Expose
    private Object type;

    @SerializedName("coordinates")
    @Expose
    private List<Double> coordinates = null;

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

}
