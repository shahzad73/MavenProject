/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hadoop.hbase;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;

public class hbasescan {
    
    public static void main(String[] args) throws Exception
    {
        
        Configuration config = HBaseConfiguration.create();

        try (HTable table = new HTable(config, "tab1")) 
        {

            Scan scan = new Scan();
            // Add a column with value "Hello", in "cf1:greet",
            // to the Scan.
            
            Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("*9*"));
            scan.setFilter(filter);

            
            scan.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("email"));
            scan.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("phone"));

            scan.setStartRow(Bytes.toBytes("usr-1"));
            scan.setStopRow(Bytes.toBytes("usr-96"));
            
            
            ResultScanner scanner = table.getScanner(scan);
            
            
            for (Result res : scanner) {
                byte[] colA = res.getValue(Bytes.toBytes("cf1"),Bytes.toBytes("email"));           
                byte[] colB = res.getValue(Bytes.toBytes("cf1"),Bytes.toBytes("phone"));           
                System.out.println(Bytes.toString(colA) + " " + Bytes.toString(colB));
            }
            scanner.close();
            table.close();            
            
        }    
    
    }
}
