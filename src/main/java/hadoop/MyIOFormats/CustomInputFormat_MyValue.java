package hadoop.MyIOFormats;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;


public class CustomInputFormat_MyValue implements WritableComparable<CustomInputFormat_MyValue>
{

	private Text value;

	public CustomInputFormat_MyValue(){
		this.value = new Text();
	}

	public CustomInputFormat_MyValue(Text value)
	{
		this.value = value;
	}

	@Override
	public void readFields(DataInput in) throws IOException
	{
		value.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException
	{
		value.write(out);
	}

	@Override
	public int compareTo(CustomInputFormat_MyValue o)
	{
		CustomInputFormat_MyValue other = o;
		int cmp = value.compareTo(other.value);
		return cmp;
	}

	
	public Text getValue() 
	{
		return value;
	}

	public void setValue(Text value) 
	{
		this.value = value;
	}

}