package com.shamiinnocent.erp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class ERPApplication {

    public static void main(String[] args) {

        SpringApplication.run(ERPApplication.class, args);
    }

//    public static void main(String[] args) {
//        SpringApplication app = new SpringApplication(ERPApplication.class);
//        Environment env = app.run(args).getEnvironment();
//        String host = getHostAddress();
//        String port = env.getProperty("server.port", "8084");
//        String url = "http";
//        String contextPath = env.getProperty("server.servlet.context-path", "/erp/api/v1") + "/swagger-ui.html";
//        String applicationUrl = String.format(url+"://%s:%s%s", host, port, contextPath);
//        log.info("FortressApplication started at: {}", applicationUrl);
//    }


//    /**
//     * Retrieves the host address for the application URL.
//     *
//     * @return the host address or localhost if unknown
//     */
//    private static String getHostAddress() {
//        try {
//            return InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            log.warn("Unable to determine host address: {}. Falling back to localhost", e.getMessage());
//            return "localhost";
//        }
//    }

}
