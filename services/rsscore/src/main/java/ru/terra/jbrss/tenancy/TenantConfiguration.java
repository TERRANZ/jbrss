package ru.terra.jbrss.tenancy;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
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
import java.util.HashMap;
import java.util.Map;

@EnableTransactionManagement
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = "ru.terra.jbrss.db.repos.tenant")
public class TenantConfiguration {

    @Autowired
    private JpaProperties jpaProperties;

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean multiTenantEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource multiTenantDataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.putAll(jpaProperties.getHibernateProperties(multiTenantDataSource));
        properties.put(Environment.SHOW_SQL, "true");
        properties.put(Environment.HBM2DDL_AUTO, "update");
        return builder
                .dataSource(multiTenantDataSource)
                .properties(properties)
                .persistenceUnit("multitenant")
                .build();
    }

    @Primary
    @Bean(name = "dataSource")
    public DataSource multiTenantDataSource() {
        return new TenantDataSource();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager multiTenantTransactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory multiTenantEntityManagerFactory) {
        return new JpaTransactionManager(multiTenantEntityManagerFactory);
    }
}
