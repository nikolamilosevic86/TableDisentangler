/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package Annotation;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Main.MarvinSemAnnotator;
import Main.Word;
import Utils.Utilities;
import ValueParser.ValueItem;
import ValueParser.ValueItem.ValueType;
import ValueParser.ValueParser;
import tablInEx.Annotation;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.TablInExMain;
import tablInEx.Table;
import tablInEx.Table.StructureType;

// TODO: Auto-generated Javadoc
/**
 * The Class Annotate.
 */
public class Annotate {

	/**
	 * Annotate article with annotation schema.
	 *
	 * @param a the read article
	 */
	public void AnnotateArticle(Article a)
	{
		try {
			Utilities.MakeDirectory(TablInExMain.Inpath +"_Annotation");
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
				authors.appendChild(author);
			}
			rootElement.appendChild(authors);
					
			Element keywords = doc.createElement("KeyWords");
			if(a.getKeywords()!=null)
			for(int i = 0; i<a.getKeywords().length;i++){
				Element keyword = doc.createElement("KeyWord");
				keyword.setTextContent(a.getKeywords()[i]);
				keywords.appendChild(keyword);
			}
			rootElement.appendChild(keywords);

			Element publisher = doc.createElement("JournalInformation");
			Element publisherName = doc.createElement("PublisherName");
			publisherName.setTextContent(a.getPublisher_name());
			publisher.appendChild(publisherName);
			
			Element publisherLoc = doc.createElement("PublisherLocation");
			publisherLoc.setTextContent(a.getPublisher_loc());
			publisher.appendChild(publisherLoc);
			rootElement.appendChild(publisher);
			
			Element venue = doc.createElement("Venue");
			venue.setTextContent(a.getVenue());
			publisher.appendChild(venue);
			
			Element journal = doc.createElement("journal");
			journal.setTextContent(a.getJournal_name());
			publisher.appendChild(journal);
			
			Element abstractEl = doc.createElement("Abstract");
			abstractEl.setTextContent(a.getAbstract());
			rootElement.appendChild(abstractEl);
			
			Table[] tables = a.getTables();
			Element tablesEl = doc.createElement("Tables");
			rootElement.appendChild(tablesEl);
			for(int i = 0;i<tables.length;i++)
			{
				Element tableEl = doc.createElement("Table");
				tablesEl.appendChild(tableEl);
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
				if(table.getTableStructureType()==null)
				{
					table.setTableStructureType(StructureType.NULL);
				}
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
				
				Cell[][] cells = table.original_cells;
				if(cells!=null){
				for(int j = 0;j<cells.length;j++)
				{
					for(int k = 0;k<cells[j].length;k++)
					{
						if(k == 0 && cells[j][k].isIs_header())
						{
							cells[j][k].setIs_stub(true);
						}
						Element CellEl = doc.createElement("Cell");
						CellsEl.appendChild(CellEl);
						Element CellID = doc.createElement("CellID");
						String cellIDStr = ""+j+"."+k;
						CellID.setTextContent(cellIDStr);
						cells[j][k].CellId = cellIDStr;
						CellEl.appendChild(CellID);
						
						if(cells[j][k].getSuperRowIndex()!=null&&!cells[j][k].getSuperRowIndex().equals(""))
						{
							Element SuperRowRef = doc.createElement("SuperRowRef");
							SuperRowRef.setTextContent(cells[j][k].getSuperRowIndex());
							cells[j][k].setSuper_row_ref(cells[j][k].getSuperRowIndex());
							CellEl.appendChild(SuperRowRef);
						}
						
						Element CellValue = doc.createElement("CellValue");
						CellValue.setTextContent(cells[j][k].getCell_content());
						CellEl.appendChild(CellValue);
						
						
						String valueToParse = cells[j][k].getCell_content();
						LinkedList<ValueItem> valueTags = TablInExMain.vp.parseValue(valueToParse);
						Element CellSemantics = doc.createElement("Annotations");
						CellEl.appendChild(CellSemantics);
						for(int p = 0; p<valueTags.size();p++)
						{
							Annotation annot = new Annotation();
							Element CellValueSemantics = doc.createElement("Annotation");
							CellValueSemantics.setAttribute("Type", "ValueType");
							annot.setType("ValueType");
							CellValueSemantics.setAttribute("TypeVal", valueTags.get(p).type.toString());
							annot.setDescription(valueTags.get(p).type.toString());
							CellValueSemantics.setAttribute("Start", valueTags.get(p).start_position+"");
							annot.setStart(valueTags.get(p).start_position);
							CellValueSemantics.setAttribute("End", valueTags.get(p).end_position+"");
							annot.setEnd(valueTags.get(p).end_position);
							CellValueSemantics.setAttribute("Content",valueTags.get(p).value);
							annot.setContent(valueTags.get(p).value);
							CellValueSemantics.setAttribute("ID","");
							annot.setID("");
							CellValueSemantics.setAttribute("URL","");
							annot.setURL("");
							CellValueSemantics.setAttribute("Source","TableAnnotatorSyntacticAnalizer");
							annot.setSource("TableAnnotatorSyntacticAnalizer");
							annot.setLocation(MarvinSemAnnotator.Location);
							annot.setEnvironment(MarvinSemAnnotator.Environment);
							annot.setAgentName("TableAnnotatorSyntacticAnalizer");
							annot.setAgentVersion("1.0");
							
							cells[j][k].annotations.add(annot);
							CellSemantics.appendChild(CellValueSemantics);
						}
						//annotating by MARVIN
						//TODO: ADD This bit when DBPedia is installed locally
						if(valueToParse==null)
						{
							valueToParse = "";
						}
						int mathTypeIndex = valueToParse.indexOf("MathType@");
						if(mathTypeIndex>0)
						{
							valueToParse = valueToParse.substring(0, mathTypeIndex);
						}
						LinkedList<Word> words = null;
						if(valueToParse!=null){
							//System.out.println(valueToParse);
							valueToParse = valueToParse.trim();
							if(!Utilities.isSpaceOrEmpty(valueToParse)){
								words = TablInExMain.marvin.annotate(valueToParse);
								}
						}
						if(words!=null){
						for(int p = 0;p<words.size();p++){
//							Element CellValueSemantics = doc.createElement("CellValueSem");
//							CellValueSemantics.setAttribute("Type", ValueType.TEXT.toString());
//							CellValueSemantics.setAttribute("Start", words.get(p).starting+"");
//							CellValueSemantics.setAttribute("End", words.get(p).ending+"");
//							CellValueSemantics.setAttribute("Content",words.get(p).word);
//							CellSemantics.appendChild(CellValueSemantics);
							for(int s = 0;s<words.get(p).wordmeanings.size();s++){
								Element Meaning = doc.createElement("Annotation");
								Annotation annot = new Annotation();
								Meaning.setAttribute("Type", "ValueSemantic");
								annot.setType("ValueSemantic");
								Meaning.setAttribute("TypeVal", "");
								annot.setTypeVal("");
								//Meaning.setAttribute("Source", words.get(p).wordmeanings.get(s).Source);
								Meaning.setAttribute("Source", words.get(p).wordmeanings.get(s).AnnotatorSystem);
								//annot.setSource( words.get(p).wordmeanings.get(s).Source);
								annot.setSource( words.get(p).wordmeanings.get(s).AnnotatorSystem);
								Meaning.setAttribute("ID", words.get(p).wordmeanings.get(s).id);
								annot.setID(words.get(p).wordmeanings.get(s).id);
								Meaning.setAttribute("URL", words.get(p).wordmeanings.get(s).URL);
								annot.setURL(words.get(p).wordmeanings.get(s).URL);
								Meaning.setAttribute("Start", words.get(p).wordmeanings.get(s).startAt+"");
								annot.setStart(words.get(p).wordmeanings.get(s).startAt);
								Meaning.setAttribute("End", words.get(p).wordmeanings.get(s).endAt+"");
								annot.setEnd(words.get(p).wordmeanings.get(s).endAt);
								Meaning.setAttribute("Content", words.get(p).wordmeanings.get(s).appearingWord);
								annot.setContent( words.get(p).wordmeanings.get(s).appearingWord);
								annot.setDescription(words.get(p).wordmeanings.get(s).Description);
								annot.setLocation(words.get(p).wordmeanings.get(s).Location);
								annot.setEnvironment(words.get(p).wordmeanings.get(s).EnvironmentDesc);
								annot.setAgentName(words.get(p).wordmeanings.get(s).AgentName);
								annot.setAgentVersion(words.get(p).wordmeanings.get(s).AgentVersion);
								Meaning.setAttribute("Description", words.get(p).wordmeanings.get(s).Description);
								cells[j][k].annotations.add(annot);
								CellSemantics.appendChild(Meaning);				
							}
						}
						}	
						
						Element CellType = doc.createElement("CellType");
						CellType.setTextContent(cells[j][k].getCellType());
						CellEl.appendChild(CellType);
						
						
						for(int s = j-1;s>=0;s--)
						{
							//Current header not empty, and cell before is not header
							if(s>=0 && cells[j][k].isIs_header() && !cells[s][k].isIs_header()&&!cells[j][k].getCell_content().equals(""))
							{
								break;
							}
							
							if(s>=0 && cells[s][k]!=null && cells[j][k].isIs_header()&&!cells[j-1][k].isIs_header())
							{
								break;
							}
							
							if(s>=0&&cells[s][k]!=null && cells[s][k].isIs_header())
							{
								Element HeaderRef = doc.createElement("HeaderRef");
								String href = ""+s+"."+k;
								HeaderRef.setTextContent(href);
								cells[j][k].setHeader_ref(href);
								CellEl.appendChild(HeaderRef);
								//break;
							}
							if(s>=0&&cells[s][k]!=null && cells[s][k].isIs_header())
							{
								Element HeaderCatRef = doc.createElement("HeadStubRef");
								String hsref = ""+s+"."+0;
								HeaderCatRef.setTextContent(hsref);
								cells[j][k].setHead_stub_ref(hsref);
								CellEl.appendChild(HeaderCatRef);
								break;
							}
						}
						
						for(int s = k-1;s>=0;s--)
						{
							if(s>=0 && cells[j][s]!=null && cells[j][s].isIs_stub())
							{
								Element StubRef = doc.createElement("StubRef");
								String sref = ""+j+"."+s;
								StubRef.setTextContent(sref);
								cells[j][k].setStub_ref(sref);
								if(cells[j][k].isIs_header()||cells[j][k].isIs_subheader())
								{
									cells[j][k].setStub_values(cells[j][s].getCell_content());
								}
								CellEl.appendChild(StubRef);
								break;
							}
						}
						
						Element CellRoles = doc.createElement("CellRoles");
						CellEl.appendChild(CellRoles);
						boolean isDataCell = true;
						if(cells[j][k].isIs_header()){
							Element CellRole = doc.createElement("CellRole");
							CellRole.setTextContent("Header");
							cells[j][k].CellRoles.add("Header");
							CellRoles.appendChild(CellRole);
							isDataCell = false;
						}
						if(cells[j][k].isIs_stub()&& cells[j][k].getColumnspanning_index()==0){
							Element CellRole = doc.createElement("CellRole");
							CellRole.setTextContent("Stub");
							cells[j][k].CellRoles.add("Stub");
							CellRoles.appendChild(CellRole);
							isDataCell = false;
						}		
						
						boolean isSuperRow = false;
						for(int l = 0;l<5;l++)
						{
							if(cells[j][k]!=null&&j+l<cells.length&&cells[j+l][k]!=null&&cells[j+l][k].getSuperRowIndex()!=null&&cells[j+l][k].getSuperRowIndex().equals(cells[j][k].CellId))
							{
								isSuperRow = true;
								break;
							}
						}
						if(isSuperRow)
						{
							for(int l = 0; l<cells[j].length;l++)
							{
								cells[j][l].setIs_subheader(true);
							}
						}
						
						
						
						if(cells[j][k].isIs_subheader()){
							Element CellRole = doc.createElement("CellRole");
							CellRole.setTextContent("SuperRow");
							cells[j][k].CellRoles.add("SuperRow");
							CellRoles.appendChild(CellRole);
						}
						//Previously was StubHeaderCell, but makes no sense when since Header is anyway included.
//						if( k==0 && cells[j][k].isIs_header()){
//							Element CellRole = doc.createElement("CellRole");
//							CellRole.setTextContent("Stub");
//							CellRoles.appendChild(CellRole);
//							isDataCell = false;
//						}
						if(isDataCell)
						{
							Element CellRole = doc.createElement("CellRole");
							CellRole.setTextContent("Data");
							cells[j][k].CellRoles.add("Data");
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
			}						
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source;
			source = new DOMSource(doc);
			StreamResult result =  new StreamResult(new File(TablInExMain.Inpath+"_Annotation/"+a.getPmc()+".xml"));
			transformer.transform(source, result);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			TablInExMain.marvin = new MarvinSemAnnotator();
		}
		
	}
}
