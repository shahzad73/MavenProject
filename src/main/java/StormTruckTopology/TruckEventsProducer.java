package StormTruckTopology;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class TruckEventsProducer
{
    public static void main(String[] args) throws IOException
    {
        // long events = Long.parseLong(args[0]);
        Random rnd = new Random();

        Properties props = new Properties();
        props.put("metadata.broker.list", "127.0.0.1:9092");
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");

        String TOPIC = "test";
        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer = new Producer<String, String>(config);

        String[] events = {"Normal", "Normal", "Normal", "Normal", "Normal", "Normal", "Lane Departure",
                           "Overspeed", "Normal", "Normal", "Normal", "Normal", "Lane Departure","Normal",
                           "Normal", "Normal", "Normal",  "Unsafe tail distance", "Normal", "Normal",
                           "Unsafe following distance", "Normal", "Normal", "Normal","Normal","Normal","Normal",
                           "Normal","Normal","Normal","Normal","Normal","Normal","Normal", "Normal", "Overspeed"
                            ,"Normal", "Normal","Normal","Normal","Normal","Normal","Normal" };

        Random random = new Random();
        String finalEvent = "";

        String[] truckIds = {"1", "2", "3","4"};
        String[] routeName = {"route17", "route17k", "route208", "route27"};
        String[] driverIds = {"11", "12", "13", "14"};

        int evtCnt = events.length;

        for (int i = 0; i < 1000; i++)
        {
  	    finalEvent = new Timestamp(new Date().getTime()) + "|" + truckIds[0] + "|" + driverIds[0] + "|" + events[random.nextInt(evtCnt)] + "|" + getLatLong();
            try {
  	        KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, finalEvent);
  	        System.out.println(finalEvent);
  	        producer.send(data);
  	        Thread.sleep(1000);
  	    } catch (Exception e) {
                e.printStackTrace();
  	    }
        }

        producer.close();
    }

    private static String getLatLong()
    {
        return "11111111" + "|" + "222222222";
    }

}
