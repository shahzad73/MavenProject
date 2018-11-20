/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.testing;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LoadingProperties {

    static Properties topologyConfig;

    public static void main(String[] args){

          topologyConfig = new Properties();

          try {
                topologyConfig.load(ClassLoader.getSystemResourceAsStream("file.properties"));   //this file is in Other Sources folder       that is src/main/resources.       remmeber this folder contents are on the root of the jar created 
          } catch (FileNotFoundException e) {
      			     System.out.println("Encountered FileNotFoundException while reading configuration properties: " + e.getMessage());
      		} catch (IOException e) {
      			     System.out.println("Encountered IOException while reading configuration properties: " + e.getMessage());
      		}

          System.out.println( topologyConfig.getProperty("info.server") );

          System.out.println("locaded");
          
          
          
          
          
    }

}
