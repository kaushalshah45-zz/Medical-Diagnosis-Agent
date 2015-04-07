package Ontology;

import java.util.ArrayList;

public class Graph 
{
	private final int V;
	
	private final ArrayList<Integer>[] adj;
	
	public Graph(int V)
	{
	this.V = V;
	adj = (ArrayList<Integer>[]) new ArrayList[V];
	for (int v = 0; v < V; v++)
	adj[v] = new ArrayList<Integer>();
	}
	
	public void addEdge(int v, int w)
	{
	adj[v].add(w);
	
	}
	
	public Iterable<Integer> adj(int v)
	{
		return adj[v];
	}

}
