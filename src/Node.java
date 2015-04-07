package Ontology;

public class Node 
{
	private int node_no = 1;
	protected String name;
	protected String givenName;
	protected String leadsTo;
	protected String bp;
	protected String temp;
	protected String alsoLeadsTo;
	
	Node(int node_no,String name,String givenName, String leadsTo, String temp, String bp, String alsoLeadsTo)
	{
		this.node_no = node_no;
		this.name = name;
		this.givenName = givenName;
		this.leadsTo = leadsTo;
		this.temp = temp;
		this.bp = bp;
		this.alsoLeadsTo = alsoLeadsTo;
		
	}
	
	public int get_node_no()
	{
		return node_no;
	}
	
	public String get_name()
	{
		return name;
	}
	
	public String get_given_name()
	{
		return givenName;
	}
	
	public String get_leads_to()
	{
		return leadsTo;
	}
	
	public String get_temp()
	{
		return temp;
	}
	
	public String get_bp()
	{
		return bp;
	}
	
	public int get_node_no(String givenName)
	{
		if(givenName.equals(this.givenName))
			return this.node_no;
		else 
			return -111;
	}
	
	public String get_also_leads_to()
	{
		return alsoLeadsTo;
	}
	

}
