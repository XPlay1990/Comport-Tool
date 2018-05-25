package Frame.newpackage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Target {

    @SerializedName("target_name")
    @Expose
    private String targetName = "asdf";
    @SerializedName("target_uuid")
    @Expose
    private String targetUuid = "70";
    @SerializedName("created")
    @Expose
    private String created = "gestern";
    @SerializedName("configuration")
    @Expose
    private Configuration configuration;

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

}
