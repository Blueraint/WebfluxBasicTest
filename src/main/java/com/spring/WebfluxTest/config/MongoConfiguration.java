package com.spring.WebfluxTest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * SpringBoot Datasource Configuration(Bean) - Mongo
 * ConfigurationProperties is pulled by springboot properties(in case, resources/application.yml -> spring -> mysqldb -> datasource -> prifix (k,v))
 * Set JpaRepositories for @EnableJpaRepositories (entity manager factory, transaction manager, ...)
 */

@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
//@EnableMongoRepositories(basePackages = {"com.SpringBoot.SpringBootBatchMultiDataSource.MongoResource"})
public class MongoConfiguration extends AbstractMongoClientConfiguration {
    /*
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
//        return MongoClients.create("mongodb://batchdev:batchdev@localhost:27017/batchtest");
    }
*/
    @Override
    protected String getDatabaseName() {
        return "MongoResource";
    }

    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
