
package Frame.generated_PASSAT_DATA_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class DataDescr {

    @SerializedName("data_type")
    @Expose
    private String dataType;
    @SerializedName("data_value")
    @Expose
    private double dataValue;

    /**
     *
     * @return
     */
    public String getDataType() {
        return dataType;
    }

    /**
     *
     * @param dataType
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     *
     * @return
     */
    public double getDataValue() {
        return dataValue;
    }

    /**
     *
     * @param dataValue
     */
    public void setDataValue(double dataValue) {
        this.dataValue = dataValue;
    }

}
