package nl.dtls.fse.model;

/**
 *
 * @author nuno
 */
public class FairDataPointElement {
    private String name;
    private String url;
    private String date;

    
    public FairDataPointElement(String name, String fdpurl, String date){
        this.url = fdpurl;
        this.name = name;
        this.date =  date;
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

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }
    
    
    
}
