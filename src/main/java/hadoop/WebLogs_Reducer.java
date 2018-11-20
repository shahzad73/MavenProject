package hadoop;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


public class WebLogs_Reducer extends MapReduceBase implements Reducer<WebLogs_Writable, IntWritable, Text, IntWritable>
{
	
	  private IntWritable result = new IntWritable();
	  private Text ip = new Text();
	  

	@Override
	public void reduce(WebLogs_Writable key, Iterator<IntWritable> values,
			OutputCollector<Text, IntWritable> output, Reporter arg3)
			throws IOException {
		// TODO Auto-generated method stub
		
	    int sum = 0;
	    ip = key.getIp(); 
	    
	    while (values.hasNext()) 
	    {
	      sum++ ;
	    }
	    result.set(sum);
	    output.collect(ip, result);		
		
	}
	
}

