package Tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import IEArmBased.ArmExtractor;
import InfoClassExtractor.InfoClassFilesReader;
import InfoClassExtractor.TypeParser;
import Utils.DecomposedValue;
import Utils.NumberFormat;
import Utils.SingleDecomposedValue;
import Utils.Utilities;

public class InfoClassTests {
	
	
	
	@Test
	public void ReadInfoClassesTest() {
		String path = "InformationClasses";
		InfoClassFilesReader fr = new InfoClassFilesReader();
		fr.ReadInfoClasses(path);
		if(fr.InfoClasses.size()!=2)
			fail("Number of extracted information classes is not correct");
		if(!fr.InfoClasses.get(0).getInformationClass().equals("BMI"))
			fail("Information class name not correctly read for the first list item");
		if(!fr.InfoClasses.get(1).getInformationClass().equals("BMIA"))
			fail("Information class name not correctly read for the second list item");
		if(!fr.InfoClasses.get(0).triggerWords.get(0).equals("bmi"))
			fail("Trigger Word not read correctly");
		if(!fr.InfoClasses.get(1).triggerWords.get(0).equals("bmi"))
			fail("Trigger Word not read correctly");
		if(!fr.InfoClasses.get(0).StopWords.get(0).equals("change"))
			fail("Stop Word not read correctly");	
		
		if(!fr.InfoClasses.get(0).NavigationalPatterns.get(0).equals("N=%d"))
			fail("NavigationalPatterns not read correctly");	
		if(!fr.InfoClasses.get(0).FreeTextPatterns.get(0).equals("%d patients[3]"))
			fail("FreeTextPatterns not read correctly");
		if(!fr.InfoClasses.get(0).DataCellPatterns.get(0).equals("%f"))
			fail("DataCellPatterns not read correctly");
		if(!fr.InfoClasses.get(0).isCanBeInCaption())
			fail("Failed reading can be in caption");
		
		//%f
	}
	
	
	@Test
	public void TypeParserTest() {
		String s = TypeParser.getSemiNumeric("abc 17");
		if(!s.equals("17"))
			fail();
		s = TypeParser.getSemiNumeric("abc 17.72 (12-19)");
		if(!s.equals("17.72 (12-19)"))
			fail();
		s = TypeParser.getSemiNumeric("abc 17.72 (12-19) asdwe");
		if(!s.equals("17.72 (12-19)"))
			fail();
		s = TypeParser.getSemiNumeric("abc 17.72 (12/19) asdwe");
		if(!s.equals("17.72 (12/19)"))
			fail();
		s = TypeParser.getSemiNumeric("abc 17.72 (-12 ± 19) asdwe");
		if(!s.equals("17.72 (-12 ± 19)"))
			fail();
	}


}
