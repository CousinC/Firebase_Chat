package hoshin.firebase_chat;

public class Message {

    private String key;
    private String content;
    private String userName;
    private String userEmail;
    private Long timestamp;

    public Message(){
    }

    public Message(String name, String mail, String content, Long timestamp){
        this.content = content;
        this.userName = name;
        this.userEmail = mail;
        this.timestamp = timestamp;
    }

    public Message(String key, String name, String mail, String content, Long timestamp){
        this.key = key;
        this.content = content;
        this.userName = name;
        this.userEmail = mail;
        this.timestamp = timestamp;
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