package server.domain;

import jakarta.persistence.*;

import java.time.Instant;
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_Id")
    private long commentId;
    @Column(name = "created_at")
    private Instant createdAt;
    @ManyToOne
    @JoinColumn(name = "created_by",nullable = false)
    private User createdBy;
    @Column(name = "body",nullable = false)
    private String body;
    @ManyToOne
    @JoinColumn(name = "task_Id")
    private Task task;


    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Task getTasks() {
        return task;
    }


    public void addTaskForComment(Task task) {
        this.task = task;
    }

}
