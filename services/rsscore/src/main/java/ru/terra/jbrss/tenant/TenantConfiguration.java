package ru.terra.jbrss.tenant;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = "ru.**.repos.tenant")
public class TenantConfiguration {
    @Primary
    @Bean(name = "entityManagerFactory")
    LocalContainerEntityManagerFactoryBean multiTenantEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource multiTenantDataSource) {
        return builder
                .dataSource(multiTenantDataSource)
                .packages("ru.terra.**.entity.tenant")
                .persistenceUnit("multitenant")
                .build();
    }

    @Primary
    @Bean(name = "dataSource")
    DataSource multiTenantDataSource() {
        return new TenantDataSource();
    }

    @Primary
    @Bean(name = "transactionManager")
    PlatformTransactionManager multiTenantTransactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory multiTenantEntityManagerFactory) {
        return new JpaTransactionManager(multiTenantEntityManagerFactory);
    }
}
