package Utils;

public class NumberFormat {
	private String number;
	private String format;
	private float value;
	
	public NumberFormat(String num,String form)
	{
		this.number = num.replace(" ", "");
		this.format = form;
		try{
			value = Float.parseFloat(num);
		}
		catch(Exception ex)
		{
			value = 0;
		}
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}

}
