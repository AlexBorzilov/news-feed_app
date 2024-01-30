package aborzilov.newsfeed.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "logs")
@Getter
@Setter
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date created;
    private String method;
    private String uri;
    private int status;
    private String exception;

    @PrePersist
    public void recordCreateTime() {
        created = new Date();
    }
}