package Distribuido.Proyecto.Distributed;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Component
public class ClusterHttpClient {

    private final List<String> peers;
    private final RestTemplate rest = new RestTemplate();

    public ClusterHttpClient() {
        String raw = System.getenv().getOrDefault("PEERS", "").trim();
        if (raw.isEmpty()) peers = List.of();
        else peers = Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public List<String> getPeers() { return peers; }

    private String normalize(String base) {
        if (base == null) return "";
        return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    }

    public void sendRequestToAll(RequestMessage msg) {
        for (String p : peers) {
            try { rest.postForEntity(normalize(p) + "/distributed/request", msg, Void.class); }
            catch (Exception e) { System.err.println("sendRequestToAll -> " + p + " : " + e.getMessage()); }
        }
    }

    public void sendOkTo(String peerUrl, RequestMessage ok) {
        try { rest.postForEntity(normalize(peerUrl) + "/distributed/ok", ok, Void.class); }
        catch (Exception e) { System.err.println("sendOkTo -> " + peerUrl + " : " + e.getMessage()); }
    }

    public void sendReleaseToAll(RequestMessage rel) {
        for (String p : peers) {
            try { rest.postForEntity(normalize(p) + "/distributed/release", rel, Void.class); }
            catch (Exception e) { System.err.println("sendReleaseToAll -> " + p + " : " + e.getMessage()); }
        }
    }

    public ResponseEntity<String> depositRemote(String peerUrl, String banco, String cuenta, double monto) {
    try {
        String base = normalize(peerUrl);
        String url = String.format("%s/transacciones/%s/depositar?cuenta=%s&monto=%s",
                base, banco, cuenta, String.valueOf(monto));
        return rest.postForEntity(url, null, String.class);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("ERROR: " + e.getMessage());
    }
}


    public ResponseEntity<String> withdrawRemote(String peerUrl, String banco, String cuenta, double monto) {
        try {
            String base = normalize(peerUrl);
            String url = String.format("%s/transacciones/%s/retirar?cuenta=%s&monto=%s",
                    base, banco, cuenta, String.valueOf(monto));
            return rest.postForEntity(url, null, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("ERROR: " + e.getMessage());
        }
    }
}
