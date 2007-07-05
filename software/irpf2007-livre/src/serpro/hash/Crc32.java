/* Crc32 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.hash;
import serpro.util.PLong;

public class Crc32 implements iCrc32
{
  private long[] Crc32Table = new long[256];
  private String m_szCrc32 = new String ();
  
  public Crc32 ()
  {
    Crc32Table[0] = 0L;
    Crc32Table[1] = 1996959894L;
    Crc32Table[2] = -301047508L;
    Crc32Table[3] = -1727442502L;
    Crc32Table[4] = 124634137L;
    Crc32Table[5] = 1886057615L;
    Crc32Table[6] = -379345611L;
    Crc32Table[7] = -1637575261L;
    Crc32Table[8] = 249268274L;
    Crc32Table[9] = 2044508324L;
    Crc32Table[10] = -522852066L;
    Crc32Table[11] = -1747789432L;
    Crc32Table[12] = 162941995L;
    Crc32Table[13] = 2125561021L;
    Crc32Table[14] = -407360249L;
    Crc32Table[15] = -1866523247L;
    Crc32Table[16] = 498536548L;
    Crc32Table[17] = 1789927666L;
    Crc32Table[18] = -205950648L;
    Crc32Table[19] = -2067906082L;
    Crc32Table[20] = 450548861L;
    Crc32Table[21] = 1843258603L;
    Crc32Table[22] = -187386543L;
    Crc32Table[23] = -2083289657L;
    Crc32Table[24] = 325883990L;
    Crc32Table[25] = 1684777152L;
    Crc32Table[26] = -43845254L;
    Crc32Table[27] = -1973040660L;
    Crc32Table[28] = 335633487L;
    Crc32Table[29] = 1661365465L;
    Crc32Table[30] = -99664541L;
    Crc32Table[31] = -1928851979L;
    Crc32Table[32] = 997073096L;
    Crc32Table[33] = 1281953886L;
    Crc32Table[34] = -715111964L;
    Crc32Table[35] = -1570279054L;
    Crc32Table[36] = 1006888145L;
    Crc32Table[37] = 1258607687L;
    Crc32Table[38] = -770865667L;
    Crc32Table[39] = -1526024853L;
    Crc32Table[40] = 901097722L;
    Crc32Table[41] = 1119000684L;
    Crc32Table[42] = -608450090L;
    Crc32Table[43] = -1396901568L;
    Crc32Table[44] = 853044451L;
    Crc32Table[45] = 1172266101L;
    Crc32Table[46] = -589951537L;
    Crc32Table[47] = -1412350631L;
    Crc32Table[48] = 651767980L;
    Crc32Table[49] = 1373503546L;
    Crc32Table[50] = -925412992L;
    Crc32Table[51] = -1076862698L;
    Crc32Table[52] = 565507253L;
    Crc32Table[53] = 1454621731L;
    Crc32Table[54] = -809855591L;
    Crc32Table[55] = -1195530993L;
    Crc32Table[56] = 671266974L;
    Crc32Table[57] = 1594198024L;
    Crc32Table[58] = -972236366L;
    Crc32Table[59] = -1324619484L;
    Crc32Table[60] = 795835527L;
    Crc32Table[61] = 1483230225L;
    Crc32Table[62] = -1050600021L;
    Crc32Table[63] = -1234817731L;
    Crc32Table[64] = 1994146192L;
    Crc32Table[65] = 31158534L;
    Crc32Table[66] = -1731059524L;
    Crc32Table[67] = -271249366L;
    Crc32Table[68] = 1907459465L;
    Crc32Table[69] = 112637215L;
    Crc32Table[70] = -1614814043L;
    Crc32Table[71] = -390540237L;
    Crc32Table[72] = 2013776290L;
    Crc32Table[73] = 251722036L;
    Crc32Table[74] = -1777751922L;
    Crc32Table[75] = -519137256L;
    Crc32Table[76] = 2137656763L;
    Crc32Table[77] = 141376813L;
    Crc32Table[78] = -1855689577L;
    Crc32Table[79] = -429695999L;
    Crc32Table[80] = 1802195444L;
    Crc32Table[81] = 476864866L;
    Crc32Table[82] = -2056965928L;
    Crc32Table[83] = -228458418L;
    Crc32Table[84] = 1812370925L;
    Crc32Table[85] = 453092731L;
    Crc32Table[86] = -2113342271L;
    Crc32Table[87] = -183516073L;
    Crc32Table[88] = 1706088902L;
    Crc32Table[89] = 314042704L;
    Crc32Table[90] = -1950435094L;
    Crc32Table[91] = -54949764L;
    Crc32Table[92] = 1658658271L;
    Crc32Table[93] = 366619977L;
    Crc32Table[94] = -1932296973L;
    Crc32Table[95] = -69972891L;
    Crc32Table[96] = 1303535960L;
    Crc32Table[97] = 984961486L;
    Crc32Table[98] = -1547960204L;
    Crc32Table[99] = -725929758L;
    Crc32Table[100] = 1256170817L;
    Crc32Table[101] = 1037604311L;
    Crc32Table[102] = -1529756563L;
    Crc32Table[103] = -740887301L;
    Crc32Table[104] = 1131014506L;
    Crc32Table[105] = 879679996L;
    Crc32Table[106] = -1385723834L;
    Crc32Table[107] = -631195440L;
    Crc32Table[108] = 1141124467L;
    Crc32Table[109] = 855842277L;
    Crc32Table[110] = -1442165665L;
    Crc32Table[111] = -586318647L;
    Crc32Table[112] = 1342533948L;
    Crc32Table[113] = 654459306L;
    Crc32Table[114] = -1106571248L;
    Crc32Table[115] = -921952122L;
    Crc32Table[116] = 1466479909L;
    Crc32Table[117] = 544179635L;
    Crc32Table[118] = -1184443383L;
    Crc32Table[119] = -832445281L;
    Crc32Table[120] = 1591671054L;
    Crc32Table[121] = 702138776L;
    Crc32Table[122] = -1328506846L;
    Crc32Table[123] = -942167884L;
    Crc32Table[124] = 1504918807L;
    Crc32Table[125] = 783551873L;
    Crc32Table[126] = -1212326853L;
    Crc32Table[127] = -1061524307L;
    Crc32Table[128] = -306674912L;
    Crc32Table[129] = -1698712650L;
    Crc32Table[130] = 62317068L;
    Crc32Table[131] = 1957810842L;
    Crc32Table[132] = -355121351L;
    Crc32Table[133] = -1647151185L;
    Crc32Table[134] = 81470997L;
    Crc32Table[135] = 1943803523L;
    Crc32Table[136] = -480048366L;
    Crc32Table[137] = -1805370492L;
    Crc32Table[138] = 225274430L;
    Crc32Table[139] = 2053790376L;
    Crc32Table[140] = -468791541L;
    Crc32Table[141] = -1828061283L;
    Crc32Table[142] = 167816743L;
    Crc32Table[143] = 2097651377L;
    Crc32Table[144] = -267414716L;
    Crc32Table[145] = -2029476910L;
    Crc32Table[146] = 503444072L;
    Crc32Table[147] = 1762050814L;
    Crc32Table[148] = -144550051L;
    Crc32Table[149] = -2140837941L;
    Crc32Table[150] = 426522225L;
    Crc32Table[151] = 1852507879L;
    Crc32Table[152] = -19653770L;
    Crc32Table[153] = -1982649376L;
    Crc32Table[154] = 282753626L;
    Crc32Table[155] = 1742555852L;
    Crc32Table[156] = -105259153L;
    Crc32Table[157] = -1900089351L;
    Crc32Table[158] = 397917763L;
    Crc32Table[159] = 1622183637L;
    Crc32Table[160] = -690576408L;
    Crc32Table[161] = -1580100738L;
    Crc32Table[162] = 953729732L;
    Crc32Table[163] = 1340076626L;
    Crc32Table[164] = -776247311L;
    Crc32Table[165] = -1497606297L;
    Crc32Table[166] = 1068828381L;
    Crc32Table[167] = 1219638859L;
    Crc32Table[168] = -670225446L;
    Crc32Table[169] = -1358292148L;
    Crc32Table[170] = 906185462L;
    Crc32Table[171] = 1090812512L;
    Crc32Table[172] = -547295293L;
    Crc32Table[173] = -1469587627L;
    Crc32Table[174] = 829329135L;
    Crc32Table[175] = 1181335161L;
    Crc32Table[176] = -882789492L;
    Crc32Table[177] = -1134132454L;
    Crc32Table[178] = 628085408L;
    Crc32Table[179] = 1382605366L;
    Crc32Table[180] = -871598187L;
    Crc32Table[181] = -1156888829L;
    Crc32Table[182] = 570562233L;
    Crc32Table[183] = 1426400815L;
    Crc32Table[184] = -977650754L;
    Crc32Table[185] = -1296233688L;
    Crc32Table[186] = 733239954L;
    Crc32Table[187] = 1555261956L;
    Crc32Table[188] = -1026031705L;
    Crc32Table[189] = -1244606671L;
    Crc32Table[190] = 752459403L;
    Crc32Table[191] = 1541320221L;
    Crc32Table[192] = -1687895376L;
    Crc32Table[193] = -328994266L;
    Crc32Table[194] = 1969922972L;
    Crc32Table[195] = 40735498L;
    Crc32Table[196] = -1677130071L;
    Crc32Table[197] = -351390145L;
    Crc32Table[198] = 1913087877L;
    Crc32Table[199] = 83908371L;
    Crc32Table[200] = -1782625662L;
    Crc32Table[201] = -491226604L;
    Crc32Table[202] = 2075208622L;
    Crc32Table[203] = 213261112L;
    Crc32Table[204] = -1831694693L;
    Crc32Table[205] = -438977011L;
    Crc32Table[206] = 2094854071L;
    Crc32Table[207] = 198958881L;
    Crc32Table[208] = -2032938284L;
    Crc32Table[209] = -237706686L;
    Crc32Table[210] = 1759359992L;
    Crc32Table[211] = 534414190L;
    Crc32Table[212] = -2118248755L;
    Crc32Table[213] = -155638181L;
    Crc32Table[214] = 1873836001L;
    Crc32Table[215] = 414664567L;
    Crc32Table[216] = -2012718362L;
    Crc32Table[217] = -15766928L;
    Crc32Table[218] = 1711684554L;
    Crc32Table[219] = 285281116L;
    Crc32Table[220] = -1889165569L;
    Crc32Table[221] = -127750551L;
    Crc32Table[222] = 1634467795L;
    Crc32Table[223] = 376229701L;
    Crc32Table[224] = -1609899400L;
    Crc32Table[225] = -686959890L;
    Crc32Table[226] = 1308918612L;
    Crc32Table[227] = 956543938L;
    Crc32Table[228] = -1486412191L;
    Crc32Table[229] = -799009033L;
    Crc32Table[230] = 1231636301L;
    Crc32Table[231] = 1047427035L;
    Crc32Table[232] = -1362007478L;
    Crc32Table[233] = -640263460L;
    Crc32Table[234] = 1088359270L;
    Crc32Table[235] = 936918000L;
    Crc32Table[236] = -1447252397L;
    Crc32Table[237] = -558129467L;
    Crc32Table[238] = 1202900863L;
    Crc32Table[239] = 817233897L;
    Crc32Table[240] = -1111625188L;
    Crc32Table[241] = -893730166L;
    Crc32Table[242] = 1404277552L;
    Crc32Table[243] = 615818150L;
    Crc32Table[244] = -1160759803L;
    Crc32Table[245] = -841546093L;
    Crc32Table[246] = 1423857449L;
    Crc32Table[247] = 601450431L;
    Crc32Table[248] = -1285129682L;
    Crc32Table[249] = -1000256840L;
    Crc32Table[250] = 1567103746L;
    Crc32Table[251] = 711928724L;
    Crc32Table[252] = -1274298825L;
    Crc32Table[253] = -1022587231L;
    Crc32Table[254] = 1510334235L;
    Crc32Table[255] = 755167117L;
  }
  
  public String getStrCrc32 ()
  {
    return m_szCrc32;
  }
  
  public long CalcCrc32 (String string, int i, PLong plong)
  {
    long l = 0L;
    String string_0_ = new String ();
    if (i <= 0)
      {
	m_szCrc32 = "";
	return 0L;
      }
    long l_1_;
    if (plong.getValue () == 0L)
      l_1_ = 4294967295L;
    else
      l_1_ = plong.getValue () ^ 0xffffffffffffffffL;
    if (string.length () > 0 && i > 0)
      {
	for (int i_2_ = 0; i_2_ < i; i_2_++)
	  {
	    char c = string.charAt (i_2_);
	    long l_3_ = (l_1_ ^ (long) c) & 0xffL;
	    long l_4_ = Crc32Table[(int) l_3_];
	    l_1_ = l_1_ >> 8 & 0xffffffL ^ l_4_;
	  }
	if (plong.getValue () != 0L)
	  {
	    plong.setValue (l_1_ ^ 0xffffffffffffffffL);
	    if (plong.getValue () < 0L)
	      plong.setValue (4294967296L + plong.getValue ());
	  }
      }
    l = l_1_ ^ 0xffffffffffffffffL;
    if (l < 0L)
      l = 4294967296L + l;
    String string_5_ = new String ("0000000000");
    String string_6_ = new String ();
    string_6_ = Long.toString (l);
    int i_7_ = 10 - string_6_.length ();
    if (i_7_ == 0)
      m_szCrc32 = string_6_;
    else
      m_szCrc32 = string_5_.substring (0, i_7_) + string_6_;
    return l;
  }
}
