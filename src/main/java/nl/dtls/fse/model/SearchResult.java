/**
 * The MIT License
 * Copyright © 2017 DTL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nl.dtls.fse.model;

/**
 *
 * @author nuno
 */
class SearchResult {
    private String FDPurl;
    private String catalogTitle;
    private String catalogURL;
    private String datasetURL;
    private String description;

    /**
     * @return the FDPurl
     */
    public String getFDPurl() {
        return FDPurl;
    }

    /**
     * @param FDPurl the FDPurl to set
     */
    public void setFDPurl(String FDPurl) {
        this.FDPurl = FDPurl;
    }

    /**
     * @return the catalogTitle
     */
    public String getCatalogTitle() {
        return catalogTitle;
    }

    /**
     * @param catalogTitle the catalogTitle to set
     */
    public void setCatalogTitle(String catalogTitle) {
        this.catalogTitle = catalogTitle;
    }

    /**
     * @return the catalogURL
     */
    public String getCatalogURL() {
        return catalogURL;
    }

    /**
     * @param catalogURL the catalogURL to set
     */
    public void setCatalogURL(String catalogURL) {
        this.catalogURL = catalogURL;
    }

    /**
     * @return the datasetURL
     */
    public String getDatasetURL() {
        return datasetURL;
    }

    /**
     * @param datasetURL the datasetURL to set
     */
    public void setDatasetURL(String datasetURL) {
        this.datasetURL = datasetURL;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
