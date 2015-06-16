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
		int start_pos = 0;
		int end_pos = 0;
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
	        	start_pos+=start;
	        	end_pos+=end;
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start_pos;
	        	vi.end_position = end_pos;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.MATH;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        }
	        
	        //(Joule|°C|mmol\\L|month[s]*|mg/kg|mg m−2||mg/m2|mmHg|ug/ml|year[s]*|day[s]*|cmH2O|pts|cm/s|cm2/s|min|sec|kg/m2|per day|ml|μl|g|µg/l|kg|yr|mo[\.]*|mg/ml|yr|d|m|mg|cm|mm|um|ns|hr|Hz|h|mW/cm2|mW)
	        patternString = "^(Joule|°C|mmol\\\\L|month[s]*|mmol|mg/kg|mg m−2|mg/m2|mmHg|ug/ml|year[s]|day[s]|cmH2O|pts|cm/s|cm2/s|min|sec|kg/m2|per day|ml|μl|g|µg/l|kg|yr|mo[\\.]*|mg/ml|yr|d|mg|cm|mm|um|ns|hr|Hz|h|mW/cm2|mW|m)\\b";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        if(!found_circle&&matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();    
	        	start_pos+=start;
	        	end_pos+=end;
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start_pos;
	        	vi.end_position = end_pos;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.MEASUREMENT_UNIT;
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
	        	start_pos+=start;
	        	end_pos+=end;
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start_pos;
	        	vi.end_position = end_pos;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.MATH;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        }
	        
	        //ALTERNATIVES ^[-—–−]*[ ]*\d{1,}[.·]{0,1}\d*[ ]*[%]{0,1}[ ]*([\\\/])[ ]*\d*[.·]*\d*[ ]*[%]{0,1}[ ]*([\\\/]){0,1}[ ]*\d*[.·]*\d*[ ]*[%]{0,1}[ ]*([\\\/]){0,1}[ ]*\d*[.·]*\d*[ ]*[%]{0,1}[ ]*([\\\/]){0,1}[ ]*\d*[.·]*\d*[ ]*[%]{0,1}
	        patternString = "^[-—–−]*[ ]*\\d{1,}[.·]{0,1}\\d*[ ]*[%]{0,1}[ ]*([\\\\\\/])[ ]*\\d*[.·]*\\d*[ ]*[%]{0,1}[ ]*([\\\\\\/]){0,1}[ ]*\\d*[.·]*\\d*[ ]*[%]{0,1}[ ]*([\\\\\\/]){0,1}[ ]*\\d*[.·]*\\d*[ ]*[%]{0,1}[ ]*([\\\\\\/]){0,1}[ ]*\\d*[.·]*\\d*[ ]*[%]{0,1}\\\\b";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        
	        if(!found_circle&&matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();       
	        	start_pos+=start;
	        	end_pos+=end;
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start_pos;
	        	vi.end_position = end_pos;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.ALTERNATIVES;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        	
	        }
	        
	        	        
	        patternString = "^[-—–−]*[ ]*\\d{1,}[.·]*\\d*[ ]*[%]{0,1}[ ]*([-—–−,;:]|to|and)[ ]*\\d*[.·]*\\d*[ ]*[%]{0,1}[ ]*(\\)|\\\\b)";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        
	        if(!found_circle&&matcher.find())
	        {
	        	float start_val = 999;
	        	float end_val = -999;
	        	start = matcher.start();
	        	end= matcher.end();
	        	start_pos+=start;
	        	end_pos+=end;
	        	boolean isFloat = true;
	        	String val = value2.substring(start, end);
	        	String[] vals = val.split("[-—–−,;:]|to|and");
	        	for(int l = 0;l<vals.length;l++)
	        	{
	        		vals[l] = vals[l].replace("%", "");
	        	}
	        	if(vals.length>1&&vals[0]!=null&&!vals[0].equals("")&&vals[1]!=null&&!vals[1].equals("")){
	        	try{
	        	start_val = Float.parseFloat(vals[0]);
	        	end_val = Float.parseFloat(vals[1]);
	        	}
	        	catch(Exception ex){
	        		isFloat = false;
	        	}
	        	if(isFloat){
	        	if(start_val<end_val){      
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start_pos;
	        	vi.end_position = end_pos;
	        	vi.value = value.substring(start, end);
	        	vi.type = ValueType.RANGE;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        	}
	        	}
	        	}
	        }
	        	
		        patternString = "^[-—–−+]{0,1}[ ]*(\\d{1,}[\\.· ]{0,1}\\d{1,}|[Ii]{1,3}|[iI][vV]|[Vv][Ii]{1,3}|[Vv])\\\\b";
		        pattern = Pattern.compile(patternString);
		        matcher = pattern.matcher(value2);
		        if(!found_circle&&matcher.find())
		        {
		        	start = matcher.start();
		        	end= matcher.end();       
		        	start_pos+=start;
		        	end_pos+=end;
		        	ValueItem vi = new ValueItem();
		        	vi.start_position = start_pos;
		        	vi.end_position = end_pos;
		        	vi.value = value.substring(start, end);
		        	vi.type = ValueType.SINGLE;
		        	VL.add(vi);
		        	move+=end;
		        	found_circle = true;
		        }
	        
	        
	       
	        
	        if(move == 0){
	        	move = 1;
	        	end_pos++;
	        }
	        start_pos +=move;
	        
		}
		
		return VL;
	}
	
	public LinkedList<ValueItem> parseSimple(String value)
	{
		LinkedList<ValueItem> VL = new LinkedList<ValueItem>();
		int start = -1;
		int end = -1;
		int start_pos = 0;
		int end_pos = 0;
		int move = 0;
		String value2 = value;
	    for(int s=0;s<value.length();s++)
		{
			boolean found_circle = false;
			value2 = value2.substring(move);
			if(move>1)
				s+=move-1;
			move = 0;
			String patternString = "^[-—–−+]{0,1}[ ]*(\\d*[\\.· ]{0,1}\\d{1,})[  ]*[%]";
			Pattern pattern = Pattern.compile(patternString);
		    Matcher  matcher = pattern.matcher(value2);
	        if(!found_circle&&matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();   
	        	start_pos+=start;
	        	end_pos+=end;
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start_pos;
	        	vi.end_position = end_pos;
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
	        	start_pos+=start;
	        	end_pos+=end;
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start_pos;
	        	vi.end_position = end_pos;
	        	vi.value = value2.substring(start, end);
	        	vi.type = ValueType.ROMAN;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        }
	        
	        patternString = "^\\d{1,}[\\.]\\d{1,}";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        if(!found_circle&&matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();   
	        	start_pos+=start;
	        	end_pos+=end;
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start_pos;
	        	vi.end_position = end_pos;
	        	vi.value = value2.substring(start, end);
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
	        	start_pos+=start;
	        	end_pos+=end;
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start_pos;
	        	vi.end_position = end_pos;
	        	vi.value = value2.substring(start, end);
	        	vi.type = ValueType.INTEGER;
	        	VL.add(vi);
	        	move+=end;
	        	found_circle = true;
	        }
	        if(move == 0){
	        	move = 1;
	        	end_pos++;
	        }
	        start_pos +=move;
	       
		}
	    return VL;
	}
	
	
	public LinkedList<ValueItem> parseValue(String value)
	{
		if(value!=null&&!value.equals("")){
		valueList.addAll(parseCompex(value));
		valueList.addAll(parseSimple(value));
		} 
				
		return valueList;
	}

}
