/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package InfoClassExtractor;

import java.util.LinkedList;

/**
 * The Class InfoClass. Class that contains data for extraction of information.
 */
public class InfoClass {

	enum ValueType{NUMERIC,SEMINUMERIC,STRING,OTHER}
	
	/** The name of the information class. */
	private String InformationClass;
	/** The type of the information class value. */
	private ValueType Type;
	/** The minimum value of the first approached value of identified value */
	private float FValueMin;
	/** The maximum value of the first approached value of identified value */
	private float FValueMax;
	/** List of the trigger words */
	public LinkedList<String> triggerWords;
	/** List of the words that stops identification of value */
	public LinkedList<String> StopWords;
	
	/**
	 * Instantiates a new info class.
	 *
	 * @param ClassName the class name
	 */
	public InfoClass(String ClassName)
	{
		InformationClass = ClassName;
	}
	
	
	/**
	 * @return the informationClass
	 */
	public String getInformationClass() {
		return InformationClass;
	}
	/**
	 * @param informationClass the informationClass to set
	 */
	public void setInformationClass(String informationClass) {
		InformationClass = informationClass;
	}
	/**
	 * @return the type
	 */
	public ValueType getType() {
		return Type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(ValueType type) {
		Type = type;
	}
	/**
	 * @return the fValueMin
	 */
	public float getFValueMin() {
		return FValueMin;
	}
	/**
	 * @param fValueMin the fValueMin to set
	 */
	public void setFValueMin(float fValueMin) {
		FValueMin = fValueMin;
	}
	/**
	 * @return the fValueMax
	 */
	public float getFValueMax() {
		return FValueMax;
	}
	/**
	 * @param fValueMax the fValueMax to set
	 */
	public void setFValueMax(float fValueMax) {
		FValueMax = fValueMax;
	}
}
