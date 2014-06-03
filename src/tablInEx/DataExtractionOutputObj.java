package tablInEx;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class DataExtractionOutputObj {
	private String filename;
	private Document XMLDocument;
	private Document XMLDocumentTagged;
	
	public DataExtractionOutputObj(String f, Document doc)
	{
		filename = f;
		XMLDocument = doc;
	}
	
	public void CreateOutput()
	{
	try{	
	
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(XMLDocument);

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
