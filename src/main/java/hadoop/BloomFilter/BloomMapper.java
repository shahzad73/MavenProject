package hadoop.BloomFilter;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.bloom.DynamicBloomFilter;
import org.apache.hadoop.util.bloom.Key;

public class BloomMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

	DynamicBloomFilter filter = new DynamicBloomFilter();
	
	@Override
	protected void setup(Context context) throws IOException,InterruptedException 
	{
		
		
		// Get file from the DistributedCache
		Path[] cacheFilesLocal = DistributedCache.getLocalCacheFiles(context.getConfiguration());
		
		System.out.println("Reading Bloom filter from: " + cacheFilesLocal[0]);
		// Open local file for read.
		DataInputStream strm = new DataInputStream( new FileInputStream(cacheFilesLocal[0].toString()) );
		
		// Read into our Bloom filter.
		filter.readFields(strm);
		strm.close();
	}
	
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	{
		
		StringTokenizer tokenizer = new StringTokenizer(value.toString());
		// For each word in the comment
		while (tokenizer.hasMoreTokens()) 
		{
			// If the word is in the filter, output the record and break
			String word = tokenizer.nextToken();
			if (filter.membershipTest(new Key(word.getBytes()))) 
			{
				context.write(value, NullWritable.get());
				break;
			}	
		}
		
	}
	
}
