package com.eresearch.elsevier.scopus.consumer.application.actuator.health;


import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerDto;
import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.exception.BusinessProcessingException;
import com.eresearch.elsevier.scopus.consumer.service.ElsevierScopusConsumerService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Log4j
@Component
public class EresearchElsevierScopusConsumerHealthCheck extends AbstractHealthIndicator {

    @Autowired
    private ElsevierScopusConsumerService elsevierScopusConsumerService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${do.specific.scopus.api.health.check}")
    private String doSpecificScopusApiHealthCheck;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        this.performBasicHealthChecks();

        Optional<Exception> ex = this.specificHealthCheck();

        if (ex.isPresent()) {
            builder.down(ex.get());
        } else {
            builder.up();
        }
    }

    private void performBasicHealthChecks() {
        //check disk...
        DiskSpaceHealthIndicatorProperties diskSpaceHealthIndicatorProperties
                = new DiskSpaceHealthIndicatorProperties();
        diskSpaceHealthIndicatorProperties.setThreshold(10737418240L); /*10 GB*/
        new DiskSpaceHealthIndicator(diskSpaceHealthIndicatorProperties);

        //check jms (active mq) is up...
        new JmsHealthIndicator(jmsTemplate.getConnectionFactory());
    }

    private Optional<Exception> specificHealthCheck() {

        if (Boolean.valueOf(doSpecificScopusApiHealthCheck)) {
            //check if we can get a response from elsevier-api...
            Optional<Exception> ex2 = specificElsevierApiHealthCheck();
            if (ex2.isPresent()) {
                return ex2;
            }
        }

        return Optional.empty();
    }

    private Optional<Exception> specificElsevierApiHealthCheck() {

        try {

            ElsevierScopusConsumerResultsDto results
                    = elsevierScopusConsumerService.elsevierScopusConsumerOperation(
                    ElsevierScopusConsumerDto.builder()
                            .scopusAuthorIdentifierNumber("23007591800")
                            .build());

            if (Objects.isNull(results)) {
                log.error("EresearchElsevierScopusConsumerHealthCheck#specificElsevierApiHealthCheck --- result from elsevier-api is null.");
                return Optional.of(new NullPointerException("result from elsevier-api is null."));
            }

        } catch (BusinessProcessingException ex) {

            log.error("EresearchElsevierScopusConsumerHealthCheck#specificElsevierApiHealthCheck --- communication issue.", ex);
            return Optional.of(ex);

        }

        return Optional.empty();
    }
}
