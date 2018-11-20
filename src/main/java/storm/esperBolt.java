package storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import stormEsper.MessageBean;



public class esperBolt extends BaseRichBolt {

        private static final long serialVersionUID = 23L;

	private static final Log log = LogFactory.getLog(esperBolt.class);
	
	private OutputCollector collector;
	private EPServiceProvider epService;
        
        
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.setUpEsper();
	}


        
	private void setUpEsper() {
		Configuration configuration = new Configuration();
                
                /*Map<String, Object> eventTypes = new HashMap<>();
                eventTypes.put("id", Integer.class);
                eventTypes.put("word", String.class);                
                configuration.addEventType("words", eventTypes);*/
                
                configuration.addEventType("words", WordsEvent.class.getName());        
                
		epService = EPServiceProviderManager.getDefaultProvider(configuration);
		epService.initialize();

		
                
                EPStatement statement = epService.getEPAdministrator().
				createEPL("select count(*) as total from words.win:time(6 second) output snapshot every 2 sec");
                statement.addListener(new UpdateListener() {
			@Override
			public void update(EventBean[] arg0, EventBean[] arg1) {
				if (arg0 != null) {
					for (EventBean e : arg0) {
						log.info("------------- Total Words in 6 seconds: " + e.get("total") + "--------------");
					}
				}
			}
		});
                
                
		EPStatement statement2 = epService.getEPAdministrator().
				createEPL("select count(*) as total from words.win:time(6 second) where words.word = 'google' output snapshot every 4 sec");
                statement2.addListener(new UpdateListener() {
			@Override
			public void update(EventBean[] arg0, EventBean[] arg1) {
				if (arg0 != null) {
					for (EventBean e : arg0) {
						log.info("------------- Total Googles in 6 seconds: " + e.get("total") + "--------------");
					}
				}
			}
		});                
                
                
        }        

        
        
        @Override
	public void execute(Tuple input)
	{
            List<Object> values = input.getValues();
            epService.getEPRuntime().sendEvent( new WordsEvent( Integer.parseInt((String)values.get(0)), (String)values.get(1)));
            collector.ack(input);            
	}

        @Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
            
	}

}


        




//  String epl = "select avg(price) from OrderEvent.win:time(30 sec)";