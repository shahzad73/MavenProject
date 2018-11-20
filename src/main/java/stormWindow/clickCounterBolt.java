package stormWindow;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;




public class clickCounterBolt extends BaseRichBolt {
	
	private static final long serialVersionUID = 2533727424628538515L;
	
	
	//each clickCounterBolt bolt will have its own instance of windowTracker.  
	//each windowTracker will have its own instance of slotCounter that implements the slot based counting. 
	//each slot can be considered as bag of destination with count. 
	//when a event is received then destination count in SELECTED slot will be incremented and if destination is not in current slot, destination is added with count 1 
	//with each 60 second tick. next slot is selected, which first is initialized to 0. that is all destination counts present in that slot is initialized to 0.
	//the next slot is the oldest slot and initializing the counts to 0 of each destination in current slot basically wipe out oldest counts 
	private final windowTracker<String> counter;       
	 
	
	private final int windowLengthInSeconds;
	private final int emitFrequencyInSeconds;
	private OutputCollector collector;
	  
	
	public clickCounterBolt(int windowLengthInSeconds, int emitFrequencyInSeconds) {
		 this.windowLengthInSeconds = windowLengthInSeconds;
		 this.emitFrequencyInSeconds = emitFrequencyInSeconds;
		 counter = new windowTracker<String>(windowLengthInSeconds/emitFrequencyInSeconds);    //create windowTracker with selected number of slots in this case   600/60    means 10 minutes divided into 10 slots 
	}

	
	  
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
	
	  
	@Override
	public void execute(Tuple tuple) {
		
		if (isTickTuple(tuple))     //if tick even is received 
		{
			//if a tick event is received  get counts of all destinations from all slots
			//advance slot to next slot and wipe out counts of destinations in newly selected slot
			//notice slots are selected  as 1 2 3 4 ... 9     next slot after 9 is again 1      
			//if 3 is the current current slot then 4 is the oldest slot in the system. 
			//so when tick is received it will first set the next slot as 4 and then wipe out all destinations 
			Map<String, Long> counts = counter.getCountsThenAdvanceWindow();		
			
		    for (Entry<String, Long> entry : counts.entrySet()) {
		        String obj = entry.getKey();
		        Long count = entry.getValue();
		        collector.emit(new Values(obj, count));
		    }
		}
	    else     //otherwise this the destination event from previous bolt 
	    {
	        String obj = (String)tuple.getValue(0);
	        clickLog log = (clickLog)tuple.getValue(1);
	        
	        if(log.getAction() == 2)				// 1=click     2=search        3=filter
	        	counter.incrementCount(obj);
	        
	        collector.ack(tuple);
	    }
	}
	
	
	
	public static boolean isTickTuple(Tuple tuple) 
	{
		return tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) && tuple.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID);
	}	
	  

	
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	   declarer.declare(new Fields("destination", "count"));
	}	  
	
	
	
	@Override
	public Map<String, Object> getComponentConfiguration() 
	{
	  //configure this bolt with 60 second tick event
	  Map<String, Object> conf = new HashMap<String, Object>();
	  conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, emitFrequencyInSeconds);
	  return conf;
	}	
	  
	  
}
