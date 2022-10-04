import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class Config {

    private List<String> servers = new ArrayList<String>();

    public List<String> getServers() {
        return this.servers;
    }
}
