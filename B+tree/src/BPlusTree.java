package BPlusTree;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * BPlusTree Class Assumptions: 1. No duplicate keys inserted 2. Order D:
 * D<=number of keys in a node <=2*D 3. All keys are non-negative
 * TODO: Rename to BPlusTree
 */
public class BPlusTree<K extends Comparable<K>, T> {

	public Node<K,T> root;
	public static final int D = 2;

	/**
	 * TODO Search the value for a specific key
	 * 
	 * @param key
	 * @return value
	 */
	public T search(K key) {
		Node<K,T> node = this.root;
		if(node!=null){
			return treeSearch(node,key);
		}
		return null;
	}
	/**
	 * If node is a leaf node, search its key list. 
	 * Return the matching value if find, otherwise return null; 
	 * 
	 * If node is a index node, recursively call the method on its matching child. 
	 * 
	 */
	public T treeSearch(Node<K,T> node, K key){
		ArrayList<K> keysList = node.keys;
		if(node.isLeafNode == true){
			ArrayList<T> valuesList = ((LeafNode<K, T>)node).values;
			for(int i=0;i<keysList.size();i++){
				if(key.compareTo(keysList.get(i))==0){
					return valuesList.get(i);
				}
			}
			return null;
		}else{
			ArrayList<Node<K,T>> child = ((IndexNode<K, T>)node).children;
			int ind = 0;
			if(key.compareTo(keysList.get(0))<0){
				return treeSearch(child.get(0),key);
			}else if(key.compareTo(keysList.get(keysList.size()-1))>0){
				return treeSearch(child.get(child.size()-1),key);
			}else{
				for(int i=0;i<keysList.size();i++){
					if(key.compareTo(keysList.get(i))>0&&key.compareTo(keysList.get(i+1))<0){
							ind = i;
							break;
					}
				}
				return treeSearch(child.get(ind),key);
				
			}
		}
	}
	/**
	 * TODO Insert a key/value pair into the BPlusTree
	 * 
	 * @param key
	 * @param value
	 * 
	 * If root is not null and not a leaf node, then call indexSearch on root.
	 * If return value of indexSearch is not null, then set the root to its entry value.
	 * 
	 * If root is not null and is a leaf node, then call leafInsert on root.
	 * If return value is not null, then use its entry key and value to construct a new index node as new root.
	 * 
	 * If root is null, then use the input key and value to construct a new root.
	 */
	public void insert(K key, T value) {
		Node<K,T> node = this.root;
		if(node!=null&&node.isLeafNode==false){
			Entry<K,Node<K,T>> newEntry = indexSearch((IndexNode<K,T>)node,key,value);
			if(newEntry!=null){
				this.root = newEntry.getValue();
			}
		}else if(node!=null&&node.isLeafNode==true){
			Entry<K,Node<K,T>> newRoot = leafInsert((LeafNode<K, T>) node,key,value);
			if(newRoot!=null){
				K newRootKey = newRoot.getKey();
				LeafNode<K,T> child2 = (LeafNode<K,T>)newRoot.getValue();
				IndexNode<K,T> newroot = new IndexNode<K,T>(newRootKey, node, child2);
				this.root = newroot;
			}
		}else{
			LeafNode<K,T> newRoot = new LeafNode<K,T> (key, value);
			this.root = newRoot;
		
		}	
	}
	/**
	 * 
	 * @param node
	 * @param key
	 * @param value
	 * @return
	 * 
	 * starting from calling this method on root, if root is not null and is a index node.
	 * 
	 * use input key to search the node key list and find the matching child. 
	 * 
	 * if the child is a index node, recursively call indexSearch on the child.
	 * if return entry is null, return null.
	 * if return entry is not null, call indexInsert on the node and return its result.
	 * 
	 * if the child is a leaf node, call leafInsert on the child.
	 * if return entry is not null, call indexInsert on the node.
	 * 
	 */
	public Entry<K,Node<K,T>> indexSearch(IndexNode<K,T> node, K key, T value){
			ArrayList<K> indexKeys = node.keys;
			int index = 0;
			for(int j=0;j<indexKeys.size();j++){
				if(key.compareTo(indexKeys.get(0))<0){
					index = 0;
					break;
				}else if(key.compareTo(indexKeys.get(indexKeys.size()-1))>0){
					index = indexKeys.size();
					break;
				}else if(key.compareTo(indexKeys.get(j))>0&&key.compareTo(indexKeys.get(j+1))<0){
					index = j+1;
					break;
				}
			}
			
			
			ArrayList<Node<K,T>> indexChild = node.children;
			Node<K,T> nextInsert = indexChild.get(index);
			if(nextInsert.isLeafNode==false){
				Entry<K,Node<K,T>> addNode1 = indexSearch((IndexNode<K,T>)nextInsert, key, value);
				if(addNode1==null){
					return null;
				}
				return indexInsert(node, addNode1);
				
			}else{
				Entry<K,Node<K,T>> addNode2 = leafInsert((LeafNode<K,T>)nextInsert,key,value);
				if(addNode2==null){
					return null;
				}
				return indexInsert(node,addNode2);
				
			}
			
	}
	/**
	 * 	
	 * @param node
	 * @param addEntry
	 * @return
	 * 
	 * Starting from when the node is index node and needs to be inserted, and addEntry is not null.
	 * 
	 * add the addEntry value and node to the input node's key list and child list.
	 * 
	 * if input node is overflowed, call splitIndexNode on it.
	 */
	
