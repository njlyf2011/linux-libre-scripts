/* JulianDate - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcode.util;
import java.util.Date;
import java.util.GregorianCalendar;

public class JulianDate
{
  private int year;
  private int month;
  private int day;
  private GregorianCalendar calendar;
  
  public JulianDate ()
  {
    calendar = new GregorianCalendar ();
    calendar.setGregorianChange (new Date ());
    year = calendar.get (1);
    month = calendar.get (2);
    month++;
    day = calendar.get (5);
  }
  
  public JulianDate (Date date)
  {
    calendar = new GregorianCalendar ();
    calendar.setGregorianChange (date);
    year = calendar.get (1);
    month = calendar.get (2);
    month++;
    day = calendar.get (5);
  }
  
  public JulianDate (String date) throws Exception
  {
    if (date.length () == 8)
      {
	day = Integer.parseInt (date.substring (0, 2));
	month = Integer.parseInt (date.substring (2, 4));
	year = Integer.parseInt (date.substring (4));
      }
    else if (date.length () == 10)
      {
	day = Integer.parseInt (date.substring (0, 2));
	month = Integer.parseInt (date.substring (3, 5));
	year = Integer.parseInt (date.substring (6));
      }
    else
      throw new Exception ("Invalid format.");
    calendar = new GregorianCalendar (year, month - 1, day);
  }
  
  public JulianDate (int day, int month, int year) throws Exception
  {
    this.day = day;
    this.month = month;
    this.year = year;
    calendar = new GregorianCalendar (year, month - 1, day);
  }
  
  public int getYear ()
  {
    return year;
  }
  
  public int getMonth ()
  {
    return month;
  }
  
  public int getDay ()
  {
    return day;
  }
  
  public String getJulianDate7Digits ()
  {
    StringBuffer sb = new StringBuffer ();
    StringBuffer sb2 = new StringBuffer ();
    int x = calendar.get (6);
    if (x < 10)
      sb2.append ("00").append (x);
    else if (x >= 10 && x < 100)
      sb2.append ("0");
    sb2.append (x);
    sb.append (year).append (sb2.toString ());
    return sb.toString ();
  }
  
  public String getJulianDate4Digits ()
  {
    return getJulianDate7Digits ().substring (3);
  }
  
  public GregorianCalendar getCalendar ()
  {
    return calendar;
  }
}
