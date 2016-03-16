import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


/** 
 * You can modify this class as you see fit, as long as you correctly update the
 * global centroids.
 */
public class ClusterToPointReducer extends Reducer<IntWritable, Point, IntWritable, Point>
{
	
	//@Override
	protected void reduce(IntWritable key, Iterable<Point> values, Context context) throws IOException, InterruptedException{
		//System.out.println("++++++++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");
		//System.out.println("enter reduce");
		//System.out.println("++++++++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");

		int count = 0;
		Point newCenter = new Point(KMeans.dimension);
		//get sum of all points in this cluster
		for (Point p : values){
			newCenter = Point.addPoints(newCenter, p);
			count ++;
		}
		//System.out.println("++++++++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");
		//System.out.println("sum up to get new center: " + newCenter);
		//System.out.println("++++++++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");

		newCenter = Point.multiplyScalar(newCenter, 1.0f/(float)count);	//get mean
		//System.out.println("++++++++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");
		//System.out.println("divide to get new center: " + newCenter);
		//System.out.println("++++++++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");
		context.write(key, newCenter);
		
		KMeans.centroids.set(key.get(), newCenter);	//update the centroid of this cluster in Kmeans.java
		
	}
}
