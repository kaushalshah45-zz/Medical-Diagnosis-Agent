package Ontology;

import java.util.ArrayList;
import java.util.Scanner;

public class Search 
{
	Scanner sc = new Scanner(System.in);
	CreateGraph nodeGraph = new CreateGraph();
	Graph graph  = nodeGraph.g;
	Node [] nodes = nodeGraph.n;
	boolean diabetic;
	Heuristic h;
	String symptom1, symptom2, symptom3;
	public int findNodeNo(String name)
	{
		int node_no = -1;
		for(int i = 0;i < nodes.length;i++)
		{
			String nodeName = nodes[i].get_name();
			if(nodeName.equals(name))
			{
				node_no = nodes[i].get_node_no();
			}
		}
		
		return node_no;
		
	}
	
	public ArrayList<Integer> getConnectedNodes(String name)
	{
		int nodeNo = findNodeNo(name);
		ArrayList<Integer> adj =(ArrayList<Integer>) graph.adj(nodeNo);
		return adj;
	}
	
	public ArrayList<Integer> getConnectedNodes(int nodeNo)
	{
		ArrayList<Integer> adj =(ArrayList<Integer>) graph.adj(nodeNo);
		return adj;
	}
	
	public ArrayList<Integer> findPath(String lowLevelSymptom)
	{
		ArrayList<Integer> path = new ArrayList<Integer>(nodes.length);
		String middle = null;
		for(int j = 0;j < nodes.length; j++)
		{
			if(nodes[j].get_name().equals(lowLevelSymptom))
			{
				middle = nodes[j].get_given_name();
			}
		}
		int mid_node = -1;
		if(!middle.contains("Mid"))
		{
		ArrayList<Integer> mid = getConnectedNodes(lowLevelSymptom);
		
		int [] mid_heuristic = h.midLevelHeuristic();
		
		if(mid_heuristic[0] > mid_heuristic[1])
		{
			mid_node = (Integer)mid.get(0);
			
		}
		else
		{
			mid_node = (Integer)mid.get(1);
		}
		}
		else
		{
			mid_node = findNodeNo(lowLevelSymptom);
		}
		path.add(mid_node);
		ArrayList<Integer> high = getConnectedNodes(mid_node);
		int high_node = -1;
		int [] high_heuristic = h.highLevelHeuristic();
		if(high_heuristic[0] > high_heuristic[1])
		{
			high_node = (Integer)high.get(0);
		}
		else
		{
			high_node = (Integer)high.get(1);
		}
		path.add(high_node);
		/*
		ArrayList<Integer> disorder = getConnectedNodes(high_node);
		int disorder_node = -1;
		int [] disorder_heuristic = h.disorderHeuristic();
		if(disorder_heuristic[0] > disorder_heuristic[1])
		{
			disorder_node = (Integer)disorder.get(0);
		}
		else
		{
			disorder_node = (Integer)disorder.get(1);
		}
		path.add(disorder_node);
		*/
		return path;
	}
	
	public String [] getMoreSymptoms(ArrayList path)
	{
		String [] more_symptoms = new String[2];
		int l = 0;
		for(int i = 0;i < path.size(); i++)
		{
			int k = (Integer)path.get(i);
			for(int j = 0; j < nodes.length; j++)
			{
				if(k == nodes[j].get_node_no())
				{
					
					more_symptoms[l++] = nodes[j].get_name();
					//System.out.println(nodes[j].get_name());
				}
				
			}
			
		}
		return more_symptoms;
	}
	
	public int getHighLevelSymptomNode(String lowLevelSymptom)
	{
		String middle = null;
		for(int j = 0;j < nodes.length; j++)
		{
			if(nodes[j].get_name().equals(lowLevelSymptom))
			{
				middle = nodes[j].get_given_name();
			}
		}
		int mid_node = -1;
		if(!middle.contains("Mid"))
		{
			ArrayList<Integer> mid = getConnectedNodes(lowLevelSymptom);
		
			int [] mid_heuristic = h.midLevelHeuristic();
			if(mid_heuristic[0] > mid_heuristic[1])
			{
				mid_node = (Integer)mid.get(0);
			
			}
			else
			{
				mid_node = (Integer)mid.get(1);
				//System.out.println(mid_node);
			}
		}
		else
		{
			mid_node = findNodeNo(lowLevelSymptom);
		}
		ArrayList<Integer> high = getConnectedNodes(mid_node);
		int high_node = -1;
		int [] high_heuristic = h.highLevelHeuristic();
		if(high_heuristic[0] > high_heuristic[1])
		{
			high_node = (Integer)high.get(0);
		}
		else
		{
			high_node = (Integer)high.get(1);
		}
				
		return high_node;
	}
	
