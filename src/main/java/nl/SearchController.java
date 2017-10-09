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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//before nl.dtls.fairsearchengine.api.controller
    
package nl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;
import java.util.logging.Level;
import nl.dtls.fairsearchengine.service.FairFdpServiceManagerImpl;
import nl.dtls.fairsearchengine.service.FairSearchServiceImpl;
import nl.dtls.fairsearchengine.utils.esClient.JestESClient2;
import org.springframework.http.HttpEntity;
/*import nl.dtl.fairmetadata4j.io.MetadataException;
import nl.dtl.fairmetadata4j.io.MetadataParserException;
import nl.dtl.fairmetadata4j.model.Agent;
import nl.dtl.fairmetadata4j.model.CatalogMetadata;
import nl.dtl.fairmetadata4j.model.DatasetMetadata;
import nl.dtl.fairmetadata4j.model.DistributionMetadata;
import nl.dtl.fairmetadata4j.model.FDPMetadata;
import nl.dtl.fairmetadata4j.model.Identifier;
import nl.dtl.fairmetadata4j.utils.MetadataUtils;
import nl.dtl.fairsearchengine.model.SearchDataset;
import nl.dtl.fairsearchengine.model.WordSuggest;
import nl.dtl.fairsearchengine.util.esClient.ESClient;
import nl.dtl.fairsearchengine.util.esClient.JestESClient;*/
//import nl.dtl.fairsearchengine.util.esClient.ESClient;
////import nl.dtl.fairsearchengine.util.esClient.JestESClient;
////import nl.dtls.fairdatapoint.api.controller.utils.LoggerUtils;
//import nl.dtls.fairsearchengine.service.FairSearchService;
//import nl.dtls.fairsearchengine.service.FairSearchServiceException;
//import nl.dtls.fairsearchengine.utils.esClient.JestESClient2;

//import org.eclipse.rdf4j.rio.RDFFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;
import springfox.documentation.annotations.ApiIgnore;



/**
 * Handle search calls
 * 
 * @version 0.1
 */


@Api(description = "Search API")
@RequestMapping("/")
@RestController
public class SearchController {

    private final static Logger LOGGER
            = LogManager.getLogger(SearchController.class);
    
    @Autowired
    private FairSearchServiceImpl fairSearchService;
    @Autowired
    private FairFdpServiceManagerImpl fairFdpServiceManager;
    
    private boolean isFDPMetaDataAvailable = false;
    private final ValueFactory valueFactory = SimpleValueFactory.getInstance();

    
    
    public SearchController() {
           LOGGER.info("Hello world");
    }
    
    /**
     * Search for datasets
     *
     * @param id
     * @param request
     * @param response
     * @return Metadata about the catalog in one of the acceptable formats (RDF
     * Turtle, JSON-LD, RDF XML and RDF N3)
     *
     * @throws IllegalStateException
     * @throws FairSearchServiceException
     */
    @ApiOperation(value = "Search")
    @RequestMapping(value = "/s", method = RequestMethod.GET,
            produces = {"text/json",
                "application/ld+json"}
    )
    @ResponseStatus(HttpStatus.OK)
    public List<SearchDataset> search(
    		@RequestParam(value="s", defaultValue="gene") String s, @RequestParam(value="site", defaultValue="") String site,
            HttpServletRequest request,
            HttpServletResponse response) throws FairSearchServiceException{
            
            //return doSearch(s, site);
                
                return null;
    }
    
    
    /**
     * List indexed Fair Data Point
     *
     * @return Metadata about the catalog in one of the acceptable formats (RDF
     * Turtle, JSON-LD, RDF XML and RDF N3)
     *
     * @throws IllegalStateException
     * @throws FairSearchServiceException
     */
    @ApiOperation(value = "Search")
    @RequestMapping(value = "/listIndexedFairDataPoints",
                    method = RequestMethod.GET
//                    produces = "text/json" 
    )
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getlistIndexFairDataPoints(
        HttpServletRequest request,
        HttpServletResponse response) throws FairSearchServiceException{
    		
    	JestESClient2 esclient = new JestESClient2();
    	List<String> list = esclient.listFairDataPoints();
    	
        return list;
    }
    
