
package Frame.PASSAT_DATA_Frame;

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
    private Integer dataValue;

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
    public Integer getDataValue() {
        return dataValue;
    }

    /**
     *
     * @param dataValue
     */
    public void setDataValue(Integer dataValue) {
        this.dataValue = dataValue;
    }

}
