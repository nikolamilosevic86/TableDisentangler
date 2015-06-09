package ValueParser;

public class ValueItem {
	public int start_position;
	public int end_position;
	public String value;
	public enum ValueType{SINGLE,MATH,RANGE,ALTERNATIVE_VAL,GENDER_MALE,GENDER_FEMALE,PERCENTAGE,TEXT};
	public ValueType type;

}
