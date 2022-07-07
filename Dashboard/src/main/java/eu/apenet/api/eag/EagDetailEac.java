package eu.apenet.api.eag;

import eu.apenet.persistence.vo.EacCpf;

public class EagDetailEac {
    private String title;
    private String id;

    public EagDetailEac(){

    }

    public EagDetailEac(EacCpf eacCpf){
        this.title = eacCpf.getTitle();
        this.id = eacCpf.getIdentifier();
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
