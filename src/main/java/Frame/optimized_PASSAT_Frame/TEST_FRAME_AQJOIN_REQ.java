package Frame.optimized_PASSAT_Frame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 *
 * @author jan.adamczyk
 */
public class TEST_FRAME_AQJOIN_REQ {

    @SerializedName("header")
    @Expose
    private Header header = new Header();
    @SerializedName("data")
    @Expose
    private Data data = new Data();

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
            Logger.getLogger(TEST_FRAME_AQJOIN_REQ.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param json
     * @return
     */
    public static TEST_FRAME_AQJOIN_REQ fromJSON(String json) {
        Gson gson = new GsonBuilder().create();
        TEST_FRAME_AQJOIN_REQ fromJson = gson.fromJson(json, TEST_FRAME_AQJOIN_REQ.class);
        return fromJson;
    }
}
