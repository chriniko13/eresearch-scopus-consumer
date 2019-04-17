package com.eresearch.elsevier.scopus.consumer.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.eresearch.elsevier.scopus.consumer.dao.ScopusConsumerDao;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class DbOperations {

    private static final String DROP_CREATE_ACTION = "drop-create";
    private static final String NOTHING = "nothing";

    @Autowired
    private ScopusConsumerDao scopusConsumerDao;

    @Value("${db.tables.creation.strategy}")
    private String dbTablesCreationStrategy;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void runTask() {

        if (DROP_CREATE_ACTION.equals(dbTablesCreationStrategy)) {

            jdbcTemplate.execute(scopusConsumerDao.getDropQueryForSearchResultsTable());
            jdbcTemplate.execute(scopusConsumerDao.getCreationQueryForSearchResultsTable());

        } else if (NOTHING.equals(dbTablesCreationStrategy)) {
            //do nothing...
        } else {
            log.error("Please provide a correct value for: ${db.tables.creation.strategy} property.");
            System.exit(-1); //NOTE: exit micro-service like it is a system process.
        }
    }

}
