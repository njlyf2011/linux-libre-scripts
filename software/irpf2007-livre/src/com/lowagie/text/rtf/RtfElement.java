/*
 * $Id: RtfElement.java,v 1.8 2004/12/14 15:14:44 blowagie Exp $
 * $Name:  $
 *
 * Copyright 2001, 2002, 2003, 2004 by Mark Hall
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the ?GNU LIBRARY GENERAL PUBLIC LICENSE?), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.rtf;

import com.lowagie.text.rtf.document.RtfDocument;

/**
 * RtfElement is the base class for all RTF Element classes
 *
 * Version: $Id: RtfElement.java,v 1.8 2004/12/14 15:14:44 blowagie Exp $
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfElement implements RtfBasicElement {
    /**
     * Constant for a rtf escape
     */
    //public static final byte[] ESCAPE = "\\".getBytes();
    /**
     * Constant for a rtf extended escape
     */
    //public static final byte[] EXTENDED_ESCAPE = "\\*\\".getBytes();

    /**
     * The RtfDocument this RtfElement belongs to
     */
    protected RtfDocument document = null;
    /**
     * Whether this RtfElement is in a table
     */
    protected boolean inTable = false;
    /**
     * Whether this RtfElement is in a header
     */
    protected boolean inHeader = false;
    
    /**
     * Constructs a RtfElement belonging to the specified RtfDocument.
     * 
     * @param doc The RtfDocument this RtfElement belongs to
     */
    public RtfElement(RtfDocument doc) {
        super();
        this.document = doc;
    }

    /**
     * Transforms an integer into its String representation and then returns the bytes
     * of that string.
     *
     * @param i The integer to convert
     * @return A byte array representing the integer
     */
    public byte[] intToByteArray(int i) {
        return Integer.toString(i).getBytes();
    }

    /**
     * Returns the content of the RtfElement in a byte array.
     *
     * @return An empty byte array
     */
    public byte[] write() {
        return new byte[0];
    }
    
    /**
     * Sets the RtfDocument this RtfElement belongs to
     * 
     * @param doc The RtfDocument to use
     */
    public void setRtfDocument(RtfDocument doc) {
        this.document = doc;
    }
    
    /**
     * Gets whether this RtfElement is in a table
     * 
     * @return Whether this RtfElement is in a table
     */
    public boolean isInTable() {
        return inTable;
    }
    
    /**
     * Sets whether this RtfElement is in a table
     * 
     * @param inTable <code>True</code> if this RtfElement is in a table, <code>false</code> otherwise
     */
    public void setInTable(boolean inTable) {
        this.inTable = inTable;
    }
    
    /**
     * Sets whether this RtfElement is in a header
     * 
     * @param inHeader <code>True</code> if this RtfElement is in a header, <code>false</code> otherwise
     */
    public void setInHeader(boolean inHeader) {
        this.inHeader = inHeader;
    }
}
