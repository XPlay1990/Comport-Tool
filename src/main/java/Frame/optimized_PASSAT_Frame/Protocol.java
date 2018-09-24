package Frame.optimized_PASSAT_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Protocol {

    @SerializedName("prot_name")
    @Expose
    private String protName;
    @SerializedName("prot_id")
    @Expose
    private Integer protId;
    @SerializedName("prot_version")
    @Expose
    private String protVersion;

    /**
     *
     * @return
     */
    public String getProtName() {
        return protName;
    }

    /**
     *
     * @param protName
     */
    public void setProtName(String protName) {
        this.protName = protName;
    }

    /**
     *
     * @return
     */
    public Integer getProtId() {
        return protId;
    }

    /**
     *
     * @param protId
     */
    public void setProtId(Integer protId) {
        this.protId = protId;
    }

    /**
     *
     * @return
     */
    public String getProtVersion() {
        return protVersion;
    }

    /**
     *
     * @param protVersion
     */
    public void setProtVersion(String protVersion) {
        this.protVersion = protVersion;
    }

}
