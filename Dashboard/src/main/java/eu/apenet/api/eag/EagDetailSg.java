package eu.apenet.api.eag;

import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.SourceGuide;

public class EagDetailSg {
    private String title;
    private String eadid;

    public EagDetailSg(SourceGuide sourceGuide){
        this.title = sourceGuide.getTitle();
        this.eadid = sourceGuide.getEadid();
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
