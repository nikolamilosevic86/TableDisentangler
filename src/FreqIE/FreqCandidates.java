package FreqIE;

import java.util.HashMap;

public class FreqCandidates {
	private String AllText;
	private String NavigationString;
	private String ValueString;
	private HashMap<String,Integer> counter = new HashMap<String,Integer>();
	private int count;
	
	public String getAllText() {
		return AllText;
	}
	public void setAllText(String allText) {
		AllText = allText;
	}
	public String getNavigationString() {
		return NavigationString;
	}
	public void setNavigationString(String navigationString) {
		NavigationString = navigationString;
	}
	public String getValueString() {
		return ValueString;
	}
	public void setValueString(String valueString) {
		ValueString = valueString;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public HashMap<String,Integer> getCounter() {
		return counter;
	}
	public void setCounter(HashMap<String,Integer> counter) {
		this.counter = counter;
	}
	

}
