
package Frame.generated_PASSAT_DATA_Frame;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Dataset {

    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("data_array")
    @Expose
    private List<DataArray> dataArray = null;

    /**
     *
     * @return
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     */
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    public List<DataArray> getDataArray() {
        return dataArray;
    }

    /**
     *
     * @param dataArray
     */
    public void setDataArray(List<DataArray> dataArray) {
        this.dataArray = dataArray;
    }

}
