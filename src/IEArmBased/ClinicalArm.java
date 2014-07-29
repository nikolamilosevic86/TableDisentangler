package IEArmBased;

import java.util.LinkedList;

public class ClinicalArm {
	private String PMC;
	private String DocumentName;
	private String ArmName;
	private int NoPatients;
	private String BMI;
	private String weight;
	private int NoMale;
	private int NoFemale;
	public LinkedList<ArmProperty> weights = new LinkedList<ArmProperty>();
	
	public ClinicalArm(String PMC, String DocumentName)
	{
		this.PMC = PMC;
		this.DocumentName = DocumentName;
	}
	public String getPMC() {
		return PMC;
	}
	public void setPMC(String pMC) {
		PMC = pMC;
	}
	public String getDocumentName() {
		return DocumentName;
	}
	public void setDocumentName(String documentName) {
		DocumentName = documentName;
	}
	public String getArmName() {
		return ArmName;
	}
	public void setArmName(String armName) {
		ArmName = armName;
	}
	public int getNoPatients() {
		return NoPatients;
	}
	public void setNoPatients(int noPatients) {
		NoPatients = noPatients;
	}
	public String getBMI() {
		return BMI;
	}
	public void setBMI(String bMI) {
		BMI = bMI;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public int getNoMale() {
		return NoMale;
	}
	public void setNoMale(int noMale) {
		NoMale = noMale;
	}
	public int getNoFemale() {
		return NoFemale;
	}
	public void setNoFemale(int noFemale) {
		NoFemale = noFemale;
	}

}
