package company.DBUtils;

import company.objects.graph.Path;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Job {

    //add a add path function to convert a path to a node array
    protected String userID;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected int time;

    protected ArrayList<Integer> path;

    public Job(String userID, LocalDate startDate, int time, Path path) {
        this.userID = userID;
        this.startDate = startDate;
        this.time = time;
        this.endDate = this.startDate.plusDays(time);
        this.path = path.getNodes();
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

    public ArrayList<Integer> getPath() {
        return path;
    }

    //add validation so that start cannot equal end
}
