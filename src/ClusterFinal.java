import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ClusterFinal
{
	
	public static void main(String[] args) {
		if (args.length >= 0) { //change to > for .jar
			String fileName = /*args[0];//*/"k_004_n_215.txt"; //change to args[0] for jar
			Graph g = new Graph(fileName);
			g.printGraph();
			Hashtable<Integer, String> ht = readVertices(fileName);
		    Hashtable<String, Integer> htrev = verticesRev(ht);
			ArrayList<ArrayList<Integer>> graph = makeGraph(fileName, htrev);
			//makeClusterGraph(graph, ht);
			/*System.out.print(htrev.get("C2_6") + " ");
			System.out.println(htrev.get("C2_27"));
			System.out.print(htrev.get("C2_8") + " ");
			System.out.println(htrev.get("C6_1"));
			System.out.print(htrev.get("C9_9") + " ");
			System.out.println(htrev.get("C9_11"));
			System.out.print(htrev.get("C9_10") + " ");
			System.out.println(htrev.get("C8_8"));
			System.out.println(graph.get(3));
			
			System.out.println(graph.get(94));
			System.out.println(graph.get(1));
			System.out.println(graph.get(156));
			/*
			C2_6 C2_27
			C2_8 C6_1
			C9_9 C9_11
			C9_10 C8_8*/
		}
	}

	//adds edge to ArrayList
    public static void addEdge(ArrayList<ArrayList<Integer>> adj, int u, int v) { 
    		adj.get(u).add(v); 
    		adj.get(v).add(u); 
    }
    
    public static boolean hasEdge(ArrayList<ArrayList<Integer>> graph, int u, int v) { 
    	if(graph.get(u).contains(v) || graph.get(v).contains(u)) {
    		return true;
    	}
    	return false;
    } 
    
    //removes edge from ArrayList
	public void removeEdge(ArrayList<ArrayList<Integer>> adj, int i, int j){
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
    /*
    // returns first P3 found in graph
    public static int[] p3(ArrayList<ArrayList<Integer>> graph) {
    	int[] inDeg = new int[graph.size()];
    	for (int u = 0; u < graph.size(); u++) {
    		inDeg[u] += graph.get(u).size();
    	}
    	for (int u = 0; u < graph.size(); u++) {
    		boolean allSame = true;
    		for(int v : graph.get(u)) {
    			if (inDeg[u] != inDeg[v]) {
    				allSame = false;
    				for (int w : graph.get(v)) {
    					if (!hasEdge(graph, u, w)) {
    						int[] e = {u, v, w};
        					return e;
    					}
    				}
    			}	
    		}
    		removeVertex(graph, u)
    		for(int v : graph.get(u)) {
    			removeVertex(graph);
    		}
    		
    	}
    	for (int u = 0; u < graph.size(); u++) {
    		for (int v : graph.get(u)) {
    			for (int w : graph.get(v)) {
    				if (hasEdge(graph, u, v) && hasEdge(graph, v, w) && !hasEdge(graph, u, w)) {
    					int[] e = {u, v, w};
    					return e;
    				}
    			}
    		}
    	}
    	return null;
    }*/
    
    //tries to turn graph into clustergraph with k edge removals/adds
    public static ArrayList<int[]> clusterInK(ArrayList<ArrayList<Integer>> graph, int k) {
    	if (k < 0) return null;
    	ArrayList<int[]> S = new ArrayList<int[]>();
    	int[] p3 = p3(graph);
    	if (p3 == null) return S;
    
       	removeEdge(graph, p3[0], p3[1]);
        S = clusterInK(graph, k - 1);
       	if (S != null) {
       		int[] e = {p3[0], p3[1]};
       		S.add(e);
       		return S;
       	}
       	addEdge(graph, p3[0], p3[1]);
       	
       	removeEdge(graph, p3[1], p3[2]);
       	S = clusterInK(graph, k - 1);
       	if (S != null) {
       		int[] e = {p3[1], p3[2]};
       		S.add(e);
       		return S;
       	}
       	addEdge(graph, p3[1], p3[2]);    
       	
       	addEdge(graph, p3[0], p3[2]);
       	S = clusterInK(graph, k - 1);
       	if (S != null) {
       		int[] e = {p3[0], p3[2]};
       		S.add(e);
       		return S;
       	}
       	removeEdge(graph, p3[0], p3[2]); 
       	return null;
   	}
    
    //loops over k until graph is clusterGraph and prints edges
    public static void makeClusterGraph(ArrayList<ArrayList<Integer>> graph, Hashtable<Integer, String> htrev) {
    	int k = 0;
    	ArrayList<int[]> edges = null;
    	while(k < graph.size()) {
    		edges = clusterInK(graph, k);
    		if(edges != null) {
    			for(int[] el : edges) {
    				System.out.println(htrev.get(el[0]) + " " + htrev.get(el[1]));    			
    			}
    		}
    		k++;
    	}
    }       
}