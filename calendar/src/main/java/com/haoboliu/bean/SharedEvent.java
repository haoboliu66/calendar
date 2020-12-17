package com.haoboliu.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class SharedEvent implements Serializable {

    private Integer id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")  // back to Vue
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // format from Vue
    private Date start;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")  // back to Vue
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")    // format from Vue
    private Date end;

    private String location;

    private String description;

    private Integer accountId;

    private String username;

    private String email;

    // an {event} from  a {account}
    public SharedEvent(Event event, Account account) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.start = event.getStart();
        this.end = event.getEnd();
        this.location = event.getLocation();
        this.description = event.getDescription();
        this.accountId = account.getId();
        this.username = account.getUsername();
        this.email = account.getEmail();
    }

    public SharedEvent(Integer id, String title, Date start, Date end, String location, String description, Integer accountId, String username, String email) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.location = location;
        this.description = description;
        this.accountId = accountId;
        this.username = username;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "SharedEvent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", accountId=" + accountId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
