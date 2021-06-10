package edu.ib.otherModels;

import java.time.LocalDateTime;

public class ResultModel {

    private String type;

    private LocalDateTime time;

    private String description;

    public ResultModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
