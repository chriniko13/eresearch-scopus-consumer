package com.eresearch.elsevier.scopus.consumer.resource;

import com.codahale.metrics.Timer;
import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerDto;
import com.eresearch.elsevier.scopus.consumer.dto.ElsevierScopusConsumerResultsDto;
import com.eresearch.elsevier.scopus.consumer.dto.ScopusFinderImmediateResultDto;
import com.eresearch.elsevier.scopus.consumer.exception.BusinessProcessingException;
import com.eresearch.elsevier.scopus.consumer.exception.DataValidationException;
import com.eresearch.elsevier.scopus.consumer.metrics.entries.ResourceLayerMetricEntry;
import com.eresearch.elsevier.scopus.consumer.service.ElsevierScopusConsumerService;
import com.eresearch.elsevier.scopus.consumer.validator.Validator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j
@RestController
@RequestMapping("/scopus-consumer")
public class ElsevierScopusConsumerResourceImpl implements ElsevierScopusConsumerResource {

    private static final Long DEFERRED_RESULT_TIMEOUT = TimeUnit.MILLISECONDS.toMinutes(7);

    @Qualifier("scopusConsumerExecutor")
    @Autowired
    private ExecutorService scopusConsumerExecutor;

    @Autowired
    private ElsevierScopusConsumerService elsevierScopusConsumerService;

    @Autowired
    private Validator<ElsevierScopusConsumerDto> elsevierScopusConsumerDtoValidator;

    @Autowired
    private ResourceLayerMetricEntry resourceLayerMetricEntry;

    @RequestMapping(method = RequestMethod.POST, path = "/find", consumes = {
            MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @Override
    public @ResponseBody
    DeferredResult<ElsevierScopusConsumerResultsDto> elsevierScopusConsumerOperation(
            @RequestBody ElsevierScopusConsumerDto elsevierScopusConsumerDto) {

        DeferredResult<ElsevierScopusConsumerResultsDto> deferredResult = new DeferredResult<>(DEFERRED_RESULT_TIMEOUT);

        Runnable task = elsevierScopusConsumerOperation(elsevierScopusConsumerDto, deferredResult);
        scopusConsumerExecutor.execute(task);

        return deferredResult;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/find-q", consumes = {
            MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @Override
    public @ResponseBody
    ResponseEntity<ScopusFinderImmediateResultDto> authorFinderNonBlockConsumption(
            @RequestHeader(value = "Transaction-Id") String transactionId,
            @RequestBody ElsevierScopusConsumerDto elsevierScopusConsumerDto) throws DataValidationException {


        elsevierScopusConsumerDtoValidator.validate(elsevierScopusConsumerDto);

        Runnable task = () -> elsevierScopusConsumerService.scopusNonBlockConsumption(transactionId, elsevierScopusConsumerDto);
        scopusConsumerExecutor.execute(task);

        return ResponseEntity.ok(new ScopusFinderImmediateResultDto("Response will be written in queue."));
    }

    private Runnable elsevierScopusConsumerOperation(ElsevierScopusConsumerDto elsevierScopusConsumerDto, DeferredResult<ElsevierScopusConsumerResultsDto> deferredResult) {

        return () -> {

            final Timer.Context context = resourceLayerMetricEntry.getResourceLayerTimer().time();

            try {

                elsevierScopusConsumerDtoValidator.validate(elsevierScopusConsumerDto);
                ElsevierScopusConsumerResultsDto elsevierScopusConsumerResultsDto = elsevierScopusConsumerService.elsevierScopusConsumerOperation(elsevierScopusConsumerDto);
                resourceLayerMetricEntry.getSuccessResourceLayerCounter().inc();
                deferredResult.setResult(elsevierScopusConsumerResultsDto);

            } catch (DataValidationException | BusinessProcessingException e) {

                log.error("ElsevierScopusConsumerResourceImpl#elsevierScopusConsumerOperation --- error occurred.", e);
                resourceLayerMetricEntry.getFailureResourceLayerCounter().inc();
                deferredResult.setErrorResult(e);

            } finally {
                context.stop();
            }
        };
    }
}
