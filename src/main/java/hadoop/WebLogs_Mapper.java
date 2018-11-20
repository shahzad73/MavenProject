package hadoop;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
//import org.apache.hadoop.io.Writable;
//import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;


public class WebLogs_Mapper extends MapReduceBase implements Mapper<LongWritable, Text, WebLogs_Writable, IntWritable>
{

	private static final IntWritable one = new IntWritable(1);
	  
	  private WebLogs_Writable wLog = new WebLogs_Writable();

	  private IntWritable reqno = new IntWritable();
	  private Text url = new Text();
	  private Text rdate = new Text();
	  private Text rtime = new Text();
	  private Text rip = new Text();
	             
	             
	  public void map(LongWritable key, Text value, OutputCollector<WebLogs_Writable, IntWritable> output, Reporter reporter) throws IOException 
	  {
	    String[] words = value.toString().split("\t") ;

	    reqno.set(Integer.parseInt(words[0]));
	    url.set(words[1]);
	    rdate.set(words[2]);
	    rtime.set(words[3]);
	    rip.set(words[4]);

	    wLog.set(reqno, url, rdate, rtime, rip);

	    output.collect(wLog, one);
	  }
	
}
