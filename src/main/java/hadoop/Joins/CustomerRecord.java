package hadoop.Joins;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;



public class CustomerRecord implements Writable
{
	private Text CusReputation, CusLocations, CusPostID, CusPostText;
	private IntWritable TableID;
	
	
	public CustomerRecord()
	{
		this.CusReputation = new Text();
		this.CusLocations = new Text();
		this.CusPostID = new Text();
		this.CusPostText = new Text();
		this.TableID = new IntWritable();
	}

	public CustomerRecord(Text CusReputation,Text  CusLocations, Text CusPostID,Text  CusPostText, IntWritable TableID)
	{
		this.CusReputation = CusReputation;
		this.CusLocations = CusLocations;
		this.CusPostID = CusPostID;
		this.CusPostText = CusPostText;
		this.TableID = TableID;
	}

	
	@Override
	public void readFields(DataInput in) throws IOException
	{
		CusReputation.readFields(in);
		CusLocations.readFields(in);
		CusPostID.readFields(in);
		CusPostText.readFields(in);
		TableID.readFields(in);
	}

	@Override   
	public void write(DataOutput out) throws IOException
	{
		CusReputation.write(out);
		CusLocations.write(out);
		CusPostID.write(out);
		CusPostText.write(out);
		TableID.write(out);
	}

	
	
	
	
	public Text getCusReputation() 
	{
		return CusReputation;
	}

	public Text getCusLocations() 
	{
		return CusLocations;
	}

	public Text getCusPostID() 
	{
		return CusPostID;
	}

	public Text getCusPostText() 
	{
		return CusPostText;
	}

	public IntWritable getTableID() 
	{
		return TableID;
	}


	
	
	
	public void setCusReputation(Text CusReputation) 
	{
		this.CusReputation = CusReputation;
	}

	public void setCusLocations(Text CusLocations) 
	{
		this.CusLocations = CusLocations;
	}

	public void setCusPostID(Text CusPostID) 
	{
		this.CusPostID = CusPostID;
	}

	public void setCusPostText(Text CusPostText) 
	{
		this.CusPostText = CusPostText;
	}

	public void setTableID(IntWritable TableID) 
	{
		this.TableID = TableID;
	}
	
	
	

}

