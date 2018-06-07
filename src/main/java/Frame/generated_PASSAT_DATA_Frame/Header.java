
package Frame.generated_PASSAT_DATA_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Header {

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("target")
    @Expose
    private String target;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("cmdclass")
    @Expose
    private String cmdclass;
    @SerializedName("command")
    @Expose
    private String command;

    /**
     *
     * @return
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     */
    public String getTarget() {
        return target;
    }

    /**
     *
     * @param target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getCmdclass() {
        return cmdclass;
    }

    /**
     *
     * @param cmdclass
     */
    public void setCmdclass(String cmdclass) {
        this.cmdclass = cmdclass;
    }

    /**
     *
     * @return
     */
    public String getCommand() {
        return command;
    }

    /**
     *
     * @param command
     */
    public void setCommand(String command) {
        this.command = command;
    }

}
