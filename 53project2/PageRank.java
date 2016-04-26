import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.conf.Configuration;

public class PageRank {

	private static final double NET_ID = 0.893;
	private static final double REJECT_MIN = 0.99*NET_ID;
	private static final double REJECT_LIMIT = REJECT_MIN + 0.01;

	public static final int NUM_OF_NODES = 685230;
	public static final double ERROR_THRESH = 0.001;

	public static final int PASSES_NUM = 10;
	public static final int BLOCK_NUM = 68;
/*	
	static final double FROM_NET_ID = 0.734;
	static final double REJECT_MIN = 0.99*FROM_NET_ID;
	static final double REJECT_LIMIT = REJECT_MIN + 0.01;
	static final int NUM_NODES = 685230;
	static final int NUM_BLOCKS = 68;
	static final double THRESHOLD = 0.001; 
*/	
	public static final double LARGE_NUM = Math.pow(10, 5);

	
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Job job = null;
		String inputPath = null;
		String outputPath = null;

		for(int i = 0; i<PASSES_NUM;i++){

			if (i==0){

				job = getInitJob();
				inputPath = "input";

			} else {

				job = getComputeJob(i);
				inputPath = "stage" + (i-1); 
			}

			outputPath = "stage" + i;

			FileInputFormat.addInputPath(job, new Path(inputPath));
            FileOutputFormat.setOutputPath(job, new Path(outputPath));


            try { 
                job.waitForCompletion(true);
            } catch(Exception e) {
                System.err.println("ERROR IN JOB: " + e);
           
            }


			Counter residualCounter = job.getCounters().findCounter(Counter.BLOCK_RESIDUAL);
			Counter iterationCounter = job.getCounters().findCounter(Counter.ITERATION_NUM);

			double globalResidual = (residualCounter.getValue()/Math.pow(10,6))/BLOCK_NUM;

			int globalIteration = iterationCounter.getValue()/BLOCK_NUM;


			if(globalResidual <= ERROR_THRESH) break;

			job.getCounters().findCounter(Counter.BLOCK_RESIDUAL).setValue(0L);

		}
/*

			if (args.length > 0){
				inputPath = args[0] + inputPath;
				outputPath = args[0] + outputPath;
			}

			if(i != 0){
			double totvariance = job.getCounters().findCounter(PageRankCounter.TOTAL_VARIANCE).getValue();
		    double average_variance  = (totvariance/(LARGE_NUM))/NUM_BLOCKS;
		    int average_iteration = (int) job.getCounters().findCounter(PageRankCounter.ITERATION_COUNTER)
		    		.getValue();

		    System.out.println("Residual error: " + average_variance);
		    System.out.println("Average Block Iteration: " + average_iteration/NUM_BLOCKS);
		    if(PageRank.THRESHOLD >= average_variance){
		    	break;
		    }
			}
*/
			
		}
	}

	public static Job getInitJob() throws IOException{
		Configuration conf = new Configuration();
		Job job = new Job(conf, "InitPR");

		job.setJarByClass(PageRank.class);
		
		job.setMapperClass(InitMapper.class);
        job.setReducerClass(InitReducer.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Node.class); //Text.class ?? 

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(NodeOrDouble.class); //IntWritable ?? 

        job.setInputFormatClass(NodeInputFormat.class);
        job.setOutputFormatClass(NodeOutputFormat.class);



		return job;
	}

	public static Job getComputeJob(int i) throws IOException{
		Configuration conf = new Configuration();
		Job job = new Job(conf, "ComputePR: "+i);

		job.setJarByClass(PageRank.class);
		
		job.setMapperClass(BlockedMapper.class);
        job.setReducerClass(BlockedReducer.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Node.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(NodeBeBc.class);

        job.setInputFormatClass(NodeInputFormat.class);
        job.setOutputFormatClass(NodeOutputFormat.class);




	
	
}