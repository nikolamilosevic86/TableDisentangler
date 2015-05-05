/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package classifiers;

import Utils.Utilities;
import tablInEx.Table;
import tablInEx.Table.StructureType;

/**
 * The Class SimpleTableClassifier. Does couple of simple classifications of table
 * based on type and content of table.
 */
public class SimpleTableClassifier {
	
	private static String folder;
	private static String ImagesFolder;
	private static String BodylessFolder;
	private static String HeadlessFolder;
	private static String ColSpanFolder;
	private static String RowSpanFolder;
	private static String ProcessableTables;
	private static String SimplestFolder;
	private static String SimpleFolder;
	private static String MediumFolder;
	private static String ComplexFolder;
	private static String ListFolder;
	private static String MatrixFolder;
	private static String SuperRowFolder;
	private static String MultiTableFolder;
	
	
	/**
	 * Inits the.
	 *
	 * @param initFolder the init folder
	 */
	public static void init(String initFolder)
	{
		setFolder(initFolder);
		ImagesFolder = initFolder+"_image";
		BodylessFolder = initFolder + "_bodyless";
		HeadlessFolder = initFolder + "_headless";
		ColSpanFolder = initFolder + "_colspan";
		RowSpanFolder = initFolder + "_rowspan";
		ProcessableTables = initFolder + "_processable";
		
		Utilities.DeleteFolderWithContent(ImagesFolder);
		Utilities.DeleteFolderWithContent(BodylessFolder);
		Utilities.DeleteFolderWithContent(HeadlessFolder);
		Utilities.DeleteFolderWithContent(ColSpanFolder);
		Utilities.DeleteFolderWithContent(RowSpanFolder);
		Utilities.DeleteFolderWithContent(ProcessableTables);
		
		Utilities.MakeDirectory(ImagesFolder);
		Utilities.MakeDirectory(BodylessFolder);
		Utilities.MakeDirectory(HeadlessFolder);
		Utilities.MakeDirectory(ColSpanFolder);
		Utilities.MakeDirectory(RowSpanFolder);
		Utilities.MakeDirectory(ProcessableTables);
	}
	
	
	/**
	 * Inits the complexity classification.
	 *
	 * @param initFolder the init folder
	 */
	public static void initComplexity(String initFolder)
	{
		setFolder(initFolder);
		SimplestFolder = initFolder+"_simplest";
		SimpleFolder = initFolder+"_simple";
		MediumFolder = initFolder + "_medium";
		ComplexFolder = initFolder + "_complex";
		
		ListFolder= initFolder+"_list";
		MatrixFolder= initFolder+"_matrix";
		SuperRowFolder= initFolder+"_superrow";;
		MultiTableFolder= initFolder+"_multitable";;
		
		
		Utilities.DeleteFolderWithContent(SimplestFolder);
		Utilities.DeleteFolderWithContent(SimpleFolder);
		Utilities.DeleteFolderWithContent(MediumFolder);
		Utilities.DeleteFolderWithContent(ComplexFolder);
		
		Utilities.DeleteFolderWithContent(ListFolder);
		Utilities.DeleteFolderWithContent(MatrixFolder);
		Utilities.DeleteFolderWithContent(SuperRowFolder);
		Utilities.DeleteFolderWithContent(MultiTableFolder);
		
		Utilities.MakeDirectory(SimplestFolder);
		Utilities.MakeDirectory(SimpleFolder);
		Utilities.MakeDirectory(MediumFolder);
		Utilities.MakeDirectory(ComplexFolder);
		
		Utilities.MakeDirectory(ListFolder);
		Utilities.MakeDirectory(MatrixFolder);
		Utilities.MakeDirectory(SuperRowFolder);
		Utilities.MakeDirectory(MultiTableFolder);
	}
	
	/**
	 * Classify table by complexity.
	 *
	 * @param t the t
	 */
	//TODO: Rewrite classifier
	public static void ClassifyTableByComplexity(Table t)
	{
		if(t==null)
			return;
		
		String tableFileName = "/"+t.getDocumentFileName()+t.getTable_title()+".xml.html";
		
		if(t.getTableStructureType() == StructureType.LIST)
		{
			Utilities.WriteFile(ListFolder+tableFileName, t.getXml());
		}
		
		if(t.getTableStructureType() == StructureType.MATRIX)
		{
			Utilities.WriteFile(MatrixFolder+tableFileName, t.getXml());
		}
		
		if(t.getTableStructureType() == StructureType.SUBHEADER)
		{
			Utilities.WriteFile(SuperRowFolder+tableFileName, t.getXml());
		}
		
		if(t.getTableStructureType() == StructureType.MULTI)
		{
			Utilities.WriteFile(MultiTableFolder+tableFileName, t.getXml());
		}
		
		if(t.stat.getNum_of_header_rows()<5 && t.isHasHeader() && !t.isNoXMLTable() && t.stat.getNum_of_empty_cells()==0 && t.stat.getNum_of_part_numeric_cells()==0)
		{
			if(t.getStructureClass()==0)
				t.setStructureClass(1);
			Utilities.WriteFile(SimplestFolder+tableFileName, t.getXml());
		}
		else if(t.stat.getNum_of_header_rows()<15 && (t.stat.getNum_of_empty_cells()==t.stat.getHeader_empty_cells()||t.isEmptyOnlyHeaders) && t.isHasHeader() && !t.isNoXMLTable())
		{
			t.setStructureClass(2);
			Utilities.WriteFile(SimpleFolder+tableFileName, t.getXml());
		}
		//else if(( (float)t.stat.getNum_of_empty_cells() / t.stat.getNum_of_cells())<0.85 && t.stat.getNum_of_header_rows()<5 && t.stat.getNum_of_part_numeric_cells()<15 &&  t.isHasHeader() && !t.isNoXMLTable())
		else if( t.isHasHeader() && !t.isNoXMLTable())
		{
			if(t.getStructureClass()==0)
				t.setStructureClass(3);
			Utilities.WriteFile(MediumFolder+tableFileName, t.getXml());
		}
		else
		{
			if(t.getStructureClass()==0)
				t.setStructureClass(4);
			Utilities.WriteFile(ComplexFolder+tableFileName, t.getXml());
		}
	}
	
	
	/**
	 * Classify table by type.
	 *
	 * @param t the t
	 */
	public static void ClassifyTableByType(Table t)
	{
		if(t==null)
			return;
		
		String tableFileName = "/"+t.getDocumentFileName()+t.getTable_title()+".xml";
		
		if(t.isNoXMLTable())
		{
			Utilities.WriteFile(ImagesFolder+tableFileName, t.getXml());
		}
		
		if(t.isHasBody()==false)
		{
			Utilities.WriteFile(BodylessFolder+tableFileName, t.getXml());
		}
		
		if(t.isHasHeader()==false)
		{
			Utilities.WriteFile(HeadlessFolder+tableFileName, t.getXml());
		}
		
		if(t.isColSpanning()==true)
		{
			Utilities.WriteFile(ColSpanFolder+tableFileName, t.getXml());
		}
		
		if(t.isRowSpanning()==true)
		{
			Utilities.WriteFile(RowSpanFolder+tableFileName, t.getXml());
		}
		
		if(!t.isNoXMLTable()&&t.isHasBody()!=false)
		{
			Utilities.WriteFile(ProcessableTables+tableFileName, t.getXml());
		}
		
	}
	
	public static void setFolder(String folder) {
		SimpleTableClassifier.folder = folder;
	}
	
	

}
