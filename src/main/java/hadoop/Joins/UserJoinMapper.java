package hadoop.Joins;


import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class UserJoinMapper extends Mapper<LongWritable, Text, IntWritable, CustomerRecord> 
{
	CustomerRecord crec;
	IntWritable skey = new IntWritable();
	

	@Override
	protected void setup(Context context) throws IOException,InterruptedException 
	{
		crec = new CustomerRecord();	
	}
	
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	{
		
	    String[] words = value.toString().split("\t") ;
	    
	    
	    crec.setTableID(new IntWritable(1));

	    crec.setCusReputation(new Text(words[1]));
	    crec.setCusLocations(new Text(words[2]));
		
	    
	    skey.set(Integer.parseInt(words[0]));
		
		context.write(skey, crec);
	}
	
}