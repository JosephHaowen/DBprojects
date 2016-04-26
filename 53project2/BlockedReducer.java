import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.util.*;

public class BlockReducer extends Reducer<IntWritable, NodeBeBc, IntWritable, Node>{
	int curBlock;
	final double d = 0.85;
	Map<Node, Double> startPR;
	Map<Integer, Node> nodesInBlock;

	Map<Integer, List<BlockEdge>> mapBE;
	Map<Integer, List<BoundaryCondition>> mapBC;
	final int iterationNum = 5;


	public void reduce(IntWritable key, Iterable<NodeBeBc> value, Context context) throws IOException, InterruptedException{		
			curBlock = key.get();
			startPR = new HashMap<> ();
			nodesInBlock = new HashMap<> ();
			mapBE = new HashMap<> ();
			mapBC = new HashMap<> ();

			for(NodeBeBc cur: value){

				char type = curNode.getType();

				if(type == 'N')){
					
					Node curNode = cur.getNode();
					nodesInBlock.put(curNode.getNodeID(), curNode);
					startPR.put(curNode, curNode.getPageRank());
				
				} else if(type == 'E'){

					BlockEdge curBE = cur.getBE();
					int des = curBE.getDes();
					List<BlockEdge> list = mapBE.get(des);

					if(list==null) list = new ArrayList<> ();
						
					list.add(curBE);
					mapBE.put(des, list);
				
				} else if(type == 'C'){

					BoundaryCondition curBC = cur.getBC();
					int des = curBC.getDes();
					List<BoundaryCondition> list = mapBC.get(int);

					if(list==null) list = new ArrayList<> ();

					list.add(curBC);
					mapBC.put(des,list);

				}

			}

			int times = 1;
			double residual = 0.0;
			iterateBlockOnce(nodesInBlock);
			while((residual = computeResidual(nodesInBlock))>PageRank.ERROR_THRESH && times<iterationNum){
				times++;
				iterateBlockOnce(nodesInBlock);

			}

			for(Node node: nodesInBlock.values()){

				context.write(new IntWritable(node.getNodeID()), node);
			}

			long blockResidual = (long)Math.floor(residual*Math.pow(10,6));
			context.getCounter(Counter.BLOCK_RESIDUAL).increment(blockResidual);
			context.getCounter(Counter.ITERATION_NUM).increment(times);





			//left pagerank counter part.



		}

		public void iterateBlockOnce(Map<Integer, Node> nodesBlock){
				List<Node> nodes = new ArrayList<> (nodesBlock.values());

				Double[] npr = new Double[nodes.size()];

				for(int i=0;i<nodes.size();i++){
					Node cur = nodes.get(i);
					int curid = cur.getNodeID();

					if(mapBE.containsKey(curid)){
						for(BlockEdge be:mapBE.get(curid)){
							
							Node src = nodesBlock.get(be.getSrc());
							npr[i] += src.getPageRank()/src.getDegree();


						}

					}

					if(mapBC.containsKey(curid)){
						for(BoundaryCondition bc:mapBC.get(curid)){

							npr[i] += bc.getR();

						}

					}

					npr[i] = d*npr[i] + (1.0-d)/PageRank.NUM_OF_NODES;


				}

				for(int i=0;i<nodes.size();i++){
					Node cur = nodes.get(i);
					cur.setPageRank(npr[i]);
					nodesBlock.put(cur.getNodeID(), cur);

				}
		}

		public double computeResidual(Map<Integer, Node> nodesBlock){

			double residual = 0.0;
			for(Node n:nodesBlock.values()){

				double curPR = n.getPageRank();
				double initPR = startPR.get(n);
				residual += Math.abs(curPR - initPR)/curPR;
			}

			return residual/nodesBlock.size();

		}








}
