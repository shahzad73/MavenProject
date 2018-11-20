package hadoop.destinationReport;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class sortingCompositeKeyComparator extends WritableComparator {

    protected sortingCompositeKeyComparator() {
        super(UserIDDateTimePair.class, true);
    }   
    @SuppressWarnings("rawtypes")
    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
    	UserIDDateTimePair k1 = (UserIDDateTimePair)w1;
    	UserIDDateTimePair k2 = (UserIDDateTimePair)w2;
         
        int result = k1.getUserID().compareTo(k2.getUserID());
        if(0 == result) {
            result = -1* k1.getdateAndTime().compareTo(k2.getdateAndTime());
        }
        
        return result;
    }
	
}
