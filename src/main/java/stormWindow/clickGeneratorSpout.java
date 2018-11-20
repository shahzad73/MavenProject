package stormWindow;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import stormWindow.clickLog;

import java.util.Map;
import java.util.Random;

public class clickGeneratorSpout  extends BaseRichSpout 
{

	  private static final long serialVersionUID = -2255023988769785899L;
	  SpoutOutputCollector _collector;
	  Random _rand;
	
	  
	  public clickGeneratorSpout()
	  {
		  
	  }
	  
	  
	  @Override
	  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) 
	  {
		  System.out.println("random generator got it");
		    _collector = collector;
		    _rand = new Random();
	  }

	  
	  @Override
	  public void nextTuple() 
	  {
		    Utils.sleep(100);
		    String destination = "Destination-" + String.valueOf(_rand.nextInt(30));	    
		    
		    clickLog log = new clickLog();
		    log.setUser_Id(_rand.nextInt(50));
		    log.setAction(_rand.nextInt(2) + 1);    //randomly generate     1=click     2=search     3=filter
		    log.setDestination( destination );
		    log.setHotel(String.valueOf(_rand.nextInt(10)));
		    log.setTime(String.valueOf(_rand.nextInt(10)));
		    
		    _collector.emit(new Values(log.getDestination(), log));
	  }

	  
	  @Override
	  public void ack(Object id) {
	  }

	  
	  @Override
	  public void fail(Object id) {
	  }

	  
	  @Override
	  public void declareOutputFields(OutputFieldsDeclarer declarer) {
	    declarer.declare(new Fields("destination", "clickLog"));
	  }	  
	
	
}
