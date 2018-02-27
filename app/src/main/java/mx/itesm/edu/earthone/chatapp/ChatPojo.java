package mx.itesm.edu.earthone.chatapp;

/**
 * Created by ovman on 27/02/2018.
 */

public class ChatPojo {
    private String name, inageUrl, message;

    public ChatPojo() {
    }

    public ChatPojo(String name, String inageUrl, String message) {
        this.name = name;
        this.inageUrl = inageUrl;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInageUrl() {
        return inageUrl;
    }

    public void setInageUrl(String inageUrl) {
        this.inageUrl = inageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
