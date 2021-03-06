package ru.terra.jbrss.tenancy;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
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
import java.util.HashMap;
import java.util.Map;

@EnableTransactionManagement
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "nonTenantEntityManagerFactory",
        transactionManagerRef = "nonTenantTransactionManager",
        basePackages = "ru.terra.jbrss.db.repos.nontenant")
public class NonTenantConfiguration {
    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "nonTenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("nonTenantDataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.putAll(jpaProperties.getHibernateProperties(dataSource));
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.HBM2DDL_AUTO, "update");
        return builder
                .dataSource(dataSource)
                .properties(properties)
                .persistenceUnit("nontenant")
                .build();
    }

    @Bean(name = "nonTenantDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DataSource();
    }

    @Bean(name = "nonTenantTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("nonTenantEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
