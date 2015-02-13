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
	private boolean canBeInCaption = false;
	private boolean canBeInFooter = false;
	public LinkedList<String> FreeTextPatterns = new LinkedList<String>();
	private boolean canBeInNavigationalCells = false;
	public LinkedList<String> NavigationalPatterns = new LinkedList<String>();
	private boolean canBeInDataCells = false;
	public LinkedList<String> DataCellPatterns = new LinkedList<String>();
	
	public LinkedList<InfoClassExtractionRule> FreeTextRules = new LinkedList<InfoClassExtractionRule>();
	public LinkedList<InfoClassExtractionRule> NavigationalRules = new LinkedList<InfoClassExtractionRule>();
	public LinkedList<InfoClassExtractionRule> DataCellRules = new LinkedList<InfoClassExtractionRule>();
	
	/** List of the trigger words for data cells. Value lookup is done in navigational cells, but extract value from data cell  */
	public LinkedList<String> triggerWords;
	/** List of the words that stops identification of value in data cells (it will look for these values in navigation cells, 
	 * but extract values from data cells) */
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


	/**
	 * @return the canBeInCaption
	 */
	public boolean isCanBeInCaption() {
		return canBeInCaption;
	}


	/**
	 * @param canBeInCaption the canBeInCaption to set
	 */
	public void setCanBeInCaption(boolean canBeInCaption) {
		this.canBeInCaption = canBeInCaption;
	}


	/**
	 * @return the canBeInFooter
	 */
	public boolean isCanBeInFooter() {
		return canBeInFooter;
	}


	/**
	 * @param canBeInFooter the canBeInFooter to set
	 */
	public void setCanBeInFooter(boolean canBeInFooter) {
		this.canBeInFooter = canBeInFooter;
	}


	/**
	 * @return the canBeInNavigationalCells
	 */
	public boolean isCanBeInNavigationalCells() {
		return canBeInNavigationalCells;
	}


	/**
	 * @param canBeInNavigationalCells the canBeInNavigationalCells to set
	 */
	public void setCanBeInNavigationalCells(boolean canBeInNavigationalCells) {
		this.canBeInNavigationalCells = canBeInNavigationalCells;
	}


	/**
	 * @return the canBeInDataCells
	 */
	public boolean isCanBeInDataCells() {
		return canBeInDataCells;
	}


	/**
	 * @param canBeInDataCells the canBeInDataCells to set
	 */
	public void setCanBeInDataCells(boolean canBeInDataCells) {
		this.canBeInDataCells = canBeInDataCells;
	}
}
