
package Frame.generated_PASSAT_DATA_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class TargetElement {

    @SerializedName("target_name")
    @Expose
    private String targetName;
    @SerializedName("target_uuid")
    @Expose
    private String targetUuid;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("configuration")
    @Expose
    private Configuration configuration;
    @SerializedName("dataset")
    @Expose
    private Dataset dataset;

    /**
     *
     * @return
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     *
     * @param targetName
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     *
     * @return
     */
    public String getTargetUuid() {
        return targetUuid;
    }

    /**
     *
     * @param targetUuid
     */
    public void setTargetUuid(String targetUuid) {
        this.targetUuid = targetUuid;
    }

    /**
     *
     * @return
     */
    public String getCreated() {
        return created;
    }

    /**
     *
     * @param created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     *
     * @return
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     *
     * @param configuration
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     *
     * @return
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     *
     * @param dataset
     */
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

}
