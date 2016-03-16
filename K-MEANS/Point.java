import java.io.*; // DataInput/DataOuput
import java.util.ArrayList;
//import java.util.Collections;

import org.apache.hadoop.io.*; // Writable

/**
 * A Point is some ordered list of floats.
 * 
 * A Point implements WritableComparable so that Hadoop can serialize
 * and send Point objects across machines.
 *
 * NOTE: This implementation is NOT complete.  As mentioned above, you need
 * to implement WritableComparable at minimum.  Modify this class as you see fit.
 */
public class Point implements WritableComparable<Point>{
	private ArrayList<Float> pointList = new ArrayList<Float>();
	private int dim;

	/**
	 * Construct for Point.init<>
	 * */
	public Point(){
		this.dim = KMeans.dimension;
		for(int i=0;i<dim;i++) 
			pointList.add(new Float(0.0));
	}

	/**
	 * Construct a Point with the given dimensions [dim]. The coordinates should all be 0.
	 * For example:
	 * Constructing a Point(2) should create a point (x_0 = 0, x_1 = 0)
	 */
	public Point(int dim)
	{
		this.dim = dim;
		for(int i=0;i<dim;i++) 
			pointList.add(new Float(0.0));
	}
	
	/**
	 * Copy constructor
	 */
	public Point(Point other)
	{
		dim = other.dim;
		for(Float f : other.pointList) 
			pointList.add(f);
	}

	/**
	 * Construct a point from a properly formatted string (i.e. line from a test file)
	 * @param str A string with coordinates that are space-delimited.
	 * For example: 
	 * Given the formatted string str="1 3 4 5"
	 * Produce a Point {x_0 = 1, x_1 = 3, x_2 = 4, x_3 = 5}
	 */
	public Point(String str)
	{
		String[] divide = str.split(" ");
		this.dim = divide.length;
		for(String s:divide)
			pointList.add(Float.parseFloat(s));
	}
	
	/**
	 * @return The dimension of the point.  For example, the point [x=0, y=1] has
	 * a dimension of 2.
	 */
	public int getDimension()
	{
		return dim;
	}

	/**
	 * Converts a point to a string.  Note that this must be formatted EXACTLY
	 * for the autograder to be able to read your answer.
	 * Example:
	 * Given a point with coordinates {x=1, y=1, z=3}
	 * Return the string "1 1 3"
	 */
	public String toString()
	{
		String result="";
		for(int i=0;i<getDimension();i++) 
			result+= pointList.get(i)+" ";
		return result.trim();
	}

	/**
	 * One of the WritableComparable methods you need to implement.
	 * See the Hadoop documentation for more details.
	 * Comparing two points of different dimensions should return that they are
	 * different and anything else is undefined behavior.
	 */

	public int compareTo(Point other)
	{   
		if(this.dim != other.dim){
			System.err.println("Different Dimension in compare.");
			System.exit(1);
		}
		double epsilon = 0.000001;
		for(int i=0;i<this.dim;i++){
			double difference = this.pointList.get(i)-other.pointList.get(i);
			if( difference > epsilon) return 1;
			if( difference < -epsilon) return -1;
		}
		return 0;
	}

	/**
	 * @return The L2 distance between two points.
	 */
	public static final float distance(Point x, Point y)
	{   
		if(x.dim!=y.dim){
			System.err.println("Different Dimension in calculating distance.");
			System.exit(1);
		}
		double distance = (float)0.0;
		for(int i=0;i<x.dim;i++){
			double diff = Math.abs(x.pointList.get(i)-y.pointList.get(i));
			distance += (diff*diff);
		}
		return (float)Math.sqrt(distance);
	}

	/**
	 * @return A new point equal to [x]+[y]
	 */
	public static final Point addPoints(Point x, Point y)
	{
		if(x.dim!=y.dim){
			System.out.println("Different dimension in add:  "+x.dim + "  " + y.dim);
			System.exit(1);
		}
		Point result = new Point(x.dim);
		for(int i=0;i<x.dim;i++){
			result.pointList.set(i,new Float(x.pointList.get(i).floatValue()+y.pointList.get(i).floatValue()));
		}
		return result;	
	}

	/**
	 * @return A new point equal to [c][x]
	 */
	public static final Point multiplyScalar(Point x, float c)
	{
		Point result = new Point(x.dim);
		for(int i=0;i<x.dim;i++){
			result.pointList.set(i,new Float(x.pointList.get(i).floatValue()*c));
		}
		return result;	
	}
	
	public void readFields(DataInput in) throws IOException { //must implement Writable
		dim = in.readInt();
		for(int i=0;i<dim;i++)
			pointList.set(i,in.readFloat());
	}

	public void write(DataOutput out) throws IOException {   //must implement Writable
		out.writeInt(dim);
		for(int i=0;i<dim;i++)
			out.writeFloat(pointList.get(i));
	}

	public int hashCode(){
		final int prime = 17;
		int result = 1;
		result = prime * result + dim;
		return result;
	}
}
