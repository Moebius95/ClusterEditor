import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ClusterFinal
{
	public static void main(String[] args) {
		if (args.length > 0) { //change to > for .jar
			String fileName = args[0];//*/"k_007_n_151.txt"; //change to args[0] for jar
			Graph g = new Graph(fileName);
			g.makeClusterGraph(g, g.ht);
		}
	}     
}