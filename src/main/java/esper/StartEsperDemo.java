package esper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import esper.RandomTemperatureEventGenerator;

/**
 * Entry point for the Demo. Run this from your IDE, or from the command line using 'mvn exec:java'.
 */
public class StartEsperDemo {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(StartEsperDemo.class);

    
    /**
     * Main method - start the Demo!
     */
    public static void main(String[] args) throws Exception {

        LOG.debug("Starting...");

        long noOfTemperatureEvents = 1000;

        if (args.length != 1) {
            LOG.debug("No override of number of events detected - defaulting to " + noOfTemperatureEvents + " events.");
        } else {
            noOfTemperatureEvents = Long.valueOf(args[0]);
        }

        // Load spring config
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "esper-context.xml" });
        BeanFactory factory = (BeanFactory) appContext;

        // Start Demo
        RandomTemperatureEventGenerator generator = (RandomTemperatureEventGenerator) factory.getBean("eventGenerator");
        generator.startSendingTemperatureReadings(noOfTemperatureEvents);

    }

}