	public Entry<K,Node<K,T>> indexInsert(IndexNode<K,T> node, Entry<K,Node<K,T>> addEntry){ /*insert new key into the index node, remember to include the index is root*/
		ArrayList<K> indexKeys = node.keys;
		ArrayList<Node<K,T>> indexChild = node.children;
		K addKey = addEntry.getKey();
		Node<K, T> addNode = addEntry.getValue();
		int index = 0;
		for(int i=0;i<indexKeys.size();i++){
			if(addKey.compareTo(indexKeys.get(0))<0){
				index = 0;
				break;
			}else if(addKey.compareTo(indexKeys.get(indexKeys.size()-1))>0){
				index = indexKeys.size();
				break;
			}else if(addKey.compareTo(indexKeys.get(i))>0&&addKey.compareTo(indexKeys.get(i+1))<0){
				index = i+1;
				break;
			}
		}
		indexKeys.add(index,addKey);
		indexChild.add(index+1,addNode);
		if(indexKeys.size()>2*D){
			return splitIndexNode(node);
		}
		return null;
		
	}
	/**
	 * 
	 * @param node
	 * @param key
	 * @param value
	 * @return
	 *
	 * starting from when the input node is a leaf node and needs to be inserted.
	 * 
	 * use the input key to find the right place to insert, then add key and value to leafKeys and values.
	 * 
	 * if input node is overflowed, call splitLeafNode on it.
	 */
	public Entry<K,Node<K,T>> leafInsert(LeafNode<K,T> node, K key, T value){
		
		ArrayList<K> leafKeys = node.keys;
		ArrayList<T> leafValues = node.values;
		int index = 0;
		  
		for(int i=0;i<leafKeys.size();i++){
			if(key.compareTo(leafKeys.get(0))<0){
				index = 0;
				break;
			}else if(key.compareTo(leafKeys.get(leafKeys.size()-1))>0){
				index = leafKeys.size();
				break;
			}else if(key.compareTo(leafKeys.get(i))>0&&key.compareTo(leafKeys.get(i+1))<0){
				index = i+1;
				break;
			}
		}
		leafKeys.add(index,key);
		leafValues.add(index,value);
		//	}else{
		if(leafKeys.size()>2*D){
			return splitLeafNode(node);
		}
		return null;
	}
	

