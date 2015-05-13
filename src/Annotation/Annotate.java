package Annotation;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Utils.Utilities;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.Table;

public class Annotate {

	public void AnnotateArticle(Article a)
	{
		try {
			Utilities.MakeDirectory("Annotation");
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("Article");
			doc.appendChild(rootElement);
			Element pmcid = doc.createElement("PMCID");
			pmcid.setTextContent(a.getPmc());
			rootElement.appendChild(pmcid);
			
			Element pcid = doc.createElement("PMID");
			pcid.setTextContent(a.getPmid());
			rootElement.appendChild(pcid);
			
			Element pissn = doc.createElement("p-issn");
			pissn.setTextContent(a.getPissn());
			rootElement.appendChild(pissn);
			
			Element eissn = doc.createElement("e-issn");
			eissn.setTextContent(a.getEissn());
			rootElement.appendChild(eissn);
			
			Element title = doc.createElement("Title");
			title.setTextContent(a.getTitle());
			rootElement.appendChild(title);
			
			Element authors = doc.createElement("Authors");
			for(int i = 0; i<a.getAuthors().size();i++){
				Element author = doc.createElement("Author");
				Element authorName = doc.createElement("AuthorName");
				authorName.setTextContent(a.getAuthors().get(i).name);
				author.appendChild(authorName);
				for(int j=0;j<a.getAuthors().get(i).affiliation.size();j++){
				Element AuthorAffiliation = doc.createElement("AuthorAffiliation");
				AuthorAffiliation.setTextContent(a.getAuthors().get(i).affiliation.get(j));
				author.appendChild(AuthorAffiliation);
				}
				Element authorEmail = doc.createElement("AuthorEmail");
				authorEmail.setTextContent(a.getAuthors().get(i).email);
				author.appendChild(authorEmail);
				//author.setTextContent(a.getAuthors()[i]);
				authors.appendChild(author);
			}
			rootElement.appendChild(authors);
					
			Element keywords = doc.createElement("KeyWords");
			for(int i = 0; i<a.getKeywords().length;i++){
				Element keyword = doc.createElement("KeyWord");
				keyword.setTextContent(a.getKeywords()[i]);
				keywords.appendChild(keyword);
			}
			rootElement.appendChild(keywords);

			Element publisher = doc.createElement("Publisher");
			Element publisherName = doc.createElement("PublisherName");
			publisherName.setTextContent(a.getPublisher_name());
			publisher.appendChild(publisherName);
			
			Element publisherLoc = doc.createElement("PublisherLocation");
			publisherLoc.setTextContent(a.getPublisher_loc());
			publisher.appendChild(publisherLoc);
			rootElement.appendChild(publisher);
			
			Element venue = doc.createElement("Venue");
			venue.setTextContent(a.getVenue());
			rootElement.appendChild(venue);
			
			Element journal = doc.createElement("journal");
			journal.setTextContent(a.getJournal_name());
			rootElement.appendChild(journal);
			
			Element abstractEl = doc.createElement("Abstract");
			abstractEl.setTextContent(a.getAbstract());
			rootElement.appendChild(abstractEl);
			
			Table[] tables = a.getTables();
			for(int i = 0;i<tables.length;i++)
			{
				Element tableEl = doc.createElement("Table");
				rootElement.appendChild(tableEl);
				Table table = tables[i];
				
				Element TabOrder = doc.createElement("TableOrder");
				TabOrder.setTextContent(table.getTable_title());
				tableEl.appendChild(TabOrder);
				
				Element TabCaption = doc.createElement("TableCaption");
				TabCaption.setTextContent(table.getTable_caption());
				tableEl.appendChild(TabCaption);
				
				Element TabFooter = doc.createElement("TableFooter");
				TabFooter.setTextContent(table.getTable_footer());
				tableEl.appendChild(TabFooter);
				
				Element TabStructure = doc.createElement("TableStructureType");
				TabStructure.setTextContent(table.getTableStructureType().toString());
				tableEl.appendChild(TabStructure);
				
				Element TabPragmatic = doc.createElement("TablePragmaticClass");
				TabPragmatic.setTextContent(table.PragmaticClass);
				tableEl.appendChild(TabPragmatic);
				
				Element TabHasXML = doc.createElement("TabHasXML");
				if(table.isNoXMLTable())
					TabHasXML.setTextContent("no");
				else
					TabHasXML.setTextContent("yes");
				
				tableEl.appendChild(TabHasXML);
				Element CellsEl = doc.createElement("Cells");
				tableEl.appendChild(CellsEl);
				
				Cell[][] cells = table.getTable_cells();
				
				for(int j = 0;j<cells.length;j++)
				{
					for(int k = 0;k<cells[j].length;k++)
					{
						Element CellEl = doc.createElement("Cell");
						CellsEl.appendChild(CellEl);
						Element CellID = doc.createElement("CellID");
						String cellIDStr = ""+j+k;
						CellID.setTextContent(cellIDStr);
						cells[j][k].CellId = cellIDStr;
						CellEl.appendChild(CellID);
						
						Element CellValue = doc.createElement("CellValue");
						CellValue.setTextContent(cells[j][k].getCell_content());
						CellEl.appendChild(CellValue);
						
						Element CellType = doc.createElement("CellType");
						CellType.setTextContent(cells[j][k].getCellType());
						CellEl.appendChild(CellType);
						
						Element CellRoles = doc.createElement("CellRoles");
						CellEl.appendChild(CellRoles);
						boolean isDataCell = true;
						if(cells[j][k].isIs_header()){
							Element CellRole = doc.createElement("CellRole");
							CellRole.setTextContent("Header");
							CellRoles.appendChild(CellRole);
							isDataCell = false;
						}
						if(cells[j][k].isIs_stub()){
							Element CellRole = doc.createElement("CellRole");
							CellRole.setTextContent("Stub");
							CellRoles.appendChild(CellRole);
							isDataCell = false;
						}
						
						if(cells[j][k].isSubheader()){
							Element CellRole = doc.createElement("CellRole");
							CellRole.setTextContent("SuperRow");
							CellRoles.appendChild(CellRole);
						}
						
						if(j==0&& k==0 && cells[j][k].isIs_header()){
							Element CellRole = doc.createElement("CellRole");
							CellRole.setTextContent("TopLeftHeadCell");
							CellRoles.appendChild(CellRole);
							isDataCell = false;
						}
						if(isDataCell)
						{
							Element CellRole = doc.createElement("CellRole");
							CellRole.setTextContent("Data");
							CellRoles.appendChild(CellRole);
						}	
						
						Element CellRow = doc.createElement("CellRowNum");
						CellRow.setTextContent(j+"");//cells[j][k].getRow_number()+""
						CellEl.appendChild(CellRow);
						
						Element CellColumn = doc.createElement("CellColumnNum");
						CellColumn.setTextContent(k+"");//cells[j][k].getColumn_number()
						
						CellEl.appendChild(CellColumn);
						
					}
				}
			}
						
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source;
			source = new DOMSource(doc);
			StreamResult result =  new StreamResult(new File("Annotation/"+a.getPmc()+".xml"));
			transformer.transform(source, result);
			

			
		} catch (Exception ex) {
			ex.getStackTrace();
		}
		
	}
}
