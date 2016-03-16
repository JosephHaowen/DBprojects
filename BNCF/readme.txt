Haijing Fu (hf284), Haowen Tao (ht398), Shuang Wu (sw825)

readme
BCNF.java (main method)
compose(attributeSet, set<fds>)
	call the composeHelper, and for each subset X, call closure for X+, if it is not a superKey 
or determines only itself, try to split it to left and right. Then recurse on each side.
closure(attributeSet, set<fds>)
	This function is meant to find all closures for each subset and function dependencies. This 
follows the algorithm in the link of instructions. 

AttributeSet.java
define all the parameters for each AttributeSet
Important functions: equals, getAllSubset

Attribute.java

FunctionalDependency.java

BCNFTest.java
test cases to check whether the BCNF decomposition is correct

