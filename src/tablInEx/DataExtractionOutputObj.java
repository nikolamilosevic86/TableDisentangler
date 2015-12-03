package tablInEx;

import Decomposition.MetaMapStats;
import Decomposition.MetaMapping;
import gov.nih.nlm.nls.metamap.Position;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DataExtractionOutputObj {
	private String filename;
	private Document XMLDocument;
	private Document XMLDocumentTagged;
	
	public DataExtractionOutputObj(String f, Document doc)
	{
		filename = f;
		XMLDocument = doc;
	}
	
	public void MetamapTagDocument()
	{
		NodeList Head00 = XMLDocument.getElementsByTagName("Head00");
		NodeList HeaderValue = XMLDocument.getElementsByTagName("HeaderValue");
		NodeList Stub = XMLDocument.getElementsByTagName("Stub");
		NodeList value = XMLDocument.getElementsByTagName("value");
		NodeList CellType = XMLDocument.getElementsByTagName("CellType");
		NodeList TableType = XMLDocument.getElementsByTagName("TableType");
		NodeList tableName = XMLDocument.getElementsByTagName("tableName");
		NodeList tableOrder = XMLDocument.getElementsByTagName("tableOrder");
		NodeList tableFooter = XMLDocument.getElementsByTagName("tableFooter");
		NodeList DocumentTitle = XMLDocument.getElementsByTagName("DocumentTitle");
		try{
		NodeList PMC = XMLDocument.getElementsByTagName("PMC");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		String Head00Str = "";
		String HeaderValueStr = "";
		String[] Subheaders;
		String StubValue = "";
		String valueStr = "";
		String CellTypeStr= "";
		String TableTypeStr = "";
		String TableNameStr = "";
		String TableOrderStr = "";
		String TableFooterStr = "";
		String DocumentTitleStr = "";
		String PMCStr = "";
		if(Head00!=null && Head00.item(0)!=null)
		{
			Head00Str = Head00.item(0).getTextContent();
		}
		Head00Str = Head00Str.replaceAll("'", "");

		 MetaMapping mp = new MetaMapping();
		 System.out.print("initiated");
		 Map<Object, Object> aMap = mp.getClassification(Head00Str);
		 System.out.println("query sent");
		 Element RootElement = doc.createElement("Information");
		 Element Cell = doc.createElement("Cell");
		 RootElement.appendChild(Cell);
		 Element NavigationPath = doc.createElement("NavigationPath");
		 Cell.appendChild(NavigationPath);
		 doc.appendChild(RootElement);
		 Element Head00e = doc.createElement("Head00");
		 
		 int lastY = 0;
		 for(int i = 1;i<aMap.size();i=i+6)
		 {
			 Position pos = (Position) ((ArrayList) aMap.get(i+1)).get(0);
			// Position posa = new ;
			 int X = pos.getX();
			 int Y = pos.getY();
			 lastY = X+Y;
			 Element HeadPart = doc.createElement("MetamapEl");
			 if(X>lastY){
				 Text tn = doc.createTextNode(Head00Str.substring(lastY, X));
				 Head00e.appendChild(tn);
			 }
			 HeadPart.setAttribute("ConceptID", aMap.get(i-1).toString());
			 HeadPart.setAttribute("SemanticType", aMap.get(i+2).toString());
			 HeadPart.setAttribute("NiceSemanticType", MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 MetaMapStats.AddHead00MMStats( MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 HeadPart.setAttribute("ConceptName", aMap.get(i+3).toString());
			 HeadPart.setAttribute("PreferredName", aMap.get(i+3).toString());
			 HeadPart.setTextContent(Head00Str.substring(X,X+ Y));
			 Head00e.appendChild(HeadPart);
		 }
		 Text tn = doc.createTextNode(Head00Str.substring(lastY, Head00Str.length()));
		 Head00e.appendChild(tn);
		 NavigationPath.appendChild(Head00e);
		 
		 //Header
		 if(HeaderValue!=null && HeaderValue.item(0)!=null)
		 {
			 HeaderValueStr = HeaderValue.item(0).getTextContent();
		 }
		 HeaderValueStr = HeaderValueStr.replaceAll("'", "");
		 mp = new MetaMapping();
		 System.out.print("initiated");
		 aMap = mp.getClassification(HeaderValueStr);
		 System.out.println("query sent");
		 Element HeaderValuee = doc.createElement("HeaderValue");
		 lastY = 0;
		 for(int i = 1;i<aMap.size();i=i+6)
		 {
			 Position pos = (Position) ((ArrayList) aMap.get(i+1)).get(0);
			// Position posa = new ;
			 int X = pos.getX();
			 int Y = pos.getY();

			 Element HeadPart = doc.createElement("MetamapEl");
			 if(X>lastY){
				 tn = doc.createTextNode(HeaderValueStr.substring(lastY, X));
				 HeaderValuee.appendChild(tn);
			 }
			 lastY = X+Y;
			 HeadPart.setAttribute("ConceptID", aMap.get(i-1).toString());
			 HeadPart.setAttribute("SemanticType", aMap.get(i+2).toString());
			 HeadPart.setAttribute("NiceSemanticType", MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 MetaMapStats.AddHeaderMMStats( MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 HeadPart.setAttribute("ConceptName", aMap.get(i+3).toString());
			 HeadPart.setAttribute("PreferredName", aMap.get(i+3).toString());
			 HeadPart.setTextContent(HeaderValueStr.substring(X, X+Y));
			 HeaderValuee.appendChild(HeadPart);
		 }
		 tn = doc.createTextNode(HeaderValueStr.substring(lastY, HeaderValueStr.length()));
		 HeaderValuee.appendChild(tn);
		 NavigationPath.appendChild(HeaderValuee);
		 
		 //Stub
		 Element Stube = doc.createElement("Stub");
		 NavigationPath.appendChild(Stube);
		 NodeList stubVals = Stub.item(0).getChildNodes();
		 for(int j = 0;j<stubVals.getLength();j++)
		 {
			 String nodeName =  stubVals.item(j).getNodeName();
			 String nodeValue = stubVals.item(j).getTextContent();
			 nodeValue = nodeValue.replaceAll("'", "");
			 mp = new MetaMapping();
			 System.out.print("initiated");
			 aMap = mp.getClassification(nodeValue);
			 System.out.println("query sent");
			 Element StubVal = doc.createElement(nodeName);
			 lastY = 0;
			 for(int i = 1;i<aMap.size();i=i+6)
			 {
				 Position pos = (Position) ((ArrayList) aMap.get(i+1)).get(0);
				// Position posa = new ;
				 int X = pos.getX();
				 int Y = pos.getY();

				 Element HeadPart = doc.createElement("MetamapEl");
				 if(X>lastY){
					 tn = doc.createTextNode(nodeValue.substring(lastY, X));
					 StubVal.appendChild(tn);
				 }
				 lastY = X+Y;
				 HeadPart.setAttribute("ConceptID", aMap.get(i-1).toString());
				 HeadPart.setAttribute("SemanticType", aMap.get(i+2).toString());
				 HeadPart.setAttribute("NiceSemanticType", MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
				 if(nodeName.equals("StubValue"))
				 {
					 MetaMapStats.AddStubMMStats( MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
				 }
				 else
				 {
					 MetaMapStats.AddSubHeaderMMStats( MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
				 }
				 HeadPart.setAttribute("ConceptName", aMap.get(i+3).toString());
				 HeadPart.setAttribute("PreferredName", aMap.get(i+3).toString());
				 HeadPart.setTextContent(nodeValue.substring(X, X+Y));
				 StubVal.appendChild(HeadPart);
			 }
			 tn = doc.createTextNode(nodeValue.substring(lastY, nodeValue.length()));
			 StubVal.appendChild(tn);
			 Stube.appendChild(StubVal);
		 }
		 // End of Stub
		 
		 //Value
		 if(value!=null && value.item(0)!=null)
		 {
			 valueStr = value.item(0).getTextContent();
		 } 
		 valueStr = valueStr.replaceAll("'", "");
		 Element Valuee = doc.createElement("value");
		 Cell.appendChild(Valuee);
		 mp = new MetaMapping();
		 System.out.print("initiated");
		 aMap = mp.getClassification(valueStr);
		 System.out.println("query sent");
		 lastY = 0;
		 for(int i = 1;i<aMap.size()-1;i=i+6)
		 {
			 Position pos = (Position) ((ArrayList) aMap.get(i+1)).get(0);
			// Position posa = new ;
			 int X = pos.getX();
			 int Y = pos.getY();

			 Element HeadPart = doc.createElement("MetamapEl");
			 if(X>lastY){
				 tn = doc.createTextNode(valueStr.substring(lastY, X));
				 Valuee.appendChild(tn);
			 }
			 lastY = X+Y;
			 HeadPart.setAttribute("ConceptID", aMap.get(i-1).toString());
			 HeadPart.setAttribute("SemanticType", aMap.get(i+2).toString());
			 HeadPart.setAttribute("NiceSemanticType", MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 MetaMapStats.AddValueMMStats( MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 HeadPart.setAttribute("ConceptName", aMap.get(i+3).toString());
			 HeadPart.setAttribute("PreferredName", aMap.get(i+3).toString());
			 HeadPart.setTextContent(valueStr.substring(X, X+Y));
			 Valuee.appendChild(HeadPart);
		 }
		 tn = doc.createTextNode(valueStr.substring(lastY, valueStr.length()));
		 Valuee.appendChild(tn);
		 
		 CellTypeStr = CellType.item(0).getTextContent();
		 Element CellTypee = doc.createElement("CellType");
		 Cell.appendChild(CellTypee);
		 CellTypee.setTextContent(CellTypeStr);
		 
		 Element Table = doc.createElement("Table");
		 RootElement.appendChild(Table);
		 
		 Element TableTypee = doc.createElement("TableType");
		 TableTypeStr = TableType.item(0).getTextContent();
		 TableTypee.setTextContent(TableTypeStr);
		 Table.appendChild(TableTypee);
		 
		 Element TableNamee = doc.createElement("tableName");
		 Table.appendChild(TableNamee);
		 if(tableName!=null && tableName.item(0)!=null)
		 {
			 TableNameStr = tableName.item(0).getTextContent();
		 }
		 TableNameStr = TableNameStr.replaceAll("'", "");
		 mp = new MetaMapping();
		 System.out.print("initiated");
		 aMap = mp.getClassification(TableNameStr);
		 System.out.println("query sent");
		 lastY = 0;
		 for(int i = 1;i<aMap.size();i=i+6)
		 {
			 Position pos = (Position) ((ArrayList) aMap.get(i+1)).get(0);
			// Position posa = new ;
			 int X = pos.getX();
			 int Y = pos.getY();

			 Element HeadPart = doc.createElement("MetamapEl");
			 if(X>lastY){
				 tn = doc.createTextNode(TableNameStr.substring(lastY, X));
				 TableNamee.appendChild(tn);
			 }
			 lastY = X+Y;
			 HeadPart.setAttribute("ConceptID", aMap.get(i-1).toString());
			 HeadPart.setAttribute("SemanticType", aMap.get(i+2).toString());
			 HeadPart.setAttribute("NiceSemanticType", MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 MetaMapStats.AddTableNameMMStats( MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 HeadPart.setAttribute("ConceptName", aMap.get(i+3).toString());
			 HeadPart.setAttribute("PreferredName", aMap.get(i+3).toString());
			 HeadPart.setTextContent(TableNameStr.substring(X, X+Y));
			 TableNamee.appendChild(HeadPart);
		 }
		 tn = doc.createTextNode(TableNameStr.substring(lastY, TableNameStr.length()));
		 TableNamee.appendChild(tn);
		 
		 Element TableOrdere = doc.createElement("tableOrder");
		 if(tableOrder!=null && tableOrder.item(0)!=null){
			 TableOrderStr = tableOrder.item(0).getTextContent();
		 }
		 TableOrdere.setTextContent(TableOrderStr);
		 Table.appendChild(TableOrdere);
		 
		 Element TableFootere = doc.createElement("tableFooter");
		 Table.appendChild(TableFootere);
		 if(tableFooter!=null && tableFooter.item(0)!=null){
			 TableFooterStr = tableFooter.item(0).getTextContent();
		 }
		 TableFooterStr = TableFooterStr.replaceAll("'", "");
		 mp = new MetaMapping();
		 System.out.print("initiated");
		 aMap = mp.getClassification(TableFooterStr);
		 System.out.println("query sent");
		 lastY = 0;
		 for(int i = 1;i<aMap.size();i=i+6)
		 {
			 Position pos = (Position) ((ArrayList) aMap.get(i+1)).get(0);
			// Position posa = new ;
			 int X = pos.getX();
			 int Y = pos.getY();

			 Element HeadPart = doc.createElement("MetamapEl");
			 if(X>lastY){
				 tn = doc.createTextNode(TableFooterStr.substring(lastY, X));
				 TableFootere.appendChild(tn);
			 }
			 lastY = X+Y;
			 HeadPart.setAttribute("ConceptID", aMap.get(i-1).toString());
			 HeadPart.setAttribute("SemanticType", aMap.get(i+2).toString());
			 HeadPart.setAttribute("NiceSemanticType", MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 MetaMapStats.AddTableFooterMMStats( MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 HeadPart.setAttribute("ConceptName", aMap.get(i+3).toString());
			 HeadPart.setAttribute("PreferredName", aMap.get(i+3).toString());
			 HeadPart.setTextContent(TableFooterStr.substring(X, X+Y));
			 TableFootere.appendChild(HeadPart);
		 }
		 tn = doc.createTextNode(TableFooterStr.substring(lastY, TableFooterStr.length()));
		 TableFootere.appendChild(tn);
		 
		 Element Documente = doc.createElement("Document");
		 RootElement.appendChild(Documente);
		 
		 Element DocumentTitlee = doc.createElement("DocumentTitle");
		 if(DocumentTitle!=null && DocumentTitle.item(0)!=null){
			 DocumentTitleStr = DocumentTitle.item(0).getTextContent();
		 }
		 DocumentTitleStr = DocumentTitleStr.replaceAll("'", "");
		 Documente.appendChild(DocumentTitlee);
		 mp = new MetaMapping();
		 System.out.print("initiated");
		 aMap = mp.getClassification(DocumentTitleStr);
		 System.out.println("query sent");
		 lastY = 0;
		 for(int i = 1;i<aMap.size();i=i+6)
		 {
			 Position pos = (Position) ((ArrayList) aMap.get(i+1)).get(0);
			// Position posa = new ;
			 int X = pos.getX();
			 int Y = pos.getY();

			 Element HeadPart = doc.createElement("MetamapEl");
			 if(X>lastY){
				 tn = doc.createTextNode(DocumentTitleStr.substring(lastY, X));
				 DocumentTitlee.appendChild(tn);
			 }
			 lastY = X+Y;
			 HeadPart.setAttribute("ConceptID", aMap.get(i-1).toString());
			 HeadPart.setAttribute("SemanticType", aMap.get(i+2).toString());
			 HeadPart.setAttribute("NiceSemanticType", MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 MetaMapStats.AddArticleNameMMStats( MetaMapping.getNiceSemanticType(aMap.get(i+2).toString()));
			 HeadPart.setAttribute("ConceptName", aMap.get(i+3).toString());
			 HeadPart.setAttribute("PreferredName", aMap.get(i+3).toString());
			 HeadPart.setTextContent(DocumentTitleStr.substring(X, X+Y));
			 DocumentTitlee.appendChild(HeadPart);
		 }
		 tn = doc.createTextNode(DocumentTitleStr.substring(lastY, DocumentTitleStr.length()));
		 DocumentTitlee.appendChild(tn);
		 
		 Element PMCe = doc.createElement("PMC");
		 Documente.appendChild(PMCe);
		 PMCe.setTextContent(PMC.item(0).getTextContent());
		 
		 
		 
		 
		 XMLDocumentTagged = doc;
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}


	}
	
	public void CreateOutput()
	{
	try{	
	
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source;
		if(TablInExMain.shouldTag){
			 source = new DOMSource(XMLDocumentTagged);
		}
		else
		{
			 source = new DOMSource(XMLDocument);
		}
		StreamResult result =  new StreamResult(new File(filename));
		transformer.transform(source, result);
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	}
	
	public void CreateTaggedOutput()
	{
	try{	
	
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(XMLDocumentTagged);

		StreamResult result =  new StreamResult(new File(filename));
		transformer.transform(source, result);
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Document getXMLDocument() {
		return XMLDocument;
	}
	public void setXMLDocument(Document xMLDocument) {
		XMLDocument = xMLDocument;
	}


	public Document getXMLDocumentTagged() {
		return XMLDocumentTagged;
	}


	public void setXMLDocumentTagged(Document xMLDocumentTagged) {
		XMLDocumentTagged = xMLDocumentTagged;
	}
}
