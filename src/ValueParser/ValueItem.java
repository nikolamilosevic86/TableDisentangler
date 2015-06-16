package ValueParser;

public class ValueItem {
	public int start_position;
	public int end_position;
	public String value;
	public enum ValueType{SINGLE,MATH,RANGE,ALTERNATIVES,GENDER_MALE,GENDER_FEMALE,PERCENTAGE,TEXT,INTEGER,FLOAT,ROMAN,MEASUREMENT_UNIT};
	public ValueType type;

}
