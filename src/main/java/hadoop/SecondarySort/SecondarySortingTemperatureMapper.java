package hadoop.SecondarySort;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class SecondarySortingTemperatureMapper extends Mapper<LongWritable, Text, TemperaturePair, NullWritable> {

    private TemperaturePair temperaturePair = new TemperaturePair();
    private NullWritable nullValue = NullWritable.get();
    private static final int MISSING = 9999;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	    String[] words = value.toString().split("\t") ;
        
        String yearMonth = words[0];

        int temp = Integer.parseInt(words[1]);

        if (temp != MISSING) {
            temperaturePair.setYearMonth(yearMonth);
            temperaturePair.setTemperature(temp);
            context.write(temperaturePair, nullValue);
        }
    }
}

