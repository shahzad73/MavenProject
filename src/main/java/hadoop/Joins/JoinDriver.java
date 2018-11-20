package hadoop.Joins;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class JoinDriver extends Configured implements Tool{
	
    public static void main(String[] args) throws Exception {
    	
        if(args.length < 3){
            System.out.println("Please supply TableA, TableB and output dir");
            System.exit(1);
        }
        
        int res = ToolRunner.run(new Configuration(), new JoinDriver(), args);
        System.exit(res);
    }	
	
	
    
    @Override
    public int run(String[] args) throws Exception 
    {
    	
        // When implementing tool
        Configuration conf = this.getConf();
        
        // Create job
        Job job = new Job(conf, "Join MapReduce");
        job.setJarByClass(JoinDriver.class);
        
        
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, UserJoinMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, UserCommentsMapper.class);
        
        TextOutputFormat.setOutputPath(job, new Path(args[2]));
        
        job.getConfiguration().set("join.type", args[3]);   //inner, leftouter, rightouter, fullouter
        
        job.setJarByClass(JoinDriver.class);
        
        job.setReducerClass( UserJoinReducer.class );
        
        job.setMapOutputKeyClass(IntWritable.class); 			//Mapper input and output classes
        job.setMapOutputValueClass(CustomerRecord.class);
        job.setOutputKeyClass(IntWritable.class);				//Reducer input and output classes
        job.setOutputValueClass(Text.class);
        
       
        
        return job.waitForCompletion(true) ? 0 : 1;
    }	
    

}
