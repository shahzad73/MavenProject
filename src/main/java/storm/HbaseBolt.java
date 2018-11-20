package storm;

import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

//	use this command to connect to hbase with shell
//	hbase is a user with full access to do all.  when you you ust hbase shell
//	sudo -u hbase hbase shell
//	create 'tab1', 'cf1'



public class HbaseBolt implements IRichBolt {

    private static final long serialVersionUID = 2946079346382650318L;

    private static final String EVENTS_TABLE_NAME =  "tab1";                   //table
    private static final byte[] CF_EVENTS_TABLE = Bytes.toBytes("cf1");         //CF

    //Columns
    private static final byte[] COL_ID = Bytes.toBytes("id");
    private static final byte[] COL_NAME = Bytes.toBytes("name");


    private OutputCollector collector;
    private HConnection connection;
    private HTableInterface eventsTable;



    public HbaseBolt()
    {

    }


    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
    {
        this.collector = collector;
        try
        {
            this.connection = HConnectionManager.createConnection(constructConfiguration());
            this.eventsTable = connection.getTable(EVENTS_TABLE_NAME);
        }
        catch (Exception e)
        {
            String errMsg = "Error retrievinging connection and access to HBase Tables";
            throw new RuntimeException(errMsg, e);
        }
    }



    @Override
    public void execute(Tuple tuple)
    {
        String sId = tuple.getStringByField("id");
        String sname = tuple.getStringByField("site");

        try
        {
            Put put = constructRow(EVENTS_TABLE_NAME, sId, sname);
            this.eventsTable.put(put);
        }
        catch (Exception e)
        {

        }
    }


    private Put constructRow(String columnFamily, String sid, String sname)
    {
        String rowKey = sid;
        Put put = new Put(Bytes.toBytes(rowKey));

        put.add(CF_EVENTS_TABLE, COL_ID, Bytes.toBytes(sid));
        put.add(CF_EVENTS_TABLE, COL_NAME, Bytes.toBytes(sname));

        return put;
    }



    @Override
    public void cleanup()
    {

    }




    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }



    @Override
    public Map<String, Object> getComponentConfiguration()
    {
            return null;
    }


    public static  Configuration constructConfiguration()
    {
        Configuration config = HBaseConfiguration.create();
        return config;
    }


}
