package InfoClassExtractor;

import java.util.LinkedList;

import tablInEx.Table;

public class Extractor {
	InfoClassFilesReader icFileReader = new InfoClassFilesReader();
	
	public Extractor(){
		// Should I create database here?
	}
	
	public Extractor(String path)
	{
		icFileReader.ReadInfoClasses(path);	
	}
	
	public void Extract(Table t)
	{
		// Check caption
		// Check footer
		// Check headers
		// Check stub
		//-----should maybe check together-------
		// Check super-rows
		// Check data
		
		//Add to database
	}
	
	public LinkedList<ExtractedData> ProcessCaption(Table t)
	{
		LinkedList<ExtractedData> edList = new LinkedList<ExtractedData>();
		String caption = t.getTable_caption();
		for(InfoClass ic:icFileReader.InfoClasses)
		{
			// Extract data by the rules!
		}
		
		
		return edList;
	}

}
