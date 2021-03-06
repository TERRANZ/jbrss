package ru.terra.jbrss.tenancy;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TenantDataSource extends AbstractRoutingDataSource {
    @Autowired
    @Qualifier("nonTenantDataSource")
    DataSource nonTenantDataSource;

    @Value("${spring.datasource.driver-class-name:com.mysql.jdbc.Driver}")
    String driver;

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/jbrss3}")
    String url;

    @Value("${spring.datasource.username:jbrss}")
    String user;

    @Value("${spring.datasource.password:jbrss}")
    String pass;

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    Map<String, DataSource> tenantDataSourceMap = new ConcurrentHashMap<>();

    /**
     * This returns a read-only view of the map.  Useful for cases where you want to display
     * the current state of the datasource and connection pool
     */
    Map<String, DataSource> getTenantDataSourceMap() {
        return Collections.unmodifiableMap(tenantDataSourceMap);
    }

    @Override
    protected DataSource determineTargetDataSource() {
        TenantDataStoreConfiguration tenantDataStoreConfiguration = TenantDataStoreAccessor.getConfiguration();
        if (tenantDataStoreConfiguration == null) {
            return nonTenantDataSource;
        }
        if (tenantDataSourceMap.containsKey(tenantDataStoreConfiguration.getUsername())) {
            return tenantDataSourceMap.get(tenantDataStoreConfiguration.getUsername());
        }

        DataSource tenantDataSource = new DataSource();
        tenantDataSource.setDriverClassName(driver);
        tenantDataSource.setUsername(user);
        tenantDataSource.setPassword(pass);
        tenantDataSource.setUrl(url + "_" + tenantDataStoreConfiguration.getUsername());
        tenantDataSource.setTestOnBorrow(true);
        tenantDataSource.setTestWhileIdle(true);

        tenantDataSourceMap.put(tenantDataStoreConfiguration.getUsername(), tenantDataSource);

        return tenantDataSource;
    }

    @Override
    public void afterPropertiesSet() {

    }
}
