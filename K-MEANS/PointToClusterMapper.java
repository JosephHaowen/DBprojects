import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


import java.io.IOException;
//import java.util.ArrayList;


/**
 * You can modify this class as you see fit.  You may assume that the global
 * centroids have been correctly initialized.
 */
public class PointToClusterMapper extends Mapper<Text, Text, IntWritable, Point>
{
	//private final ArrayList<Point> centroids = KMeans.centroids;
	
	//@Override
	public void map(Text key, Text value, Context context) throws IOException, InterruptedException{
		Point nearestCenter = null;
		int nearestIndex = 0;
		float nearestDistance = Float.MAX_VALUE;
		Point p = new Point(key.toString());
		//Configuration conf = context.getConfiguration();
		//System.out.println(conf);
		for (int i = 0; i < KMeans.centroids.size(); i++){
			Point eachCenter = new Point(KMeans.centroids.get(i));
			float dist = Point.distance(eachCenter, p);	// the distance from this centroid to this value
			//System.out.println("+++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");
			//System.out.println("calculate the distance between two points");
			//System.out.println("++++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");
			if (nearestDistance > dist || nearestCenter == null){
				nearestCenter = new Point(eachCenter);
				nearestDistance = dist;
				nearestIndex = i;
			}
		}
		
		context.write(new IntWritable(nearestIndex), p);
		//System.out.println("+++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");
		//System.out.println("finish one time map");
		//System.out.println("++++++++++++++++++++++++++++++++\n+++++++++++++++++++++++++++++++");
	}
}