	/**
	 * TODO Split a leaf node and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param leaf, any other relevant data
	 * @return the key/node pair as an Entry
	 * 
	 */
	public Entry<K, Node<K,T>> splitLeafNode(LeafNode<K,T> leaf) {
		ArrayList<K> leafKeys = leaf.keys;
		ArrayList<T> leafValues = leaf.values;
		List<K> newKeys = new ArrayList<K> ();
		List<T> newValues = new ArrayList<T> ();

		for(int i = D;i<leafKeys.size();i++){
			newKeys.add(leafKeys.get(i));
			newValues.add(leafValues.get(i));
		}
		LeafNode<K,T> newLeaf = new LeafNode<K,T>(newKeys,newValues);
		int sizeOfLeafKey = leafKeys.size(); 
		for(int j = D;j<sizeOfLeafKey;j++){
			leafKeys.remove(leafKeys.size()-1);
			leafValues.remove(leafValues.size()-1);
		}
		newLeaf.previousLeaf = leaf;
		newLeaf.nextLeaf = leaf.nextLeaf;
		if(leaf.nextLeaf!=null) leaf.nextLeaf.previousLeaf=newLeaf;
		leaf.nextLeaf = newLeaf;
		Entry<K, Node<K, T>> map = new AbstractMap.SimpleEntry<K, Node<K,T>>(newLeaf.keys.get(0), newLeaf);
		
		return map;
	}

	/**
	 * TODO split an indexNode and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param index, any other relevant data
	 * @return new key/node pair as an Entry
	 * 
	 *
	 */
	public Entry<K, Node<K,T>> splitIndexNode(IndexNode<K,T> index) {
		
			ArrayList<K> indexKeys = index.keys;
			List<K> newKeys = new ArrayList<K> ();
			K newKey = indexKeys.get(D);
			indexKeys.remove(newKeys);
			ArrayList<Node<K,T>> indexChild = index.children;
			List<Node<K,T>> newChild = new ArrayList<Node<K,T>>();
			
			for(int i = D+1;i<indexKeys.size();i++){
				newKeys.add(indexKeys.get(i));
			}
			for(int j = D+1;j<indexChild.size();j++){
				newChild.add(indexChild.get(j));
			}
			IndexNode<K,T> newIndex = new IndexNode<K,T>(newKeys,newChild);
			int sizeOfIndexKey = indexKeys.size(); 
			for(int j = D;j<sizeOfIndexKey;j++){
				indexKeys.remove(indexKeys.size()-1);
				indexChild.remove(indexChild.size()-1);
			}
			
			Entry<K, Node<K, T>> map = new AbstractMap.SimpleEntry<K, Node<K,T>>(newKey, newIndex);
			if(index==this.root){
				ArrayList<Node<K,T>> rootChild = new ArrayList<Node<K,T>>();
				rootChild.add(index);
				rootChild.add(newIndex);
				ArrayList<K> rootKey =  new ArrayList<K>();
				rootKey.add(newKey);
				IndexNode<K,T> newRoot = new IndexNode<K,T>(rootKey, rootChild);
				Entry<K, Node<K, T>> newRootEntry = new AbstractMap.SimpleEntry<K, Node<K,T>>(newKey, newRoot);
				return newRootEntry;
 			}	
			return map;
		
		
	}

	/**
	 * TODO Delete a key/value pair from this B+Tree
	 * 
	 * @param key
	 * 
	 * If root is not null and is a leaf node, search the node key list, delete the target key instantly.
	 * 
	 * If root is not null and is a index node, call indexDelte on it.
	 * If return value is bigger than -1, then that represents root key is changed or should be removed.
	 * If root key after the removal is empty, then set the new root to its child.
	 * 
	 */
	public void delete(K key) {
		Node<K,T> root = this.root;
		int index = -2;
		if(root!=null&&root.isLeafNode==false){
			index = indexDelete((IndexNode<K,T>)root, key);
			if(index>-1){
				root.keys.remove(index);
				if(root.keys.size()==0)  this.root = ((IndexNode<K,T>)root).children.get(0);
			}
		}else if(root!=null&&root.isLeafNode==true){
			for(int i = 0; i< root.keys.size();i++){
				if(key==root.keys.get(i)){
					root.keys.remove(i);
					((LeafNode<K,T>)root).values.remove(i);
					if(root.keys.size()==0) this.root = null;
				}
			}
		}
	}
	/**
	 * 
	 * @param node
	 * @param key
	 * @return
	 * 
	 * 
	 */
	
