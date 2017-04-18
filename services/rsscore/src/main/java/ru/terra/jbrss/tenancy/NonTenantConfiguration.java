package ru.terra.jbrss.tenancy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "nonTenantEntityManagerFactory",
        transactionManagerRef = "nonTenantTransactionManager",
        basePackages = "ru.terra.jbrss.db.repos.nontenant")
public class NonTenantConfiguration {

    @Bean(name = "nonTenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("nonTenantDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("ru.terra.jbrss.db.entity.nontenant")
                .persistenceUnit("nontenant")
                .build();
    }

    @Bean(name = "nonTenantDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "nonTenantTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("nonTenantEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
