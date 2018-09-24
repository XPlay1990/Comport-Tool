package Frame.generated_PASSAT_DATA_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Hardware {

    @SerializedName("iftype")
    @Expose
    private String iftype;
    @SerializedName("ifconfig")
    @Expose
    private Ifconfig ifconfig;

    /**
     *
     * @return
     */
    public String getIftype() {
        return iftype;
    }

    /**
     *
     * @param iftype
     */
    public void setIftype(String iftype) {
        this.iftype = iftype;
    }

    /**
     *
     * @return
     */
    public Ifconfig getIfconfig() {
        return ifconfig;
    }

    /**
     *
     * @param ifconfig
     */
    public void setIfconfig(Ifconfig ifconfig) {
        this.ifconfig = ifconfig;
    }

}
