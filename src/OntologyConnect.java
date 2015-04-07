package Ontology;


import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



public class OntologyConnect 
{
	static String s;
	// opens a connection to the medical OWL file
	static  OntModel openOWL()
	{
        
	    OntModel model = null;
	    model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_RULE_INF );
	    java.io.InputStream in = FileManager.get().open( "C:/Users/Kaushal/Google Drive/Assignments/AI/Ontology/backup.owl" );  
	    
	    if (in == null)
	    {
	        throw new IllegalArgumentException("File does not exist");  // there is no file to connect
	    }
	    return  (OntModel) model.read(in, "");
	}
	static void printStatements()
	{
		OntModel myModel = openOWL();
		// list the statements in the Model
		StmtIterator iter = myModel.listStatements();
		while (iter.hasNext()) 
		{
		    Statement stmt      = iter.nextStatement();  // get next statement
		    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    System.out.print(subject.toString());
		    System.out.print(" " + predicate.toString() + " ");
		    if (object instanceof Resource)
		    {
		    	 System.out.print(object.toString());
		    }
		    else 
		    {
		        // object is a literal
		        System.out.print(" \"" + object.toString() + "\"");
		    }
		    System.out.println(" .");		
	    }		
	}
	
	
	static ArrayList<Individual> getIndividuals()
	{
		ArrayList<Individual> ind = new ArrayList<Individual>();
		OntModel my = openOWL();
		ExtendedIterator classes = my.listHierarchyRootClasses();
		OntClass root = null;
		Individual thisInstance = null;
		while(classes.hasNext())
		{
			root = (OntClass) classes.next();
			
			ExtendedIterator indi = root.listInstances();
			while (indi.hasNext())
			{
			thisInstance = (Individual) indi.next();
			ind.add(thisInstance);
			
			}
		}
	    return ind;	
	}
	
	static  com.hp.hpl.jena.query.ResultSet ExecSparQl(String Query)
	{
        
              com.hp.hpl.jena.query.Query query = QueryFactory.create(Query);

              QueryExecution qe = QueryExecutionFactory.create(query, openOWL());
              com.hp.hpl.jena.query.ResultSet results = qe.execSelect();

              return results;  // Return jena.query.ResultSet 
       
    }  
	
	static  String ExecSparQlString(String Query)
	{
        try
        {
            com.hp.hpl.jena.query.Query query = QueryFactory.create(Query);

                  QueryExecution qe = QueryExecutionFactory.create(query, openOWL());

                  com.hp.hpl.jena.query.ResultSet results = qe.execSelect();

                  // Test
                  if(results.hasNext())
                  {
                  	//if iS good 
                    ByteArrayOutputStream go = new ByteArrayOutputStream ();
                    ResultSetFormatter.out((OutputStream)go ,results, query);
                    //String s = go.toString();
                       s = new String(go.toByteArray(), "UTF-8");
                  }
                    // not okay
                  else
                  {
                      s = "no result found ";
                  }
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(OntologyConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;   // return  jena.query.ResultSet  as string 
    }
	
	public static String getPropertiesOfIndividuals(String individual)
	{
		String query = "Prefix ab: <http://www.semanticweb.org/kaushal/ontologies/2014/10/medical#> SELECT ?propertyName ?propertyValue WHERE{ ab:"+ individual +" ?propertyName ?propertyValue . }";
		String result = ExecSparQlString(query);		
		String finalresult = result.replace("-", "").replace("=", "").replace("|", "").replace("propertyName", "").replace("propertyValue", "").replace("ab", "").replace(":", "").trim();
		String output [] = finalresult.split("<");
		return output[0];
		
	}
	
	public static String [] getStringIndividuals()
	{
		ArrayList<Individual> indi = getIndividuals();
		String [] temp = new String[indi.size()];
		for(int i = 0; i < indi.size(); i++)
		{
			String f = indi.get(i).toString();
			int index = f.indexOf("#");
			temp[i] = f.substring(index+1);
		}
		
		return temp;
	}
	
	public static String [] getIndividualsWithProperty(String [] ind)
	{
		
		String [] properties = new String[ind.length];
		for(int i = 0; i < ind.length; i++)
		{
			
			properties[i] = getPropertiesOfIndividuals(ind[i]).trim();
			
		}
		return properties;
	}
	
	public static String getGivenName(String i)
	{
		String query = "Prefix ab: <http://www.semanticweb.org/kaushal/ontologies/2014/10/medical#> SELECT  ?propertyValue WHERE { ab:"+i.trim()+" ab:givenName ?propertyValue . }";
		String result = ExecSparQlString(query);
		String finalresult = result.replace("-", "").replace("=", "").replace("|", "").replace("propertyName", "").replace("propertyValue", "").replace("ab", "").replace(":", "").replaceAll("\"", "").trim();
		return finalresult;
	}
	
	public static String getLeadsTo(String i)
	{
		String query = "Prefix ab: <http://www.semanticweb.org/kaushal/ontologies/2014/10/medical#> SELECT  ?propertyValue WHERE { ab:"+i+" ab:leadsTo ?propertyValue . }";
		String result = ExecSparQlString(query);
		String finalresult = result.replace("-", "").replace("=", "").replace("|", "").replace("propertyName", "").replace("propertyValue", "").replace("ab", "").replace(":", "").replaceAll("\"", "").trim();
		return finalresult;
	}
	
	public static String getName(String i)
	{
		String query = "Prefix ab: <http://www.semanticweb.org/kaushal/ontologies/2014/10/medical#> SELECT  ?propertyValue WHERE { ab:"+i+" ab:name ?propertyValue . }";
		String result = ExecSparQlString(query);
		String finalresult = result.replace("-", "").replace("=", "").replace("|", "").replace("propertyName", "").replace("propertyValue", "").replace("ab", "").replace(":", "").replaceAll("\"", "").replace("\\n", "").trim();
		return finalresult;
	}
	
	public static String getTemp(String i)
	{
		String query = "Prefix ab: <http://www.semanticweb.org/kaushal/ontologies/2014/10/medical#> SELECT  ?propertyValue WHERE { ab:"+i+" ab:bodyTemperature ?propertyValue . }";
		String result = ExecSparQlString(query);
		String finalresult = result.replace("-", "").replace("=", "").replace("|", "").replace("propertyName", "").replace("propertyValue", "").replace("ab", "").replace(":", "").replaceAll("\"", "").trim();
		return finalresult;
	}
	
	public static String getBloodPressure(String i)
	{
		String query = "Prefix ab: <http://www.semanticweb.org/kaushal/ontologies/2014/10/medical#> SELECT  ?propertyValue WHERE { ab:"+i+" ab:bloodPressure ?propertyValue . }";
		String result = ExecSparQlString(query);
		String finalresult = result.replace("-", "").replace("=", "").replace("|", "").replace("propertyName", "").replace("propertyValue", "").replace("ab", "").replace(":", "").replaceAll("\"", "").trim();
		return finalresult;
	}
	 
	public static String getAlsoLeadsTo(String i)
	{
		String query = "Prefix ab: <http://www.semanticweb.org/kaushal/ontologies/2014/10/medical#> SELECT  ?propertyValue WHERE { ab:"+i+" ab:alsoLeadsTo ?propertyValue . }";
		String result = ExecSparQlString(query);
		String finalresult = result.replace("-", "").replace("=", "").replace("|", "").replace("propertyName", "").replace("propertyValue", "").replace("ab", "").replace(":", "").replaceAll("\"", "").trim();
		return finalresult;
	}
	
	public static Node [] generateNodes()
	{
		String [] ind = getStringIndividuals();
		Node [] n = new Node[ind.length];
		String name = null;
		String givenName = null;
		String leadsTo = null;
		String temp = null;
		String bp = null;
		String alsoLeadsTo = null;
		for(int i = 0;i < ind.length; i++ )
		{
			name = getName(ind[i]);
			givenName = getGivenName(ind[i]);
			leadsTo = getLeadsTo(ind[i]);
			temp = getTemp(ind[i]);
			bp = getBloodPressure(ind[i]);
			alsoLeadsTo = getAlsoLeadsTo(ind[i]);
			n[i] = new Node(i,name, givenName, leadsTo, temp, bp, alsoLeadsTo);
			
		}
		return n;
		
		
	}
	
	/*
	public static void main(String [] args)
	{
		
		Node [] n = generateNodes();
		for(int i = 0; i < n.length; i++)
		System.out.println(n[i].get_given_name() + i);
		System.out.println(n[6].get_leads_to());
		System.out.println(n[6].get_also_leads_to());
		System.out.println(n[6].get_node_no());
		String s = n[20].get_also_leads_to();
		String dis = "Disorder_Jaundice";
		if(dis.equals(s))
				System.out.println("This");
			
	}
    */

}
	