    /**
     * Submit Fair Data Point for indexin
     *
     * @return Metadata about the catalog in one of the acceptable formats (RDF
     * Turtle, JSON-LD, RDF XML and RDF N3)
     *
     * @throws IllegalStateException
     * @throws FairSearchServiceException
     */
    //TODO move to a different controller
    @ApiOperation(value = "Submit Fair Data Point for indexing")
    @RequestMapping(value = "/submitFdp",
                    method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity submitFdp(
        @RequestParam(value="fdp", defaultValue="") String fdp,
        HttpServletRequest request,
        HttpServletResponse response) throws FairSearchServiceException{
        
        try {
            URI fpd = new URI(fdp);
            this.fairFdpServiceManager.addFdp(fpd);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);    
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);    
    }
    
    /**
     * Submit fair data 
     *
     * @param id
     * @param request
     * @param response
     * @return Metadata about the catalog in one of the acceptable formats (RDF
     * Turtle, JSON-LD, RDF XML and RDF N3)
     *
     * @throws IllegalStateException
     * @throws FairSearchServiceException
     */
    @ApiOperation(value = "Raw search (returns ES response)")
    @RequestMapping(value = {"/rs/**"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String rawSearch(@RequestBody(required=false)  String body, HttpMethod method, HttpServletRequest request,
                             HttpServletResponse response) throws FairSearchServiceException{  
          
            String server = "localhost";
            int port = 9200;        

            URI uri;
            
            try {
                String resource = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
                resource = resource.substring(4);
                uri = new URI("http", null, server, port, request.getRequestURI(), request.getQueryString(), null);
                 ResponseEntity<String> responseEntity =
                   new RestTemplate().exchange(uri, method, new HttpEntity<String>(body), String.class);

               return responseEntity.getBody();    
               
            } catch (URISyntaxException ex) {
                java.util.logging.Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return "";
    }
    
    /**
     * Search for datasets
     *
     * @param id
     * @param request
     * @param response
     * @return Metadata about the catalog in one of the acceptable formats (RDF
     * Turtle, JSON-LD, RDF XML and RDF N3)
     *
     * @throws IllegalStateException
     * @throws FairSearchServiceException
     */
    @ApiOperation(value = "Word suggestion")
    @RequestMapping(value = "/ws", method = RequestMethod.GET,
            produces = {"text/json",
                "application/ld+json"}
    )
    @ResponseStatus(HttpStatus.OK)
    public String wordSuggest(
    //public List<WordSuggest> wordSuggest(
    		@RequestParam(value="s", defaultValue="a") String s,
            HttpServletRequest request,
            HttpServletResponse response) throws FairSearchServiceException{
    	
    		//ESClient esclient = new JestESClient();
    		
    		//return esclient.wordSuggest(s);
    		//return esclient.wordSuggest(s, WordSuggest.class);          
        	return null;      
        	
    }

    private String doSearch(String search, String site){
    //private List<SearchDataset> doSearch(String search, String site){
        String result = "test";
	//ESClient esclient = new JestESClient();
        JestESClient2 esclient = new JestESClient2();
	    
	    if(site != null && site.length()>0)
	    	search = search + " site:" + site; 
	    
    	//String result = esclient.search(search);
    	//List<SearchDataset> result = null;
    	//result = esclient.search(search, SearchDataset.class);
    	
    	return "hello";
    }
   
    
    /**
     * Trim white space at start, end and between strings
     *
     * @param str Input string
     * @return Trimmed string
     */
    private String trimmer(String str) {
        String trimmedStr = str;
        trimmedStr = trimmedStr.trim();
        trimmedStr = trimmedStr.replace(" ", "-");
        return trimmedStr;
    }
}