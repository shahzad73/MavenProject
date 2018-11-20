package hadoop.destinationReport;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class destinationReport_Reducer extends Reducer<UserIDDateTimePair, Text, Text, Text>
{
	public void reduce( UserIDDateTimePair key, Iterable<Text> values, Context context) throws IOException, InterruptedException
	{

		Set<String> set = new HashSet<String>();
		
		for(Text w: values)
			set.add(w.toString());
		
		if(set.size() > 1)
		{
			Iterator<String> it = set.iterator();
			it.next();		
			context.write(new Text(key.getUserID().toString()), new Text( it.next() ));
		}
		else
			context.write(new Text(key.getUserID().toString()), new Text("Nil"));

	}
}
