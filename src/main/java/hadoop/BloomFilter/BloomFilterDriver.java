package hadoop.BloomFilter;

import hadoop.BloomFilter.BloomMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.util.bloom.DynamicBloomFilter;
import org.apache.hadoop.util.bloom.Key;
import org.apache.hadoop.util.hash.Hash;




public class BloomFilterDriver extends Configured implements Tool {

	DynamicBloomFilter filter = null;
	
    public static void main(String[] args) throws Exception {
    	
        if(args.length < 1)
        {
            System.out.println("Supply information");
            System.exit(1);
        }

        
        //TrainBloomFilter(args);
        
        
        
        int res = ToolRunner.run(new Configuration(), new BloomFilterDriver(), args);
        System.exit(res);
        
    }	

    
    
    public static void TrainBloomFilter(String[] args) throws IOException
    {
    	
        Path inputFile = new Path(args[0]);
        int numMembers = Integer.parseInt(args[1]);
        float falsePosRate = Float.parseFloat(args[2]);
        Path bfFile = new Path(args[3]);
     	
    	
        // Calculate our vector size and optimal K value based on approximations
        //int vectorSize = getOptimalBloomFilterSize(numMembers, falsePosRate);
        int vectorSize = 50;
        //int nbHash = getOptimalK(numMembers, vectorSize);
        int nbHash = 5;
        // Create new Bloom filter        
        DynamicBloomFilter filter = new DynamicBloomFilter(vectorSize, nbHash, Hash.MURMUR_HASH, 1000000);
        
        
        		System.out.println("Training Bloom filter of size " + vectorSize
        		+ " with " + nbHash + " hash functions, " + numMembers
        		+ " approximate number of records, and " + falsePosRate
        		+ " false positive rate");
        		
        		// Open file for read
        		
        		String line = null;
        		int numElements = 0;
        		
        		FileSystem fs = FileSystem.get(new Configuration());
        		
        		for (FileStatus status : fs.listStatus(inputFile)) 
        		{
	        		BufferedReader rdr = new BufferedReader(new InputStreamReader(fs.open(status.getPath())));
	        		System.out.println("Reading " + status.getPath());
	        		while ((line = rdr.readLine()) != null) 
	        		{
	        			filter.add(new Key(line.getBytes()));
	        			++numElements;
	        		}
	        		rdr.close();
        		}
        		
        		
        		System.out.println("Trained Bloom filter with " + numElements + " entries.");
        		System.out.println("Serializing Bloom filter to HDFS at " + bfFile);
        		FSDataOutputStream strm = fs.create(bfFile);
        		
        		filter.write(strm);
        		strm.flush();
        		strm.close();
    	
    }
    
    
    /**
    * Gets the optimal-k value based on the input parameters.
    *
    * @param numElements
    * The number of elements used to train the set.
    * @param vectorSize
    * The size of the Bloom filter.
    * @return The optimal-k value, rounded to the closest integer.
    */
    public static int getOptimalK(float numElements, float vectorSize) 
    {
    	return (int) Math.round(vectorSize * Math.log(2) / numElements);
    }
    
    
    /**
    * Gets the optimal Bloom filter sized based on the input parameters and the
    * optimal number of hash functions.
    *
    * @param numElements
    * The number of elements used to train the set.
    * @param falsePosRate
    * The desired false positive rate.
    * @return The optimal Bloom filter size.
    */
    public static int getOptimalBloomFilterSize(int numElements,float falsePosRate) 
    {
    	return (int) (-numElements * (float) Math.log(falsePosRate) / Math.pow(Math.log(2), 2));
    }
    
    
    
    
    
    @Override
    public int run(String[] args) throws Exception 
    {
        
		Job job = new Job(getConf());
		Configuration conf = job.getConfiguration();
		job.setJobName("Testing Bloom Filter information");
        
        DistributedCache.addCacheFile(new URI("/tmp/bloom"),conf);
        
        job.setJarByClass(BloomFilterDriver.class);
        
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(BloomMapper.class);
		
		job.setNumReduceTasks(0);
	
		boolean success = job.waitForCompletion(true);
		return success ? 0 : 1;
    }
	
}



