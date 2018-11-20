package storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;


import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy.Units;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;


public class RandomTopologyBuikder
{

	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, InterruptedException
	{
		// create an instance of TopologyBuilder class
		TopologyBuilder builder = new TopologyBuilder();

		// set the spout class
		builder.setSpout("Random_Storm_Spout", new RandomStormSpout(), 2);

		// set the bolt class
		builder.setBolt("Random_Storm_Bolt", new RandomStormBolt(), 1).shuffleGrouping ("Random_Storm_Spout");

                builder.setBolt("Esper_Bolt", new esperBolt(), 1).shuffleGrouping ("Random_Storm_Spout");
                
		//configureHDFSBolt(builder, "Random_Storm_Spout");

		//builder.setBolt("HBaseBolt", new HbaseBolt(), 4).shuffleGrouping ("LearningStormSpout");

		Config conf = new Config();
		conf.setDebug(false);

		try {
			StormSubmitter.submitTopology("deef", conf, builder.createTopology());
		}
		catch (AuthorizationException ex)
		{

		}

		/*
		// create an instance of LocalCluster class for
		// executing topology in local mode.
		LocalCluster cluster = new LocalCluster();

		// LearningStormTopolgy is the name of submitted topology
		cluster.submitTopology("LearningStormToplogy", conf, builder.createTopology());
		Thread.sleep((long) 5000 * 1000);   //Seconds topology will run.

		// kill the LearningStormTopology
		cluster.killTopology("LearningStormToplogy");

		// shutdown the storm test cluster
		cluster.shutdown();*/

	}



    private static void configureHDFSBolt(TopologyBuilder builder, String spoutID)
    {
        /*
                HDFS bolt for the storm
                http://storm.apache.org/releases/0.10.0/storm-hdfs.html
            
                review following interfaces 
                Record Formats
                File Naming
                others ....
        */
        
    	// use "," for field delimiter
    	RecordFormat format = new DelimitedRecordFormat().withFieldDelimiter(",");

        // sync the filesystem after every 1k tuples
    	SyncPolicy syncPolicy = new CountSyncPolicy(1000);

        // rotate files when they reach 5MB
    	FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(30.0f, Units.KB);

        FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath("/user/guest/storm/");

    	HdfsBolt bolt = new HdfsBolt()
    	        .withFsUrl("hdfs://sandbox.hortonworks.com:8020")
    	        .withFileNameFormat(fileNameFormat)
    	        .withRecordFormat(format)
    	        .withRotationPolicy(rotationPolicy)
    	        .withSyncPolicy(syncPolicy);

    	builder.setBolt("HDFS_BOLT", bolt, 2).shuffleGrouping(spoutID);
    }


}
