package com.eresearch.elsevier.scopus.consumer.service;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

@Log4j

@Component
@Aspect
public class CaptureScopusResponseService {

    @Value("${capture.scopus-response}")
    private boolean captureScopus;

    @Value("${capture-service.path-to-store-files}")
    private String pathToStoreFiles;

    @Pointcut("execution(String com.eresearch.elsevier.scopus.consumer.connector.communicator.BasicCommunicator.communicateWithElsevier(java.net.URI))")
    private void interceptScopusCommunication() {
    }

    @Around("interceptScopusCommunication()")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {

        Object result = pjp.proceed();

        if (captureScopus) {
            //Example 1: https://api.elsevier.com/content/search/scopus?apikey=f560b7d8fb2ee94533209bc0fdf5087f&query=AU-ID(23007591800)&view=COMPLETE
            //Example 2: https://api.elsevier.com/content/search/scopus?apikey=f560b7d8fb2ee94533209bc0fdf5087f&query=AU-ID(23007591800)&view=COMPLETE&start=17&count=25
            URI uri = (URI) pjp.getArgs()[0];

            String queryParamsConcatenated = uri.toString().split("\\?")[1];

            String[] queryParams = queryParamsConcatenated.split("&");

            String filename = Arrays.stream(queryParams)
                    .filter(q -> !q.contains("apikey") && !q.contains("view"))
                    .collect(Collectors.joining("_"));

            //System.out.println("    >>>" + uri);
            log("getScopus__" + filename, (String) result, "json");
        }

        return result;
    }


    private void log(String filename, String contents, String fileType) {
        try {
            Path path = Paths.get(pathToStoreFiles, filename + "." + fileType);

            Files.deleteIfExists(path);
            Files.createFile(path);

            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
                bufferedWriter.write(contents);
                bufferedWriter.newLine();
            }
        } catch (Exception error) {
            log.error("error occurred: " + error.getMessage(), error);
        }
    }

}
