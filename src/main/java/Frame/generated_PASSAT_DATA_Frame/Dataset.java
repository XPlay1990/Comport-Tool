
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
    private Double timestamp;
    @SerializedName("data_args")
    @Expose
    private String data_args;
    @SerializedName("data_array")
    @Expose
    private List<DataArray> dataArray = null;

    /**
     *
     * @return
     */
    public Double getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     */
    public void setTimestamp(Double timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     *
     * @return
     */
    public String getData_args() {
        return data_args;
    }

    /**
     *
     * @param data_args
     */
    public void setData_args(String data_args) {
        this.data_args = data_args;
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
