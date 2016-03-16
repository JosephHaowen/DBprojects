import java.util.*;

/**
 * An unordered set of Attributes. This could very easily be a Java collection,
 * but an important operation (namely examining the powerset) is not easily done
 * with the Java collection.
 **/
public class AttributeSet {

	//a list of the backing attributes
	private final List<Attribute> _attributes;

	//construct an empty AttributeSet
	public AttributeSet() {
		_attributes = new ArrayList<>();
	}

	//copy constructor
	public AttributeSet(AttributeSet other) {
		_attributes = new ArrayList<>(other._attributes);
	}

	public void addAttribute(Attribute a) {
		if(!_attributes.contains(a))     //only add inequal items
			_attributes.add(a);
	}
	
	public void addAllAttribute(AttributeSet attrSet){
		Iterator<Attribute> iter = attrSet.iterator();
		while(iter.hasNext())
			addAttribute(iter.next());
	}

	public boolean contains(Attribute a) {
		return _attributes.contains(a);
	}
	
	public boolean contains(AttributeSet attrSet){
		return _attributes.containsAll(attrSet._attributes);
	}

	public int size() {
		return _attributes.size();
	}

	public boolean equals(Object other) {
		//TODO: you should probably implement this
		if(other == null || !(other instanceof AttributeSet))
			return false;
		List<Attribute> o = ((AttributeSet)other)._attributes;
		//System.out.println("i am in equals");
		return _attributes.containsAll(o) && o.containsAll(_attributes);
	}
	
	/**
	 * find the intersect of two attributeSet
	 * **/
	public AttributeSet interSect(AttributeSet attrSet){ 
		AttributeSet res = new AttributeSet();
		Iterator<Attribute> iter = attrSet.iterator();
		while (iter.hasNext()){
			Attribute item = iter.next();
			if (contains(item))
				res.addAttribute(item);
		}
		return res;
	}
	
	/**
	 * calculate the right table, without X
	 * **/
	public AttributeSet eliminate(AttributeSet attrSet){
		AttributeSet res = new AttributeSet(this);
		for (Attribute a : attrSet._attributes){
			res._attributes.remove(a);
		}
		return res;
	}

	public AttributeSet sortInside(){
		AttributeSet res = new AttributeSet(this);
		Collections.sort(res._attributes, new CustomComparator());
		return res;
	}
	
	public Iterator<Attribute> iterator() {
		return _attributes.iterator();
	}

	public String toString() {
		String out = "";
		Iterator<Attribute> iter = iterator();
		while(iter.hasNext())
			out += iter.next() + "\t";

		return out;
	}
	
	
	
	private void getAllSubsetHelper(Set<AttributeSet> rs, AttributeSet attrs, int start, AttributeSet path){
		if(start <= _attributes.size()){
			rs.add(new AttributeSet(path));
		}
		
		for(int i = start; i < _attributes.size(); i++){
			path._attributes.add(attrs._attributes.get(i));
			getAllSubsetHelper(rs, attrs, i+1, path);
			path._attributes.remove(path.size() - 1);
			}
		}
	
	public Set<AttributeSet> getAllSubset(){
		Set<AttributeSet> rs = new HashSet<AttributeSet>();
		if (_attributes.size() == 0)
			return rs;
		getAllSubsetHelper(rs, this, 0, new AttributeSet());
		return rs;
	}

}




//Iterator<AttributeSet> powerSet(){
//	return new Iterator<AttributeSet>(){
//		private long cur = 0;
//		private long size = (1L<<_attributes.size());
//		
//		@Override
//		public boolean hasNext() {
//			return cur < size;
//		}
//
//		@Override
//		public AttributeSet next() {
//			AttributeSet res = new AttributeSet();
//			for (int i = 0; i < 64; i ++){
//				long mask = 1L << i;
//				if ((cur & mask) != 0)
//					res._attributes.add(_attributes.get(i));
//			}
//			cur ++;
//			return res;
//		}	
//	};
//}
