package Utils;

public class SingleDecomposedValue {
	private String type;//may be value, percent, range or mean_sd
	private ValueType int_type; 
	private String full_value;
	private String value;//only for the value and percent
	private String first_value; //only for the range and mean_sd
	private String second_value; //only for the range and mean_sd
	public enum ValueType {VALUE,PERCENT,RANGE,MEAN_SD,SEPARATED};
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFull_value() {
		return full_value;
	}
	public void setFull_value(String full_value) {
		this.full_value = full_value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getFirst_value() {
		return first_value;
	}
	public void setFirst_value(String first_value) {
		this.first_value = first_value;
	}
	public String getSecond_value() {
		return second_value;
	}
	public void setSecond_value(String second_value) {
		this.second_value = second_value;
	}
	public ValueType getInt_type() {
		return int_type;
	}
	public void setInt_type(ValueType int_type) {
		this.int_type = int_type;
	}

}
