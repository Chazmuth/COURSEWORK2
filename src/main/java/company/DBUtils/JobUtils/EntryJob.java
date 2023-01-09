package company.DBUtils.JobUtils;

import company.objects.graph.Path;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class EntryJob extends Job{

    //add a add path function to convert a path to a node array
    protected int time;
    protected ArrayList<Integer> path;

    public EntryJob(String userID, String startDate, boolean strip, int time, Path path) {
        super(userID);
        this.userID = userID;
        this.startDate = parseDate(startDate, strip);
        this.time = time;
        this.endDate = this.startDate.plusDays(time);
        System.out.println(this.endDate);
        this.path = path.getNodes();
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    //add validation so that start cannot equal end

    public static void main(String[] args){
        new EntryJob("bnnaijskd", "2000-10-10", false, 7, new Path());
    }
}
