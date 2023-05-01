
package in.msmartpayagent.network.model.fastag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FastagRechargeRequest {

    @SerializedName("AgentID")
    private String agentID;
    @SerializedName("Key")
    private String key;
    @Expose
    private String opname;
    @Expose
    private FastagData data;

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOpname() {
        return opname;
    }

    public void setOpname(String opname) {
        this.opname = opname;
    }

    public FastagData getData() {
        return data;
    }

    public void setData(FastagData data) {
        this.data = data;
    }
}