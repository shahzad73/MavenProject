package stormWindow;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import stormWindow.clickGeneratorSpout;
import stormWindow.rankIntermediateBolt;


public class timeSlidingWindowTopology {
	
	  
	  private static final int NumberOfTopRankedItemsToTrack = 10;    //this is the top number of items to be tracked 
	  
	  private static TopologyBuilder builder;
	  private static String topologyName;
	  private static Config topologyConfig;
	  
	  
	  public timeSlidingWindowTopology() throws InterruptedException 
	  {

	  }
	  
	  
	  private static Config createTopologyConfiguration() 
	  {
		    Config conf = new Config();
		    conf.setDebug(false);
		    return conf;
	  }
	
	  
	  public static void main(String[] args) throws Exception 
	  {
		  builder = new TopologyBuilder();
		  topologyConfig = createTopologyConfiguration();
		  topologyName = "Track_Top_N_Ranked_Items";
		  
		  //clickGeneratorSpout will generate random events that may include click, search or filter.   
		  //only search items will be ranked. other events will be ignored
		  builder.setSpout("Random_Click_Genetator_Spout", new clickGeneratorSpout(), 5);      
		  
		  
		  //This bolt will receive click, search and filter events.   only search events will be tracked and ranked
		  //Each bolt will create an instance of windowTrack which in turn create an instance of slotCounter.
		  //both of these objects will track search events in 10 slots of 60 seconds.
		  //one of the slots will be current slot where each destination search event will be aggregated
		  //this event also initialize itself with a Tick event that will be generated after every 60 seconds.
		  //upon receiving tick event, current slot's aggregated results will be forwared to next blot  next slot will be selected.   When a slot is selected it will initialize 
		  //all destination with in that slot to 0.        slots are selected in round robin  starting with 1 2 3 ... 9 and then again to 1
		  //Each slot act as a bag of destination where search event count is aggregated.
		  builder.setBolt("Rolling_Time_Window_Tracking_Bolt", new clickCounterBolt(600, 60), 4).fieldsGrouping("Random_Click_Genetator_Spout", new Fields("destination"));		  																														
		  
		  
		  //this bolt will receive aggregated results from previous bolt and manage them in slots
		  //each slot is again a bag of destinations with counts. the only difference is that this bolt delete all previous
		  //counts from all slot if fresh count of a specific destination are received
		  //these bolts act like combiners in Hadoop terms tracking N ranks of only subset of destinations
		  //again this bolt configure itself with tick event of 30 seconds. if a tick event is received
		  //it will RANK top N items and forward to next stage. 
		  //notice 10 minutes are tracked in 20 slots (30 seconds each)
		  builder.setBolt("Top_N_Intermediate_Results_Bolt", new rankIntermediateBolt(600,30, false, NumberOfTopRankedItemsToTrack), 4).fieldsGrouping("Rolling_Time_Window_Tracking_Bolt", new Fields("destination"));
		  
		  
		  //this bolt is the same class as previous but with only one instance
		  //it will receive top N counts from previous stage and produce final N rank items every 20 seconds. 
		  //it also configure itself with 20 seconds tick tuple
		  //when 20 minutes tick received, it will rank final top N destinations
		  builder.setBolt("Final_Top_N_Results_Bolt", new rankIntermediateBolt(600,20, true, NumberOfTopRankedItemsToTrack)).globalGrouping("Top_N_Intermediate_Results_Bolt");
		  
		  
		  int MILLIS_IN_SEC = 1000;
		  LocalCluster cluster = new LocalCluster();
		  cluster.submitTopology(topologyName, topologyConfig, builder.createTopology());
		  System.out.println("Cluster initialized ....");
		  Thread.sleep((long) 5000 * MILLIS_IN_SEC);   //Seconds topology will run.
		  cluster.killTopology(topologyName);
		  cluster.shutdown();  
	  }	  
	  
	  
}
