package cc.ixcc.novelthree.bean;

public class CensusBean {
    private String slot_id;
    private String type;
    private String times;

    public CensusBean(String slot_id, String type, String times) {
        this.slot_id = slot_id;
        this.type = type;
        this.times = times;
    }

    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
