package Distribuido.Proyecto.Config;

import Distribuido.Proyecto.Bancos.BancoNode;
import Distribuido.Proyecto.Distributed.ClusterHttpClient;
import Distribuido.Proyecto.Storage.BancoStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NodeConfig {

    @Bean
    public ClusterHttpClient clusterHttpClient() {
        return new ClusterHttpClient();
    }

    @Bean
    public BancoNode localNode(BancoStorage storage, ClusterHttpClient client) {
        String nodeId = System.getenv().getOrDefault("NODE_ID", "A").toUpperCase();
        return new BancoNode(nodeId, storage, client);
    }
}
