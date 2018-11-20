package hadoop.MyIOFormats;

import hadoop.SecondarySort.TemperaturePair;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class TextCustomInputDriver {

    public static void main(String[] args) throws Exception {

        if(args.length < 2){
            System.out.println("Please supply in input and output path");
            System.exit(1);
        }

        Job job = Job.getInstance(new Configuration());

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        

        job.setJarByClass(TextCustomInputDriver.class);
		job.setJobName("Testing Custom Input Format");
		job.setNumReduceTasks(0);
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(TemperaturePair.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.setInputFormatClass(MyInputFormat.class);       
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}