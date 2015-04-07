package Ontology;

public class Heuristic 
{
	private boolean diabetic;
	public Heuristic(boolean diabetic)
	{
		this.diabetic = diabetic;
	}
	public int [] midLevelHeuristic()
	{
		int h [] = new int[2];
		if (diabetic)
		{
			h[0] = 204;
			h[1] = 308;
		}
		else
		{
			h[0] = 307;
			h[1] = 200;
		}
		return h;
	}
	
	public int [] highLevelHeuristic()
	{
		int h [] = new int[2];
		if (diabetic == true)
		{
			h[0] = 34;
			h[1] = 49;
		}
		else
		{
			h[0] = 49;
			h[1] = 34;
		}
		return h;
	}
	
	public int [] disorderHeuristic()
	{
		int h [] = new int[2];
		if (diabetic == true)
		{
			h[0] = 68;
			h[1] = 82;
		}
		else
		{
			h[0] = 82;
			h[1] = 68;
		}
		return h;
		
	}
	

}
