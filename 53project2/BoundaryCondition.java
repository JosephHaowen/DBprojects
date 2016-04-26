import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.Counter;


public class BoundaryCondition implements Writable{
    private int src;
    private int des;
    private double r;

    public BoundaryCondition{

    }

    public BoundaryCondition(int src, int des, double r) {
        this.src = src;
        this.des = des;
        this.r = r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public Double getR(){
        return r;
    }

    public int getSrc(){
        return src;
    }

    public int getDes(){
        return des;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        src = in.readInt();
        des = in.readInt();
        r = in.readDouble();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(src);
        out.writeInt(des);
        out.writeInt(r);
    }

}