package hr.algebra.jndi;


public enum ServerConfigurationKey {
    GAMESERVER_PORT("gameserver.port"),
    RMI_SERVER_PORT("rmi.server.port");

    private String key;

    private ServerConfigurationKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
