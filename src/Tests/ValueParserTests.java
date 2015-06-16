package Tests;

import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.Test;

import ValueParser.ValueItem;
import ValueParser.ValueItem.ValueType;
import ValueParser.ValueParser;
import IEArmBased.ArmExtractor;

public class ValueParserTests {
	
	@Test
	public void CheckMath() {
		ValueParser parser = new ValueParser();
		LinkedList<ValueItem> list = parser.parseValue(">15");
		if(list.get(0).type!=ValueType.MATH)
			fail();
		if(!list.get(0).value.equals(">15"))
			fail();
	}
	
	@Test
	public void CheckSINGLE() {
		ValueParser parser = new ValueParser();
		LinkedList<ValueItem> list = parser.parseValue("15.2");
		if(list.get(0).type!=ValueType.SINGLE)
			fail();
		if(!list.get(0).value.equals("15.2"))
			fail();
	}
	
	@Test
	public void CheckRange() {
		ValueParser parser = new ValueParser();
		LinkedList<ValueItem> list = parser.parseValue("15.2 - 18.3");
		if(list.get(0).type!=ValueType.RANGE)
			fail();
		if(!list.get(0).value.equals("15.2 - 18.3"))
			fail();
	}
	
	@Test
	public void CheckNotRange() {
		ValueParser parser = new ValueParser();
		LinkedList<ValueItem> list = parser.parseValue("19.2 - 18.3");
		if(list!=null &&list.size()!=0&& list.get(0).type==ValueType.RANGE)
			fail();
	}
	
	@Test
	public void SingleAndRange() {
		ValueParser parser = new ValueParser();
		LinkedList<ValueItem> list = parser.parseValue("15.6 (12.2 - 18.3)");
		if(list!=null &&list.size()!=0&& list.get(0).type!=ValueType.SINGLE)
			fail();
		if(list!=null &&list.size()<1&& list.get(1).type!=ValueType.RANGE)
			fail();
	}
	
	@Test
	public void MeasurementUnit() {
		ValueParser parser = new ValueParser();
		LinkedList<ValueItem> list = parser.parseValue("15.6 mmol");
		if(list!=null &&list.size()!=0&& list.get(0).type!=ValueType.SINGLE)
			fail();
		if(list!=null &&list.size()<1&& list.get(1).type!=ValueType.FLOAT)
			fail();
		if(list!=null &&list.size()<1&& list.get(2).type!=ValueType.MEASUREMENT_UNIT)
			fail();
	}
	
	@Test
	public void SingleAndMath() {
		ValueParser parser = new ValueParser();
		LinkedList<ValueItem> list = parser.parseValue("15.6 (24 ± 3)");
		if(list!=null &&list.size()!=0&& list.get(0).type!=ValueType.SINGLE)
			fail();
		if(list!=null &&list.size()<1&& list.get(1).type!=ValueType.MATH)
			fail();
		if(list!=null &&list.size()<1&& list.get(2).type!=ValueType.FLOAT)
			fail();
	}
	
	@Test
	public void AlternativesTest() {
		ValueParser parser = new ValueParser();
		LinkedList<ValueItem> list = parser.parseValue("18 (15.6/12.3/123.3)");
		if(list!=null &&list.size()!=0&& list.get(0).type!=ValueType.SINGLE)
			fail();
		if(list!=null &&list.size()<1&& list.get(1).type!=ValueType.ALTERNATIVES)
			fail();
		if(list!=null &&list.size()<1&& list.get(2).type!=ValueType.INTEGER)
			fail();
		if(list!=null &&list.size()<1&& list.get(3).type!=ValueType.FLOAT)
			fail();
	}
	
	@Test
	public void SingleAndPerc() {
		ValueParser parser = new ValueParser();
		LinkedList<ValueItem> list = parser.parseValue("15.6 (18 %)");
		if(list!=null &&list.size()!=0&& list.get(0).type!=ValueType.SINGLE)
			fail();
		if(list!=null &&list.size()<1&& list.get(1).type!=ValueType.FLOAT)
			fail();
		if(list!=null &&list.size()<1&& list.get(1).type!=ValueType.PERCENTAGE)
			fail();
	}
	
	@Test
	public void ComplexPercAndRange() {
		ValueParser parser = new ValueParser();
		LinkedList<ValueItem> list = parser.parseValue("12%-18%");
		if(list!=null &&list.size()!=0&& list.get(0).type!=ValueType.RANGE)
			fail();
		if(list!=null &&list.size()<1&& list.get(1).type!=ValueType.PERCENTAGE)
			fail();
		if(list!=null &&list.size()<1&& list.get(2).type!=ValueType.PERCENTAGE)
			fail();
	}
	

}
