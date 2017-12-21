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

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.Analyze;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.PutMapping;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import nl.dtls.fse.controller.SearchController;
import nl.dtls.fse.model.FairDataPointElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 *
 * @author nuno
 */
public class JestESClient2 {

    private final static Logger LOGGER
            = LogManager.getLogger(JestESClient2.class);

    JestClient client = null;

    /**
     *
     */
    public JestESClient2() {
        init();
    }

    private void init() {
        JestClientFactory factory = new JestClientFactory();
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
     * @param elastic server json schema
     * @throws IOException
     */
    public void loadSchema(String schema) throws IOException {

        JestClient client = getJestClient();

        if (indexExists()) {
            LOGGER.info("Index already exists. Schema not loaded.");
        } else {
            LOGGER.info("Creating elasticsearch index and mapping.");
            client.execute(new CreateIndex.Builder("dataset").build());
            client.execute(new PutMapping.Builder("dataset", "dataset", schema).build());
        }

    }

    /**
     * Checks if elasticsearch is up
     * 
     * @return
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
     * @return
     */
    public List<FairDataPointElement> listFairDataPoints() {

        //Request request = new Request.Builder().
        //ejs.Request().size(0)
        //	.agg( ejs.TermsAggregation('test').field('FDPurl'));
        //Analyze analyzer = new Analyze.Builder()
        //					.analyzer(analyser)
        //					.text(text)
        //					.build();
        String query = "{\n"
                + "    \"size\" : 0,"
                + "    \"aggs\" : {\n"
                + "        \"fdplist\" : {\n"
                + "            \"terms\" : { \"field\" : \"FDPurl\" }\n"
                + "        }\n"
                + "    }\n"
                + "}";

        query = "{\"size\" : 0,  \n  \"aggs\": {\n    \"fdp\": {\n      \"terms\": {\n        \"field\": \"repositoryTitle.raw\"\n      },\n    \"aggs\": {\n        \"fdp2\": {\n          \"terms\": {\n            \"field\": \"FDPurl\"\n          },\n        \"aggs\": {\n          \"fdp3\": {\n           \"terms\": {\n             \"field\": \"updateTimestamp\"\n             }\n          }\n        }\n        }\n      }\n    \n    }\n  }\n}\n";

        System.out.println(query);

        SearchResult sr = null;
        try {
            sr = this.search(query);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //List<String> tokenList = new Vector();
        System.out.println(sr.getJsonString());
        JsonObject json = sr.getJsonObject();

        ///JsonArray jsonarray = json.getAsJsonObject("aggregations")
        //		.getAsJsonObject("fdp")
        //		.getAsJsonArray("buckets");	
        //System.out.println(json.getAsJsonObject("aggregations").getAsJsonObject("fdplist").getAsJsonArray("buckets"));
        JsonArray jsonarray = json.getAsJsonObject("aggregations").getAsJsonObject("fdp").getAsJsonArray("buckets");

        List<FairDataPointElement> result = new Vector<FairDataPointElement>();

        for (int i = 0; i < jsonarray.size(); i++) {

            System.out.println(jsonarray.get(i).getAsJsonObject().get("key").getAsString());
            //result.add(new FairDataPointElement("name", jsonarray.get(i).getAsJsonObject().get("key").getAsString(), ""));

            String fdpName = jsonarray.get(i).getAsJsonObject().get("key").getAsString();

            //todo: create method to extract values, given a base object
            String fdpUrl = jsonarray.get(i).getAsJsonObject().getAsJsonObject("fdp2").getAsJsonArray("buckets").get(0).getAsJsonObject().get("key").getAsString();
            String lastUpdate = jsonarray.get(i).getAsJsonObject().getAsJsonObject("fdp2").getAsJsonArray("buckets").get(0).getAsJsonObject().getAsJsonObject("fdp3").getAsJsonArray("buckets").get(0).getAsJsonObject().get("key").getAsString();
            System.out.println(lastUpdate + " " + fdpUrl + " " + fdpName);
            result.add(new FairDataPointElement(fdpName, fdpUrl, lastUpdate));
            System.err.println("returning..");
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