	public int indexDelete(IndexNode<K,T> node, K key){
		//search the node key list and get the matching child.
		ArrayList<K> indexKey = node.keys;
		ArrayList<Node<K, T>> indexChild = node.children;
		int index=0;
		for(int i=0; i<indexKey.size();i++){
			if(key.compareTo(indexKey.get(0))<0){
				index = 0;
				break;
			}else if(key.compareTo(indexKey.get(indexKey.size()-1))>=0){
				index = indexKey.size();
				break;
			}else if(key.compareTo(indexKey.get(i))>=0&&key.compareTo(indexKey.get(i+1))<0){
				index = i+1;
				break;
			}
		}
		Node<K,T> nextNode = indexChild.get(index);
		//If the matching child is a index node, call indexDelete recursively.
		//If the return value is not -1, that means we should remove key from the index node.
		//If the node is underflow after the removal, then select its left or right sibling node and call handleUnderflow method.
		
		//If the matching child is a leaf node, remove the key instantly.
		//If the node is underflow after the removal, then select its left or right sibling node and call handleUnderflow method.
		if(nextNode.isLeafNode==false){
			int parentIndex = indexDelete((IndexNode<K,T>)nextNode, key);
			if(parentIndex!=-1)	nextNode.keys.remove(parentIndex);
			if(nextNode.keys.size()<D){
				if(index==0){
					IndexNode<K,T> rightSibling = (IndexNode<K, T>) indexChild.get(1); 
					return handleIndexNodeUnderflow((IndexNode<K,T>)nextNode,rightSibling,(IndexNode<K,T>)node);
				}else{
					IndexNode<K,T> leftSibling = (IndexNode<K, T>) indexChild.get(index-1);
					return handleIndexNodeUnderflow(leftSibling,(IndexNode<K,T>)nextNode,(IndexNode<K,T>)node);
				}
			}else{
				return -1;
			}
		}else{
			for(int j=0;j<nextNode.keys.size();j++){
				if(nextNode.keys.get(j)==key){
					nextNode.keys.remove(j);
					((LeafNode<K,T>)nextNode).values.remove(j);
					break;
				}
			}
			LeafNode<K,T> siblingLeaf = ((LeafNode<K,T>)nextNode).previousLeaf;
			if(nextNode.keys.size()<D){
				if(index==0){
					siblingLeaf = ((LeafNode<K,T>)nextNode).nextLeaf;
					return handleLeafNodeUnderflow((LeafNode<K,T>)nextNode, siblingLeaf, (IndexNode<K, T>) node);
				}else{
					return handleLeafNodeUnderflow(siblingLeaf,(LeafNode<K,T>)nextNode,(IndexNode<K, T>) node);
				}
			}
			return -1;
		}
	}

	/**
	 * TODO Handle LeafNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 *         
	 *         
	 *         At first, judge which node is truly underflow, and whether its sibling node has extra nodes or not.
	 *         
	 *         According to the criteria, split into four situations to handle separately. 
	 */
	public int handleLeafNodeUnderflow(LeafNode<K,T> left, LeafNode<K,T> right,
			IndexNode<K,T> parent) {
		ArrayList<K> leftKey = left.keys;
		ArrayList<K> rightKey = right.keys;
		ArrayList<T> leftVal = left.values;
		ArrayList<T> rightVal = right.values;
		ArrayList<Node<K,T>> children = parent.children;
		ArrayList<K> parentKey = parent.keys;
		int index = 0, leftSize = leftKey.size(), rightSize = rightKey.size();
		int total = leftSize + rightSize;
		for(int i=0;i<children.size();i++){
			if(left==children.get(i)) index = i;
		}
		if(leftKey.size()<D && rightKey.size()>D){//redistribute
			for(int i=0;i<total/2-leftSize;i++){
					leftKey.add(leftKey.size(),rightKey.get(0));
					leftVal.add(leftVal.size(),rightVal.get(0));
					rightKey.remove(0);
					rightVal.remove(0);
				}
			parentKey.set(index, rightKey.get(0));
		}else if(rightKey.size()<D && leftKey.size()>D){//redistribute
			for(int j=0;j<leftSize-(total/2);j++){
				rightKey.add(0,leftKey.get(leftKey.size()-1));
				rightVal.add(0,leftVal.get(leftVal.size()-1));
				leftKey.remove(leftKey.size()-1);
				leftVal.remove(leftVal.size()-1);
			}
			parentKey.set(index, rightKey.get(0));
		}else if(leftKey.size()<D && rightKey.size()<=D){//merge
			for(int k=0;k<leftSize;k++){
				rightKey.add(0, leftKey.get(leftKey.size()-1));
				rightVal.add(0, leftVal.get(leftVal.size()-1));
				leftKey.remove(leftKey.size()-1);
				leftVal.remove(leftVal.size()-1);
			}
			right.previousLeaf = left.previousLeaf;
			if(left.previousLeaf!=null) left.previousLeaf.nextLeaf = right;
			children.remove(index);
			return index;
		}else if(leftKey.size()<=D && rightKey.size()<D){//merge
			for(int g=0;g<rightSize;g++){
				leftKey.add(leftKey.size(),rightKey.get(0));
				leftVal.add(leftVal.size(), rightVal.get(0));
				rightKey.remove(0);
				rightVal.remove(0);
			}
			left.nextLeaf = right.nextLeaf;
			if(right.nextLeaf!=null) right.nextLeaf.previousLeaf = left;
			children.remove(index+1);
			return index;
		}
		return -1;

	}

