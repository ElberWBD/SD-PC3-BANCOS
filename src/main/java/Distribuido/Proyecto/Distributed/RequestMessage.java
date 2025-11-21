package Distribuido.Proyecto.Distributed;

public class RequestMessage {
    private MessageType type;
    private int timestamp;
    private String fromId;
    private String fromUrl;

    public RequestMessage() {}

    public RequestMessage(MessageType type, int timestamp, String fromId, String fromUrl) {
        this.type = type;
        this.timestamp = timestamp;
        this.fromId = fromId;
        this.fromUrl = fromUrl;
    }

    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }

    public int getTimestamp() { return timestamp; }
    public void setTimestamp(int timestamp) { this.timestamp = timestamp; }

    public String getFromId() { return fromId; }
    public void setFromId(String fromId) { this.fromId = fromId; }

    public String getFromUrl() { return fromUrl; }
    public void setFromUrl(String fromUrl) { this.fromUrl = fromUrl; }
}
