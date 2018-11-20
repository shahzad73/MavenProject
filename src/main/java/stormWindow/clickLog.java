package stormWindow;

import java.io.Serializable;

public class clickLog implements Serializable{

	private static final long serialVersionUID = -5255023988769785810L;
	int user_id = 1;
	String time = "2";
	int action = 1;            // 1=click     2=search        3=filter
	String destination = "4";  
	String hotel = "5";
	
	
	public clickLog()
	{}
	
	
	public clickLog(int uid, String time, int action, String destination, String hotel)
	{
		this.user_id = uid;
		this.time = time;
		this.action = action;            
		this.destination = destination;  
		this.hotel = hotel;
				
	}
	
	public int getUser_Id()
	{
		return user_id;
	}
	
	
	public void setUser_Id(int i)
	{
		user_id = i;
	}
	

	public int getAction()
	{
		return action;
	}
	
	
	public void setAction(int a)
	{
		action = a;
	}	
	
	
	public String getTime()
	{
		return time;
	}
	
	
	public void setTime(String t)
	{
		time = t;
	}
	
	
	
	
	public String getDestination()
	{
		return destination;
	}
	
	
	public void setDestination(String d)
	{
		destination = d;
	}
	
	
	
	public String getHotel()
	{
		return hotel;	
	}
	
	public void setHotel(String h)
	{
		hotel = h;
	}	

	
    @Override
    public boolean equals(Object obj) {
        clickLog other= (clickLog)obj;
        if(other.user_id == user_id && other.destination == destination && other.action == action)
        	return true;
        else
        	return false;
    }

    @Override
    public int hashCode() {
        return (int)(user_id%Integer.MAX_VALUE);
    }


    @Override
    public String toString() {
        return "UserID:"+user_id+ " destination: "+destination+ " action:"+action;
    }
	
	
}
