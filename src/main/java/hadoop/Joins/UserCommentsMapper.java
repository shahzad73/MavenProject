package hadoop.Joins;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class UserCommentsMapper extends Mapper<LongWritable, Text, IntWritable, CustomerRecord> {

	CustomerRecord crec;
	IntWritable skey = new IntWritable();
	
	public static enum COUNTERS {
	    Total_Records_Processed
	}

	@Override
	protected void setup(Context context) throws IOException,InterruptedException 
	{
		crec = new CustomerRecord();	
	}
	
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	{
		// Parse the input string into a nice map
		//Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());		
		///String userId = parsed.get("Id");
		// The foreign join key is the user ID
		
		
	    String[] words = value.toString().split("\t") ;
	    
	    crec.setTableID(new IntWritable(2));
	    
	    crec.setCusPostID(new Text(words[1]));
	    crec.setCusPostText(new Text(words[2]));
	    
	    skey.set(Integer.parseInt(words[0]));
		
		context.write(skey, crec);
	}	
	
}
