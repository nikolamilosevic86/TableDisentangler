package Utils;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecomposedValue {
	private String initial_value;
	private String pattern;
	public LinkedList<SingleDecomposedValue> values = new LinkedList<SingleDecomposedValue>();
	private String rangeRegEx = "\\b[0-9]*[\\.]{0,1}[0-9]{0,}[ ]{0,1}[-–,][ ]{0,1}[0-9]{0,}[\\.]{0,1}[0-9]{0,}\\b";
	private String separatedRegEx = "\\b[0-9]*[\\.]{0,1}[0-9]{0,}[ ]{0,1}[://][ ]{0,1}[0-9]{0,}[\\.]{0,1}[0-9]{0,}\\b";
	private String percentRegEx = "[\\.]{0,1}[0-9]{1,}[\\.]{0,1}[0-9]{1,}[ ]{0,1}[%]";
	private String mean_sdRegEx = "[\\.]{0,1}[0-9]*[\\.]{0,1}[0-9]{0,}[ ]{0,1}[±][ ]{0,1}[0-9]{0,}[\\.]{0,1}[0-9]{0,}";
	private String valueRegEx = "[\\.]{0,1}[0-9]*[\\.]{0,1}[0-9]{0,}";
	public String findDV = "dv_[a-z]*";
	
	public DecomposedValue(String value)
	{
		initial_value = value;
		pattern = initial_value;
		//Find occurences
		pattern = FindOccuringValueType(rangeRegEx,pattern,"dv_range",SingleDecomposedValue.ValueType.RANGE,"range");
		pattern = FindOccuringValueType(separatedRegEx,pattern,"dv_separated",SingleDecomposedValue.ValueType.SEPARATED,"separated");       
		pattern = FindOccuringValueType(percentRegEx,pattern,"dv_percent",SingleDecomposedValue.ValueType.PERCENT,"percent");
		pattern = FindOccuringValueType(mean_sdRegEx,pattern,"dv_mean_sd",SingleDecomposedValue.ValueType.MEAN_SD,"mean_sd");
		pattern = FindOccuringValueType(valueRegEx,pattern,"dv_value",SingleDecomposedValue.ValueType.VALUE,"value");
        //Order them in a way of appearance
        values = sortSingleDecomposedValueListByOccurance(values);     		
	}
	
	private String FindOccuringValueType(String regex,String currentPattern,String DV_Value,SingleDecomposedValue.ValueType valueType,String type)
	{
		Pattern r = Pattern.compile(regex);
	    Matcher m = r.matcher(currentPattern);
        while(m.find()&&!m.group().equals("")) {
        	currentPattern = currentPattern.replace(m.group(), DV_Value);
        	SingleDecomposedValue sdv = new SingleDecomposedValue();
        	sdv.setInt_type(valueType);
        	sdv.setType(type);
        	sdv.setFull_value(m.group());
        	String[] vals;
        	switch(valueType){
        	case RANGE:
        		vals = m.group().split("-|–|,");
            	if(vals.length==2){
            		sdv.setFirst_value(vals[0]);
            		sdv.setSecond_value(vals[1]);
            	}
            	break;
        	case SEPARATED:
        		vals = m.group().split(":|//");
            	if(vals.length==2){
            		sdv.setFirst_value(vals[0]);
            		sdv.setSecond_value(vals[1]);
            	}
            	break;
        	case MEAN_SD:
        		vals = m.group().split("±");
            	if(vals.length==2){
            		sdv.setFirst_value(vals[0]);
            		sdv.setSecond_value(vals[1]);
            	}
            	break;
        	case PERCENT:
        		sdv.setFull_value(m.group());
            	sdv.setValue(m.group().replace("%", ""));
            	break;
        	case VALUE:
        		sdv.setFull_value(m.group());
            	sdv.setValue(m.group());
            	break;
        	}
        	
        	values.add(sdv);
        	
        }
        return currentPattern;
	}
	
	private LinkedList<SingleDecomposedValue> sortSingleDecomposedValueListByOccurance(LinkedList<SingleDecomposedValue> values)
	{
		LinkedList<SingleDecomposedValue> sdv2 = new LinkedList<SingleDecomposedValue>();
		Pattern r = Pattern.compile(findDV);
	    Matcher m = r.matcher(pattern);
	    while(m.find()) {
	    	if(m.group().equals("dv_value")){
	    		for(int i = 0;i<values.size();i++)
	    		{
	    			if(values.get(i).getInt_type()==SingleDecomposedValue.ValueType.VALUE)
	    			{
	    				sdv2.add(values.get(i));
	    				break;
	    			}
	    		}
	    	}
	    	if(m.group().equals("dv_mean_sd")){
	    		for(int i = 0;i<values.size();i++)
	    		{
	    			if(values.get(i).getInt_type()==SingleDecomposedValue.ValueType.MEAN_SD)
	    			{
	    				sdv2.add(values.get(i));
	    				break;
	    			}
	    		}
	    	}
	    	if(m.group().equals("dv_percent")){
	    		for(int i = 0;i<values.size();i++)
	    		{
	    			if(values.get(i).getInt_type()==SingleDecomposedValue.ValueType.PERCENT)
	    			{
	    				sdv2.add(values.get(i));
	    				break;
	    			}
	    		}
	    	}
	    	if(m.group().equals("dv_separated")){
	    		for(int i = 0;i<values.size();i++)
	    		{
	    			if(values.get(i).getInt_type()==SingleDecomposedValue.ValueType.SEPARATED)
	    			{
	    				sdv2.add(values.get(i));
	    				break;
	    			}
	    		}
	    	}
	    	if(m.group().equals("dv_range")){
	    		for(int i = 0;i<values.size();i++)
	    		{
	    			if(values.get(i).getInt_type()==SingleDecomposedValue.ValueType.RANGE)
	    			{
	    				sdv2.add(values.get(i));
	    				break;
	    			}
	    		}
	    	}
	    }
	    return sdv2;
	}
	
	public String getInitial_value() {
		return initial_value;
	}
	public void setInitial_value(String initial_value) {
		this.initial_value = initial_value;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
