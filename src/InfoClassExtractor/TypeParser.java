/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package InfoClassExtractor;

public class TypeParser {
	
	private static char[] SemiNumericAllowed = {' ','.',',',8195,160,8194,8201, '-','-','—','–','−','+','±','(',')','[',']','·',';',':','%','/'};
	private static char[] Numerals = {'0','1','2','3','4','5','6','7','8','9'};
	
	/**
	 * Gets the semi numeric. Extracts first seminumeric value from the string
	 *
	 * @param s the s
	 * @return the semi numeric
	 */
	public static String getSemiNumeric(String s)
	{
		String SemiNumeric ="";
		boolean started = false; 
		for(int i = 0;i<s.length();i++)
		{
			if(Contains(Numerals,s.charAt(i)))
			{
				started = true;
			}
			if(started)
			{
				if(Contains(Numerals,s.charAt(i))||Contains(SemiNumericAllowed,s.charAt(i)))
				{
					SemiNumeric += s.charAt(i);
				}
				else
				{
					started = false;
					break;
				}
			}
						
		}
	return SemiNumeric.trim();
	}
	
	/**
	 * Contains. Check if array of character contains character
	 *
	 * @param array the array
	 * @param ch the ch
	 * @return true, if successful
	 */
	public static boolean Contains(char[] array, char ch)
	{
		for(char c:array)
		{
			if(ch==c)
				return true;
		}
		return false;
	}

}
