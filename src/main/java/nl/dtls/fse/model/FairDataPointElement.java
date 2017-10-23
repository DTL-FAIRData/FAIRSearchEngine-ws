package nl.dtls.fse.model;

/**
 *
 * @author nuno
 */
public class FairDataPointElement {
    private String name;
    private String url;

    
    public FairDataPointElement(String name, String fdpurl){
        this.url = fdpurl;
        this.name = name;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
