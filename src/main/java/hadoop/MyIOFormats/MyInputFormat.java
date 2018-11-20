package hadoop.MyIOFormats;

import java.io.IOException;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;


public class MyInputFormat extends FileInputFormat<CustomInputFormat_MyKey, CustomInputFormat_MyValue> 
{
	@Override
	public RecordReader<CustomInputFormat_MyKey, CustomInputFormat_MyValue> createRecordReader(
			InputSplit arg0, TaskAttemptContext arg1) throws IOException,
			InterruptedException {
			
		return (RecordReader<CustomInputFormat_MyKey, CustomInputFormat_MyValue>) new MyRecordReader();
	}
	
}


