package FreqIE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.text.Utilities;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tablInEx.Article;
import tablInEx.DataExtractionOutputObj;
import tablInEx.Table;
import weka.core.stemmers.SnowballStemmer;

public class FreqExtractor {
	LinkedList<String> extractList;
	
	public LinkedList<String> makeExtractList(String filePath)
	{
		String content;
		LinkedList<String> extractList = new LinkedList<String>();
		File file = new File(filePath);
		try {
			Scanner sc = new Scanner(new FileInputStream(file));
			while (sc.hasNextLine()) {
				content = sc.nextLine();
				SnowballStemmer porter = new SnowballStemmer();
				content = porter.stem(content);
				System.out.println(content);
				extractList.add(content);
			}
			sc.close();
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\nProgram terminated Safely...");
		}
		return extractList;
	}
	public LinkedList<FreqCandidates> getCandidates(LinkedList<DataExtractionOutputObj> extdata)
	{
		 LinkedList<FreqCandidates> candidates = new LinkedList<FreqCandidates>();
		 SnowballStemmer porter = new SnowballStemmer();
		 try{
			InputStream modelIn1 = new FileInputStream("en-token.bin");
			TokenizerModel model1 = new TokenizerModel(modelIn1);
			Tokenizer tokenizer = new TokenizerME(model1);
			
		 for(DataExtractionOutputObj ext: extdata)
		 {
			 
			 	String allText = "";
			 	String navValue = "";
			 	NodeList Head00 = ext.getXMLDocument().getElementsByTagName("Head00");
				NodeList HeaderValue = ext.getXMLDocument().getElementsByTagName("HeaderValue");
				NodeList Stub = ext.getXMLDocument().getElementsByTagName("Stub");
				NodeList value = ext.getXMLDocument().getElementsByTagName("value");
				if(Head00!=null && Head00.item(0)!=null){
					allText += " " + Head00.item(0).getTextContent();
					navValue+=" " + Head00.item(0).getTextContent();
				}
				if(HeaderValue!=null && HeaderValue.item(0)!=null){
					allText += " " + HeaderValue.item(0).getTextContent();
					navValue+=" " + HeaderValue.item(0).getTextContent();
				}
				for(int i = 0;i<Stub.getLength();i++)
				{
					allText += " " + Stub.item(i).getTextContent();
					navValue+=" " + Stub.item(i).getTextContent();
				}
				if(value!=null && value.item(0)!=null)
					allText += " " + value.item(0).getTextContent();
				allText = Utils.Utilities.ReplaceNonBrakingSpaceToSpace(allText).toLowerCase().replace('\'', ' ');
				navValue = Utils.Utilities.ReplaceNonBrakingSpaceToSpace(navValue);

				String sent[] = tokenizer.tokenize(allText);
				int i = 0;
				for(String s:sent)
				{
					sent[i] = porter.stem(sent[i]);
					i++;
				}
				for(String s:sent)
				{
					if(extractList.contains(s))
					{
						FreqCandidates cand = new FreqCandidates();
						cand.setAllText(allText);
						if(value!=null && value.item(0)!=null)
							cand.setValueString(value.item(0).getTextContent());
						cand.setNavigationString(navValue);
						
						candidates.add(cand);
						break;
					}
				}


		 }
		 
				for (FreqCandidates cands : candidates) {
					String sent[] = tokenizer.tokenize(cands.getAllText());
					int i = 0;
					for(String s:sent)
					{
						sent[i] = porter.stem(sent[i]);
						i++;
					}
					for(String s:sent)
					{
						if (extractList.contains(s)) {
							if(cands.getCounter().get(s)!=null)
								cands.getCounter().put(s, cands.getCounter().get(s).intValue()+1);
							else
							{
								cands.getCounter().put(s, 1);
							}
					}
				}
				}
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
		 
		 return candidates;
	}
	
	public LinkedList<FreqCandidates> getProbableCandidate(LinkedList<FreqCandidates> candidates)
	{
		LinkedList<FreqCandidates> freq = new LinkedList<FreqCandidates>();
		int max = 0;
		int index = -1;
		for(FreqCandidates cand:candidates){
			for(String s:cand.getCounter().keySet())
			{
				cand.setCount(cand.getCount()+cand.getCounter().get(s).intValue());
			}
			if(max<cand.getCount())
				max = cand.getCount();
		}
		for(int i=0;i<candidates.size();i++)
		{
			if(max==candidates.get(i).getCount())
			{
				freq.add(candidates.get(i));
			}
		}
		System.out.println("Max = "+max);
		return freq;
	}
	
	public int getPatientNumber(Article art)
	{
		extractList = makeExtractList("NumOfPatientList.txt");
		Table[] tables = art.getTables();
		int numOfPatients = 0;
		for(Table table:tables)
		{
			LinkedList<DataExtractionOutputObj> extractedData = table.output;
			LinkedList<FreqCandidates> candidates = getCandidates(extractedData);
			LinkedList<FreqCandidates> cand = getProbableCandidate(candidates);
			for(int i = 0;i<cand.size();i++)
				System.out.println("Candidate for "+art.getFile_name()+":"+cand.get(i).getAllText());
			//break;
		}
		return numOfPatients;
	}

}
