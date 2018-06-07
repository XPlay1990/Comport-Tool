package Frame.optimized_PASSAT_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Ifconfig {

    @SerializedName("portname")
    @Expose
    private String portname;
    @SerializedName("bitrate")
    @Expose
    private Integer bitrate;
    @SerializedName("data_bits")
    @Expose
    private Integer dataBits;
    @SerializedName("stop_bits")
    @Expose
    private Integer stopBits;
    @SerializedName("parity_bits")
    @Expose
    private String parityBits;
    @SerializedName("port")
    @Expose
    private Integer port;
    @SerializedName("hostname")
    @Expose
    private String hostname;
    @SerializedName("target_ip")
    @Expose
    private String targetIp;
    @SerializedName("eth_name")
    @Expose
    private String ethName;

    /**
     *
     * @return
     */
    public String getPortname() {
        return portname;
    }

    /**
     *
     * @param portname
     */
    public void setPortname(String portname) {
        this.portname = portname;
    }

    /**
     *
     * @return
     */
    public Integer getBitrate() {
        return bitrate;
    }

    /**
     *
     * @param bitrate
     */
    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    /**
     *
     * @return
     */
    public Integer getDataBits() {
        return dataBits;
    }

    /**
     *
     * @param dataBits
     */
    public void setDataBits(Integer dataBits) {
        this.dataBits = dataBits;
    }

    /**
     *
     * @return
     */
    public Integer getStopBits() {
        return stopBits;
    }

    /**
     *
     * @param stopBits
     */
    public void setStopBits(Integer stopBits) {
        this.stopBits = stopBits;
    }

    /**
     *
     * @return
     */
    public String getParityBits() {
        return parityBits;
    }

    /**
     *
     * @param parityBits
     */
    public void setParityBits(String parityBits) {
        this.parityBits = parityBits;
    }

    /**
     *
     * @return
     */
    public Integer getPort() {
        return port;
    }

    /**
     *
     * @param port
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     *
     * @return
     */
    public String getHostname() {
        return hostname;
    }

    /**
     *
     * @param hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     *
     * @return
     */
    public String getTargetIp() {
        return targetIp;
    }

    /**
     *
     * @param targetIp
     */
    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    /**
     *
     * @return
     */
    public String getEthName() {
        return ethName;
    }

    /**
     *
     * @param ethName
     */
    public void setEthName(String ethName) {
        this.ethName = ethName;
    }

}
