import java.io.IOException;
import java.io.DataOutputStream;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;


public class NodeRecordWriter extends RecordWriter<IntWritable, Node> {

    private DataOutputStream out; 

    public NodeRecordWriter(DataOutputStream out) throws IOException{
        this.out = out;
    }

    public synchronized void write(IntWritable key, Node value) throws IOException {
            
            System.out.println("");
            boolean keyNull   = key == null;
            boolean valueNull = value == null;
            
            if(valueNull) { //Can't write a Null value
                return;
            }
            if(keyNull) {
                write(new IntWritable(value.getNodeID()), value); //If we have a null key, then just use the Node's nodeid
            }

            String nodeInfo = formatNode(value);

            //key.toString() + " " + value.getPageRank() + " "; //We can just output the nodeid and PageRank space-seperated
            //String outGoing = "";
            //for(Integer n : value) {//for outgoing edges, we need to comma-seperate them
            //    outGoing += n.toString() + ",";
           // }
            //if(!outGoing.equals("")) nodeRep += outGoing.substring(0,outGoing.length()-1); //If there are outgoing nodes, we need to get rid of the trailing comma
            System.out.println(nodeInfo + " IN THIS PLACE \n");
            out.writeBytes(nodeInfo + "\n");//And write out the resulting record!
            out.flush();
    }

    public synchronized void close(TaskAttemptContext ctxt) throws IOException{
        out.close();
    }


    public String formatNode(Node node){
        String res = node.getNodeID() + " " + node.getBlockID() + " " + node.getPageRank() + " ";
        
        List<Integer> nodes = node.getNeighbors();

        for(int i=0;i<nodes.size();i++){  

            int cur = nodes.get(i);

            if(i==nodes.size()-1){
                res += cur;
            } else {
                res += cur + ",";
            }
        }

        return res.trim();
        

    }
}
