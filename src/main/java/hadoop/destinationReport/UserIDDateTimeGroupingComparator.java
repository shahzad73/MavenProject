package hadoop.destinationReport;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;


public class UserIDDateTimeGroupingComparator extends WritableComparator {

    public UserIDDateTimeGroupingComparator() {
        super(UserIDDateTimePair.class, true);
    }


    @Override
    public int compare(WritableComparable tp1, WritableComparable tp2) 
    {
    	UserIDDateTimePair k1 = (UserIDDateTimePair) tp1;
    	UserIDDateTimePair k2 = (UserIDDateTimePair) tp2;
    	

        int result = k1.getUserID().compareTo(k2.getUserID());

        
        return result;    	
    }
    
}
