package com.eresearch.elsevier.scopus.consumer.dao;

import org.springframework.stereotype.Component;

@Component
public class ScopusConsumerDaoImpl implements ScopusConsumerDao {

    @Override
    public String getInsertQueryForSearchResultsTable() {
        return "INSERT scopus_consumer.search_results(au_id, author_results, links_consumed, first_link, last_link, creation_timestamp) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    public String getDeleteQueryForSearchResultsTable() {
        return "DELETE FROM scopus_consumer.search_results";
    }

    @Override
    public String getResetAutoIncrementForSearchResultsTable() {
        return "ALTER TABLE scopus_consumer.search_results AUTO_INCREMENT = 1";
    }

    @Override
    public String getSelectQueryForSearchResultsTable() {
        return "SELECT * FROM scopus_consumer.search_results";
    }

    @Override
    public String getCreationQueryForSearchResultsTable() {
        return "CREATE TABLE IF NOT EXISTS scopus_consumer.search_results(id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT, au_id VARCHAR(255) DEFAULT NULL, author_results LONGTEXT, links_consumed MEDIUMTEXT, first_link MEDIUMTEXT, last_link MEDIUMTEXT, creation_timestamp TIMESTAMP NULL DEFAULT NULL, PRIMARY KEY (id), KEY au_id_idx (au_id) ) ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }

    @Override
    public String getDropQueryForSearchResultsTable() {
        return "DROP TABLE IF EXISTS scopus_consumer.search_results";
    }
}
