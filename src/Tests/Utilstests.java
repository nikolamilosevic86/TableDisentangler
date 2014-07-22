package Tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import Utils.NumberFormat;
import Utils.Utilities;

public class Utilstests {

	@Test
	public void test() {
		LinkedList<NumberFormat> nf = Utilities.getNumsAndFormats("abc 15");
		
		if(!nf.get(0).getFormat().equals("SingleVal"))
			fail("Wrong format");
		if(!nf.get(0).getNumber().equals("15"))
			fail("Wrong number");
		
		nf = Utilities.getNumsAndFormats("15");
		
		if(!nf.get(0).getFormat().equals("SingleVal"))
			fail("Wrong format");
		if(!nf.get(0).getNumber().equals("15"))
			fail("Wrong number");
		if(nf.get(0).getValue()!=15)
			fail("Wrong value");
		nf = Utilities.getNumsAndFormats("15-18");
		if(!nf.get(0).getFormat().equals("Range"))
			fail("Wrong format");
		if(!nf.get(0).getNumber().equals("15-18"))
			fail("Wrong number");
		
		nf = Utilities.getNumsAndFormats("abc 15-18");
		if(!nf.get(0).getFormat().equals("Range"))
			fail("Wrong format");
		if(!nf.get(0).getNumber().equals("15-18"))
			fail("Wrong number");
		
		nf = Utilities.getNumsAndFormats("15 (15-18)");
		if(!nf.get(0).getFormat().equals("SingleVal"))
			fail("Wrong format");
		if(nf.size()!=2)
			fail("More elements");
		if(!nf.get(0).getNumber().equals("15"))
			fail("Wrong number");
		if(!nf.get(1).getFormat().equals("Range"))
			fail("Wrong format");
		if(!nf.get(1).getNumber().equals("15-18"))
			fail("Wrong number");
		
		nf = Utilities.getNumsAndFormats("15 (30%)");
		if(!nf.get(0).getFormat().equals("SingleVal"))
			fail("Wrong format");
		if(nf.size()!=2)
			fail("More elements");
		if(!nf.get(0).getNumber().equals("15"))
			fail("Wrong number");
		if(!nf.get(1).getFormat().equals("Percent"))
			fail("Wrong format");
		if(!nf.get(1).getNumber().equals("30%"))
			fail("Wrong number");
		
		nf = Utilities.getNumsAndFormats("abc 15 (30%) 12 cde");
		if(nf.size()!=3)
			fail("More elements");
		if(!nf.get(0).getFormat().equals("SingleVal"))
			fail("Wrong format");

		if(!nf.get(0).getNumber().equals("15"))
			fail("Wrong number");
		if(!nf.get(1).getFormat().equals("Percent"))
			fail("Wrong format");
		if(!nf.get(1).getNumber().equals("30%"))
			fail("Wrong number");
		if(!nf.get(2).getFormat().equals("SingleVal"))
			fail("Wrong format");

		if(!nf.get(2).getNumber().equals("12"))
			fail("Wrong number");
	}

}
