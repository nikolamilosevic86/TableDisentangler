package ValueParser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ValueParser.ValueItem.ValueType;

public class ValueParser {
	
	public LinkedList<ValueItem> valueList = new LinkedList<ValueItem>();
	
	
	public LinkedList<ValueItem> parseCompex (String value)
	{
		LinkedList<ValueItem> VL = new LinkedList<ValueItem>();
		int start = -1;
		int end = -1;
		int move = 0;
		String value2 = value;
		for(int s=0;s<value.length();s++)
		{
			boolean found_circle = false;
			value2 = value2.substring(move);
			if(move>1)
				s+=move-1;
			move = 0;
			String patternString = "^[<>≥≤±]{1}[0-9]*[.]{0,1}[0-9]*[ ±]*[0-9]*[.]{0,1}[0-9]*";
	        Pattern pattern = Pattern.compile(patternString);
	        Matcher matcher = pattern.matcher(value2);
	        if(!found_circle&&matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();    
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.MATH;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        }
	        
			patternString = "^[<>≥≤±]*[0-9]*[.·]{0,1}[0-9]*[ ]*[±]{1}[ ]*[0-9]*[.·]{0,1}[0-9]*";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        if(!found_circle&&matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();     
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.MATH;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        }
	        
	        patternString = "^[-—–−]*[ ]*\\d{1,}[.·]*\\d*[ ]*[%]{0,1}[ ]*([-—–−,;:]|to|and)[ ]*\\d*[.·]*\\d*[ ]*[%]{0,1}[ ]*";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        
	        if(!found_circle&&matcher.find())
	        {
	        	float start_val = 999;
	        	float end_val = -999;
	        	start = matcher.start();
	        	end= matcher.end();
	        	String val = value2.substring(start, end);
	        	String[] vals = val.split("[-—–−,]|to|and");
	        	for(int l = 0;l<vals.length;l++)
	        	{
	        		vals[l] = vals[l].replace("%", "");
	        	}
	        	start_val = Float.parseFloat(vals[0]);
	        	end_val = Float.parseFloat(vals[1]);
	        	if(start_val<end_val){      
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.RANGE;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        	}
	        }
	        	
		        patternString = "^[-—–−+]{0,1}[ ]*(\\d*[.· ]{0,1}\\d{1,}|[Ii]{1,3}|[iI][vV]|[Vv][Ii]{1,3}|[Vv])";
		        pattern = Pattern.compile(patternString);
		        matcher = pattern.matcher(value2);
		        if(!found_circle&&matcher.find())
		        {
		        	start = matcher.start();
		        	end= matcher.end();       
		        	ValueItem vi = new ValueItem();
		        	vi.start_position = start;
		        	vi.end_position = end;
		        	vi.value = value.substring(start, end);
		        	vi.type = ValueType.SINGLE;
		        	VL.add(vi);
		        	move+=end;
		        	found_circle = true;
		        }
	        
	        
	       
	        
	        if(move == 0)
	        	move = 1;
	        
		}
		
		return VL;
	}
	
	public LinkedList<ValueItem> parseSimple(String value)
	{
		LinkedList<ValueItem> VL = new LinkedList<ValueItem>();
		int start = -1;
		int end = -1;
		int move = 0;
		String value2 = value;
	    for(int s=0;s<value.length();s++)
		{
			boolean found_circle = false;
			value2 = value2.substring(move);
			if(move>1)
				s+=move-1;
			move = 0;
			String patternString = "^[-—–−+]{0,1}[ ]*(\\d*[.· ]{0,1}\\d{1,})[  ]*[%]";
			Pattern pattern = Pattern.compile(patternString);
		    Matcher  matcher = pattern.matcher(value2);
	        if(!found_circle&&matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();       
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.PERCENTAGE;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        }
	        
	        patternString = "^[Ii]{1,3}|[iI][vV]|[Vv][Ii]{0,3}";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        if(!found_circle&&matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();       
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.ROMAN;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        }
	        
	        patternString = "^\\d*[\\.]\\d*";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        if(!found_circle&&matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();       
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.FLOAT;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        }
	        
	        patternString = "^\\d{1,}";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        if(!found_circle&&matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();       
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.INTEGER;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        }
	        if(move == 0)
	        	move = 1;
		}
	    return VL;
	}
	
	
	public LinkedList<ValueItem> parseValue(String value)
	{
		valueList.addAll(parseCompex(value));
		valueList.addAll(parseSimple(value));
				
		return valueList;
	}

}
