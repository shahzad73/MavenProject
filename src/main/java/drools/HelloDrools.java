package drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;


public class HelloDrools {

    public static void main(final String[] args) {
        KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
        execute( kc );
    }

    public static void execute( KieContainer kc ) {
        KieSession ksession = kc.newKieSession("HelloDroolsSession");

        String[] names = new String[]{"Fred", "Joe", "Bob", "Tom"};
        String[] colors = new String[]{"red", "blue", "plaid", "orange"};
        int[] positions = new int[]{1, 2, 3, 4};

        for ( int n = 0; n < names.length; n++ ) 
            for ( int c = 0; c < colors.length; c++ ) 
                for ( int p = 0; p < positions.length; p++ ) 
                    ksession.insert( new Golfer( names[n], colors[c], positions[p] ) );

        ksession.fireAllRules();


        
        ksession.setGlobal("topicLevel", "First");
        ksession.insert(new DroolsIntro("Drools"));
        ksession.insert(new DroolsIntro("spring"));
        ksession.fireAllRules();
        
        ksession.setGlobal("topicLevel", "Beginner");
        ksession.insert(new DroolsIntro("spr 35345345"));
        ksession.insert(new DroolsIntro("spring 232332"));
        ksession.insert(new DroolsIntro("Drools"));
        ksession.fireAllRules();
        
        
        
        ksession.dispose();
    }

    
    
    
    
    
    
    
    
    public static class Golfer {
        private String name;
        private String color;
        private int    position;

        public Golfer() {

        }

        public Golfer(String name, String color, int position) {
            super();
            this.name = name;
            this.color = color;
            this.position = position;
        }

        public String getColor() {
            return this.color;
        }

        public String getName() {
            return this.name;
        }

        public int getPosition() {
            return this.position;
        }
    }
    
    
}

