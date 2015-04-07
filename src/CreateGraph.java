package Ontology;

import java.util.ArrayList;
import java.util.Iterator;

public class CreateGraph 
{
	Graph g = new Graph(23);
	Node [] n = OntologyConnect.generateNodes();
	public CreateGraph()
	{
		
		for(int j = 0;j < n.length; j++)
		{
			if(!n[j].get_given_name().contains("Disorder"))
			{
				String s = n[j].get_leads_to();
				int node1 = n[j].get_node_no();
				int node2 = -1;
				for(int i = 0;i < n.length; i++)
				{
					String ss = n[i].get_given_name();
					if(ss.equals(s))
					{
						node2 = n[i].get_node_no();
					}
				}
				if(node2 > 0)
				{
				
				g.addEdge(node1, node2);
				}
			
			
			//System.out.println(j+n[j].get_given_name());
			
				   String f = n[j].get_also_leads_to();
				   int node11 = n[j].get_node_no();
					int node22 = -1;
					//System.out.print(f+node1);
					for(int i = 0;i < n.length; i++)
					{
						String sss = n[i].get_given_name();
						
						if(sss.equalsIgnoreCase(f))
						{
							node22 = n[i].get_node_no();
						}
					}
					if(node22 > 0)
					{
					
					g.addEdge(node11, node22);
					}
				   
			}
		}
	}
	
	/*
	public static void main(String [] args)
	{
		CreateGraph c = new CreateGraph();
		ArrayList a = (ArrayList) c.g.adj(6);
		System.out.println(a.get(1));
	}
	*/
}


