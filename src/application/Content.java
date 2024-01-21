package application;

public class Content {
    private String content;

    public Content(String content) {
        this.content = content;
    }

    public Content() {
        this.content = "";
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addContent(String content) {
        this.content += content;
    }
    
}
