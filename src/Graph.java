import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;

public class Graph {
	ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>();
	Hashtable<Integer, String> ht = new Hashtable<Integer, String>();
    Hashtable<String, Integer> htrev = new Hashtable<String, Integer>();
    ArrayList<Integer> weights = new ArrayList<Integer>();
	public Graph(String fileName) {
		this.ht = readVertices(fileName);
		this.htrev = verticesRev(ht);
		this.adj = makeGraph(fileName, htrev);
		for (int el : this.ht.keySet()) {
			weights.add(0);
		}
	}
	
	
    public static boolean hasEdge(ArrayList<ArrayList<Integer>> graph, int u, int v) { 
    	if(graph.get(u).contains(v) || graph.get(v).contains(u)) {
    		return true;
    	}
    	return false;
    } 
    
    //removes edge from ArrayList
	public static void removeEdge(ArrayList<ArrayList<Integer>> adj, int i, int j){
		if(!this.fixed.get(i).contains(j) && !this.fixed.get(i).contains(j)) {
			Iterator<Integer> it1 = adj.get(i).iterator();
			while(it1.hasNext()){
				if (it1.next()==j){
					it1.remove();
				}
			}
			Iterator<Integer> it2 = adj.get(j).iterator();
			while(it2.hasNext()){
				if (it2.next()==i){
					it2.remove();
				}
			}
		}
	
	
	//adds edge to ArrayList
    public static void addEdge(ArrayList<ArrayList<Integer>> adj, int u, int v) { 
    		adj.get(u).add(v); 
    		adj.get(v).add(u); 
    }
    
	//
    public static void addEdge(ArrayList<ArrayList<Integer>> adj, int u, int v) { 
    		adj.get(u).add(v); 
    		adj.get(v).add(u); 
    }
    
	//reads vertices in order of occurence into Hashtable
    public static Hashtable<Integer, String> readVertices(String fileName) {
    	Hashtable<Integer, String> ht = new Hashtable<Integer, String>();
	    int i = 0;
		try {
			Scanner graphReader = new Scanner(new File(fileName));
			while (graphReader.hasNextLine()) {
				String data[] = graphReader.nextLine().trim().replaceAll("\\s+", " ").split(" ", 3);
			    if (data[0].startsWith("#") || data.length < 2) continue;
			    String start = data[0];
			    String end = data[1];
			    if(!ht.contains(start)) {
			       	ht.put(i, start);
			       	i++;
			    }	        
			    if(!ht.contains(end)) {
			       	ht.put(i, end);
			       	i++;
			    }
			}
		    graphReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	   return ht;
    }
    
    // creates inverted Hashtable translate integers back to vertex names
    public static Hashtable<String, Integer> verticesRev(Hashtable<Integer, String> ht) {
    	Hashtable<String, Integer> htrev = new Hashtable<String, Integer>();
    	for (int i = 0; i < ht.size(); i++) {
         	htrev.put(ht.get(i), i);
    	}
    	return htrev;
    }
    
    //reads Graph into ArrayList
    public static ArrayList<ArrayList<Integer>> makeGraph(String fileName, Hashtable <String, Integer> ht) {
	    ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>();
    	for (int i = 0; i < ht.size(); i++) {
    		adj.add(new ArrayList<Integer>()) ;
    	}
        Scanner graphReader;
		try {
			graphReader = new Scanner(new File(fileName));
			while (graphReader.hasNextLine()) {
				String data[] = graphReader.nextLine().trim().replaceAll("\\s+", " ").split(" ", 3);
				if (data.length < 2 || data[0].startsWith("#")) continue;
				String start = data[0];
				String end = data[1];	
				addEdge(adj, ht.get(start), ht.get(end));
			}
		    graphReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	return adj;
    }
    
    public void printGraph() {
    	System.out.println(this.adj);
    	System.out.println(this.weights);
    }
}
