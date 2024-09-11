package com.slowstarter.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.slowstarter.second.repository",
        entityManagerFactoryRef = "secondEntityManger",
        transactionManagerRef = "secondTransactionManager"
)
public class SecondDatabaseConfig {

    @Bean
    DataSource secondDataSource() {

        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/test2?serverTimezone=Asia/Seoul&characterEncoding=UTF-8")
                .username("test")
                .password("test")
                .build();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean secondEntityManger() {

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();

        entityManager.setDataSource(secondDataSource());
        entityManager.setPackagesToScan(new String[] { "com.slowstarter.second.entity" });
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> properties = new ConcurrentHashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");

        entityManager.setJpaPropertyMap(properties);

        return entityManager;
    }

    @Bean
    PlatformTransactionManager secondTransactionManager() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(secondEntityManger().getObject());

        return transactionManager;
    }
}
