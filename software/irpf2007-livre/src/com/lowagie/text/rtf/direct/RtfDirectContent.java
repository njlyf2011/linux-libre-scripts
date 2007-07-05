/**
 * $Id: RtfDirectContent.java,v 1.3 2006/08/28 11:21:15 blowagie Exp $
 * $Name:  $
 *
 * Copyright 2006 by Mark Hall
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
 * the Initial Developer are Copyright (C) 1999-2006 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2006 by Paulo Soares. All Rights Reserved.
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
package com.lowagie.text.rtf.direct;

import com.lowagie.text.rtf.RtfAddableElement;

/**
 * The RtfDirectContent makes it possible to directly add RTF code into
 * an RTF document. This can be used to directly add RTF fragments that
 * have been created with other RTF editors. One important aspect is that
 * font and color numbers will not be modified. This means that the
 * fonts and colors visible in the final document might not be equivalent
 * with those set on the direct content.<br /><br />
 * 
 * For convenience the RtfDirectContent provides a DIRECT_SOFT_LINEBREAK
 * constant that makes it possible to easily add soft line-breaks anywhere in
 * the RTF document.
 * 
 * @version $Revision: 1.3 $
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfDirectContent extends RtfAddableElement {
	/**
	 * Add the DIRECT_SOFT_LINEBREAK to the Document to insert
	 * a soft line-break at that position.
	 */
	public static final RtfDirectContent DIRECT_SOFT_LINEBREAK = new RtfDirectContent("\\line");
	
	/**
	 * The direct content to add.
	 */
	private String directContent = "";
	
	/**
	 * Constructs a new RtfDirectContent with the content to add.
	 * 
	 * @param directContent The content to add.
	 */
	public RtfDirectContent(String directContent) {
		this.directContent = directContent;
	}
	
	/**
	 * Writes the direct content.
	 */
	public byte[] write() {
		return this.directContent.getBytes();
	}
}