	/**
	 * TODO Handle IndexNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 *         
	 *         At first, judge which node is truly underflow, and whether its sibling node has extra nodes or not.
	 *         
	 *         According to the criteria, split into four situations to handle separately. 
	 */
	public int handleIndexNodeUnderflow(IndexNode<K,T> leftIndex,
			IndexNode<K,T> rightIndex, IndexNode<K,T> parent) {
		ArrayList<K> leftKey = leftIndex.keys;
		ArrayList<K> rightKey = rightIndex.keys;
		ArrayList<Node<K,T>> leftChild = leftIndex.children;
		ArrayList<Node<K,T>> rightChild = rightIndex.children;
		ArrayList<Node<K,T>> children = parent.children;
		ArrayList<K> parentKey = parent.keys;
		int index = 0, leftSize = leftKey.size(), rightSize = rightKey.size();
		int total = leftSize + rightSize;
		for(int i=0;i<children.size();i++){
			if(children.get(i)==leftIndex) index = i; 
		}
		
		if(leftKey.size()<D && rightKey.size()>D){//redis
			for(int i=0;i<total/2-leftSize;i++){
				leftKey.add(leftKey.size(),parentKey.get(index));
				parentKey.set(index,rightKey.get(0));
				leftChild.add(leftChild.size(),rightChild.get(0));
				rightKey.remove(0);
				rightChild.remove(0);
			}
		}else if(rightKey.size()<D && leftKey.size()>D){//redis
			for(int j=0;j<leftSize-(total/2);j++){
				rightKey.add(0,parentKey.get(index));
				parentKey.set(index, leftKey.get(leftKey.size()-1));
				rightChild.add(0,leftChild.get(leftChild.size()-1));
				leftKey.remove(leftKey.size()-1);
				leftChild.remove(leftChild.size()-1);
			}
	    }else if(leftKey.size()<D && rightKey.size()<=D){//merge
	    	rightKey.add(0, parentKey.get(index));
	    	rightChild.add(0, leftChild.get(leftChild.size()-1));
	    	leftChild.remove(leftChild.size()-1);
	    	for(int k=0;k<leftSize;k++){
				rightKey.add(0, leftKey.get(leftKey.size()-1));
				rightChild.add(0, leftChild.get(leftChild.size()-1));
				leftKey.remove(leftKey.size()-1);
				leftChild.remove(leftChild.size()-1);
	    	}
			children.remove(index);//merge into one node,so delete one child index.
			return index;
		}else if(rightKey.size()<D && leftKey.size()<=D){//merge
			leftKey.add(leftKey.size(), parentKey.get(index));
			leftChild.add(leftChild.size(), rightChild.get(0));
			rightChild.remove(0);
			for(int g=0;g<rightSize;g++){
				leftKey.add(leftKey.size(),rightKey.get(0));
				leftChild.add(leftChild.size(), rightChild.get(0));
				rightKey.remove(0);
				rightChild.remove(0);
			}
			children.remove(index+1);//merge into one node, so delete one child index.
			return index;
		}
		
		return -1;
	}

}
