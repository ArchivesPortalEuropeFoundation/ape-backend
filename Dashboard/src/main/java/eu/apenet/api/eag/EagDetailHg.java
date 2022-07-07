package eu.apenet.api.eag;

import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;

public class EagDetailHg {
    private String title;
    private String eadid;

    public EagDetailHg(){

    }

    public EagDetailHg(HoldingsGuide holdingsGuide){
        this.title = holdingsGuide.getTitle();
        this.eadid = holdingsGuide.getEadid();
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
