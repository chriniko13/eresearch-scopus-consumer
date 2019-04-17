package com.eresearch.elsevier.scopus.consumer.connector.communicator;

import com.eresearch.elsevier.scopus.consumer.error.EresearchElsevierScopusConsumerError;
import com.eresearch.elsevier.scopus.consumer.exception.BusinessProcessingException;
import lombok.extern.log4j.Log4j;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Log4j
@Component
@Qualifier("basicCommunicator")
public class BasicCommunicator implements Communicator {

    @Autowired
    @Qualifier("consumerRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("basicRetryPolicy")
    private RetryPolicy retryPolicy;

    @Value("${apply.retry.policy}")
    private String applyRetryPolicy;

    @Override
    public String communicateWithElsevier(URI uri) {

        if (!Boolean.valueOf(applyRetryPolicy)) {
            return restTemplate.getForObject(uri, String.class);
        }

        return Failsafe
                .with(retryPolicy)
                .withFallback(() -> {
                    throw new BusinessProcessingException(
                            EresearchElsevierScopusConsumerError.CONNECTOR_CONNECTION_ERROR,
                            EresearchElsevierScopusConsumerError.CONNECTOR_CONNECTION_ERROR.getMessage());
                })
                .onSuccess(s -> log.info("BasicCommunicator#communicateWithElsevier, completed successfully!"))
                .onFailure(error -> log.error("BasicCommunicator#communicateWithElsevier, failed!"))
                .onAbort(error -> log.error("BasicCommunicator#communicateWithElsevier, aborted!"))
                .get(context -> {

                    long startTime = context.getStartTime().toMillis();
                    long elapsedTime = context.getElapsedTime().toMillis();
                    int executions = context.getExecutions();

                    String message = String.format("BasicCommunicator#communicateWithElsevier, retrying...with params: " +
                            "[executions=%s, startTime(ms)=%s, elapsedTime(ms)=%s]", executions, startTime, elapsedTime);

                    log.warn(message);

                    return restTemplate.getForObject(uri, String.class);
                });
    }
}
