package hadoop.destinationReport;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class destinationReport_Mapper extends Mapper<LongWritable, Text, UserIDDateTimePair, Text>
	{
	UserIDDateTimePair keyValue= new UserIDDateTimePair();
	
	
	
	public void map( LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	{
		// Iterate over all of the words in the string
		String st[] = value.toString().split("\t");
		
		
		keyValue.setUserID( Integer.parseInt(st[0]));
		keyValue.setdateAndTime(st[1] + " " + st[2]);
		
		context.write(keyValue, new Text(st[5]));
	}

}
