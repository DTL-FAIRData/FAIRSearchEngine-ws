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
import nl.dtls.fse.model.FairDataPointElement;

public class JestESClient2 {
	
        public JestESClient2(){
        }
	
	
	public List<FairDataPointElement> listFairDataPoints() {
		
		//Request request = new Request.Builder().
		//ejs.Request().size(0)
		//	.agg( ejs.TermsAggregation('test').field('FDPurl'));
		
		
		//Analyze analyzer = new Analyze.Builder()
		//					.analyzer(analyser)
		//					.text(text)
		//					.build();
		
		String query = "{\n" +
		        "    \"size\" : 0,"+
				"    \"aggs\" : {\n" + 
				"        \"fdplist\" : {\n" + 
				"            \"terms\" : { \"field\" : \"FDPurl\" }\n" + 
				"        }\n" + 
				"    }\n" + 
				"}";	
                
                query = "{\"size\" : 0,  \n  \"aggs\": {\n    \"fdp\": {\n      \"terms\": {\n        \"field\": \"repositoryTitle.raw\"\n      },\n    \"aggs\": {\n        \"fdp2\": {\n          \"terms\": {\n            \"field\": \"FDPurl\"\n          },\n        \"aggs\": {\n          \"fdp3\": {\n           \"terms\": {\n             \"field\": \"updateTimestamp\"\n             }\n          }\n        }\n        }\n      }\n    \n    }\n  }\n}\n";
		
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
		
                
                
		for(int i = 0; i < jsonarray.size(); i++) {
			List<FairDataPointElement> result = new Vector<FairDataPointElement>();
			System.out.println(jsonarray.get(i).getAsJsonObject().get("key").getAsString());
			//result.add(new FairDataPointElement("name", jsonarray.get(i).getAsJsonObject().get("key").getAsString(), ""));
                        
                        String fdpUrl = jsonarray.get(i).getAsJsonObject().get("key").getAsString();
                        
                        //todo: create method to extract values, given a base object
                        
                        String fdpName = jsonarray.get(i).getAsJsonObject().getAsJsonObject("fdp2").getAsJsonArray("buckets").get(0).getAsJsonObject().get("key").getAsString();
                        
                        String lastUpdate = jsonarray.get(i).getAsJsonObject().getAsJsonObject("fdp2").getAsJsonArray("buckets").get(0).getAsJsonObject().getAsJsonObject("fdp3").getAsJsonArray("buckets").get(0).getAsJsonObject().get("key").getAsString();
                       
                        System.out.println(lastUpdate);
                        
                        result.add(new FairDataPointElement(fdpName, fdpUrl, lastUpdate));
                        
                        
			return result;
		}
		
		//System.out.println("Data: "+json.toString());
		return null;
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
	private JestClient getJestClient(){
		 JestClientFactory factory = new JestClientFactory();
		 factory.setHttpClientConfig(new HttpClientConfig
		                        .Builder("http://localhost:9200")
		                        .multiThreaded(true)
					//Per default this implementation will create no more than 2 concurrent connections per given route
					.defaultMaxTotalConnectionPerRoute(1)
					// and no more 20 connections in total
					.maxTotalConnection(1)
		                        .build());
		 JestClient client = factory.getObject();
		 return client;
	}
	
	
	
}
