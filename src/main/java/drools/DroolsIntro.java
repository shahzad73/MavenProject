/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drools;

/**
 *
 * @author sehzad
 */
public class DroolsIntro {
    private String topic;

    public DroolsIntro(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public String introduceYourself() {
        return "Drools 6.2.0.Final";
    }    
}

