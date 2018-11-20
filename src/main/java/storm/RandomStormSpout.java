package storm;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Values;
import backtype.storm.tuple.Fields;


public class RandomStormSpout extends BaseRichSpout{


	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector spoutOutputCollector;
	private static final Map<Integer, String> map = new HashMap<Integer, String>();

	static {
		map.put(0, "google");
		map.put(1, "facebook");
		map.put(2, "twitter");
		map.put(3, "youtube");
		map.put(4, "AllahAkbar");
	}



	public void open(Map conf, TopologyContext context, SpoutOutputCollector spoutOutputCollector)
	{
		// Open the spout
		this.spoutOutputCollector = spoutOutputCollector;
	}



	public void nextTuple()
	{
		try {
			Thread.sleep((long) 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final Random rand = new Random();
		int randomNumber = rand.nextInt(5);
		spoutOutputCollector.emit(new Values(String.valueOf(randomNumber), map.get(randomNumber)));
	}



	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
		// emit the tuple with field "site"
		declarer.declare(new Fields("id", "site"));
	}


}
