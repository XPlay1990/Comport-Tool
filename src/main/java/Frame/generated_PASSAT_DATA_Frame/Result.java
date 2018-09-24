
package Frame.generated_PASSAT_DATA_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Result {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private String details;
    /**
     *
     * @return
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message 
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    
    /** @return
     */
    public String getDetails() {
        return details;
    }

    /**
     *
     * @param details 
     */
    public void setDetails(String details) {
        this.details = details;
    }

}
