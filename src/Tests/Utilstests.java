package Tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import IEArmBased.ArmExtractor;
import Utils.DecomposedValue;
import Utils.NumberFormat;
import Utils.SingleDecomposedValue;
import Utils.Utilities;

public class Utilstests {
	
	//clearContent(String CellContent){
	@Test
	public void ClearContentTest() {
		ArmExtractor ae = new ArmExtractor();
		String s = ae.clearContent("Paracetamol SD".toLowerCase());
		if(!s.equals("paracetamol "))
			fail();
	}
	
	@Test
	public void ValueDecompositionTest() {
		String testVal = "16";
		DecomposedValue dv = new DecomposedValue(testVal);
		if(!dv.getInitial_value().equals("16"))
			fail("Initial Value not right");
		if(!dv.getPattern().equals("dv_value"))
			fail("Pattern not right");
		if(dv.values.size()!=1)
			fail("Wrong number of single decomposed values");
		if(dv.values.get(0).getInt_type()!=SingleDecomposedValue.ValueType.VALUE)
			fail("Wrong Value type");
		if(!dv.values.get(0).getType().equals("value"))
			fail("Wrong Value type");
		if(!dv.values.get(0).getValue().equals("16"))
			fail("Wrong Value");
		
		testVal = "16 (14-18)";
		dv = new DecomposedValue(testVal);
		if(!dv.getInitial_value().equals("16 (14-18)"))
			fail("Initial Value not right");
		if(!dv.getPattern().equals("dv_value (dv_range)"))
			fail("Pattern not right");
		if(dv.values.size()!=2)
			fail("Wrong number of single decomposed values");
		if(dv.values.get(0).getInt_type()!=SingleDecomposedValue.ValueType.VALUE)
			fail("Wrong Value type");
		if(!dv.values.get(0).getType().equals("value"))
			fail("Wrong Value type");
		if(!dv.values.get(0).getValue().equals("16"))
			fail("Wrong Value");
		
		if(dv.values.get(1).getInt_type()!=SingleDecomposedValue.ValueType.RANGE)
			fail("Wrong Value type");
		if(!dv.values.get(1).getType().equals("range"))
			fail("Wrong Value type");
		if(!dv.values.get(1).getFull_value().equals("14-18"))
			fail("Wrong Value");
		if(!dv.values.get(1).getFirst_value().equals("14"))
			fail("Wrong First Value");
		if(!dv.values.get(1).getSecond_value().equals("18"))
			fail("Wrong Second Value");
		
		testVal = "16 (14:18)";
		dv = new DecomposedValue(testVal);
		if(!dv.getInitial_value().equals("16 (14:18)"))
			fail("Initial Value not right");
		if(!dv.getPattern().equals("dv_value (dv_separated)"))
			fail("Pattern not right");
		if(dv.values.size()!=2)
			fail("Wrong number of single decomposed values");
		if(dv.values.get(0).getInt_type()!=SingleDecomposedValue.ValueType.VALUE)
			fail("Wrong Value type");
		if(!dv.values.get(0).getType().equals("value"))
			fail("Wrong Value type");
		if(!dv.values.get(0).getValue().equals("16"))
			fail("Wrong Value");
		
		if(dv.values.get(1).getInt_type()!=SingleDecomposedValue.ValueType.SEPARATED)
			fail("Wrong Value type");
		if(!dv.values.get(1).getType().equals("separated"))
			fail("Wrong Value type");
		if(!dv.values.get(1).getFull_value().equals("14:18"))
			fail("Wrong Value");
		if(!dv.values.get(1).getFirst_value().equals("14"))
			fail("Wrong First Value");
		if(!dv.values.get(1).getSecond_value().equals("18"))
			fail("Wrong Second Value");
		
		testVal = "16 (20%)";
		dv = new DecomposedValue(testVal);
		if(!dv.getInitial_value().equals("16 (20%)"))
			fail("Initial Value not right");
		if(!dv.getPattern().equals("dv_value (dv_percent)"))
			fail("Pattern not right");
		if(dv.values.size()!=2)
			fail("Wrong number of single decomposed values");
		if(dv.values.get(0).getInt_type()!=SingleDecomposedValue.ValueType.VALUE)
			fail("Wrong Value type");
		if(!dv.values.get(0).getType().equals("value"))
			fail("Wrong Value type");
		if(!dv.values.get(0).getValue().equals("16"))
			fail("Wrong Value");
		
		if(dv.values.get(1).getInt_type()!=SingleDecomposedValue.ValueType.PERCENT)
			fail("Wrong Value type");
		if(!dv.values.get(1).getType().equals("percent"))
			fail("Wrong Value type");
		if(!dv.values.get(1).getFull_value().equals("20%"))
			fail("Wrong Value");
		if(!dv.values.get(1).getValue().equals("20"))
			fail("Wrong First Value");
	}
	
	
	
	

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
