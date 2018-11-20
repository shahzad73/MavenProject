package hadoop;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class Wordcount extends Configured implements Tool{
	
    public static void main(String[] args) throws Exception {
    	
        if(args.length < 1){
            System.out.println("Please supply TableA, TableB and output dir");
            System.exit(1);
        }
        
        int res = ToolRunner.run(new Configuration(), new Wordcount(), args);
        System.exit(res);
    }	
	
	
    
    @Override
    public int run(String[] args) throws Exception 
    {
    	
        // When implementing tool
        Configuration conf = this.getConf();
        
        // Create job
        Job job = new Job(conf, "Word Counts");
        job.setJarByClass(Wordcount.class);
               
        TextInputFormat.addInputPath(job, new Path(args[0]));
        TextOutputFormat.setOutputPath(job, new Path(args[1]));
                        
        job.setMapperClass( Wordcount_Mapper.class );
        job.setReducerClass( Wordcount_Reducer.class );
        
        job.setMapOutputKeyClass(Text.class); 			//Mapper input and output classes
        job.setMapOutputValueClass(IntWritable.class);
        
        job.setOutputKeyClass(Text.class);				//Reducer input and output classes
        job.setOutputValueClass(IntWritable.class);
        
       
        
        return job.waitForCompletion(true) ? 0 : 1;
    }	
    

}
