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

public class JestESClient2 {
	
        public JestESClient2(){
        }
	
	
	public List<String> listFairDataPoints() {
		
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
		
		SearchResult sr = null;
		try {
			sr = this.search(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		//List<String> tokenList = new Vector();
		JsonObject json = sr.getJsonObject();
		
		///JsonArray jsonarray = json.getAsJsonObject("aggregations")
		//		.getAsJsonObject("fdp")
		//		.getAsJsonArray("buckets");	
		
		System.out.println(json.getAsJsonObject("aggregations").getAsJsonObject("fdplist").getAsJsonArray("buckets"));
		
		JsonArray jsonarray = json.getAsJsonObject("aggregations").getAsJsonObject("fdplist").getAsJsonArray("buckets");
		
		for(int i = 0; i < jsonarray.size(); i++) {
			List<String> result = new Vector<String>();
			System.out.println(jsonarray.get(i).getAsJsonObject().get("key").getAsString());
			result.add(jsonarray.get(i).getAsJsonObject().get("key").getAsString());
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
