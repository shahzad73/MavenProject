/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hadoop.hbase;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.regionserver.InternalScanner;

/*
    What is Coprocessor? Simply stated, Coprocessor is a framework that provides an easy way to run your 
    custom code on Region Server.

    Some uses of CoProcessors
        Security: Before performing any operation (like ‘Get’, ‘Put’) you can check for permission in the ‘preXXX’ methods.
        Referential Integrity: Unlike traditional RDBMS, HBase doesn’t have the concept of referential integrity (foreign key). Suppose for example you have a requirement that whenever you insert a record in ‘users’ table, a corresponding entry should also be created in ‘user_daily_attendance’ table. One way you could solve this is by using two ‘Put’ one for each table, this way you are throwing the responsibility (of the referential integrity) to the user. A better way is to use Coprocessor and overriding ‘postPut’ method in which you write the code to insert the record in ‘user_daily_attendance’ table. This way client code is more lean and clean.
        Secondary Index: Coprocessor can be used to maintain secondary indexes. For more information please see SecondaryIndexing.



    To run this coprocesor you need followigs
    
    1. Make Jar
    2. deploy on the server in a directory
    3. Add following to hbase-site.xml
        <property>
        <name>hbase.coprocessor.region.classes</name>
        <value>hadoop.hbase.CoProcessorRegionObserverExample</value>
        </property>        
    4. export HBASE_CLASSPATH=$HBASE_CLASSPATH:/root/shahzad/<filename.jar>    you can add it in hbase-env.sh   file
    5. $ stop-hbase.sh     restart hbase
    6. $ start-hbase.sh
*/

public class CoProcessorRegionObserverExample extends BaseRegionObserver
{
   
         
    @Override
    public void preGetOp(final ObserverContext e, final Get get, final List results) throws IOException {
        //you can add any sort of code that will execute before any GET operation like you can implement security 
        //procedures or manipulate the results like below you added a extra field in result set if the 
        //ROW key is   use-99
        
        
        //Add extra field to result set
        //this code will add a extra field in the result of get if the ROW KEY is   usr-99.   otherwise normal result is returned
        if(Bytes.equals(get.getRow(), Bytes.toBytes("row15883853")))
        {
            KeyValue kv = new KeyValue(get.getRow(),Bytes.toBytes("cf1"),Bytes.toBytes("nameadded"),Bytes.toBytes("Allah Akbar"));
            results.add(kv);
        }

        if(Bytes.equals(get.getRow(), Bytes.toBytes("mykey"))){
            KeyValue kv = new KeyValue(get.getRow(),Bytes.toBytes("name"),Bytes.toBytes("shahzad"),Bytes.toBytes("aslam"));
            results.add(kv);
	}        
        
    }
    
    
    
    //this will run after a scan operation has been performed 
    @Override
    public boolean postScannerNext(final ObserverContext e, final InternalScanner s, final List results, final int limit, final boolean hasMore) throws IOException {
        
        //in case of scan operation  record with ROW KEY     usr-98     will be removed from the results
        //but in case of get operation the record can be retrieved 
                
        byte[] ROWKEY1 = Bytes.toBytes("usr-98");
        byte[] ROWKEY2 = Bytes.toBytes("row15883853");            
        
        Result result = null;
        Iterator iterator = results.iterator();
        while (iterator.hasNext()) {
            result = (Result)iterator.next();
            if (Bytes.equals(result.getRow(), ROWKEY1)) {
                iterator.remove();
                break;
            }
            
            if (Bytes.equals(result.getRow(), ROWKEY2)) {
                KeyValue kv = new KeyValue(result.getRow(),Bytes.toBytes("cf1"),Bytes.toBytes("nameadded"),Bytes.toBytes("Allah Akbar"));
                results.add(kv);                
            }
        }
        return hasMore;
    }

    
    
    // Similarly override prePut(), preDelete(), etc. based on the need
    
}




        /*if (Bytes.equals(get.getRow(),ADMIN)) {
            Cell c = CellUtil.createCell(get.getRow(),COLUMN _FAMILY, COLUMN, System.currentTimeMillis(), (byte)4, VALUE);
            results.add(c);
            e.bypass();
        }
         
        List kvs = new ArrayList(results.size());
        for (Cell c : results) {
            kvs.add(KeyValueUtil.ensureKeyValue(c));
        }
        preGet(e, get, kvs);
        results.clear();
        results.addAll(kvs);
        */
        
        /*byte[] region = c.getEnvironment().getRegion().getRegionInfo().getRegionName();
        // TODO Code for checking the permissions over
        // table or region...
        if (true) {
            throw new AccessDeniedException("User is not allowed for access");
        }*/
        