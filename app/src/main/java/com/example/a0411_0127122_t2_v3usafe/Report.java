package com.example.a0411_0127122_t2_v3usafe;

import java.io.Serializable;
import java.util.ArrayList;

public class Report implements Serializable {
    private String issue;
    private String location;
    private String description;
    private String reportId;
    private String userId;

    public Report(String issue, String location, String description, String reportId, String userId) {
        this.issue = issue;
        this.location = location;
        this.description = description;
        this.reportId = reportId;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
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

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
}
