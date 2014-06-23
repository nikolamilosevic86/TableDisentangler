/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package IEData;

import java.util.LinkedList;

import tablInEx.Utilities;

import org.apache.commons.lang3.StringUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class CandidateIEObject.
 */
public class CandidateIEObject {
	
	/** The value. */
	private String value;
	
	private boolean isValueFromNav;
	
	/** The Stub val. */
	private String StubVal;
	
	/** The Header val. */
	private String HeaderVal;
	
	/** The Head00 val. */
	private String Head00Val;
	
	/** The Navigation values. */
	private String NavigationValues;
	
	/** The is usable. */
	private boolean isUsable;
	
	/** The Pattern string. */
	private String PatternString;
	
	private int NumOfOccurrences;
	
	/** The Normalized pattern. */
	private String NormalizedPattern;
	
	/** The Levenstein distances. */
	private LinkedList<Integer> LevensteinDistances = new LinkedList<Integer>();
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the stub val.
	 *
	 * @return the stub val
	 */
	public String getStubVal() {
		return StubVal;
	}
	
	/**
	 * Sets the stub val.
	 *
	 * @param stubVal the new stub val
	 */
	public void setStubVal(String stubVal) {
		StubVal = stubVal;
	}
	
	/**
	 * Gets the header val.
	 *
	 * @return the header val
	 */
	public String getHeaderVal() {
		return HeaderVal;
	}
	
	/**
	 * Sets the header val.
	 *
	 * @param headerVal the new header val
	 */
	public void setHeaderVal(String headerVal) {
		HeaderVal = headerVal;
	}
	
	/**
	 * Gets the head00 val.
	 *
	 * @return the head00 val
	 */
	public String getHead00Val() {
		return Head00Val;
	}
	
	/**
	 * Sets the head00 val.
	 *
	 * @param head00Val the new head00 val
	 */
	public void setHead00Val(String head00Val) {
		Head00Val = head00Val;
	}
	
	/**
	 * Checks if is usable.
	 *
	 * @return true, if is usable
	 */
	public boolean isUsable() {
		return isUsable;
	}
	
	/**
	 * Sets the usable.
	 *
	 * @param isUsable the new usable
	 */
	public void setUsable(boolean isUsable) {
		this.isUsable = isUsable;
	}
	
	/**
	 * Gets the pattern string.
	 *
	 * @return the pattern string
	 */
	public String getPatternString() {
		return PatternString;
	}
	
	/**
	 * Sets the pattern string.
	 *
	 * @param patternString the new pattern string
	 */
	public void setPatternString(String patternString) {
		PatternString = patternString;
	}
	
	/**
	 * Creates the pattern string.
	 */
	public void createPatternString()
	{
		String pattern = "";
		
		for(int i = 0; i<value.length();i++)
		{
			if(Utilities.isNumeric(value.charAt(i)+""))
			{
				pattern+="N";
			}
			else
			{
				pattern+=value.charAt(i)+"";
			}
		}
		
		PatternString = pattern;
	}
	
	/**
	 * Calculate levenshtein. Calculation should be done with normalized pattern.
	 *
	 * @param pattern the pattern
	 * @return the int
	 */
	public int calculateLevenshtein(String pattern)
	{
		return StringUtils.getLevenshteinDistance(NormalizedPattern,pattern);
	}
	
	/**
	 * Gets the levenstein distances.
	 *
	 * @return the levenstein distances
	 */
	public LinkedList<Integer> getLevensteinDistances() {
		return LevensteinDistances;
	}
	
	/**
	 * Sets the levenstein distances.
	 *
	 * @param levensteinDistances the new levenstein distances
	 */
	public void setLevensteinDistances(LinkedList<Integer> levensteinDistances) {
		LevensteinDistances = levensteinDistances;
	}
	
	/**
	 * Gets the normalized pattern.
	 *
	 * @return the normalized pattern
	 */
	public String getNormalizedPattern() {
		return NormalizedPattern;
	}
	
	/**
	 * Sets the normalized pattern.
	 *
	 * @param normalizedPattern the new normalized pattern
	 */
	public void setNormalizedPattern(String normalizedPattern) {
		NormalizedPattern = normalizedPattern;
	}
	
	/**
	 * Normalize pattern. Creates patter from ie. NN (NN.N-NN.N) to N* (N*.N-N*.N) or NNN -> N*
	 */
	public void NormalizePattern()
	{
		String pattern = "";
		for(int i = 0; i<PatternString.length();i++)
		{
			if(i>0)
			{
				if(pattern.charAt(pattern.length()-1) == 'N')
				{
					pattern+="*";
				}
				if(pattern.charAt(pattern.length()-1) == '*' && PatternString.charAt(i) == 'N')
				{
					//Do nothing, don't add anything to pattern. Pattern stays as is.
				}
				if(PatternString.charAt(i)!='N')
				{
					pattern+=PatternString.charAt(i)+"";
				}
				if(PatternString.charAt(i)=='N' && pattern.charAt(pattern.length()-1)!='N'&&pattern.charAt(pattern.length()-1)!='*')
				{
					pattern+=PatternString.charAt(i)+"";
				}
			}
			else
			{
				if(PatternString.charAt(i)=='N')
				{
					pattern+=PatternString.charAt(i)+"*";
				}
				else{
				pattern+=PatternString.charAt(i)+"";
				}
			}
		}
		NormalizedPattern = pattern;
	}

	public String getNavigationValues() {
		return NavigationValues;
	}

	public void setNavigationValues(String navigationValues) {
		NavigationValues = navigationValues;
	}

	public int getNumOfOccurrences() {
		return NumOfOccurrences;
	}

	public void setNumOfOccurrences(int numOfOccurrences) {
		NumOfOccurrences = numOfOccurrences;
	}

	public boolean isValueFromNav() {
		return isValueFromNav;
	}

	public void setValueFromNav(boolean isValueFromNav) {
		this.isValueFromNav = isValueFromNav;
	}
	

}
