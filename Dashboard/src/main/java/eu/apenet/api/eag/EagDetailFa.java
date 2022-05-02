package eu.apenet.api.eag;

import eu.apenet.persistence.vo.FindingAid;

public class EagDetailFa {
    private String title;
    private String eadid;

    public EagDetailFa(FindingAid findingAid){
        this.title = findingAid.getTitle();
        this.eadid = findingAid.getEadid();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEadid(String eadid) {
        this.eadid = eadid;
    }

    public String getTitle() {
        return title;
    }

    public String getEadid() {
        return eadid;
    }
}