	public String getDisorderNodes(String s1, String s2, String s3)
	{
		String disorderString = null;
		int disorderNode = -1;
		int a1 = getHighLevelSymptomNode(s1);
		int a2 = getHighLevelSymptomNode(s2);
		int a3 = getHighLevelSymptomNode(s3);
		ArrayList<Integer> a11 = getConnectedNodes(a1);
		ArrayList<Integer> a22 = getConnectedNodes(a2);
		ArrayList<Integer> a33 = getConnectedNodes(a3);
		
		ArrayList<Integer> disorder = new ArrayList<Integer>();
		disorder.addAll((ArrayList)a11);
		disorder.addAll((ArrayList)a22);
		disorder.addAll((ArrayList)a33);
		
		/*
		for(int i = 0;i < disorder.size(); i++)
		{
			for(int j = 0;j < disorder.size(); j++)
			{
				if(disorder.get(i) == disorder.get(j))
				{
					//System.out.println(disorder.get(i));
					disorderNode = disorder.get(j);
				}
					
			}
		}
		*/
		if(disorderNode == -1)
		{
			if(diabetic)
				disorderNode = disorder.get(0);
			else
				disorderNode = disorder.get(2);
		}
		for(int i = 0;i < nodes.length; i++)
		{
			if(nodes[i].get_node_no() == disorderNode)
			{
				disorderString = nodes[i].get_name();
			}
		}
		
		
		
		return disorderString;
	}
	
	public String infer(String line)
	{
		String node = null;
		if(line.contains("temp"))
		{
			for(int i = 0; i < nodes.length; i++)
			{
				if(nodes[i].get_temp().contains("98.6"))
				{
					node = nodes[i].get_name();
				}
			}
		}
		
		if(line.contains("blood") || line.contains("pressure"))
		{
			for(int i = 0; i < nodes.length; i++)
			{
				if(nodes[i].get_bp().contains("141"))
				{
					node = nodes[i].get_name();
				}
			}
		}
		
		
		return node;
	}
	public void takeInput()
	{
		System.out.println("Choose any 3 symptoms that you have");
		System.out.println("bleeding");
		System.out.println("blood pressure");
		System.out.println("blurry vision");
		System.out.println("chest pain");
		System.out.println("cough");
		System.out.println("fatigue");
		System.out.println("fever");
		System.out.println("hiccups");
		System.out.println("numbness");
		System.out.println("yellow fever");
		System.out.println("Enter symptom 1");
		symptom1 = sc.nextLine();
		int p = findNodeNo(symptom1);
		if(p == -1)
		{
			
			symptom1 = infer(symptom1);
			System.out.println("You have  "+symptom1);
		}
		int p2 = findNodeNo(symptom1);
		if(p2 == -1)
		{
			
			symptom1 = infer(symptom1);
			System.out.println("You have  "+symptom1);
		}
		System.out.println("Enter symptom 2");
		symptom2 = sc.nextLine();
		System.out.println("Enter symptom 3");
		symptom3 = sc.nextLine();
		
	}
	
	public static void main(String [] args)
	{
		Search s = new Search();
		System.out.println("Do you have diabetes");
		Scanner sc = new Scanner(System.in);
		String dia = sc.next();
		boolean flag;
		if(dia.equalsIgnoreCase("Y"))
			flag = true;
		else
			flag = false;
		
		
		s.h = new Heuristic(flag);
		//System.out.println(flag);
		s.takeInput();
		
		ArrayList no = s.findPath(s.symptom1);
		String [] more_symptoms = s.getMoreSymptoms(no);
		System.out.println("Additional symptoms you might have");
		System.out.print(more_symptoms[0]+"  "+more_symptoms[1]+"  ");

		ArrayList no2 = s.findPath(s.symptom2);
		String [] more_symptoms2 = s.getMoreSymptoms(no2);
		System.out.print(more_symptoms2[0]+"  "+more_symptoms2[1]+"  ");

		ArrayList no3 = s.findPath(s.symptom3);
		String [] more_symptoms3 = s.getMoreSymptoms(no3);
		System.out.println(more_symptoms3[0]+"  "+more_symptoms3[1]);
		System.out.println("You have the disorder");
		System.out.println(s.getDisorderNodes(s.symptom1, s.symptom2, s.symptom3));
			
	}
	
	
	

}
