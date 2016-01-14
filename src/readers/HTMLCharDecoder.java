package readers;

public class HTMLCharDecoder {
	
	public static String Decode(String input)
	{
		String decoded = input;
		decoded = decoded.replaceAll("&#32;", " ");
		decoded = decoded.replaceAll("&#33;", "!");
		decoded = decoded.replaceAll("&#34;", "\"");
		decoded = decoded.replaceAll("&#35;", "#");
		decoded = decoded.replaceAll("&#36;", "$");
		decoded = decoded.replaceAll("&#37;", "%");
		decoded = decoded.replaceAll("&#38;", "&");
		decoded = decoded.replaceAll("&#39;", "'");
		decoded = decoded.replaceAll("&#40;", "(");
		decoded = decoded.replaceAll("&#41;", ")");
		decoded = decoded.replaceAll("&#42;", "*");
		decoded = decoded.replaceAll("&#43;", "+");
		decoded = decoded.replaceAll("&#44;", ",");
		decoded = decoded.replaceAll("&#45;", "-");
		decoded = decoded.replaceAll("&#46;", ".");
		decoded = decoded.replaceAll("&#47;", "/");
		decoded = decoded.replaceAll("&quot;", "\"");
		decoded = decoded.replaceAll("&amp;", "&");
		decoded = decoded.replaceAll("&#48;", "0");
		decoded = decoded.replaceAll("&#49;", "1");
		decoded = decoded.replaceAll("&#50;", "2");
		decoded = decoded.replaceAll("&#51;", "3");
		decoded = decoded.replaceAll("&#52;", "4");
		decoded = decoded.replaceAll("&#53;", "5");
		decoded = decoded.replaceAll("&#54;", "6");
		decoded = decoded.replaceAll("&#55;", "7");
		decoded = decoded.replaceAll("&#56;", "8");
		decoded = decoded.replaceAll("&#57;", "9");
		decoded = decoded.replaceAll("&#58;", ":");
		decoded = decoded.replaceAll("&#59;", ";");
		decoded = decoded.replaceAll("&#60;", "<");
		decoded = decoded.replaceAll("&#61;", "=");
		decoded = decoded.replaceAll("&#62;", ">");
		decoded = decoded.replaceAll("&#63;", "?");
		decoded = decoded.replaceAll("&lt;", "<");
		decoded = decoded.replaceAll("&gt;", ">");
		decoded = decoded.replaceAll("&#64;", "@");
		decoded = decoded.replaceAll("&#65;", "A");
		
		decoded = decoded.replaceAll("&#161;", "¡");
		decoded = decoded.replaceAll("&#162;", "¢");
		decoded = decoded.replaceAll("&#163;", "£");
		decoded = decoded.replaceAll("&#164;", "¤");
		decoded = decoded.replaceAll("&#165;", "¥");
		decoded = decoded.replaceAll("&#166;", "¦");
		decoded = decoded.replaceAll("&#167;", "§");
		decoded = decoded.replaceAll("&#168;", "¨");
		decoded = decoded.replaceAll("&#169;", "©");
		decoded = decoded.replaceAll("&#170;", "ª");
		decoded = decoded.replaceAll("&#171;", "«");
		decoded = decoded.replaceAll("&#172;", "¬");
		decoded = decoded.replaceAll("&#173;", "");
		decoded = decoded.replaceAll("&#174;", "®");
		decoded = decoded.replaceAll("&#175;", "¯");
		decoded = decoded.replaceAll("&#176;", "°");
		decoded = decoded.replaceAll("&#177;", "±");
		decoded = decoded.replaceAll("&#178;", "²");
		decoded = decoded.replaceAll("&#179;", "³");
		decoded = decoded.replaceAll("&#180;", "´");
		decoded = decoded.replaceAll("&#181;", "µ");
		decoded = decoded.replaceAll("&#182;", "¶");
		decoded = decoded.replaceAll("&#183;", "·");
		decoded = decoded.replaceAll("&#184;", "¸");
		decoded = decoded.replaceAll("&#185;", "¹");
		decoded = decoded.replaceAll("&#186;", "º");
		decoded = decoded.replaceAll("&#187;", "»");
		decoded = decoded.replaceAll("&#188;", "¼");
		decoded = decoded.replaceAll("&#189;", "½");
		decoded = decoded.replaceAll("&#190;", "¾");
		decoded = decoded.replaceAll("&#191;", "¿");
		
		decoded = decoded.replaceAll("&#8211;", "–");
		decoded = decoded.replaceAll("&#8212;", "—");
		decoded = decoded.replaceAll("&#8216;", "‘");
		decoded = decoded.replaceAll("&#8224;", "†");
		decoded = decoded.replaceAll("&#8225;", "‡");
		decoded = decoded.replaceAll("&#8226;", "•");
		decoded = decoded.replaceAll("&#8240;", "‰");
		
		return decoded;
	}

}
