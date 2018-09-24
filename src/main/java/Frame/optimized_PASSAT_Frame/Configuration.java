
package Frame.optimized_PASSAT_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Configuration {

    @SerializedName("hardware")
    @Expose
    private Hardware hardware;
    @SerializedName("protocol")
    @Expose
    private Protocol protocol;

    /**
     *
     * @return
     */
    public Hardware getHardware() {
        return hardware;
    }

    /**
     *
     * @param hardware
     */
    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    /**
     *
     * @return
     */
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     *
     * @param protocol
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

}
