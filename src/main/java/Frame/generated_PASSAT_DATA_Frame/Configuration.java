
package Frame.generated_PASSAT_DATA_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Configuration {

    @SerializedName("protocol")
    @Expose
    private Protocol protocol;

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
