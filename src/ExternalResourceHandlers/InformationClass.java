package ExternalResourceHandlers;
import java.util.LinkedList;


public class InformationClass {

	private String class_name;
	private String default_unit;
	private String value_range;
	private double min_value = 999; //if there is no min value min_value = 999
	private double max_value = 999; //if there is no max value max_value = 999
	private String value_type;
	private String where;
	private int vicinity;
	public LinkedList<String> MatchList = new LinkedList<String>();
	public LinkedList<String> BlackList = new LinkedList<String>();
	public LinkedList<String> WhiteList = new LinkedList<String>();
	public LinkedList<ResourceRule> RuleList = new LinkedList<ResourceRule>();
	/**
	 * @return the class_name
	 */
	public String getClass_name() {
		return class_name;
	}
	/**
	 * @param class_name the class_name to set
	 */
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	/**
	 * @return the default_unit
	 */
	public String getDefault_unit() {
		return default_unit;
	}
	/**
	 * @param default_unit the default_unit to set
	 */
	public void setDefault_unit(String default_unit) {
		this.default_unit = default_unit;
	}
	/**
	 * @return the value_range
	 */
	public String getValue_range() {
		return value_range;
	}
	/**
	 * @param value_range the value_range to set
	 */
	public void setValue_range(String value_range) {
		this.value_range = value_range;
	}
	/**
	 * @return the min_value
	 */
	public double getMin_value() {
		return min_value;
	}
	/**
	 * @param min_value the min_value to set
	 */
	public void setMin_value(double min_value) {
		this.min_value = min_value;
	}
	/**
	 * @return the max_value
	 */
	public double getMax_value() {
		return max_value;
	}
	/**
	 * @param max_value the max_value to set
	 */
	public void setMax_value(double max_value) {
		this.max_value = max_value;
	}
	/**
	 * @return the value_type
	 */
	public String getValue_type() {
		return value_type;
	}
	/**
	 * @param value_type the value_type to set
	 */
	public void setValue_type(String value_type) {
		this.value_type = value_type;
	}
	/**
	 * @return the where
	 */
	public String getWhere() {
		return where;
	}
	/**
	 * @param where the where to set
	 */
	public void setWhere(String where) {
		this.where = where;
	}
	/**
	 * @return the vicinity
	 */
	public int getVicinity() {
		return vicinity;
	}
	/**
	 * @param vicinity the vicinity to set
	 */
	public void setVicinity(int vicinity) {
		this.vicinity = vicinity;
	}

}
