package IE;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import tablInEx.TablInExMain;
import tablInEx.Utilities;

public class MetaMapStats {

	private static HashMap<String, Integer> Head00MMStats = new HashMap<String, Integer>();
	private static HashMap<String, Integer> HeaderMMStats = new HashMap<String, Integer>();
	private static HashMap<String, Integer> SubHeaderMMStats = new HashMap<String, Integer>();
	private static HashMap<String, Integer> StubMMStats = new HashMap<String, Integer>();
	private static HashMap<String, Integer> ValueMMStats = new HashMap<String, Integer>();
	private static HashMap<String, Integer> TableNameMMStats = new HashMap<String, Integer>();
	private static HashMap<String, Integer> TableFooterMMStats = new HashMap<String, Integer>();
	private static HashMap<String, Integer> ArticleNameMMStats = new HashMap<String, Integer>();
	
	public static void AddHead00MMStats (String key)
	{
		if(!Head00MMStats.containsKey(key))
		{
			Head00MMStats.put(key, 1);
		}
		else
		{
			int val = Head00MMStats.get(key);
			Head00MMStats.put(key, val+1);
		}
	}
	
	public static void AddHeaderMMStats (String key)
	{
		if(!HeaderMMStats.containsKey(key))
		{
			HeaderMMStats.put(key, 1);
		}
		else
		{
			int val = HeaderMMStats.get(key);
			HeaderMMStats.put(key, val+1);
		}
	}
	
	public static void AddSubHeaderMMStats (String key)
	{
		if(!SubHeaderMMStats.containsKey(key))
		{
			SubHeaderMMStats.put(key, 1);
		}
		else
		{
			int val = SubHeaderMMStats.get(key);
			SubHeaderMMStats.put(key, val+1);
		}
	}
	
	public static void AddStubMMStats (String key)
	{
		if(!StubMMStats.containsKey(key))
		{
			StubMMStats.put(key, 1);
		}
		else
		{
			int val = StubMMStats.get(key);
			StubMMStats.put(key, val+1);
		}
	}
	
	public static void AddValueMMStats (String key)
	{
		if(!ValueMMStats.containsKey(key))
		{
			ValueMMStats.put(key, 1);
		}
		else
		{
			int val = ValueMMStats.get(key);
			ValueMMStats.put(key, val+1);
		}
	}
	
	public static void AddTableNameMMStats (String key)
	{
		if(!TableNameMMStats.containsKey(key))
		{
			TableNameMMStats.put(key, 1);
		}
		else
		{
			int val = TableNameMMStats.get(key);
			TableNameMMStats.put(key, val+1);
		}
	}
	
	public static void AddTableFooterMMStats (String key)
	{
		if(!TableFooterMMStats.containsKey(key))
		{
			TableFooterMMStats.put(key, 1);
		}
		else
		{
			int val = TableFooterMMStats.get(key);
			TableFooterMMStats.put(key, val+1);
		}
	}
	
	public static void AddArticleNameMMStats (String key)
	{
		if(!ArticleNameMMStats.containsKey(key))
		{
			ArticleNameMMStats.put(key, 1);
		}
		else
		{
			int val = ArticleNameMMStats.get(key);
			ArticleNameMMStats.put(key, val+1);
		}
	}
	
