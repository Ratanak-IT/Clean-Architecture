package kh.edu.istad.platform.customer.persistence.config;

import kh.edu.istad.common.persistence.query.MongoFilterBuilder;
import kh.edu.istad.platform.customer.persistence.convert.CustomerIdReadConverter;
import kh.edu.istad.platform.customer.persistence.convert.CustomerIdWriteConvertor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
@EnableReactiveMongoAuditing
public class MongoConfig {
    @Bean
    public MongoCustomConversions bean(){
        return MongoCustomConversions.create(
                adapter->{
                    adapter.registerConverter(new CustomerIdReadConverter());
                    adapter.registerConverter((new CustomerIdWriteConvertor()));
                }
        );
    }

    @Bean
    public MongoFilterBuilder mongoFilterBuilder(ReactiveMongoTemplate mongoTemplate){
        return new MongoFilterBuilder(mongoTemplate);
    }

}
