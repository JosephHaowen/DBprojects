import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.util.*;

//Based off of the code at http://developer.yahoo.com/hadoop/tutorial/module5.html
public class NodeRecordReader extends RecordReader<IntWritable, Node> {
    private LineRecordReader lineReader; //Does the actual reading for us
    private LongWritable lineKey; // The key from lineReader
                                  // Contains the line number we're reading from
    private Text lineValue; // The value from lineReader
                            // Contains the text from the line we're reading
    private IntWritable curKey; // The current key of our NodeRecordReader, the current Node's nodeid
    private Node curVal; // The current value of our NodeRecordReader, the current Node
    private long end = 0, start = 0, pos = 0, maxLineLength; //Line tracking variables


    public void initialize(InputSplit genericSplit, TaskAttemptContext context)throws IOException, InterruptedException {
        //To initialize, create and initialize our lineReader
        lineReader = new LineRecordReader();
        lineReader.initialize(genericSplit, context); 
    }

    public boolean nextKeyValue() throws IOException {
        if(!lineReader.nextKeyValue()) {//If we're out of lines, we're done
            return false;
        }
        //get the correct current key and value
        lineKey = lineReader.getCurrentKey();
        lineValue = lineReader.getCurrentValue();

        //The line needs to be split into 3 pieces: the nodeid, the current PageRank, and the optional list of outgoing links
        String[] pieces = lineValue.toString().split("\\s+");
        if(pieces.length < 3 || pieces.length > 4 ) {
            throw new IOException("Given poorly-formatted record: " + lineValue.toString()); //If this isn't what's going on, then we need to report an error.
        }
        
        int nodeID = Integer.parseInt(pieces[0]);
        int blockID = Integer.parseInt(pieces[1]);
        double value = Double.parseDouble(pieces[2]);
        Node cur = new Node(nodeID, blockID, value);

        if(pieces.legnth==4){

            String[] neighbors = pieces[3].split(",");

            for(int i=0;i<neighbors.length;i++){

                int nid = Integer.parseInt(neighbors[i].trim());
                cur.addNeighbors(nid);
            }
        }

        curKey = new IntWritable(nodeID);
        curVal = cur;
        return true;
    }
                

    public IntWritable getCurrentKey() {
        return curKey;
    }

    public Node getCurrentValue() {
        return curVal;
    }
    
    public void close() throws IOException {
        lineReader.close();
    }

    public float getProgress() throws IOException {
        return lineReader.getProgress();
    }
}