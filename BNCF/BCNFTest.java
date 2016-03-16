import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class BCNFTest {
  /**
   * Performs a basic test on a simple table.
   * gives input attributes (a,b,c) and functional dependency a->c
   * and expects output (a,c),(b,c) or any reordering
   **/
  @Test
  public void testSimpleBCNF() {
    //construct table
    AttributeSet attrs = new AttributeSet();
    attrs.addAttribute(new Attribute("a"));
    attrs.addAttribute(new Attribute("b"));
    attrs.addAttribute(new Attribute("c"));

    //create functional dependencies
    Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
    AttributeSet ind = new AttributeSet();
    AttributeSet dep = new AttributeSet();
    ind.addAttribute(new Attribute("a"));
    dep.addAttribute(new Attribute("c"));
    FunctionalDependency fd = new FunctionalDependency(ind, dep);
    fds.add(fd);

    //run client code
    Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

    //verify output
    assertEquals("Incorrect number of tables", 2, bcnf.size());

    for(AttributeSet as : bcnf) {
      assertEquals("Incorrect number of attributes", 2, as.size());
      assertTrue("Incorrect table", as.contains(new Attribute("a")));
    }
  }
  
  @Test
  public void testSimpleBCNF1() {
    //construct table
    AttributeSet attr = new AttributeSet();
    attr.addAttribute(new Attribute("c"));
    attr.addAttribute(new Attribute("s"));
    attr.addAttribute(new Attribute("j"));
    attr.addAttribute(new Attribute("d"));
    attr.addAttribute(new Attribute("p"));
    attr.addAttribute(new Attribute("q"));
    attr.addAttribute(new Attribute("v"));
//    attr.addAttribute(new Attribute("a"));
//    attr.addAttribute(new Attribute("b"));
    
    Set<FunctionalDependency> fdss = new HashSet<FunctionalDependency>();
    
    AttributeSet in3 = new AttributeSet();
    AttributeSet de3 = new AttributeSet();
    in3.addAttribute(new Attribute("c"));
    de3.addAttribute(new Attribute("c"));
    de3.addAttribute(new Attribute("s"));
    de3.addAttribute(new Attribute("j"));
    de3.addAttribute(new Attribute("d"));
    de3.addAttribute(new Attribute("p"));
    de3.addAttribute(new Attribute("q"));
    de3.addAttribute(new Attribute("v"));
    FunctionalDependency fd4 = new FunctionalDependency(in3, de3);
    fdss.add(fd4);
    
    AttributeSet in1 = new AttributeSet();
    AttributeSet de1 = new AttributeSet();
    in1.addAttribute(new Attribute("s"));
    in1.addAttribute(new Attribute("d"));
    de1.addAttribute(new Attribute("p"));
    FunctionalDependency fd2 = new FunctionalDependency(in1, de1);
    fdss.add(fd2);
    
    AttributeSet in2 = new AttributeSet();
    AttributeSet de2 = new AttributeSet();
    in2.addAttribute(new Attribute("j"));
    de2.addAttribute(new Attribute("s"));
    //de2.addAttribute(new Attribute("c"));
    FunctionalDependency fd3 = new FunctionalDependency(in2, de2);
    fdss.add(fd3);
    
    
    AttributeSet in = new AttributeSet();
    AttributeSet de = new AttributeSet();
    in.addAttribute(new Attribute("j"));
    in.addAttribute(new Attribute("p"));
    //de.addAttribute(new Attribute("b"));
    de.addAttribute(new Attribute("c"));
    FunctionalDependency fd1 = new FunctionalDependency(in, de);
    fdss.add(fd1);
    
    Set<AttributeSet> bcnf1 = BCNF.decompose(attr, fdss);
    for(AttributeSet as:bcnf1){
    	//System.out.println(as);
    }
    
  }
  
  @Test
  public void testSimpleBCNF2() {
    //construct table
    AttributeSet attr = new AttributeSet();
    attr.addAttribute(new Attribute("a"));
    attr.addAttribute(new Attribute("b"));
    attr.addAttribute(new Attribute("c"));
    attr.addAttribute(new Attribute("d"));
//    attr.addAttribute(new Attribute("e"));
//    attr.addAttribute(new Attribute("f"));
//    attr.addAttribute(new Attribute("g"));

    
    Set<FunctionalDependency> fdss = new HashSet<FunctionalDependency>();
    
    
    AttributeSet in3 = new AttributeSet();
    AttributeSet de3 = new AttributeSet();
    in3.addAttribute(new Attribute("a"));
    de3.addAttribute(new Attribute("b"));
    de3.addAttribute(new Attribute("c"));
    FunctionalDependency fd4 = new FunctionalDependency(in3, de3);
    fdss.add(fd4);
    
//    AttributeSet in1 = new AttributeSet();
//    AttributeSet de1 = new AttributeSet();
//    in1.addAttribute(new Attribute("s"));
//    in1.addAttribute(new Attribute("d"));
//    de1.addAttribute(new Attribute("p"));
//    FunctionalDependency fd2 = new FunctionalDependency(in1, de1);
//    fdss.add(fd2);
//    
//    AttributeSet in2 = new AttributeSet();
//    AttributeSet de2 = new AttributeSet();
//    in2.addAttribute(new Attribute("j"));
//    de2.addAttribute(new Attribute("s"));
//    //de2.addAttribute(new Attribute("c"));
//    FunctionalDependency fd3 = new FunctionalDependency(in2, de2);
//    fdss.add(fd3);
//    
//    
//    AttributeSet in = new AttributeSet();
//    AttributeSet de = new AttributeSet();
//    in.addAttribute(new Attribute("j"));
//    in.addAttribute(new Attribute("p"));
//    //de.addAttribute(new Attribute("b"));
//    de.addAttribute(new Attribute("c"));
//    FunctionalDependency fd1 = new FunctionalDependency(in, de);
//    fdss.add(fd1);
    
    Set<AttributeSet> bcnf1 = BCNF.decompose(attr, fdss);
    for(AttributeSet as:bcnf1){
    	//System.out.println(as);
    }
    
//    Attribute temp1 = new Attribute("1");
//    Attribute temp2 = new Attribute("2");
//    AttributeSet set1 = new AttributeSet();
//    AttributeSet set2 = new AttributeSet();
//    Set<AttributeSet> set1set = new HashSet<AttributeSet>();
//    Set<AttributeSet> set2set = new HashSet<AttributeSet>();
//    set1.addAttribute(temp1);
//    set1.addAttribute(temp2);
//    set2.addAttribute(temp1);
//    set2.addAttribute(temp2);
//    System.out.println(set1);
//    System.out.println(set2+"\n");
//   
//    set1set.add(set1);
//    set2set.add(set2);
//    for(AttributeSet eachSet2 : set2set){
//    	boolean isContain = false;
//    	for (AttributeSet eachSet1 : set1set){
//    		if (eachSet1.equals(eachSet2))
//    			isContain = true;
//    	}
//    	if(!isContain)
//    		set1set.add(eachSet2);
//    }
//    
//    System.out.println(set1set.size());
  }
  
  @Test
  public void testSimpleBCNF3() {
    
	  AttributeSet attr = new AttributeSet();
	    attr.addAttribute(new Attribute("a"));
	    attr.addAttribute(new Attribute("b"));
	    attr.addAttribute(new Attribute("c"));
	    attr.addAttribute(new Attribute("d"));
	    attr.addAttribute(new Attribute("e"));
	    attr.addAttribute(new Attribute("f"));
	    attr.addAttribute(new Attribute("g"));
	    
	    Set<FunctionalDependency> fdss = new HashSet<FunctionalDependency>();
	    AttributeSet in = new AttributeSet();
	    AttributeSet de = new AttributeSet();
	    in.addAttribute(new Attribute("d"));
	    de.addAttribute(new Attribute("b"));
	    de.addAttribute(new Attribute("c"));
	    FunctionalDependency fd1 = new FunctionalDependency(in, de);
	    fdss.add(fd1);
	    
	    AttributeSet in1 = new AttributeSet();
	    AttributeSet de1 = new AttributeSet();
	    in1.addAttribute(new Attribute("a"));
	    in1.addAttribute(new Attribute("f"));
	    de1.addAttribute(new Attribute("e"));
	    FunctionalDependency fd2 = new FunctionalDependency(in1, de1);
	    fdss.add(fd2);
	    
	    AttributeSet in2 = new AttributeSet();
	    AttributeSet de2 = new AttributeSet();
	    in2.addAttribute(new Attribute("b"));
	    de2.addAttribute(new Attribute("a"));
	    de2.addAttribute(new Attribute("c"));
	    FunctionalDependency fd3 = new FunctionalDependency(in2, de2);
	    fdss.add(fd3);
	    
	    AttributeSet in3 = new AttributeSet();
	    AttributeSet de3 = new AttributeSet();
	    in3.addAttribute(new Attribute("ch"));
	    de3.addAttribute(new Attribute("bd"));
	    FunctionalDependency fd4 = new FunctionalDependency(in3, de3);
	    fdss.add(fd4);
	    
	    Set<AttributeSet> bcnf1 = BCNF.decompose(attr, fdss);
	    for(AttributeSet as:bcnf1){
	    	System.out.println(as);
	    }
    
  }
  @Test
  public void testPiazzaBCNF() {
		//a,b,c,d
		//a -> x, x -> b
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("b"));
		attrs.addAttribute(new Attribute("c"));

		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		ind.addAttribute(new Attribute("a"));
		dep.addAttribute(new Attribute("d"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		ind = new AttributeSet();
		dep = new AttributeSet();
		ind.addAttribute(new Attribute("d"));
		dep.addAttribute(new Attribute("b"));
		fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		Set<AttributeSet> bcnf1 = BCNF.decompose(attrs, fds);

		for(AttributeSet as:bcnf1){
	    	//System.out.println(as);
	    }
	}
  @Test
  public void testPiazzaBCNF1() {
		//a,b,c,d
		//a -> x, x -> b
		AttributeSet attrs = new AttributeSet();
		attrs.addAttribute(new Attribute("a"));
		attrs.addAttribute(new Attribute("b"));
		attrs.addAttribute(new Attribute("c"));
		attrs.addAttribute(new Attribute("d"));
		attrs.addAttribute(new Attribute("e"));

		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet ind = new AttributeSet();
		AttributeSet dep = new AttributeSet();
		ind.addAttribute(new Attribute("a"));
		dep.addAttribute(new Attribute("e"));
		FunctionalDependency fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		ind = new AttributeSet();
		dep = new AttributeSet();
		ind.addAttribute(new Attribute("b"));
		ind.addAttribute(new Attribute("c"));
		dep.addAttribute(new Attribute("a"));
		fd = new FunctionalDependency(ind, dep);
		fds.add(fd);
		
		ind = new AttributeSet();
		dep = new AttributeSet();
		ind.addAttribute(new Attribute("d"));
		ind.addAttribute(new Attribute("e"));
		dep.addAttribute(new Attribute("b"));
		fd = new FunctionalDependency(ind, dep);
		fds.add(fd);

		Set<AttributeSet> bcnf1 = BCNF.decompose(attrs, fds);

		for(AttributeSet as:bcnf1){
	    	//System.out.println(as);
	    }
	}

  
}