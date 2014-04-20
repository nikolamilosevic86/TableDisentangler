/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package IE;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.Table;
import tablInEx.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleIE. Does simple Information extraction. 
 */
public class SimpleIE {
	
	/** The folder. */
	private static String folder;
	
	/**
	 * Instantiates a new simple ie.
	 *
	 * @param inpath the inpath
	 */
	public SimpleIE(String inpath) {
		// TODO Auto-generated constructor stub
		folder = inpath+"_ie";
		Utilities.DeleteFolderWithContent(folder);
		Utilities.MakeDirectory(folder);
	}

	/**
	 * Extract information from simple tables.
	 *
	 * @param art the art
	 */
	public void ExtractInformation(Article art)
	{
		if(art==null)
			return;
		Table[] tables = art.getTables();
		for(int i = 0; i< tables.length;i++)
		{
			//only simple tables
			if(tables[i].getStructureClass()!=2)
				continue;
			
			String tableFileName = "/"+tables[i].getDocumentFileName()+tables[i].getTable_title();
			Cell[][] cells = tables[i].cells;
			for(int j=1;j<cells.length;j++)
			{
				for(int k=1;k<cells[j].length;k++)
				{
					try{
						DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

						//root elements
						Document doc = docBuilder.newDocument();

						Element rootElement = doc.createElement("information");
						doc.appendChild(rootElement);
						
						Element attribute = doc.createElement("attribute");
						attribute.setTextContent(cells[0][0].getCell_content()+";"+cells[0][k].getCell_content()+";"+cells[j][0].getCell_content());
						rootElement.appendChild(attribute);
						
						//info elements
						Element info = doc.createElement("value");
						info.setTextContent(cells[j][k].getCell_content());
						rootElement.appendChild(info);
						
						Element tname = doc.createElement("tableName");
						tname.setTextContent(tables[i].getTable_caption());
						rootElement.appendChild(tname);
						
						Element torder = doc.createElement("tableOrder");
						torder.setTextContent(tables[i].getTable_title());
						rootElement.appendChild(torder);
						
						Element tfooter = doc.createElement("tableFooter");
						tfooter.setTextContent(tables[i].getTable_footer());
						rootElement.appendChild(tfooter);
						
						Element docTitle = doc.createElement("DocumentTitle");
						docTitle.setTextContent(art.getTitle());
						rootElement.appendChild(docTitle);
						
						Element pmc = doc.createElement("PMC");
						pmc.setTextContent(art.getPmc());
						rootElement.appendChild(pmc);
						
											
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);

						StreamResult result =  new StreamResult(new File(folder+tableFileName+"e"+j+","+k+".xml"));
						transformer.transform(source, result);
					}catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
		
	}


}
