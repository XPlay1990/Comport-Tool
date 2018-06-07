package Frame.optimized_PASSAT_Frame;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 *
 * @author jan.adamczyk
 */
public class Data {

    @SerializedName("targets")
    @Expose
    private List<Target> targets = new ArrayList<>();

    /**
     *
     */
    public Data() {
        for (int i = 0; i < 3; i++) {
            targets.add(new Target());
        }
    }

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
