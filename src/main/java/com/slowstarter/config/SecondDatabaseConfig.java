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
    LocalContainerEntityManagerFactoryBean secondEntityManager() {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(secondDataSource());
        entityManager.setPackagesToScan(new String[] { "com.slowstarter.second.entity" });
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> properties = new ConcurrentHashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");

        // Physical naming strategy 설정
        /**
         * # Hibernate 물리 네이밍 전략 (스네이크 케이스로 테이블/컬럼명 매핑)
         * spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
         * # Hibernate 생성 네이밍 전략 (엔티티 클래스와 일치하는 이름 사용)
         * spring.jpa.hibernate.naming.implicit-strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
         */
        properties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");

        entityManager.setJpaPropertyMap(properties);

        return entityManager;
    }

    @Bean
    PlatformTransactionManager secondTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(secondEntityManager().getObject());
        return transactionManager;
    }
}
