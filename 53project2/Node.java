import java.util.*;
import java.io.*;
import org.apache.hadoop.io.Writable;

public class Node implements Iterable<Integer>, Writable{
    
    private int nodeID;
    private int blockID;
    private double pr;
    private List<Integer> neighbors;

    public Node(){

    }

    public Node(int nodeID){

    	this.nodeID = nodeID;
    	this.blockID = Block.getInstance().getBlock(nodeID);
    	this.pr = (double) 1.0/PageRank.NUM_OF_NODES;
    	this.neighbors = new ArrayList<> ();
  
    }

    public Node(int nodeID, int blockID, double value){
        this.nodeID = nodeID;
        this.blockID = blockID;
        this.pr = value;
        this.neighbors = new ArrayList<> ();
    }

    public int getNodeID(){
    	return nodeID;
    }

    public int getBlockID(){
    	return blockID;
    }

    public double getPageRank(){
    	return pr;
    }

    public void setPageRank(double pr){
    	this.pr = pr;
    }

    public void addNeighbors(int n){
    	this.neighbors.add(n);
    }


    public List<Integer> getNeighbors(){
    	return neighbors;
    }

    public int getDegree(){
    	return neighbors.size();
    }

    @Override
    public boolean equals(Object obj){
    	if(this == obj) return true;
    	if(obj == null) return false;
    	Node n = (Node)obj;
    	return this.getNodeID() == n.getNodeID();
    }

    @Override
    public int hashCode(){
    	return this.getNodeID().hashCode();
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        nodeID = in.readInt();
        blockID = in.readInt();
        pr = in.readDouble();
        int next = in.readInt();
        neighbors = new ArrayList<> ();
        while (next != -1){
            this.addNeighbors(next);
            next = in.readInt();
        }
        
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.getNodeID());
        out.writeInt(this.getBlockID());
        out.writeDouble(this.getPageRank());
        for (Integer n : this.getNeighbors()){
            out.writeInt(n);
        }
        out.writeInt(-1);
        
    }




}