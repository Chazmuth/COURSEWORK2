package company.DBUtils.JobUtils;

import company.objects.graph.Path;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

public class Job {
    //add a add path function to convert a path to a node array
    protected String userID;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected Boolean complete;

    public Job(ArrayList<String> jobArray) {
        this.userID = jobArray.get(0);
        this.startDate = parseDate(jobArray.get(1));
        this.endDate = parseDate(jobArray.get(2));
        this.complete = Boolean.valueOf(jobArray.get(3));
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

    public LocalDate parseDate(String unformattedDate){
        String dateParseInput = unformattedDate.
                substring(0, unformattedDate.indexOf(" "));
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder().
                parseCaseInsensitive().
                parseLenient().
                appendPattern("[yyyy-MM-dd]");
        DateTimeFormatter formatter = builder.toFormatter();
        return LocalDate.parse(dateParseInput, formatter);
    }

    public Object[] toArray(){
        Object[] array = new Object[4];
        array[0] = this.userID;
        array[1] = this.startDate;
        array[2] = this.endDate;
        array[3] = this.complete;
        return array;
    }

    @Override
    public String toString() {
        return "Job{" +
                "userID='" + userID + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", complete=" + complete +
                '}';
    }

    //add validation so that start cannot equal end
}

