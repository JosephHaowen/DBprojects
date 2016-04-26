import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class BlockEdge implements Writable {

	private int src;
	private int des;

	public BlockEdge(){}

	public BlockEdge(int src, int des){
		this.src = src;
		this.des = des;
	}
	public Node getDes(){
		return des;
	}
	public Node getSrc(){
		return src;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		src = in.readInt();
		des = in.readInt();
		
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(src);
		out.writeInt(des);
	}

}