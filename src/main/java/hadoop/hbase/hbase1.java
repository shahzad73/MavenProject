package hadoop.hbase;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;


public class hbase1
{
    //static String tableName = "users";
    static String tableName = "tab1";
    static String familyName = "cf1";
    public static void main(String[] args) throws Exception
    {
        // open connection just once and re-user it
        System.out.println("going to insert");
        
        Configuration config = HBaseConfiguration.create();

        try (HTable htable = new HTable(config, tableName)) 
        {
            int total = 100;
            long t1 = System.currentTimeMillis();
            // loop to insert users value, which we are creating
            // later we will do better user simulation

            for (int i=0; i< total ; i++)
            {
                int userid = i;
                String email = "user-" + i + "@foo.com";
                String phone = "555-1234";
                byte [] key = Bytes.toBytes("usr-" + userid);
                Put put = new Put (key);
                put.add(Bytes.toBytes(familyName), Bytes.toBytes("email"),
                Bytes.toBytes(email)); // <-- email goes here
                put.add(Bytes.toBytes(familyName), Bytes.toBytes("phone"),
                Bytes.toBytes(phone)); // <-- phone goes here
                htable.put(put);
            }

            long t2 = System.currentTimeMillis();
            System.out.println ("inserted " + total + " users in " +(t2-t1) + " ms");
        }
    }
}

