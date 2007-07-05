/* JRXmlDataSourcePPGD - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.apache.commons.beanutils.ConvertUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import serpro.ppgd.negocio.util.LogPPGD;

public class JRXmlDataSourcePPGD implements JRRewindableDataSource
{
  private static final String[] CLASSES_XPATH_API = { "com.sun.org.apache.xpath.internal.CachedXPathAPI", "org.apache.xpath.CachedXPathAPI", "com.sun.org.apache.xpath.internal.XPathAPI", "org.apache.xpath.XPathAPI" };
  private Document document;
  private String selectExpression;
  private NodeList nodeList;
  private int nodeListLength;
  private Node currentNode;
  private int currentNodeIndex = -1;
  private Object xpathAPI;
  
  public JRXmlDataSourcePPGD (Document document) throws JRException
  {
    this (document, ".");
  }
  
  public JRXmlDataSourcePPGD (Document document, String selectExpression) throws JRException
  {
    Class xpathClass = null;
    xpathClass = getXpathClass ();
    try
      {
	xpathAPI = xpathClass.newInstance ();
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
    this.document = document;
    this.selectExpression = selectExpression;
    moveFirst ();
  }
  
  private Class getXpathClass ()
  {
    Class xpathClass = null;
    for (int i = 0; i < CLASSES_XPATH_API.length; i++)
      {
	try
	  {
	    xpathClass = Class.forName (CLASSES_XPATH_API[i]);
	  }
	catch (ClassNotFoundException e)
	  {
	    xpathClass = null;
	  }
	if (xpathClass != null)
	  i = CLASSES_XPATH_API.length + 1;
      }
    if (xpathClass == null)
      throw new IllegalArgumentException ("N\u00e3o foi poss\u00edvel encontrar nenhuma biblioteca do XPathAPI ou CachedXPathAPI!");
    return xpathClass;
  }
  
  public JRXmlDataSourcePPGD (InputStream in) throws JRException
  {
    this (in, ".");
  }
  
  public JRXmlDataSourcePPGD (InputStream in, String selectExpression) throws JRException
  {
    this (parse (new InputSource (in)), selectExpression);
  }
  
  public JRXmlDataSourcePPGD (String uri) throws JRException
  {
    this (uri, ".");
  }
  
  public JRXmlDataSourcePPGD (String uri, String selectExpression) throws JRException
  {
    this (parse (new InputSource (uri)), selectExpression);
  }
  
  public JRXmlDataSourcePPGD (File file) throws JRException
  {
    this (file, ".");
  }
  
  public JRXmlDataSourcePPGD (File file, String selectExpression) throws JRException
  {
    this (parse (file), selectExpression);
  }
  
  private static Document parse (InputSource is) throws JRException
  {
    Document document;
    try
      {
	document = createDocumentBuilder ().parse (is);
      }
    catch (SAXException e)
      {
	throw new JRException ("Failed to parse the xml document", e);
      }
    catch (IOException e)
      {
	throw new JRException ("Failed to parse the xml document", e);
      }
    return document;
  }
  
  private static Document parse (File file) throws JRException
  {
    Document document;
    try
      {
	document = createDocumentBuilder ().parse (file);
      }
    catch (SAXException e)
      {
	throw new JRException ("Failed to parse the xml document", e);
      }
    catch (IOException e)
      {
	throw new JRException ("Failed to parse the xml document", e);
      }
    return document;
  }
  
  private static DocumentBuilder createDocumentBuilder () throws JRException
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ();
    dbf.setValidating (false);
    dbf.setIgnoringComments (true);
    DocumentBuilder documentbuilder;
    try
      {
	documentbuilder = dbf.newDocumentBuilder ();
      }
    catch (ParserConfigurationException e)
      {
	throw new JRException ("Failed to create a document builder factory", e);
      }
    return documentbuilder;
  }
  
  public void moveFirst () throws JRException
  {
    if (document == null)
      throw new JRException ("document cannot be not null");
    if (selectExpression == null)
      throw new JRException ("selectExpression cannot be not null");
    try
      {
	currentNode = null;
	currentNodeIndex = -1;
	nodeListLength = 0;
	try
	  {
	    Method mSelectNodeList = getXpathClass ().getMethod ("selectNodeList", new Class[] { org.w3c.dom.Node.class, java.lang.String.class });
	    nodeList = (NodeList) mSelectNodeList.invoke (xpathAPI, new Object[] { document, selectExpression });
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
	nodeListLength = nodeList.getLength ();
      }
    catch (Exception e)
      {
	throw new JRException ("XPath selection failed. Expression: " + selectExpression, e);
      }
  }
  
  public boolean next () throws JRException
  {
    if (currentNodeIndex == nodeListLength - 1)
      return false;
    currentNode = nodeList.item (++currentNodeIndex);
    return true;
  }
  
  public Object getFieldValue (JRField jrField) throws JRException
  {
    if (currentNode == null)
      return null;
    String expression = jrField.getDescription ();
    if (expression == null || expression.length () == 0)
      return null;
    Object value = null;
    Class valueClass = jrField.getValueClass ();
    if ((java.lang.Object.class) != valueClass)
      {
	Node node = null;
	try
	  {
	    try
	      {
		Method mSelectSingleNode = getXpathClass ().getMethod ("selectSingleNode", new Class[] { org.w3c.dom.Node.class, java.lang.String.class });
		node = (Node) mSelectSingleNode.invoke (xpathAPI, new Object[] { currentNode, expression });
	      }
	    catch (Exception e)
	      {
		e.printStackTrace ();
	      }
	  }
	catch (Exception e)
	  {
	    throw new JRException ("XPath selection failed. Expression: " + expression, e);
	  }
	if (node != null)
	  {
	    String text = getText (node);
	    if (text != null)
	      {
		if ((java.lang.String.class) == valueClass)
		  value = text;
		else
		  value = ConvertUtils.convert (text.trim (), valueClass);
	      }
	  }
      }
    return value;
  }
  
  public JRXmlDataSourcePPGD subDataSource (String selectExpression) throws JRException
  {
    if (currentNode == null)
      throw new JRException ("No node available. Iterate or rewind the data source.");
    Document document = createDocumentBuilder ().newDocument ();
    Node node = document.importNode (currentNode, true);
    document.appendChild (node);
    return new JRXmlDataSourcePPGD (document, selectExpression);
  }
  
  public JRXmlDataSourcePPGD subDataSource () throws JRException
  {
    return subDataSource (".");
  }
  
  public JRXmlDataSourcePPGD dataSource (String selectExpression) throws JRException
  {
    return new JRXmlDataSourcePPGD (document, selectExpression);
  }
  
  public JRXmlDataSourcePPGD dataSource () throws JRException
  {
    return dataSource (".");
  }
  
  public String getText (Node node)
  {
    if (! node.hasChildNodes ())
      return node.getNodeValue ();
    StringBuffer result = new StringBuffer ();
    NodeList list = node.getChildNodes ();
    for (int i = 0; i < list.getLength (); i++)
      {
	Node subnode = list.item (i);
	if (subnode.getNodeType () == 3)
	  {
	    String value = subnode.getNodeValue ();
	    if (value != null)
	      result.append (value);
	  }
	else if (subnode.getNodeType () == 4)
	  {
	    String value = subnode.getNodeValue ();
	    if (value != null)
	      result.append (value);
	  }
	else if (subnode.getNodeType () == 5)
	  {
	    String value = getText (subnode);
	    if (value != null)
	      result.append (value);
	  }
      }
    return result.toString ();
  }
  
  public static void main (String[] args) throws Exception
  {
    JRXmlDataSourcePPGD ds = new JRXmlDataSourcePPGD (new FileInputStream ("northwind.xml"), "/Northwind/Customers");
    JRDesignField field = new JRDesignField ();
    field.setDescription ("CustomerID");
    field.setValueClass (java.lang.String.class);
    ds.next ();
    String v = (String) ds.getFieldValue (field);
    LogPPGD.debug (field.getDescription () + "=" + v);
    JRXmlDataSourcePPGD subDs = ds.dataSource ("/Northwind/Orders");
    JRDesignField field1 = new JRDesignField ();
    field1.setDescription ("OrderID");
    field1.setValueClass (java.lang.String.class);
    subDs.next ();
    String v1 = (String) subDs.getFieldValue (field1);
    System.out.println (field1.getDescription () + "=" + v1);
  }
  
}
