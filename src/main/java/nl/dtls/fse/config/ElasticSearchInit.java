package nl.dtls.fse.config;

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

/**
 *
 * @author nuno
 */
import io.searchbox.client.JestClient;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import nl.dtls.fairsearchengine.utils.esClient.JestESClient2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * Elasticsearch schema initialization on spring start
 * 
 * @author nuno <nuno.nunes@dtls.nl>
 * @version 0.1
 */
@Component
public class ElasticSearchInit {

  private final static Logger LOGGER
                = LogManager.getLogger(ElasticSearchInit.class);
  @Autowired
  private ResourceLoader resourceLoader;

  /**
  * Listens to ContentRefreshedEven (on start) and tries to load elastic search schema
  */
  @EventListener(ContextRefreshedEvent.class)
  public void contextRefreshedEvent() {
            final int maxRetries = 10;  // maximum number of retries
            final int retryTimeout = 5; //seconds between loading retries
            Resource resource = resourceLoader.getResource("classpath:schema.es");
            String schemaAcum = "";
            try {
                InputStream is = resource.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = "";
                while ((line = br.readLine()) != null) {
                    schemaAcum = schemaAcum + line;
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            
            final String schema = schemaAcum;
            JestESClient2 jestClient = new JestESClient2();
            
            Thread schemaLoadThread = new Thread(() -> {
                try {
                    boolean success = false;
                    int retryCounter = 0;
                    
                    while(!success && retryCounter++ < maxRetries){
                        if(jestClient.isServerUp()){
                            jestClient.loadSchema(schema);
                            success=true;
                        } else {
                            if(retryCounter == maxRetries)
                                LOGGER.info("No connection to ES. Giving up checking schema."); 
                            else
                                LOGGER.info("No connection to ES. Trying again in "+retryTimeout+" seconds (retry "+retryCounter+")");
                            
                            Thread.sleep(retryTimeout * 1000);
                        }
                    }
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(JestESClient2.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(ElasticSearchInit.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            schemaLoadThread.start();
            
            
            try {
                jestClient.loadSchema(schema);
            } catch (IOException ex) {
                LOGGER.warn("ElasticSearch server not available.");
            }
  }
}
