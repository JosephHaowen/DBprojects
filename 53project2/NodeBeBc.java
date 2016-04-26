import org.apache.hadoop.io.Writable;
import java.io.*;

public class NodeBeBc implements Writable{
    private Node n;
    private BoundaryCondition bc;
    private BlockEdge edge;
    private char type;

    public NodeBeBc(){

    }

    public NodeBeBc(Node n) {
        this.n = n;
        type = 'N';
    }

    public NodeBeBc(BoundaryCondition bc) {
        this.bc = bc;
        type = 'C';
    }

    public NodeBeBc(BlockEdge edge) {
        this.edge = edge;
        type = 'E';
    }

    public char getType() {
        return type;
    }

    public Node getNode() {
        return n;
    }
    
    public BoundaryCondition getBC(){
        return bc;
    }

    public BlockEdge getBE(){
        return edge;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        type = in.readChar();
        if (type == 'N'){
            n = new Node();
            n.readFields(in);
            bc = null;
            edge =null;
        }else if (type == 'C'){
            bc = new BoundaryCondition();
            bc.readFields(in);
            n = null;
            edge = null;
        }else if (type == 'E'){
            edge = new Edge();
            edge.readFields(in);
            n = null;
            bc = null;
        }
        
    }
    @Override
    public void write(DataOutput out) throws IOException {
        
        out.writeChar(type);
        if(type == 'N'){
            n.write(out);
        } else if (type == 'C'){
            bc.write(out);
        } else if (type == 'E'){
            edge.write(out);
        }
    }

}
