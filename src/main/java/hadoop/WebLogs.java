package hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


//hadoop jar myjar123.jar hadoop.wordcount /tmp/wordcounttext /tmp/outputDir

public class WebLogs extends Configured implements Tool {

	
	public int run(String[] args) throws Exception
    {
        // Create a configuration
        Configuration conf = getConf();

        // Create a job from the default configuration that will use the WordCount class
        JobConf job = new JobConf( conf, WebLogs.class );

        // Define our input path as the first command line argument and our output path as the second
        Path in = new Path( args[0] );
        Path out = new Path( args[1] );
        

        // Create File Input/Output formats for these paths (in the job)
        FileInputFormat.setInputPaths( job, in );
        FileOutputFormat.setOutputPath( job, out );

        // Configure the job: name, mapper, reducer, and combiner
        job.setJobName( "WebLogs" );
        job.setMapperClass( WebLogs_Mapper.class );
        job.setReducerClass( WebLogs_Reducer.class );
        job.setCombinerClass( WebLogs_Reducer.class );

        // Configure the output
        job.setOutputFormat( TextOutputFormat.class );
        job.setOutputKeyClass( Text.class );
        job.setOutputValueClass( IntWritable.class );

        // Run the job
        JobClient.runJob(job);
        return 0;
    }

    public static void main(String[] args) throws Exception
    {
        // Start the WordCount MapReduce application
        int res = ToolRunner.run( new Configuration(),
                new WebLogs(),
                args );
        System.exit( res );
    }
}