	public static void PrintMMStats()
	{
		try {
		LinkedHashMap lm = Utilities.sortHashMapByValuesD(Head00MMStats);
		Object [] ss = lm.keySet().toArray();
		String[] sa = new String[ss.length];
		int k = 0;
		for(Object o:ss)
		{
			sa[k] = (String)ss[k];
			k++;
		}
		PrintWriter writer;

			writer = new PrintWriter("head00Stats.txt", "UTF-8");


		for (String name: sa){

            String key =name.toString();
            String value = lm.get(name).toString();  
            writer.println(key + " " + value);  

		}
		writer.close();
		
		//Header
		lm = Utilities.sortHashMapByValuesD(HeaderMMStats);
		ss = lm.keySet().toArray();
		sa = new String[ss.length];
		k = 0;
		for(Object o:ss)
		{
			sa[k] = (String)ss[k];
			k++;
		}
			writer = new PrintWriter("headerStats.txt", "UTF-8");


		for (String name: sa){

            String key =name.toString();
            String value = lm.get(name).toString();  
            writer.println(key + " " + value);  

		}
		writer.close();
		
		//Subheader
		lm = Utilities.sortHashMapByValuesD(SubHeaderMMStats);
		ss = lm.keySet().toArray();
		sa = new String[ss.length];
		k = 0;
		for(Object o:ss)
		{
			sa[k] = (String)ss[k];
			k++;
		}
			writer = new PrintWriter("subheaderStats.txt", "UTF-8");


		for (String name: sa){

            String key =name.toString();
            String value = lm.get(name).toString();  
            writer.println(key + " " + value);  

		}
		writer.close();
		
		//Stub
		lm = Utilities.sortHashMapByValuesD(StubMMStats);
		ss = lm.keySet().toArray();
		sa = new String[ss.length];
		k = 0;
		for(Object o:ss)
		{
			sa[k] = (String)ss[k];
			k++;
		}
			writer = new PrintWriter("stubStats.txt", "UTF-8");


		for (String name: sa){

            String key =name.toString();
            String value = lm.get(name).toString();  
            writer.println(key + " " + value);  

		}
		writer.close();
		
//		ValueMMStats = new HashMap<String, Integer>();
//		private static HashMap<String, Integer> TableNameMMStats = new HashMap<String, Integer>();
//		private static HashMap<String, Integer> TableFooterMMStats = new HashMap<String, Integer>();
//		private static HashMap<String, Integer> ArticleNameMMStats
		//Value
		lm = Utilities.sortHashMapByValuesD(ValueMMStats);
		ss = lm.keySet().toArray();
		sa = new String[ss.length];
		k = 0;
		for(Object o:ss)
		{
			sa[k] = (String)ss[k];
			k++;
		}
			writer = new PrintWriter("valueStats.txt", "UTF-8");


		for (String name: sa){

            String key =name.toString();
            String value = lm.get(name).toString();  
            writer.println(key + " " + value);  

		}
		writer.close();
		
		//TableName
		lm = Utilities.sortHashMapByValuesD(TableNameMMStats);
		ss = lm.keySet().toArray();
		sa = new String[ss.length];
		k = 0;
		for(Object o:ss)
		{
			sa[k] = (String)ss[k];
			k++;
		}
			writer = new PrintWriter("tableNameStats.txt", "UTF-8");


		for (String name: sa){

            String key =name.toString();
            String value = lm.get(name).toString();  
            writer.println(key + " " + value);  

		}
		writer.close();
		
		//TableFooter
		lm = Utilities.sortHashMapByValuesD(TableFooterMMStats);
		ss = lm.keySet().toArray();
		sa = new String[ss.length];
		k = 0;
		for(Object o:ss)
		{
			sa[k] = (String)ss[k];
			k++;
		}
			writer = new PrintWriter("tableFooterStats.txt", "UTF-8");


		for (String name: sa){

            String key =name.toString();
            String value = lm.get(name).toString();  
            writer.println(key + " " + value);  

		}
		writer.close();
		
		//Article name
		lm = Utilities.sortHashMapByValuesD(ArticleNameMMStats);
		ss = lm.keySet().toArray();
		sa = new String[ss.length];
		k = 0;
		for(Object o:ss)
		{
			sa[k] = (String)ss[k];
			k++;
		}
			writer = new PrintWriter("ArticleNameStats.txt", "UTF-8");


		for (String name: sa){

            String key =name.toString();
            String value = lm.get(name).toString();  
            writer.println(key + " " + value);  

		}
		writer.close();
		
		
		
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
}
