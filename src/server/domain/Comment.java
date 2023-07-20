package server.domain;

public class Comment {

    private String createdAt;
    private String createdBy;
    private String body;

    // Constructors
    public Comment() {
        // Default constructor
    }

    public Comment(String createdAt, String createdBy, String body) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.body = body;
    }

    // Getters and Setters
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
