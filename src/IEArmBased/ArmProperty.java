package IEArmBased;

public class ArmProperty {

	private String Type;//BMI, Weight, Weight change, BMI change
	private String value;
	private String unit;
	private String AdditionalInfo;
	
	public String getType() {
		return Type;
	}
	public void setType(String weightType) {
		Type = weightType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAdditionalInfo() {
		return AdditionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		AdditionalInfo = additionalInfo;
	}
	
}
