package ru.terra.jbrss.tenant;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
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
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@EnableTransactionManagement
@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "nonTenantEntityManagerFactory",
        transactionManagerRef = "nonTenantTransactionManager")
public class NonTenantConfiguration {
    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "nonTenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("nonTenantDataSource") DataSource dataSource) {
        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        hibernateProps.putAll(jpaProperties.getHibernateProperties(dataSource));
        hibernateProps.put(Environment.SHOW_SQL, "true");
        hibernateProps.put(Environment.HBM2DDL_AUTO, "update");
        hibernateProps.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        return builder
                .dataSource(dataSource)
                .packages("ru.terra")
                .properties(hibernateProps)
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
