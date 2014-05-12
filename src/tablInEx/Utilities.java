/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package tablInEx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class Utilities.
 */
public class Utilities {
	
	
	/**
	 * Checks if is string numeric.
	 *
	 * @param str the str
	 * @return true, if is numeric
	 */
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
		  Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	/**
	 * Creates the xml string from sub node.
	 *
	 * @param xml the xml
	 * @return the string
	 */
	public static String CreateXMLStringFromSubNode(Node xml)
	{
		String result = "";
		try{
		StringWriter sw = new StringWriter();
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.transform(new DOMSource(xml), new StreamResult(sw));
		result = sw.toString(); 
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Delete folder with content.
	 *
	 * @param folder the folder
	 */
	public static void DeleteFolderWithContent(String folder)
	{
		File folderToDelete = new File(folder);
		//File Dir = new File(ImagesFolder);
		//File[] files = folderToDelete.listFiles();
		String[] entries = folderToDelete.list();
		if(entries!=null){
			for(String s: entries){
		    	File currentFile = new File(folderToDelete.getPath(),s);
		    	currentFile.delete();
			}
		}
		folderToDelete.delete();		
	}
	
	/**
	 * Make directory.
	 *
	 * @param path the path
	 * @return true, if successful
	 */
	public static boolean MakeDirectory(String path)
	{
		File f = new File(path);
		return f.mkdir();
	}
	
	/**
	 * Write file.
	 *
	 * @param filePath the file path
	 * @param content the content
	 */
	public static void WriteFile(String filePath,String content)
	{
		try{
			PrintWriter writer = new PrintWriter(filePath, "UTF-16");
			writer.print(content);
			writer.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
	}
	
	/**
	 * Append to file.
	 *
	 * @param filePath the file path
	 * @param content the content
	 */
	public static void AppendToFile(String filePath, String content){
	try {
	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
	    out.println(content);
	    out.close();
	} catch (IOException e) {
		e.printStackTrace();
	    //exception handling left as an exercise for the reader
	}
	}
	
	public static boolean isSpace(char ch)
	{
		if(((int)ch)==8195 || (int)ch==160 || (int)ch== ' ' || (int)ch==8194 || (int)ch==8201 )
			return true;
		else
			return false;
	}
	
	public static boolean isSpaceOrEmpty(String s)
	{
		if(s==null)
			return false;
		if(s.length()>1)
			return false;
		if(s.length()==0)
			return true;
		char ch = s.charAt(0);
		if(((int)ch)==8195 || (int)ch==160 || (int)ch== ' ' || (int)ch==8194 || (int)ch==8201 )
			return true;
		else
			return false;
	}
}
