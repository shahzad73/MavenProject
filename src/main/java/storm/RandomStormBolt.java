package storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;



public class RandomStormBolt extends BaseBasicBolt
{

	private static final long serialVersionUID = 1L;


	public void execute(Tuple input, BasicOutputCollector collector)
	{
		// fetched the field "site" from input tuple.
		String site = input.getStringByField("site");
		String id = input.getStringByField("id");
		System.out.println("id:" + id + " Site:" + site);
        }


	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

}
