package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RecommendationSystemApplication {

	public static String data="/Users/fahadhasan/Downloads/data.txt";
	private static final int NEIGHBORHOOD_SIZE=5;
	private static DataModel model=null;
	private static LogLikelihoodSimilarity similarity=null;
	private static UserNeighborhood neighberhood=null;
	private static UserBasedRecommender recommender=null;
	private static String[] products= {"book","car","fruit","pen","cloth"};
	private static String[] users= {"fahad","hasan","nur","rana","amlai"};
	
	public static void main(String[] args) throws IOException, TasteException {
		SpringApplication.run(RecommendationSystemApplication.class, args);
		
		 model=new FileDataModel(new File(data));
		
		similarity=new LogLikelihoodSimilarity(model);
		//EuclideanDistanceSimilarity similarity=new EuclideanDistanceSimilarity(model);
		neighberhood =new ThresholdUserNeighborhood(0.1,similarity,model);
		
		recommender=new GenericUserBasedRecommender(model,neighberhood,similarity);
		
		List<RecommendedItem> recommenderItem=recommender.recommend(100, 5);
		
		System.out.println("Recommendation for User"+users[0]+" are");
		System.out.println();
		System.out.println("Id    Name     Preference");
		for(RecommendedItem r: recommenderItem ) {
			int productId=(int) r.getItemID();
			float estimatedPreference=recommender.estimatePreference(200, productId);
			System.out.println(productId+ "   "+  products[productId-2]+"    "+ estimatedPreference );
		}
	//	System.out.println(Long.parseLong("9079398138053964306"));
		System.out.println();
		System.out.println();
		itemSimilarity();
		
		
//		long[] neighbor=neighberhood.getUserNeighborhood(1);
//		System.out.println("Similarity Between user 1 and 3 is"+similarity.userSimilarity(1, 3));
//		System.out.println("Neighbor for user 1 are:");
//		for(long user:neighbor) {
//			System.out.println(user+ "  ");
//		}
		
	}
	static void itemSimilarity() throws TasteException, IOException {
		DataModel dataModel =new FileDataModel(new File(data));
		ItemSimilarity sim=new LogLikelihoodSimilarity(dataModel);
		GenericItemBasedRecommender rec=new GenericItemBasedRecommender(dataModel,sim) ;
		int x=1;
		for(LongPrimitiveIterator items=  dataModel.getItemIDs(); items.hasNext();) {
			long itemId=items.nextLong();
			List<RecommendedItem> recommendations=rec.mostSimilarItems(itemId, 5);
			for(RecommendedItem recommendation: recommendations) {
				System.out.println(itemId+ ",  "+recommendation.getItemID()+ ",   "+ recommendation.getValue());
			}
		}
	}

}
