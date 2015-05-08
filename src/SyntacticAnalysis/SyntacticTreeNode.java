package SyntacticAnalysis;

public class SyntacticTreeNode {
	
	public SyntacticTreeNode parent;
	public SyntacticTreeNode leftNode;
	public SyntacticTreeNode rightNode;
	public String value;
	public String type;
	public boolean isSimpleType;
	public boolean isLeftNodeComplex;
	public boolean isRightNodeComplex;
	
	
	public boolean hasLeftNode()
	{
		if(leftNode==null)
			return false;
		else
			return true;
	}
	
	public boolean hasRightNode()
	{
		if(rightNode==null)
			return false;
		else
			return true;
	}
	
	public void Process()
	{
		
	}

}
