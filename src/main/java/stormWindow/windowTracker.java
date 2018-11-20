package stormWindow;

import java.io.Serializable;
import java.util.Map;

public class windowTracker<String> implements Serializable {

	  private static final long serialVersionUID = 4245065987762385981L;

	  private slotCounter<String> objCounter;
	  private int headSlot;
	  private int tailSlot;
	  private int windowLengthInSlots;
	  	  
	  
	  public windowTracker(int windowLengthInSlots) 
	  {
		    this.windowLengthInSlots = windowLengthInSlots;
		    this.objCounter = new slotCounter<String>(this.windowLengthInSlots);

		    this.headSlot = 0;
		    this.tailSlot = slotAfter(headSlot);
	  }
	  
	  
	  
	  public void incrementCount(String obj) 
	  {
		    objCounter.incrementCount(obj, headSlot);
	  }	  
	  
	  
	  
	  public Map<String, Long> getCountsThenAdvanceWindow() 
	  {
		    Map<String, Long> counts = objCounter.getCounts();
		    objCounter.wipeZeros();
		    objCounter.wipeSlot(tailSlot);
		    advanceHead();
		    return counts;
	  }	  

	  
	  
	  private void advanceHead() 
	  {
		    headSlot = tailSlot;
		    tailSlot = slotAfter(tailSlot);
	  }

	  
	  
	  private int slotAfter(int slot) 
	  {
		    return (slot + 1) % windowLengthInSlots;
	  }	  
	  
	  
}
