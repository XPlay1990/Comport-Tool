
package Frame.generated_PASSAT_DATA_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class DataArray {

    @SerializedName("data_descr")
    @Expose
    private DataDescr dataDescr;

    /**
     *
     * @return
     */
    public DataDescr getDataDescr() {
        return dataDescr;
    }

    /**
     *
     * @param dataDescr
     */
    public void setDataDescr(DataDescr dataDescr) {
        this.dataDescr = dataDescr;
    }

}
