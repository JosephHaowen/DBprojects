import java.util.*;

public class BCNF {

  /**
   * Implement your algorithm here
   * **/
	public static Set<AttributeSet> decompose(AttributeSet attributeSet,Set<FunctionalDependency> functionalDependencies) {
		Set<AttributeSet> bcnf = decomposeHelper(attributeSet, functionalDependencies); 
		return bcnf;
	}
	
  public static Set<AttributeSet> decomposeHelper(AttributeSet attributeSet,Set<FunctionalDependency> functionalDependencies) {
		
	  Set<AttributeSet> powerSet = attributeSet.getAllSubset();
	  HashMap<Integer, ArrayList<AttributeSet>> map = new HashMap<Integer, ArrayList<AttributeSet>>();
	  for (AttributeSet eachSet : powerSet){
		  int len = eachSet.size();
		  if (map.containsKey(len)){
			  ArrayList<AttributeSet> temp = map.get(len);
			  temp.add(eachSet);
			  map.put(len, temp);
		  }
		  else{
			  ArrayList<AttributeSet> temp = new ArrayList<AttributeSet>();
			  temp.add(eachSet);
			  map.put(len, temp);
		  }
	  }
	  
	  for(int i = 1; i <= attributeSet.size(); i++){
		  ArrayList<AttributeSet> temp1 = map.get(i);
		  for (AttributeSet s : temp1){
			  AttributeSet item = s;
			  if (item.size() == 0)
				  continue;
			  AttributeSet itemClosure = closure(item, functionalDependencies);
			  //if it is a superKey, or determined by itself, try an different subset/let it loop to next()
			  AttributeSet itemIntersect = itemClosure.interSect(attributeSet);   // find intersect avoid super key
			  boolean isSuperKey = itemIntersect.equals(attributeSet);
			  boolean isSelf = itemIntersect.equals(item);		// determine itself
			  //boolean isEmpty = itemIntersect.size()==0;		// size == 0
			  if (!isSuperKey && ! isSelf ){
				  Set<AttributeSet> left = decomposeHelper(itemIntersect, functionalDependencies);   // left table = X^+
				  AttributeSet temp = attributeSet.eliminate(itemIntersect);		// right table = X Uion (X^+)^c
				  temp.addAllAttribute(item);
				  Set<AttributeSet> right = decomposeHelper(temp,functionalDependencies);
				 
				  left.addAll(right);
				  return left;
			  }
		  }
	  }
	  
//	  Iterator<AttributeSet> powerSet2 = attributeSet.powerSet();
//	  while (powerSet2.hasNext()){
//		  AttributeSet item = powerSet2.next();
//		  if (item.size() == 0)
//			  continue;
//		  AttributeSet itemClosure = closure(item, functionalDependencies);
//		  //if it is a superKey, or determined by itself, try an different subset/let it loop to next()
//		  AttributeSet itemIntersect = itemClosure.interSect(attributeSet);   // find intersect avoid super key
//		  boolean isSuperKey = itemIntersect.equals(attributeSet);
//		  boolean isSelf = itemIntersect.equals(item);		// determine itself
//		  //boolean isEmpty = itemIntersect.size()==0;		// size == 0
//		  if (!isSuperKey && ! isSelf ){
//			  Set<AttributeSet> left = decomposeHelper(itemIntersect, functionalDependencies);   // left table = X^+
//			  AttributeSet temp = attributeSet.eliminate(itemIntersect);		// right table = X Uion (X^+)^c
//			  temp.addAllAttribute(item);
//			  Set<AttributeSet> right = decomposeHelper(temp,functionalDependencies);
//			 
//			  left.addAll(right);
//			  return left;
//		  }
//	  }
//	  
	  Set<AttributeSet> res = new HashSet<AttributeSet>();
	  res.add(attributeSet);
	  return res;
  }

  /**
   * Recommended helper method
   **/
  public static AttributeSet closure(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {
		AttributeSet newDep = new AttributeSet(attributeSet);
		if (functionalDependencies.size() < 1)
			return newDep;
		AttributeSet oldDep = null;
		while(oldDep == null || !newDep.equals(oldDep)) {
			oldDep = new AttributeSet(newDep);
			for(FunctionalDependency fd : functionalDependencies) {
				// if u is a subset of closure x
				if(newDep.contains(fd.independent())) {
					newDep.addAllAttribute(fd.dependent());
				}
			}
		}
		return newDep;
	}
  
}



//System.out.println("here dfhdakfdkjfdksize is " + bcnf.size());
//for(AttributeSet as:bcnf){
//	System.out.println(as);
//}

//Set<AttributeSet> result = new HashSet<AttributeSet>();
//if (bcnf.size() == 1)
//	return bcnf;
//for(AttributeSet eachSet1 : bcnf){
//	  boolean isDuplicate = false;
//	  for(AttributeSet eachSet2 : bcnf){
//		  if (eachSet2 == eachSet1)
//			  continue;
//		  else if(eachSet2.equals(eachSet1)){
//			  isDuplicate = true;
//			  break;
//		  }
//	  }
//	  if(!isDuplicate)
//		  result.add(eachSet1);
//  }
//return result;
