package hadoop.MyIOFormats;


import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class MyMapper extends Mapper<CustomInputFormat_MyKey, CustomInputFormat_MyValue, Text, Text>
{
	protected void map(CustomInputFormat_MyKey key, CustomInputFormat_MyValue value, Context context) throws IOException, InterruptedException
	{
		String sensor = key.getSensorType().toString();
		Text keyText = new Text(sensor);
		if(sensor.toLowerCase().equals("a"))
		{
			context.write(keyText,value.getValue());
		}
	}


}


