import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;

//write out the pagerank;
//write out the OutEdge list in the block;
//write out the boundary condition;
public class BlockedMapper extends Mapper<IntWritable, Node, IntWritable, NodeBeBc> {

    public void map(IntWritable key, Node value, Context context) throws IOException, InterruptedException {

        int block = value.getBlockID();

        for(int n: value.getNeighbors()){

            int nblock = Block.getInstance().getBlock();

            if(nblock == block){
                
                BlockEdge be = new BlockEdge(value.getNodeID(),n);
                NodeBeBc nbe = new NodeBeBc(be);
                context.write(new IntWritable(block),nbe);
            } else {

                double r = value.getPageRank()/value.getDegree();
                BoundaryCondition bc = new BoundaryCondition(value.getNodeID(), n, r);
                NodeBeBc nbc = new NodeBeBc(bc);
                context.write(new IntWritable(nblock),nbc);

            }

        }

        context.write(new IntWritable(block), new NodeBeBc(value));
    }
}
