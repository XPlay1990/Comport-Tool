package Frame.generated_PASSAT_DATA_Frame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class Passat_Data_Frame {

    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     *
     * @return
     */
    public Header getHeader() {
        return header;
    }

    /**
     *
     * @param header
     */
    public void setHeader(Header header) {
        this.header = header;
    }

    /**
     *
     * @return
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     *
     */
    public void toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter filewriter = new FileWriter("Test_Frame.json")) {
            gson.toJson(this, filewriter);
            filewriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(Passat_Data_Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String toJson_string() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
    
    /**
     *
     * @param json
     * @return
     */
    public static Passat_Data_Frame fromJSON(String json) {
        Gson gson = new GsonBuilder().create();
        Passat_Data_Frame fromJson = gson.fromJson(json, Passat_Data_Frame.class);
        return fromJson;
    }
}
