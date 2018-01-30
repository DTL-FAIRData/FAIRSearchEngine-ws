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
package nl.dtls.fairsearchengine.utils.esClient;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation.Entry;
import io.searchbox.indices.Analyze;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.PutMapping;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import nl.dtls.fse.controller.SearchController;
import nl.dtls.fse.model.FairDataPointElement;
import org.apache.logging.log4j.LogManager;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


/**
 * ElasticSearch client
 * 
 * @author nuno
 * @version 0.1
 */
@Component
public class JestESClient2 {

    private final static Logger LOGGER
            = LoggerFactory.getLogger(JestESClient2.class);

    JestClient client;

    /**
     * Class constructor
     */
    public JestESClient2() {
        init();
    }

    private void init() {
        JestClientFactory factory = new JestClientFactory();
        //TODO get URL from configuration file
        factory.setHttpClientConfig(new HttpClientConfig.Builder("http://localhost:9200")
                .multiThreaded(true)
                //Per default this implementation will create no more than 2 concurrent connections per given route
                .defaultMaxTotalConnectionPerRoute(1)
                // and no more 20 connections in total
                .maxTotalConnection(1)
                .build());
        client = factory.getObject();
    }

    
    /**
     * Loads schema into elasticsearch
     * 
     * @param schema    string with json schema for elasticsearch
     * @throws IOException
     */
    public void loadSchema(String schema) throws IOException {

        JestClient client = getJestClient();

        if (indexExists()) {
            LOGGER.info("Index already exists. Schema not loaded.");
        } else {
            LOGGER.info("Creating elasticsearch index and mapping. "+schema);
            client.execute(new CreateIndex.Builder("dataset").build());
            client.execute(new PutMapping.Builder("dataset", "dataset", schema).build());
        }

    }

    /**
     * Checks if elasticsearch is up
     * 
     * @return  boolean depending on if the server can be contacted or not
     */
    public boolean isServerUp() {
        try {
            indexExists();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    /**
     * Checks if index exists on elasticsearch
     * 
     * @return @throws IOException
     */
    public boolean indexExists() throws IOException {
        JestClient client = getJestClient();
        boolean indexExists = client.execute(new IndicesExists.Builder("dataset").build()).isSucceeded();
        return indexExists;
    }

    /**
     * List of fair data points
     * 
     * @return  list of fair data points with last update date
     */
    public List<FairDataPointElement> listFairDataPoints() {
        
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      
        searchSourceBuilder.aggregation(AggregationBuilders
                                          .terms("fdp").field("repositoryTitle.raw")
                                          .subAggregation(AggregationBuilders.terms("fdp2").field("FDPurl")
                                          .subAggregation(AggregationBuilders.terms("fdp3").field("updateTimestamp"))));
               

        SearchResult sr = null;
        try {      
            sr = this.search(searchSourceBuilder.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        TermsAggregation terms = sr.getAggregations().getTermsAggregation("fdp");
        
        List<FairDataPointElement> result = new ArrayList<FairDataPointElement>();
        
        for (Entry entry_level1 : terms.getBuckets() ) {
            String fdpName = entry_level1.getKey();
            
            TermsAggregation terms2 = entry_level1.getTermsAggregation("fdp2");
            Entry entry_level2 =  terms2.getBuckets().get(0);
            String fdpUrl = entry_level2.getKey();
            
            TermsAggregation terms3 = entry_level2.getTermsAggregation("fdp3");
            Entry entry_level3 = terms3.getBuckets().get(0);
            String lastUpdate = entry_level3.getKey();
            
            result.add(new FairDataPointElement(fdpName, fdpUrl, lastUpdate));
        }
        
        //System.out.println("Data: "+json.toString());
        return result;
    }

    private SearchResult search(String query) throws IOException {
        Search search = new Search.Builder(query)
                // multiple index or types can be added.
                .addIndex("dataset")
                .addType("dataset")
                .build();


        JestClient client = this.getJestClient();
 

        SearchResult result = client.execute(search);

        return result;
    }

    //make this common to all clients
    private JestClient getJestClient() {
        return this.client;
    }

}
