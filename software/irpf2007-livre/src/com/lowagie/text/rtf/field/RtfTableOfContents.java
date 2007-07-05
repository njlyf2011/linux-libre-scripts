/*
 * $Id: RtfTableOfContents.java,v 1.11 2007/03/04 14:34:20 hallm Exp $
 * $Name:  $
 *
 * Copyright 2004 by Mark Hall
 * Uses code Copyright 2002
 *   Steffen.Stundzig (Steffen.Stundzig@smb-tec.com) 
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

package com.lowagie.text.rtf.field;

import java.io.IOException;

import com.lowagie.text.Font;



/**
 * The RtfTableOfContents together with multiple RtfTOCEntry objects generates a table 
 * of contents. The table of contents will display no entries in the viewing program
 * and the user will have to update it first. A text to inform the user of this is
 * displayed instead.
 * 
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 * @author Steffen.Stundzig (Steffen.Stundzig@smb-tec.com) 
 */
public class RtfTableOfContents extends RtfField {

    /**
     * The default text to display
     */
    private String defaultText = "Table of Contents - Click to update";
    
    /**
     * Constructs a RtfTableOfContents. The default text is the text that is displayed
     * before the user updates the table of contents
     * 
     * @param defaultText The default text to display
     */
    public RtfTableOfContents(String defaultText) {
        super(null, new Font());
        this.defaultText = defaultText;
    }

    /**
     * Writes the field instruction content
     * 
     * @return A byte array containing with the field instructions
     * @throws IOException
     */
    protected byte[] writeFieldInstContent() throws IOException {
        return "TOC \\\\f \\\\h \\\\u \\\\o \"1-5\" ".getBytes();
    }

    /**
     * Writes the field result content
     * 
     * @return An byte array containing the default text
     * @throws IOException
     */
    protected byte[] writeFieldResultContent() throws IOException {
        return document.filterSpecialChar(defaultText, true, true).getBytes();
    }
}
