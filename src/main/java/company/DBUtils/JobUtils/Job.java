package company.DBUtils.JobUtils;

import company.objects.graph.Path;

import java.time.LocalDate;
import java.util.ArrayList;

public class Job {
    //add a add path function to convert a path to a node array
    protected String userID;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected Boolean complete;

    public Job() {
    }


    public Job(String userID, LocalDate startDate) {
        this.userID = userID;
        this.startDate = startDate;
    }

    public String getUserID() {
        return userID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    //add validation so that start cannot equal end
}

