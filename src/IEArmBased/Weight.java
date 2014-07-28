package IEArmBased;

public class Weight {

	private String WeightType;//BMI, Weight, Weight change, BMI change
	private String value;
	private String unit;
	private String AdditionalInfo;
	
	public String getWeightType() {
		return WeightType;
	}
	public void setWeightType(String weightType) {
		WeightType = weightType;
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
