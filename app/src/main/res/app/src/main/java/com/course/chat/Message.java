package app.src.main.java.com.course.chat;

/**
 * Created by vhernest on 19/11/15.
 */
public class Message {

    private long id;
    private long when;
    private String username;
    private String text;

    public Message(long id, long when, String username, String text) {
        this.id = id;
        this.when = when;
        this.username = username;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
