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
 * $Id: DTMWSFilter.java,v 1.4 2004/02/16 23:03:44 minchau Exp $
 */
package org.apache.xml.dtm;

/**
 * This interface is meant to be implemented by a client of the DTM, and allows
 * stripping of whitespace nodes.
 */
public interface DTMWSFilter
{
  /**
   * Do not strip whitespace child nodes of this element.
   */
  public static final short NOTSTRIP = 1;

  /**
   * Strip whitespace child nodes of this element.
   */
  public static final short STRIP = 2;

  /**
   * Inherit whitespace stripping behavior of the parent node.
   */
  public static final short INHERIT = 3;

  /**
   * Test whether whitespace-only text nodes are visible in the logical 
   * view of <code>DTM</code>. Normally, this function
   * will be called by the implementation of <code>DTM</code>; 
   * it is not normally called directly from
   * user code.
   * 
   * @param elementHandle int Handle of the element.
   * @return one of NOTSTRIP, STRIP, or INHERIT.
   */
  public short getShouldStripSpace(int elementHandle, DTM dtm);
  
}
