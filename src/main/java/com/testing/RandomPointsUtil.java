/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.mahout.clustering.UncommonDistributions;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

public class RandomPointsUtil 
{
	public static void generateSamples(List<Vector> vectors, int num, double mx, double my, double sd) {
            for (int i = 0; i < num; i++) {
		vectors.add(new DenseVector(new double[] 
                    {
                        UncommonDistributions.rNorm(mx, sd),
			UncommonDistributions.rNorm(my, sd) 
                    }));
            }
	}	
	
  public static List<Vector> chooseRandomPoints(Iterable<Vector> vectors, int k) 
  {

      List<Vector> chosenPoints = new ArrayList<Vector>(k);
        Random random = RandomUtils.getRandom();
        // resevoir sampling
        int totalSize = 0;
        for (Vector value : vectors) {
            totalSize++;
          int currentSize = chosenPoints.size();
          if (currentSize < k) {
            chosenPoints.add(value);
          } else if (random.nextInt(totalSize) < currentSize) { // with chance k/totalSize, pick new element
            int indexToRemove = random.nextInt(currentSize); // evict one chosen randomly
            chosenPoints.remove(indexToRemove);
            chosenPoints.add(value);
          }
        }
        
        return chosenPoints;
  }
  
  
}