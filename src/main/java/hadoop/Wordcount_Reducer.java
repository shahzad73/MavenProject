package hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Wordcount_Reducer extends Reducer<Text, IntWritable, Text, IntWritable>
{
	public void reduce( Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
	{
			// Iterate over all of the values (counts of occurrences of this word)
			int count = 0;
			for(IntWritable w: values)
			{
			    // Add the value to our count
			    count += Integer.parseInt(w.toString());
			}
			
			// Output the word with its count (wrapped in an IntWritable)
			context.write( key, new IntWritable( count ) );
	}
}
