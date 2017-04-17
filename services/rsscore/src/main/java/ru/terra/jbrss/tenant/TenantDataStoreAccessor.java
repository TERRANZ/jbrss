package ru.terra.jbrss.tenant;

public class TenantDataStoreAccessor {
    private static ThreadLocal configuration = new ThreadLocal();

    public static TenantDataStoreConfiguration getConfiguration() {
        return (TenantDataStoreConfiguration) configuration.get();
    }

    public static void removeConfiguration() {
        configuration.remove();
    }

    public static void setConfiguration(String username) {

        if (getConfiguration() != null) {
            removeConfiguration();
        }

        TenantDataStoreConfiguration tenantDataStoreConfiguration = new TenantDataStoreConfiguration();
        tenantDataStoreConfiguration.setUsername(username);
        configuration.set(tenantDataStoreConfiguration);
    }
}
