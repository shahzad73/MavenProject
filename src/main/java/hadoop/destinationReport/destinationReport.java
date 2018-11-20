package hadoop.destinationReport;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



public class destinationReport extends Configured implements Tool{
	
    public static void main(String[] args) throws Exception {
        
        int res = ToolRunner.run(new Configuration(), new destinationReport(), args);
        System.exit(res);
    }	
	
	
    

    public int run(String[] args) throws Exception 
    {    	
        // This application uses Secondary Sort to sort destinations according to date/time
    	// sort ordering. 
        
        Job job = Job.getInstance(new Configuration());

        TextInputFormat.addInputPath(job, new Path(args[0]));        
        TextOutputFormat.setOutputPath(job, new Path(args[1]));
        
        job.setJarByClass(destinationReport.class);

        // UserIDDateTimePart key has UserID and DateTime fields. Sorting is done both on 
        //UserID and DateTime but grouping is done only on UserID. So on reduce side all 
        //destinations are received in the order they were searched during the day
        job.setMapOutputKeyClass(UserIDDateTimePair.class);     			
        job.setMapOutputValueClass(Text.class);       
        job.setOutputKeyClass(Text.class);        
        job.setOutputValueClass(Text.class);
        
        job.setMapperClass( destinationReport_Mapper.class );
        job.setReducerClass( destinationReport_Reducer.class );
        
        job.setPartitionerClass(destinationReportPartitioner.class);       //Tuples are partitioned on UserID
        job.setGroupingComparatorClass(UserIDDateTimeGroupingComparator.class);  //Reduce side group is done on UserID
        job.setSortComparatorClass(sortingCompositeKeyComparator.class);  // sorting is done on UserID and DateTime combined. So on reduce side when destination of a user are received in the precise order they were searched by the user during the day
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);        
        return job.waitForCompletion(true) ? 0 : 1;
    }	
    

}
