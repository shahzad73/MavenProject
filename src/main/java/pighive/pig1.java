package hadoop.pighive;

import java.io.IOException;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;


public class pig1 extends EvalFunc<String> 
{
	
	
    public String exec(Tuple input) throws IOException 
    {
        if (input.size() == 0)
            return null;
        try {
            String str = (String) input.get(0);
            char ch = str.toUpperCase().charAt(0);
            String str1 = String.valueOf(ch);
            return str1;

        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    
}
