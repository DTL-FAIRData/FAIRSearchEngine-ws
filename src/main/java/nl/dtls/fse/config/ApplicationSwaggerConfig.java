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
package nl.dtls.fse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration.
 * 
 * @version 0.1
 */
@Configuration
@EnableSwagger2
public class ApplicationSwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            //.apis(RequestHandlerSelectors.basePackage("nl.dtls.fse.controller.*"))
            .paths(regex("/.*"))
            .build()
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
            "FDP Search API based",
            "<p>This API is a prototype version, If you find bugs in this api "
                    + "please contact the developer.</p>"
                    + "<p>"
                    + "<li><a target='_blank' href = 'https://dtl-fair."
                    + "atlassian.net/wiki/display/FDP/FAIR+Data+Point+"
                    + "Software+Specification'>API specs</li>"
                    + "<li><a target='_blank' href = 'https://github.com/"
                    + "DTL-FAIRData/ODEX-FAIRDataPoint/tree/master/fdp-api"
                    + "/java'>Source code</a> </li></p>",
            "0.1-beta",
            "ATO",
            "nuno.nunes@dtls.nl",
            "The MIT License",
            "https://opensource.org/licenses/MIT"
        );
        return apiInfo;
    }
}