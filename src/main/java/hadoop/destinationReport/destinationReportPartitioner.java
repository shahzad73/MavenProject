package hadoop.destinationReport;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;


public class destinationReportPartitioner extends Partitioner<UserIDDateTimePair, NullWritable> {

    @Override
    public int getPartition(UserIDDateTimePair userIDDateTimePair, NullWritable nullWritable, int numPartitions) {
        return userIDDateTimePair.getUserID().hashCode() % numPartitions;
    }
}
