/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.testing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


/**
 *
 * @author sehzad
 */
public class mahout1 {
    
    
	public static void main(String[] args) throws IOException, TasteException {
		
		DataModel model = new FileDataModel (new File("intro.csv"));
		
		UserSimilarity similarity = new PearsonCorrelationSimilarity (model);		
		
		UserNeighborhood neighborhood = new NearestNUserNeighborhood (2, similarity, model);
				
		Recommender  recommender = new GenericUserBasedRecommender (model, neighborhood, similarity);
		
		List<RecommendedItem> recommendations = recommender.recommend(1, 3);
		
		for (RecommendedItem recommendation : recommendations) {
			System.out.println(recommendation);
		}
		
                
                
	}    
    
}
