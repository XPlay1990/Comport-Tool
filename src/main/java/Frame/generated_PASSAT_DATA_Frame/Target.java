
package Frame.generated_PASSAT_DATA_Frame;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Target {

    @SerializedName("target_element")
    @Expose
    private TargetElement targetElement;

    /**
     *
     * @return
     */
    public TargetElement getTargetElement() {
        return targetElement;
    }

    /**
     *
     * @param targetElement
     */
    public void setTargetElement(TargetElement targetElement) {
        this.targetElement = targetElement;
    }

}
