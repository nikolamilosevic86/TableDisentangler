package IEData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.NodeList;

import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.DataExtractionOutputObj;
import tablInEx.Table;
import tablInEx.Utilities;

public class PatientNumExtractor {

	public static int getPatientNumber(Article art)
	{
		
		int NumOfPatients = 0;
		LinkedList<CandidateIEObject> PatientNumCandidates;// = new LinkedList<CandidateIEObject>();
		for(Table t :art.getTables())
		{
			// Candidates are just for table
			PatientNumCandidates = new LinkedList<CandidateIEObject>();
			LinkedList<DataExtractionOutputObj> list = t.output;
			//XML documents was not retrieved since table is not formatted well
			if(list==null || list.size()==0)
			{
				Cell[][] cells = t.cells;
				String tableName = t.getTable_caption();
				if(tableName.toLowerCase().contains("n=")||tableName.toLowerCase().contains("n ="))
				{
					Pattern regex2 = Pattern.compile("\\bn[ ]{0,1}=[ ]{0,1}[0-9]*\\b");
				    Matcher regexMatcher2 = regex2.matcher(tableName.toLowerCase());
				    if(regexMatcher2.find()){
					int n = Utilities.getFirstValue(regexMatcher2.group());
					return n;
				    }
				}
				
			}
			for(DataExtractionOutputObj obj:list)
			{
				CandidateIEObject cand = new CandidateIEObject();
				NodeList Head00 = obj.getXMLDocument().getElementsByTagName("Head00");
				NodeList HeaderValue = obj.getXMLDocument().getElementsByTagName("HeaderValue");
				NodeList Stub = obj.getXMLDocument().getElementsByTagName("Stub");
				NodeList value = obj.getXMLDocument().getElementsByTagName("value");
				NodeList caption = obj.getXMLDocument().getElementsByTagName("tableName");
				String Head00Value = "";
				String Header = "";
				String stubVal = "";
				String Vvalue = "";
				String tableName = "";
				if(caption!=null && caption.item(0)!=null)
					tableName = caption.item(0).getTextContent();
				if(tableName.toLowerCase().contains("n=")||tableName.toLowerCase().contains("n ="))
				{
					Pattern regex2 = Pattern.compile("\\bn[ ]{0,1}=[ ]{0,1}[0-9]*\\b");
				    Matcher regexMatcher2 = regex2.matcher(tableName.toLowerCase());
				    if(regexMatcher2.find()){
					int n = Utilities.getFirstValue(regexMatcher2.group());
					return n;
				    }
				}
				if(Head00!=null && Head00.item(0)!=null)
					Head00Value = Head00.item(0).getTextContent();
				if(HeaderValue!=null && HeaderValue.item(0)!=null)
					Header = HeaderValue.item(0).getTextContent();
				for(int i=0;i<Stub.getLength();i++)
				{
					if(Stub!=null && Stub.item(i)!=null)
						stubVal+=";"+ Stub.item(i).getTextContent();
				}
				if(value!=null && value.item(0)!=null)
				{
					Vvalue = value.item(0).getTextContent();
				}
				cand.setValue(Utilities.ReplaceNonBrakingSpaceToSpace(Vvalue));
				cand.setValueFromNav(false);
				cand.setNavigationValues(Utilities.ReplaceNonBrakingSpaceToSpace(Header+";"+Head00Value+";"+stubVal));
				String nav = cand.getNavigationValues();
				if((nav.toLowerCase().contains("number of patients") ||nav.toLowerCase().contains("patient number")|| nav.toLowerCase().contains("no of patients")|| nav.toLowerCase().contains("no. of patients")||nav.toLowerCase().contains("total")|| nav.toLowerCase().contains("n of patients")||nav.toLowerCase().contains("n=")||nav.toLowerCase().contains("n =") || (nav.toLowerCase().contains("total") && nav.toLowerCase().contains("patients") && !nav.toLowerCase().contains("male"))))
				{
					cand.setHead00Val(Head00Value);
					cand.setHeaderVal(Header);
					cand.setStubVal(stubVal);
					cand.setUsable(true);
					cand.createPatternString();
					cand.NormalizePattern();
					if(nav.toLowerCase().contains("n=")||nav.toLowerCase().contains("n ="))
					{
						Pattern regex3 = Pattern.compile("\\bn[ ]{0,1}=[ ]{0,1}[0-9]*\\b");
					    Matcher regexMatcher3 = regex3.matcher(nav.toLowerCase());
					    if(regexMatcher3.find()){
						int n = Utilities.getFirstValue(regexMatcher3.group());
						cand.setValue(n+"");
						cand.setValueFromNav(true);
					}
					}
					int numOfOccurrences = 0;
					int temp = Utilities.GetNumOfOccurrences(nav, "number of patients");
					if(temp>numOfOccurrences)
						numOfOccurrences = temp;
					temp = Utilities.GetNumOfOccurrences(nav, "patient number");
					if(temp>numOfOccurrences)
						numOfOccurrences = temp;
					temp = Utilities.GetNumOfOccurrences(nav, "no of patients");
					if(temp>numOfOccurrences)
						numOfOccurrences = temp;
					temp = Utilities.GetNumOfOccurrences(nav, "no. of patients");
					if(temp>numOfOccurrences)
						numOfOccurrences = temp;
					temp = Utilities.GetNumOfOccurrences(nav, "n of patients");
					if(temp>numOfOccurrences)
						numOfOccurrences = temp;
					temp = Utilities.GetNumOfOccurrences(nav, "total");
					if(temp>numOfOccurrences)
						numOfOccurrences = temp;
					temp = Utilities.GetNumOfOccurrences(nav, "n=");
					if(temp>numOfOccurrences)
						numOfOccurrences = temp;
					temp = Utilities.GetNumOfOccurrences(nav, "n =");
					if(temp>numOfOccurrences)
						numOfOccurrences = temp;
					cand.setNumOfOccurrences(numOfOccurrences);
					PatientNumCandidates.add(cand);
				}
				
			}// end of loop trough documents
			
			int maxNumOfOccurrences = 0;
			for(int i = 0;i<PatientNumCandidates.size();i++)
			{
				if(maxNumOfOccurrences<PatientNumCandidates.get(i).getNumOfOccurrences())
					maxNumOfOccurrences = PatientNumCandidates.get(i).getNumOfOccurrences();
			}
			LinkedList<CandidateIEObject> temp =  new LinkedList<CandidateIEObject>();
			for(int i = 0;i<PatientNumCandidates.size();i++)
			{
				if(PatientNumCandidates.get(i).getNumOfOccurrences()==maxNumOfOccurrences && !PatientNumCandidates.get(i).getNavigationValues().toLowerCase().contains("male"))
				{
					temp.add(PatientNumCandidates.get(i));
				}
			}
			PatientNumCandidates = temp;
			
			for(int i = 0;i<PatientNumCandidates.size();i++)
			{
				LinkedList<Integer> LevenSteinDistances = new LinkedList<Integer>();
				for(int j = 0;j< PatientNumCandidates.size();j++)
				{
					int LevenStein = PatientNumCandidates.get(i).calculateLevenshtein(PatientNumCandidates.get(j).getNormalizedPattern());
					
					//This 2 patterns are basically the same. In brackets is some 
					//not so important value, so we will read first one
					if(PatientNumCandidates.get(j).getNormalizedPattern().equals("N* (N*)")||PatientNumCandidates.get(j).getNormalizedPattern().equals("N*(N*)"))
					{
						int LS = PatientNumCandidates.get(i).calculateLevenshtein("N*");
						if(LS<LevenStein)
						{
							LevenStein = LS;
						}
					}
					//This 2 patterns are basically the same. In brackets is some 
					//not so important value, so we will read first one
					if(PatientNumCandidates.get(i).getNormalizedPattern().equals("N* (N*)")||PatientNumCandidates.get(i).getNormalizedPattern().equals("N*(N*)"))
					{
						//We backup and than return pattern
						String patternBackup = PatientNumCandidates.get(i).getNormalizedPattern();
						PatientNumCandidates.get(i).setNormalizedPattern("N*");
						int LS = PatientNumCandidates.get(i).calculateLevenshtein("N*");
						PatientNumCandidates.get(i).setNormalizedPattern(patternBackup);
						if(LS<LevenStein)
						{
							LevenStein = LS;
						}
					}
					LevenSteinDistances.add(LevenStein);
				}
				PatientNumCandidates.get(i).setLevensteinDistances(LevenSteinDistances);
			}
			LinkedList<CandidateIEObject> validCandidates =  new LinkedList<CandidateIEObject>();
			//Clean candidates that has Levenstein distance from first candidate larger than 0
			if(PatientNumCandidates!=null && PatientNumCandidates.size()>0){
				for(int i = 0;i<PatientNumCandidates.get(0).getLevensteinDistances().size();i++)
				{
					if(PatientNumCandidates.get(0).getLevensteinDistances().get(i)==0)
					{
						validCandidates.add(PatientNumCandidates.get(i));
					}
				}
			}
			LinkedList<String> readNavs = new LinkedList<String>();
			boolean overall = false;
			for(CandidateIEObject obj:validCandidates)
			{
				if(obj.isValueFromNav())
				{
					boolean shouldContinue = false;
					for(String s : readNavs)
					{
						if(s.equals(obj.getHead00Val()+";"+obj.getHeaderVal()))
						{
							shouldContinue = true;
							break;
						}
					}
					if(shouldContinue)
						continue;
				}
				if((obj.getNavigationValues().toLowerCase().contains("number of patients")||obj.getNavigationValues().toLowerCase().contains("patients")||obj.getNavigationValues().toLowerCase().contains("patients number")||obj.getNavigationValues().toLowerCase().contains("no of patients")||obj.getNavigationValues().toLowerCase().contains("no"))&&(obj.getNavigationValues().toLowerCase().contains("total")||obj.getNavigationValues().toLowerCase().contains("all")))
				{
					NumOfPatients = Utilities.getFirstValue(obj.getValue());
					break;
				}
				else
				{
					if(obj.getNavigationValues().toLowerCase().contains("overall")){
					NumOfPatients +=  Utilities.getFirstValue(obj.getValue());
					overall = true;
					}
					else
					{
						if(overall == true)
							break;
						NumOfPatients +=  Utilities.getFirstValue(obj.getValue());
					}
				}
				if(obj.isValueFromNav())
				{
					readNavs.add(obj.getHead00Val()+";"+obj.getHeaderVal());
				}

			}
			
			if(NumOfPatients>0)
				break;
		} // end of loop trough tables
		
		
		return NumOfPatients;
	}
}
