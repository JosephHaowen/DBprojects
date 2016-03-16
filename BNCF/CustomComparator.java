import java.util.Comparator;


public class CustomComparator implements Comparator<Attribute> {

	@Override
	public int compare(Attribute o1, Attribute o2) {
		// TODO Auto-generated method stub
		return o1.toString().compareTo(o2.toString());
	}
	
}
