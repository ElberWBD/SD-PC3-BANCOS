package Distribuido.Proyecto.Distributed;

import java.util.*;

public class RicartAgrawala {

    private final String myId;
    private final String myUrl;
    private final ClusterHttpClient client;

    private int logicalClock = 0;
    private boolean requesting = false;
    private int requestTS = 0;
    private int okReceived = 0;

    private final Map<String, Boolean> oks = new HashMap<>();
    private final List<RequestMessage> deferred = new ArrayList<>();

    public RicartAgrawala(String myId, String myUrl, ClusterHttpClient client) {
        this.myId = myId;
        this.myUrl = myUrl;
        this.client = client;
    }

    private synchronized int tick() {
        logicalClock++;
        return logicalClock;
    }

    private synchronized void reset() {
        oks.clear();
        for (String p : client.getPeers()) oks.put(p, false);
        okReceived = 0;
    }

    public synchronized void requestAccess() {
        reset();
        requesting = true;
        requestTS = tick();
        RequestMessage req = new RequestMessage(MessageType.REQUEST, requestTS, myId, myUrl);
        client.sendRequestToAll(req);

        while (okReceived < oks.size()) {
            try { wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
        }
    }

    public synchronized void onReceiveRequest(RequestMessage msg) {
        // update clock
        logicalClock = Math.max(logicalClock, msg.getTimestamp()) + 1;

        boolean defer = false;
        if (requesting) {
            // CORRECCION: si mi timestamp es mayor -> el otro tiene prioridad (yo debo deferir)
            if (requestTS > msg.getTimestamp()) defer = true;
            else if (requestTS == msg.getTimestamp() && myId.compareTo(msg.getFromId()) > 0) defer = true;
        }

        if (defer) {
            deferred.add(msg);
        } else {
            RequestMessage ok = new RequestMessage(MessageType.OK, tick(), myId, myUrl);
            client.sendOkTo(msg.getFromUrl(), ok);
        }
    }

    public synchronized void onReceiveOk(RequestMessage msg) {
        logicalClock = Math.max(logicalClock, msg.getTimestamp()) + 1;
        okReceived++;
        notifyAll();
    }

    public synchronized void onReceiveRelease(RequestMessage msg) {
        logicalClock = Math.max(logicalClock, msg.getTimestamp()) + 1;
        for (RequestMessage d : new ArrayList<>(deferred)) {
            RequestMessage ok = new RequestMessage(MessageType.OK, tick(), myId, myUrl);
            client.sendOkTo(d.getFromUrl(), ok);
        }
        deferred.clear();
    }

    public synchronized void release() {
        requesting = false;
        RequestMessage rel = new RequestMessage(MessageType.RELEASE, tick(), myId, myUrl);
        client.sendReleaseToAll(rel);

        for (RequestMessage d : new ArrayList<>(deferred)) {
            RequestMessage ok = new RequestMessage(MessageType.OK, tick(), myId, myUrl);
            client.sendOkTo(d.getFromUrl(), ok);
        }
        deferred.clear();
    }
}
