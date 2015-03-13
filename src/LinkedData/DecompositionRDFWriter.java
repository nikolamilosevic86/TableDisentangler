package LinkedData;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class DecompositionRDFWriter {
	String ArticleDefault    = "http://inspiratron.org/Articles#";
	Model model;
	Property ArticleName;
	Property ArticleID;
	Property TableOrder;
	Property TableCaption;
	Property TableFooter;
	Property Table;
	Property TableXML;
	Property TableType;
	Property TablePragmaticType;
	Property Cell;
	Property NavigationalPath;
	Property CellStub;
	Property CellStubValue;
	Property CellSubheadeing;
	Property CellHeader;
	Property CellValue;
	Property CellType;
	Property CellRow;
	Property CellColumn;
	Property HasNavigationalPath;
	Property Head00;
	Property HasArticle;
	String outputFileName = "decomposition.rdf";
	Resource currentArticle;
	Resource currentTable;
	Resource currentCell;
	Resource Root;

	public DecompositionRDFWriter()
	{
		model = ModelFactory.createDefaultModel();
		ArticleName = model.createProperty( ArticleDefault + "ArticleName" );
		ArticleID = model.createProperty( ArticleDefault + "ArticleID" );
		TableOrder = model.createProperty( ArticleDefault + "TableOrder" );
		TableCaption = model.createProperty( ArticleDefault + "TableCaption" );
		TableFooter = model.createProperty( ArticleDefault + "TableFooter" );
		Table = model.createProperty( ArticleDefault + "HasTable" );
		TableType = model.createProperty( ArticleDefault + "TableType" );
		TablePragmaticType = model.createProperty( ArticleDefault + "TablePragmaticType" );
		TableXML= model.createProperty( ArticleDefault + "TableXML" );
		Cell = model.createProperty( ArticleDefault + "HasCell" );
		CellStub = model.createProperty( ArticleDefault + "CellStub" );
		CellStubValue = model.createProperty( ArticleDefault + "CellStubValue" );
		CellSubheadeing = model.createProperty( ArticleDefault + "CellSubheadeing" );
		CellHeader = model.createProperty( ArticleDefault + "CellHeader" );
		CellValue = model.createProperty( ArticleDefault + "CellValue" );
		CellType = model.createProperty( ArticleDefault + "CellType" );
		CellRow = model.createProperty( ArticleDefault + "CellRow" );
		HasNavigationalPath = model.createProperty( ArticleDefault + "HasNavigationalPath" );
		CellColumn = model.createProperty( ArticleDefault + "CellColumn" );
		Head00 = model.createProperty( ArticleDefault + "Head00" );
		NavigationalPath = model.createProperty( ArticleDefault + "NavigationalPath" );
		Root = model.createResource(ArticleDefault+"Root");
		HasArticle = model.createProperty( ArticleDefault + "HasArticle" );
	}
	
	public void AddArticle(String ArticleNameS,String ArticleIDS)
	{
		Resource Article = model.createResource(ArticleDefault+ArticleIDS);
		model.add(Article,ArticleName,ArticleNameS);
		model.add(Article,ArticleID,ArticleIDS);
		model.add(Root, HasArticle,Article);
		currentArticle = Article;
	}
	
	public void AddTable(String TableOrderS,String TableCaptionS,String TableTypeS, String TableTypePragmaticS, String TableFooterS,String TableXMLS)
	{
		String TableID = UUID.randomUUID().toString();
		Resource TableS = model.createResource(ArticleDefault+"Table-"+TableID);
		model.add(TableS,TableOrder,TableOrderS);
		model.add(TableS,TableCaption,TableCaptionS);
		model.add(TableS,TableFooter,TableFooterS);
		model.add(TableS,TableType,TableTypeS);
		model.add(TableS,TablePragmaticType,TableTypePragmaticS);
		model.add(TableS,TableXML,TableXMLS);
		model.add(currentArticle,Table,TableS);
		currentTable = TableS;
	}
	
	public void AddCell(String StubValueS,String[] SubHeadings,String valueS, String CellTypeS,String[] HeaderS,String Head00S, int Row, int Columnt )
	{
		String CellID = UUID.randomUUID().toString();
		Resource CellS = model.createResource(ArticleDefault+"Cell-"+CellID);
		model.add(CellS,CellValue,valueS);
		model.add(CellS,CellType,CellTypeS);
		Resource NavigationalPath = model.createResource();
		model.add(NavigationalPath,Head00,Head00S);
		if(HeaderS!=null)
		for(int i = 0;i<HeaderS.length;i++)
		{
			if(HeaderS[i]!=null)
				model.add(NavigationalPath,CellHeader,HeaderS[i]);
		}
		Resource Stub = model.createResource();
		model.add(Stub,CellStubValue,StubValueS);
		if(SubHeadings!=null)
		for(int i=0;i<SubHeadings.length;i++)
		{
			if(SubHeadings[i]!=null)
				model.add(Stub,CellSubheadeing,SubHeadings[i]);
		}
		model.add(NavigationalPath,CellStub,Stub);
		model.add(CellS,HasNavigationalPath,NavigationalPath);
		model.add(CellS,CellColumn,Columnt+"");
		model.add(CellS,CellRow,Row+"");
		model.add(currentTable,Cell,CellS);
		currentCell = CellS;
	}
	
	public DecompositionRDFWriter(String url)
	{
		ArticleDefault = url;
		model = ModelFactory.createDefaultModel();
		ArticleName = model.createProperty( ArticleDefault + "ArticleName" );
		ArticleID = model.createProperty( ArticleDefault + "ArticleID" );
		TableOrder = model.createProperty( ArticleDefault + "TableOrder" );
		TableCaption = model.createProperty( ArticleDefault + "TableCaption" );
		TableFooter = model.createProperty( ArticleDefault + "TableFooter" );
		Table = model.createProperty( ArticleDefault + "HasTable" );
		TableType = model.createProperty( ArticleDefault + "TableType" );
		TablePragmaticType = model.createProperty( ArticleDefault + "TablePragmaticType" );
		TableXML= model.createProperty( ArticleDefault + "TableXML" );
		Cell = model.createProperty( ArticleDefault + "HasCell" );
		CellStub = model.createProperty( ArticleDefault + "CellStub" );
		CellStubValue = model.createProperty( ArticleDefault + "CellStubValue");
		CellSubheadeing = model.createProperty( ArticleDefault + "CellSubheadeing");
		CellHeader = model.createProperty( ArticleDefault + "CellHeader" );
		CellValue = model.createProperty( ArticleDefault + "CellValue" );
		CellType = model.createProperty( ArticleDefault + "CellType" );
		CellRow = model.createProperty( ArticleDefault + "CellRow" );
		HasNavigationalPath = model.createProperty( ArticleDefault + "HasNavigationalPath" );
		CellColumn = model.createProperty( ArticleDefault + "CellColumn" );
		Head00 = model.createProperty( ArticleDefault + "Head00" );
		NavigationalPath = model.createProperty( ArticleDefault + "NavigationalPath" );
		Root = model.createResource(ArticleDefault+"Root");
		HasArticle = model.createProperty( ArticleDefault + "HasArticle" );
	}
	
	public void printToFile()
	{
		 try {
				model.write(new FileOutputStream(outputFileName));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
