/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: VariableSafeAbsRef.java,v 1.6 2004/08/17 19:25:41 jycli Exp $
 */
package org.apache.xpath.operations;

import org.apache.xml.dtm.DTMManager;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;


/**
 * This is a "smart" variable reference that is used in situations where 
 * an absolute path is optimized into a variable reference, but may 
 * be used in some situations where the document context may have changed.
 * For instance, in select="document(doc/@href)//name[//salary &gt; 7250]", the 
 * root in the predicate will be different for each node in the set.  While 
 * this is easy to detect statically in this case, in other cases static 
 * detection would be very hard or impossible.  So, this class does a dynamic check 
 * to make sure the document context of the referenced variable is the same as 
 * the current document context, and, if it is not, execute the referenced variable's 
 * expression with the current context instead.
 */
public class VariableSafeAbsRef extends Variable
{
    static final long serialVersionUID = -9174661990819967452L;
	
  /**
   * Dereference the variable, and return the reference value.  Note that lazy 
   * evaluation will occur.  If a variable within scope is not found, a warning 
   * will be sent to the error listener, and an empty nodeset will be returned.
   *
   *
   * @param xctxt The runtime execution context.
   *
   * @return The evaluated variable, or an empty nodeset if not found.
   *
   * @throws javax.xml.transform.TransformerException
   */
  public XObject execute(XPathContext xctxt, boolean destructiveOK) 
  	throws javax.xml.transform.TransformerException
  {
  	XNodeSet xns = (XNodeSet)super.execute(xctxt, destructiveOK);
  	DTMManager dtmMgr = xctxt.getDTMManager();
  	int context = xctxt.getContextNode();
  	if(dtmMgr.getDTM(xns.getRoot()).getDocument() != 
  	   dtmMgr.getDTM(context).getDocument())
  	{
  		Expression expr = (Expression)xns.getContainedIter();
  		xns = (XNodeSet)expr.asIterator(xctxt, context);
  	}
  	return xns;
  }

}

