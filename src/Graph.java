import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;

public class Graph {
	ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>();
	Hashtable<Integer, String> ht = new Hashtable<Integer, String>();
    Hashtable<String, Integer> htrev = new Hashtable<String, Integer>();
    ArrayList<int[]> prot = new ArrayList<int[]>();
    ArrayList<Integer> indegree = new ArrayList<Integer>();
	public Graph(String fileName) {
		this.ht = readVertices(fileName);
		this.htrev = verticesRev(ht);
		this.adj = makeGraph(fileName, htrev);
		for (ArrayList<Integer> v : adj) {
			Collections.sort(v);
			indegree.add(v.size());
		}
	}
	
	public void protectEdge(int u, int v) {
		if (u < v) {
			prot.add(new int[] {u,v});
		}
		else {
			prot.add(new int[] {v, u});
		}
	}
	
	public boolean protectedEdge(int u, int v) {
		return prot.contains(new int[] {u, v}) || prot.contains(new int[] {v, u});
	}
    public boolean hasEdge(ArrayList<ArrayList<Integer>> graph, int u, int v) { 
    	if(graph.get(u).contains(v) || graph.get(v).contains(u)) {
    		return true;
    	}
    	return false;
    } 
    
    //removes edge from ArrayList
	public void removeEdge(ArrayList<ArrayList<Integer>> adj, int i, int j){
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
    public void addEdge(ArrayList<ArrayList<Integer>> adj, int u, int v) { 
    		adj.get(u).add(v); 
    		adj.get(v).add(u); 
    }
    
	//

    
	//reads vertices in order of occurence into Hashtable
    public Hashtable<Integer, String> readVertices(String fileName) {
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
    public Hashtable<String, Integer> verticesRev(Hashtable<Integer, String> ht) {
    	Hashtable<String, Integer> htrev = new Hashtable<String, Integer>();
    	for (int i = 0; i < ht.size(); i++) {
         	htrev.put(ht.get(i), i);
    	}
    	return htrev;
    }
    
    //reads Graph into ArrayList
    public ArrayList<ArrayList<Integer>> makeGraph(String fileName, Hashtable <String, Integer> ht) {
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
    	System.out.println(this.indegree);
    }
    
    // returns first P3 found in graph
    public int[] p3(ArrayList<ArrayList<Integer>> graph) {
    	for (int u = 0; u < graph.size(); u++) {
    		for (int v : graph.get(u)) {
    			for (int w : graph.get(v)) {
    				if (u!=w && !graph.get(u).contains(w)) {
    					int[] e = {u, v, w};
    					return e;
    				}
    			}
    		}
    	}
    	return null;
    }
    
    public int[] p3alt(Graph g) {
    	Graph copy = g;
    	for(int u = 0; u < copy.adj.size(); u++) {
    		boolean isClique = true;
    		for(int v : copy.adj.get(u)) {
    			isClique = isClique && (indegree.get(u) == indegree.get(v));
    		}
    		if(isClique) {
    			indegree.set(u, 0);
    			for(int v : copy.adj.get(u)) {
        			indegree.set(v, 0);
        		}
    		}
    		else {
    			for(int v : copy.adj.get(u)) {
        			if (indegree.get(u) != indegree.get(v)) {
        				for(int w : copy.adj.get(v)) {
        					if (!copy.adj.get(u).contains(w) && u != w) {
        						int[] e = {u, v, w};
        						return e;
        					}
        				}
        			}
        		}
    		}
    	}
    	return null;	
    }
    
    //tries to turn graph into clustergraph with k edge removals/adds
    public ArrayList<int[]> clusterInK(Graph graph, int k) {
    	if (k < 0) return null;
    	ArrayList<int[]> S = new ArrayList<int[]>();
    	int[] p3 = p3alt(graph);
    	if (p3 == null) return S;
       	if (!graph.protectedEdge(p3[0], p3[1])) {
	    	removeEdge(graph.adj, p3[0], p3[1]);
	        S = clusterInK(graph, k - 1);
	       	if (S != null) {
	       		int[] e = {p3[0], p3[1]};
	       		S.add(e);
	       		return S;
	       	}
	       	addEdge(graph.adj, p3[0], p3[1]);
	       	graph.protectEdge(p3[0], p3[1]);
       	}
       	
       	if (!graph.protectedEdge(p3[1], p3[2])) {
	       	removeEdge(graph.adj, p3[1], p3[2]);
	       	S = clusterInK(graph, k - 1);
	       	if (S != null) {
	       		int[] e = {p3[1], p3[2]};
	       		S.add(e);
	       		return S;
	       	}
	       	addEdge(graph.adj, p3[1], p3[2]);
	       	graph.protectEdge(p3[1], p3[2]);
       	}
       	
       	if (!graph.protectedEdge(p3[0], p3[2])) {
	       	addEdge(graph.adj, p3[0], p3[2]);
	       	S = clusterInK(graph, k - 1);
	       	if (S != null) {
	       		int[] e = {p3[0], p3[2]};
	       		S.add(e);
	       		return S;
	       	}
	       	removeEdge(graph.adj, p3[0], p3[2]);
	       	graph.protectEdge(p3[0], p3[2]);
       	}
       	return null;
   	}
    
    //loops over k until graph is clusterGraph and prints edges
    public void makeClusterGraph(Graph graph, Hashtable<Integer, String> htrev) {
    	int k = 0;
    	ArrayList<int[]> edges = null;
    	while(k < graph.adj.size()) {
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
