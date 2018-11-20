/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.testing;


//package mia.clustering.ch09;


import java.util.ArrayList;
import java.util.List;

//import org.apache.mahout.clustering.kmeans.Cluster;
//import org.apache.mahout.clustering.kmeans.KMeansClusterer;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.Vector;

public class mahout2 {

	public static void main(String[] args) {
		/*List<Vector> sampleData = new ArrayList<Vector>();

		RandomPointsUtil.generateSamples(sampleData, 400, 1, 1, 3);
		RandomPointsUtil.generateSamples(sampleData, 300, 1, 0, 0.5);
		RandomPointsUtil.generateSamples(sampleData, 300, 0, 2, 0.1);

		int k = 3;
		List<Vector> randomPoints = RandomPointsUtil.chooseRandomPoints(
				sampleData, k);
		List<Cluster> clusters = new ArrayList<Cluster>();

		int clusterId = 0;
		for (Vector v : randomPoints) {
			clusters.add(new Cluster(v, clusterId++, new EuclideanDistanceMeasure()));
		}

		List<List<Cluster>> finalClusters = KMeansClusterer.clusterPoints(
				sampleData, clusters, new EuclideanDistanceMeasure(), 3, 0.01);
		for (Cluster cluster : finalClusters.get(finalClusters.size() - 1)) {
			System.out.println("Cluster id: " + cluster.getId() + " center: "
					+ cluster.getCenter().asFormatString());
		}*/
	}

}
