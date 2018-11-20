package hadoop.MyIOFormats;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;


public class MyRecordReader extends RecordReader<CustomInputFormat_MyKey, CustomInputFormat_MyValue> 
{

	private CustomInputFormat_MyKey key;
	private CustomInputFormat_MyValue value;
	private static LineRecordReader reader;


	  public MyRecordReader() throws IOException {	    

	  }
	

		@Override
		public void initialize(InputSplit gs, TaskAttemptContext ta)
				throws IOException, InterruptedException {
		
			reader = new LineRecordReader();
			reader.initialize(gs, ta);
		}	  
	  
	
	@Override
	public void close() throws IOException 
	{
		// TODO Auto-generated method stub
		reader.close();
	}

	
	@Override
	public CustomInputFormat_MyKey getCurrentKey() throws IOException 
	{
		// TODO Auto-generated method stub
		return key;
	}

	
	@Override
	public CustomInputFormat_MyValue getCurrentValue() throws IOException, InterruptedException 
	{
		// TODO Auto-generated method stub
		return value;
	}

	
	@Override
	public float getProgress() throws IOException, InterruptedException 
	{
		// TODO Auto-generated method stub
		return reader.getProgress();
	}

	
	
	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException 
	{
		// TODO Auto-generated method stub
		
		  if (reader.nextKeyValue())    //if there is an more value it will be retrived in shape of Key=LongWritable  and  Value=Text    then you can use getCurrentKey and getCurrentValue 
		  {
				if(key==null)
					key = new CustomInputFormat_MyKey();
				if(value == null)
					value = new CustomInputFormat_MyValue();
				
				//LongWritable kk = reader.getCurrentKey();       //this will get the current key			
				String line = reader.getCurrentValue().toString();				
				
				String[] tokens = line.toString().split("\t");
				key.setSensorType(new Text(tokens[0]));
				key.setTimestamp(new Text(tokens[1]));
				key.setStatus(new Text(tokens[2]));
				value.setValue(new Text(tokens[3]));
				
				return true;
		  }
		  else
			  return false;
	}

}