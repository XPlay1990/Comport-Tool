
package Frame.generated_PASSAT_DATA_Frame;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jan.adamczyk
 */
public class Data {

    @SerializedName("targets")
    @Expose
    private List<Target> targets = null;

    /**
     *
     * @return
     */
    public List<Target> getTargets() {
        return targets;
    }

    /**
     *
     * @param targets
     */
    public void setTargets(List<Target> targets) {
        this.targets = targets;
    }

}
