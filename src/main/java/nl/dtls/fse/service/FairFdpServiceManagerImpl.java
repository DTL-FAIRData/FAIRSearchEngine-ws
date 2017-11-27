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
package nl.dtls.fse.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.eclipse.rdf4j.model.IRI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author nuno
 */
@Configuration
//@PropertySource("classpath:WEB-INF/fairsearchengine.properties")
@PropertySource("classpath:/fairsearchengine.properties")
@Component
public class FairFdpServiceManagerImpl {

    //TODO currently it's a file, in the future it can support database or other queuing services
    @Value("${fairsearchservice.fdpqueue}")
    private String fdpqueue = "/tmp/tmp_fdpqueue.txt";

    @Value("${fairsearchservice.onQueueSubmitProcess}")
    private Boolean onQueueSubmitProcess = false;

    @Value("${fairsearchservice.queueProcess}")
    private String queueProcess = "";

    //TODO check logging
    private final static org.apache.logging.log4j.Logger LOGGER
            = LogManager.getLogger(FairFdpServiceManagerImpl.class);

    //TODO create specific type of exception
    public void addFdp(URI fdp) throws Exception {
        try {
            Files.write(Paths.get(fdpqueue), (fdp.toString() + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            //System.out.println("thisisq" + this.fdpqueue);
        } catch (IOException e) {
            throw new Exception(e.getMessage(), e.fillInStackTrace());
        }

        try {
            if (onQueueSubmitProcess) {
                Runtime rt = Runtime.getRuntime();
                Process pr = rt.exec(queueProcess + " -f " + fdp.toString());
                logOutput(pr);
            }
        } catch (IOException e) {
            Logger l = java.util.logging.Logger.getLogger(FairFdpServiceManagerImpl.class.getName());
            l.log(Level.WARNING, null, e);
        }
    }

    public void removeFdp(URI fdp) {
        throw new UnsupportedOperationException();
    }

    public void redirect(URI fdp, URI newFdp) {
        throw new UnsupportedOperationException();
    }

    //for debug purpose only
    private void logOutput(Process proc) throws IOException {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        // read the output from the command
        System.out.println("Standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            //TODO change to log
            System.out.println(s);
        }

        // read any errors from the attempted command
        System.out.println("Standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
             //TODO change to log
            System.out.println(s);
        }
        

    }

}
