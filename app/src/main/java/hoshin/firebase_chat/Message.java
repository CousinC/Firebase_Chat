package hoshin.firebase_chat;

public class Message {

    private String key;
    private String content;
    private String userName;
    private String userEmail;
    private Long timestamp;

    public Message(){
    }

    public Message(String name, String mail, String content){
        this.content = content;
        this.userName = userName;
        this.userEmail = userEmail;
        this.timestamp = System.currentTimeMillis();
    }

    public Message(String key, String name, String mail, String content){
        this.key = key;
        this.content = content;
        this.userName = userName;
        this.userEmail = userEmail;
        this.timestamp = System.currentTimeMillis();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}