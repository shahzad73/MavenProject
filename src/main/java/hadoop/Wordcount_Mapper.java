package hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class Wordcount_Mapper extends Mapper<LongWritable, Text, Text, IntWritable>
	{
	private Text word = new Text();
	private final static IntWritable one = new IntWritable( 1 );
	
	public void map( LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	{
	// Get the value as a String
	String text = value.toString().toLowerCase();
	
	// Replace all non-characters
	text = text.replaceAll( "'", "" );
	text = text.replaceAll( "[^a-zA-Z]", " " );
	
	// Iterate over all of the words in the string
	StringTokenizer st = new StringTokenizer( text );
	while( st.hasMoreTokens() )
	{
	    // Get the next token and set it as the text for our "word" variable
	    word.set( st.nextToken() );
	
	    // Output this word as the key and 1 as the value
	    context.write( word, one );
	}
	}
}
