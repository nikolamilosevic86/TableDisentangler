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
			
//			Element affiliations = doc.createElement("Affiliations");
//			for(int i = 0; i<a.getAffiliation().length;i++){
//				Element affiliation = doc.createElement("Affiliation");
//				affiliation.setTextContent(a.getAffiliation()[i]);
//				affiliations.appendChild(affiliation);
//			}
//			rootElement.appendChild(affiliations);
			
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
