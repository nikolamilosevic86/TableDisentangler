package Tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import IEArmBased.ArmExtractor;
import InfoClassExtractor.InfoClassFilesReader;
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
		
		
		
	}
	


}
