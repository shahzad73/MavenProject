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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class rankIntermediateBolt extends BaseRichBolt{

	private static final long serialVersionUID = 7533727625528533215L;
	private OutputCollector collector;
	
	private int headSlot;
	private int tailSlot;	
	  
	private final int windowLengthInSeconds;
	private final int emitFrequencyInSeconds;
	private final int numberOFSlotsToCreate;
	private final boolean isFinalRankedBolt;    //this variable initialized from storm driver program    it indicated weather current bolt is the final stage bolt.     this bolt work in 2 levels.    first level track subset of N ranked items and last level with only one instance of bolt will final N ranks      
	private final int NumberOfRankedItems; 
	
	private final HashMap<Integer, HashMap<String, Long>> destinationsTracking = new HashMap<Integer, HashMap<String, Long>>();
	private final HashMap<String, Long> topItems = new HashMap<String, Long>();

		
	public rankIntermediateBolt(int windowLengthInSeconds, int emitFrequencyInSeconds, boolean isFinalRanked, int NumberOfRankedItems) {
		 this.windowLengthInSeconds = windowLengthInSeconds;
		 this.emitFrequencyInSeconds = emitFrequencyInSeconds;
		 this.isFinalRankedBolt = isFinalRanked;
		 numberOFSlotsToCreate = windowLengthInSeconds/emitFrequencyInSeconds;
		 this.NumberOfRankedItems = NumberOfRankedItems;
	}	
	
	
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		
		for(int a=0; a<numberOFSlotsToCreate; a++)
		{
			HashMap<String, Long> innerMap = new HashMap<String, Long>();
			destinationsTracking.put(a, innerMap);
		}

		 this.headSlot = 0;
		 this.tailSlot = slotAfter(headSlot);		
	}
	
	
	@Override
	public void execute(Tuple tuple) {
		
		if (isTickTuple(tuple)) 
		{		
			//if a tick event is received then find out the top
			//N ranked items from all N slots.   note that only one count is maintained for a specific 
			//destination in all slots. so ranks is based on all destinations in all slots
			//also if 2,3 or any number of destinations have same count then it will be treated as same ranked 
			//destination. so if 10 ranked destinations are being tracked  then more than 10 destination will
			//be produced in different ranks
			//following algo will calculate this rank
			topItems.clear();
			int TotalNumberOfRanksInRankedMap = 0;
			
			for(int a=0; a<destinationsTracking.size(); a++)
			{
				HashMap<String, Long> currentHashSet = destinationsTracking.get(a);
				
				for (Entry<String, Long> currentEntry : currentHashSet.entrySet())
				{
					if(!topItems.containsKey(currentEntry.getKey()))     //if item already ranked then no need for ranking
					{
						long lowestrankvalue = findLowestRankedValueFromRankedMap();
						if(currentEntry.getValue() > lowestrankvalue)
						{
							if(TotalNumberOfRanksInRankedMap < NumberOfRankedItems)
								TotalNumberOfRanksInRankedMap++;    //one more ranked item is going to be added
							else
								removeLowestRankedItems(lowestrankvalue);	 
							
							addSelectedRankInRankedMap(currentEntry.getValue());
						}
					}
				}			
			}
			
			
			if(isFinalRankedBolt == true)
			{
				System.out.println("====================================================");
				System.out.println("=============Current Ranked Items===================");
				System.out.println("====================================================");
			}
			
			for (Entry<String, Long> entry : topItems.entrySet())
			{
				if(isFinalRankedBolt == false)
					collector.emit(new Values(entry.getKey(), entry.getValue()));
				else
					System.out.println(entry.getKey() + " " + entry.getValue());
			}
			if(isFinalRankedBolt == true)
				System.out.println("====================================================");

			advanceHead();
			HashMap<String, Long> currentSlot = destinationsTracking.get(headSlot);
			currentSlot.clear();
		}
		else
		{
			String destination = (String)tuple.getValue(0);
			Long count = (Long) tuple.getValue(1);
			
			//delete tuple from all slots as this is the current tuple now
			for(int a=0; a<destinationsTracking.size(); a++)
				destinationsTracking.get(a).remove(destination);			
			
			destinationsTracking.get(headSlot).put(destination, count);
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
	

	
	
	  private void advanceHead() 
	  {
		    headSlot = tailSlot;
		    tailSlot = slotAfter(tailSlot);
	  }

	  
	  
	  private int slotAfter(int slot) 
	  {
		  return (slot + 1) % numberOFSlotsToCreate;
	  }	  
	
	
		@Override
		public Map<String, Object> getComponentConfiguration() 
		{
			  Map<String, Object> conf = new HashMap<String, Object>();
			  conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, emitFrequencyInSeconds);
			  return conf;
		}	

		
		

		
		private long findLowestRankedValueFromRankedMap()
		{
			long LowestValue = -1;
			
			for (Entry<String, Long> tempEntry : topItems.entrySet())
			{
				if(LowestValue == -1)
					LowestValue = tempEntry.getValue();
				else
				{
					if(LowestValue > tempEntry.getValue() )
						LowestValue = tempEntry.getValue();
				}
			}
			
			return LowestValue;
		}
		
			
		
		private void removeLowestRankedItems(long lowestrankvalue)
		{
			HashSet<String> tempHset = new HashSet<String>();
			for (Entry<String, Long> tempEntry : topItems.entrySet())
			{
				if(tempEntry.getValue() == lowestrankvalue)
					tempHset.add(tempEntry.getKey());
			}
			Iterator<String> tempIterator = tempHset.iterator();
		    while(tempIterator.hasNext())
		    	topItems.remove( tempIterator.next() );
		}
		
		
		private void addSelectedRankInRankedMap(long rankitem)
		{
			for(int a=0; a<destinationsTracking.size(); a++)
				           
			{
				HashMap<String, Long> currentHashSet = destinationsTracking.get(a);
				
				for (Entry<String, Long> currentEntry : currentHashSet.entrySet())
				{
					if(currentEntry.getValue() == rankitem)
						topItems.put(currentEntry.getKey(), currentEntry.getValue());
				}
			}
		}			
	  
}
