package ExternalResourceHandlers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

public class ResourceReader {

	public static LinkedList<InformationClass> read(String path) {

		LinkedList<InformationClass> infoclasses = new LinkedList<InformationClass>();
		try {
			File folder = new File(path);
			LinkedList<String> files = new LinkedList<String>();
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				boolean isClassDefinition = false;
				boolean isMetadata = false;
				boolean isMatchList = false;
				boolean isBlackList = false;
				boolean isWhiteList = false;
				boolean isCommandList = false;	
				InformationClass classa=new InformationClass();
				if (listOfFiles[i].isFile()) {
					files.add(listOfFiles[i].getPath());
					BufferedReader br = new BufferedReader(new FileReader(
							listOfFiles[i].getPath()));
					String line;
					while ((line = br.readLine()) != null) {
						if(line.equals("\\information_class_resource"))
						{
							isClassDefinition = true;
							continue;
						}
						if(isClassDefinition)
						{
							
							if(line.equals("Metadata:"))
							{
								isMetadata = true;
								isMatchList = false;
								isBlackList = false;
								isWhiteList = false;
								isCommandList = false;	
								continue;
							}
							
							if(line.equals("MatchList:"))
							{
								isMetadata = false;
								isMatchList = true;
								isBlackList = false;
								isWhiteList = false;
								isCommandList = false;	
								continue;
							}
							
							if(line.equals("BlackList:"))
							{
								isMetadata = false;
								isMatchList = false;
								isBlackList = true;
								isWhiteList = false;
								isCommandList = false;	
								continue;
							}
							
							if(line.equals("WhiteList:"))
							{
								isMetadata = false;
								isMatchList = false;
								isBlackList = false;
								isWhiteList = true;
								isCommandList = false;	
								continue;
							}
							
							if(line.equals("CommandList:"))
							{
								isMetadata = false;
								isMatchList = false;
								isBlackList = false;
								isWhiteList = false;
								isCommandList = true;	
								continue;
							}
							if(isMetadata)
							{
								
								String[] linedata = line.split(":");
								if(linedata[0].contains("class_name"))
								{
									classa.setClass_name(linedata[1]);
								}
								if(linedata[0].contains("class_unit_def"))
								{
									classa.setDefault_unit(linedata[1]);
								}
								if(linedata[0].contains("value_range"))
								{
									classa.setValue_range(linedata[1]);
									String range = linedata[1];
									if(range.contains(">"))
									{
										range = range.replace(">", "");
										classa.setMin_value(Double.parseDouble(range));
									}
									if(range.contains("<"))
									{
										range = range.replace("<", "");
										classa.setMax_value(Double.parseDouble(range));
									}
									if(range.contains("-"))
									{
										String[] ranges = range.split("-");
										classa.setMin_value(Double.parseDouble(ranges[0]));
										classa.setMax_value(Double.parseDouble(ranges[1]));
									}
								}
								if(linedata[0].contains("value_type"))
								{
									classa.setValue_type(linedata[1]);
								}
								if(linedata[0].contains("where"))
								{
									classa.setWhere(linedata[1]);
								}
								if(linedata[0].contains("vicinity"))
								{
									classa.setVicinity(Integer.parseInt(linedata[1]));
								}
							}

							if(isMatchList)
							{
								line = line.replace("\t", "");
								classa.MatchList.add(line);
							}
						
							if(isBlackList)
							{
								line = line.replace("\t", "");
								classa.BlackList.add(line);
							}

							if(isWhiteList)
							{
								line = line.replace("\t", "");
								classa.WhiteList.add(line);
							}
							

							if(isCommandList)
							{
								line = line.replace("\t", "");
								ResourceRule rule = new ResourceRule();
								rule.setRule(line);
								classa.RuleList.add(rule); //TODO: More processing to be added
							}
							
							
						}
					}
					br.close();
					if(isClassDefinition){
						infoclasses.add(classa);
				
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return infoclasses;
	}

}
