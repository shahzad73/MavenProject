package hadoop.Joins;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;


public class UserJoinReducer extends Reducer<IntWritable, CustomerRecord, IntWritable, Text> {

	private ArrayList<CustomerRecord> listA = new ArrayList<CustomerRecord>();
	private ArrayList<CustomerRecord> listB = new ArrayList<CustomerRecord>();
	
	private String joinType = null;
	IntWritable keyval;

	@Override
	protected void setup(Context context) throws IOException,InterruptedException 
	{
		joinType = context.getConfiguration().get("join.type");	
		keyval = new IntWritable();
	}	
	
	
	
    @Override
    protected void reduce(IntWritable key, Iterable<CustomerRecord> values, Context context) throws IOException, InterruptedException 
    {
    	    	
    	//listA and B are declared globally so lists will retain its data between reduce calls.
    	//to make sure previous data does not create problems. lists are cleared.
    	listA.clear();
    	listB.clear();
    	

    	for (CustomerRecord rec: values ) 
    	{
    		if(rec.getTableID().get() == 1)
    			listA.add(rec);
    		if(rec.getTableID().get() == 2)
    			listB.add(rec);
    	}
    	

    	String s = "";

		if (joinType.equalsIgnoreCase("inner")) 
		{
			// If both lists are not empty, join A with B
			if (!listA.isEmpty() && !listB.isEmpty()) 
			{
				for (int i = 0; i < listA.size(); i++) 
				{
					for (int i2 = 0; i2 < listB.size(); i2++) 
					{
						CustomerRecord A1,B1;
						A1 = listA.get(i);
						B1 = listB.get(i2);
						s = "";
						s = A1.getCusReputation().toString() + "\t" + A1.getCusLocations() + "\t";
						s = B1.getCusPostID().toString() + "\t" + B1.getCusPostText();
						
						context.write(key, new Text(s));    						
					}
				}
			}
		}
		
		
		
		if (joinType.equalsIgnoreCase("leftouter")) 
		{
			// For each entry in A,
			for (CustomerRecord A : listA) 
			{
				// If list B is not empty, join A and B
				if (!listB.isEmpty()) 
				{
					for (CustomerRecord B : listB) 
					{
						s = A.getCusReputation().toString() + "\t" + A.getCusLocations() + "\t";
						s = s + B.getCusPostID().toString() + "\t" + B.getCusPostText();
						
						context.write(key, new Text(s));  
					}
				} 
				else 
				{
					s = A.getCusReputation().toString() + "\t" + A.getCusLocations() + "\t";
					s = s + "N/A\tN/A";
					
					context.write(key, new Text(s));  
				}
			}
		}		
    	

		
		if (joinType.equalsIgnoreCase("rightouter")) 
		{
			// For each entry in A,
			for (CustomerRecord B : listA) 
			{
				// If list B is not empty, join A and B
				if (!listA.isEmpty()) 
				{
					for (CustomerRecord A : listA) 
					{
						s = A.getCusReputation().toString() + "\t" + A.getCusLocations() + "\t";
						s = s + "\t" + B.getCusPostID().toString() + "\t" + B.getCusPostText();
						
						context.write(key, new Text(s));  
					}
				} 
				else 
				{
					s = "\tN/A\tN/A";
					s = s + B.getCusReputation().toString() + "\t" + B.getCusLocations() + "\t";
					
					
					context.write(key, new Text(s));  
				}
			}
		}	
		
		
		
		
    }	
	
}
