package hadoop.destinationReport;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class UserIDDateTimePair implements Writable, WritableComparable<UserIDDateTimePair> {

    private Text dateAndTime = new Text();
    private IntWritable userID = new IntWritable();


    public UserIDDateTimePair() {
    }


    public UserIDDateTimePair(String ym, int uid) {
        dateAndTime.set(ym);
        userID.set(uid);
    }

    public static UserIDDateTimePair read(DataInput in) throws IOException {
    	UserIDDateTimePair UserIDDateTimePair = new UserIDDateTimePair();
    	UserIDDateTimePair.readFields(in);
        return UserIDDateTimePair;
    }


    public void write(DataOutput out) throws IOException {
        dateAndTime.write(out);
        userID.write(out);
    }


    public void readFields(DataInput in) throws IOException {
        dateAndTime.readFields(in);
        userID.readFields(in);
    }


    public int compareTo(UserIDDateTimePair UserIDDateTimePair) {
        int compareValue = this.dateAndTime.compareTo(UserIDDateTimePair.getdateAndTime());
        if (compareValue == 0) {
            compareValue = userID.compareTo(UserIDDateTimePair.getUserID());
        }
        return compareValue;
    }

    public Text getdateAndTime() {
        return dateAndTime;
    }

    public IntWritable getUserID() {
        return userID;
    }

    public void setdateAndTime(String dateAndTimeStr) {
        dateAndTime.set(dateAndTimeStr);
    }

    public void setUserID(int temp) {
    	userID.set(temp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserIDDateTimePair that = (UserIDDateTimePair) o;

        if (userID != null ? !userID.equals(that.userID) : that.userID != null) return false;
        if (dateAndTime != null ? !dateAndTime.equals(that.dateAndTime) : that.dateAndTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dateAndTime != null ? dateAndTime.hashCode() : 0;
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TemperaturePair{" +
                "dateAndTime=" + dateAndTime +
                ", userID=" + userID +
                '}';
    }
}
