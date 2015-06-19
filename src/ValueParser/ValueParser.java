package ValueParser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import ValueParser.ValueItem.ValueType;

public class ValueParser {
	
	public LinkedList<ValueItem> valueList = new LinkedList<ValueItem>();
	Tokenizer tokenizer;
	
	public ValueParser(){
		try{
		InputStream is = new FileInputStream("en-token.bin");

		TokenizerModel model = new TokenizerModel(is);
 
		tokenizer = new TokenizerME(model);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public LinkedList<ValueItem> parseCompex (String value)
	{
		LinkedList<ValueItem> VL = new LinkedList<ValueItem>();
		int start = -1;
		int end = -1;
		int move = 0;
		String value2 = value;
		value2 = value2.substring(move);
		move = 0;
		String patternString = "(\\b|^)[<>≥≤±]{1}[0-9]*[.]{0,1}[0-9]*[ ±]*[0-9]*[.]{0,1}[0-9]*\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(value2);
        while(matcher.find())
        {
	        	start = matcher.start();
	        	end= matcher.end();    
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value2.substring(start, end);
	        	vi.type = ValueType.MATH;
	        	VL.add(vi);
	     }
	        

	        
	        
			patternString = "(\\b|^)[<>≥≤±]*[0-9]*[.·]{0,1}[0-9]*[ ]*[±]{1}[ ]*[0-9]*[.·]{0,1}[0-9]*\\b";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        while(matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();   
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value2.substring(start, end);
	        	vi.type = ValueType.MATH;
	        	VL.add(vi);
	        }
	        
	        patternString = "(\\b|^)[-—–−]*[ ]*\\d{1,}[\\.·]{0,1}\\d*[ \\t]*[%]{0,1}[ \\t]*([\\\\\\/])[ \\t]*\\d{1,}[.·]*\\d*[ \\t]*[%]{0,1}[ \\t]*([\\\\\\/]){0,1}[ \\t]*\\d*[.·]*\\d*[ \\t]*[%]{0,1}[ \\t]*([\\\\\\/]){0,1}[ \\t]*\\d*[\\.·]*\\d*[ \\t]*[%]{0,1}[ \\t]*([\\\\\\/]){0,1}[ \\t]*\\d*[\\.·]*\\d*[ \\t]*[%]{0,1}\\b";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        
	        while(matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end(); 
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value2.substring(start, end);
	        	vi.type = ValueType.ALTERNATIVES;
	        	VL.add(vi);
	        	
	        }
	        
	        	        
	        patternString = "(\\b|^)[-—–−]*[ ]*\\d{1,}[.·]*\\d*[ ]*[%]{0,1}[ ]*([-—–−,;:]|to|and)[ ]*\\d*[.·]*\\d*[ ]*[%]{0,1}[ ]*\\b";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        
	        while(matcher.find())
	        {
	        	float start_val = 999;
	        	float end_val = -999;
	        	start = matcher.start();
	        	end= matcher.end();
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
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value2.substring(start, end);
	        	vi.type = ValueType.RANGE;
	        	VL.add(vi);
	        	}
	        	}
	        	}
	        }
	        	
		        patternString = "(\\b|^)[-—–−+]{0,1}[ ]*(\\d{1,}[\\.· ]{0,1}\\d{1,}|\\d{1,}|[Ii]{1,3}|[iI][vV]|[Vv][Ii]{1,3}|[Vv])\\b";
		        pattern = Pattern.compile(patternString);
		        matcher = pattern.matcher(value2);
		        while(matcher.find())
		        {
		        	start = matcher.start();
		        	end= matcher.end(); 
		        	ValueItem vi = new ValueItem();
		        	vi.start_position = start;
		        	vi.end_position = end;
		        	vi.value = value2.substring(start, end);
		        	vi.type = ValueType.SINGLE;
		        	VL.add(vi);
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
		value2 = value2.substring(move);
		move = 0;
		String patternString = "(\\b|^)[-—–−+]{0,1}[ ]*(\\d*[\\.· ]{0,1}\\d{1,})[  ]*[%]\\b";
		Pattern pattern = Pattern.compile(patternString);
		Matcher  matcher = pattern.matcher(value2);
	    while(matcher.find())
	    {
	       	start = matcher.start();
	       	end= matcher.end();   
	        ValueItem vi = new ValueItem();
	        vi.start_position = start;
	        vi.end_position = end;
	        vi.value = value2.substring(start, end);
	        vi.type = ValueType.PERCENTAGE;
	       	VL.add(vi);
	        }
	        
	        patternString = "(\\b|^)([Ii]{1,3}|[iI][vV]|[Vv][Ii]{0,3})\\b";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        while(matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();   
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value2.substring(start, end);
	        	vi.type = ValueType.ROMAN;
	        	VL.add(vi);
	        }
	        
	        patternString = "(\\b|^)\\d{1,}[\\.]{0,}\\d{0,}\\b";
	        pattern = Pattern.compile(patternString);
	        matcher = pattern.matcher(value2);
	        while(matcher.find())
	        {
	        	start = matcher.start();
	        	end= matcher.end();   
	        	ValueItem vi = new ValueItem();
	        	vi.start_position = start;
	        	vi.end_position = end;
	        	vi.value = value2.substring(start, end);
	        	try{
	        		int integer = Integer.parseInt(vi.value);
	        		vi.type = ValueType.INTEGER;
	        	}catch(Exception ex)
	        	{
	        		vi.type = ValueType.FLOAT;
	        	}
	        	
	        	VL.add(vi);
	        }
	        		
	    return VL;
	}
	
	public LinkedList<ValueItem> parseString(String value) {
		LinkedList<ValueItem> VL = new LinkedList<ValueItem>();
		boolean found_circle = false;
		int start = -1;
		int end = -1;
		int start_pos = 0;
		int end_pos = 0;
		int move = 0;
		String value2 = value;
		String[] tokens = tokenizer.tokenize(value);
		String patternString = "(\\b|^)(Joule|°C|mmol\\\\L|month[s]*|mmol|mg/kg|mg m−2|mg/m2|mmHg|ug/ml|U/ml|year[s]|day[s]|cmH2O|pts|cm/s|cm2/s|min|sec|kg/m2|per day|ml|μl|g|µg/l|kg|yr|mo[\\.]*|mg/ml|yr|d|mg|cm|mm|um|ns|hr|Hz|h|mW/cm2|mW|m)\\b";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(value);
		while ( matcher.find()) {
			start_pos = matcher.start();
			end_pos = matcher.end();
			ValueItem vi = new ValueItem();
			vi.start_position = start_pos;
			vi.end_position = end_pos;
			vi.value =  value2.substring(start, end);
			vi.type = ValueType.MEASUREMENT_UNIT;
			VL.add(vi);
			}
		

		return VL;
	}
	
	
	public LinkedList<ValueItem> parseValue(String value)
	{
		if(value!=null&&!value.equals("")){
		valueList.addAll(parseCompex(value));
		valueList.addAll(parseSimple(value));
		valueList.addAll(parseString(value));
		} 
				
		return valueList;
	}

}
