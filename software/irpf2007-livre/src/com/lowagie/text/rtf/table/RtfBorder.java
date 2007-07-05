/*
 * $Id: RtfBorder.java,v 1.17 2006/08/07 10:42:35 blowagie Exp $
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

package com.lowagie.text.rtf.table;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.style.RtfColor;


/**
 * The RtfBorder handle one row or cell border.
 * INTERNAL USE ONLY
 * 
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfBorder extends RtfElement {

    /**
     * Constant for the left row border
     */
    protected static final byte[] ROW_BORDER_LEFT = "\\trbrdrl".getBytes();
    /**
     * Constant for the top row border
     */
    protected static final byte[] ROW_BORDER_TOP = "\\trbrdrt".getBytes();
    /**
     * Constant for the right row border
     */
    protected static final byte[] ROW_BORDER_RIGHT = "\\trbrdrr".getBytes();
    /**
     * Constant for the bottom row border
     */
    protected static final byte[] ROW_BORDER_BOTTOM = "\\trbrdrb".getBytes();
    /**
     * Constant for the horizontal line
     */
    protected static final byte[] ROW_BORDER_HORIZONTAL = "\\trbrdrh".getBytes();
    /**
     * Constant for the vertical line
     */
    protected static final byte[] ROW_BORDER_VERTICAL = "\\trbrdrv".getBytes();
    /**
     * Constant for the left cell border
     */
    protected static final byte[] CELL_BORDER_LEFT = "\\clbrdrl".getBytes();
    /**
     * Constant for the top cell border
     */
    protected static final byte[] CELL_BORDER_TOP = "\\clbrdrt".getBytes();
    /**
     * Constant for the right cell border
     */
    protected static final byte[] CELL_BORDER_RIGHT = "\\clbrdrr".getBytes();
    /**
     * Constant for the bottom cell border
     */
    protected static final byte[] CELL_BORDER_BOTTOM = "\\clbrdrb".getBytes();
    /**
     * Constant for the border width
     */
    protected static final byte[] BORDER_WIDTH = "\\brdrw".getBytes();
    /**
     * Constant for the border colour number
     */
    protected static final byte[] BORDER_COLOR_NUMBER = "\\brdrcf".getBytes();
    /**
     * Constant for the single border style
     */
    protected static final byte[] BORDER_STYLE_SINGLE = "\\brdrs".getBytes();
    /**
     * Constant for the double thick border style
     */
    protected static final byte[] BORDER_STYLE_DOUBLE_THICK	= "\\brdrth".getBytes();
    /**
     * Constant for the shadowed border style
     */
    protected static final byte[] BORDER_STYLE_SHADOWED = "\\brdrsh".getBytes();
    /**
     * Constant for the dotted border style
     */
    protected static final byte[] BORDER_STYLE_DOTTED = "\\brdrdot".getBytes();
    /**
     * Constant for the dashed border style
     */
    protected static final byte[] BORDER_STYLE_DASHED = "\\brdrdash".getBytes();
    /**
     * Constant for the hairline border style
     */
    protected static final byte[] BORDER_STYLE_HAIRLINE = "\\brdrhair".getBytes();
    /**
     * Constant for the double border style
     */
    protected static final byte[] BORDER_STYLE_DOUBLE = "\\brdrdb".getBytes();
    /**
     * Constant for the dot dash border style
     */
    protected static final byte[] BORDER_STYLE_DOT_DASH = "\\brdrdashd".getBytes();
    /**
     * Constant for the dot dot dash border style
     */
    protected static final byte[] BORDER_STYLE_DOT_DOT_DASH	= "\\brdrdashdd".getBytes();
    /**
     * Constant for the triple border style
     */
    protected static final byte[] BORDER_STYLE_TRIPLE = "\\brdrtriple".getBytes();
    /**
     * Constant for the thick thin border style
     */
    protected static final byte[] BORDER_STYLE_THICK_THIN = "\\brdrtnthsg".getBytes();
    /**
     * Constant for the thin thick border style
     */
    protected static final byte[] BORDER_STYLE_THIN_THICK = "\\brdrthtnsg".getBytes();
    /**
     * Constant for the thin thick thin border style
     */
    protected static final byte[] BORDER_STYLE_THIN_THICK_THIN = "\\brdrtnthtnsg".getBytes();
    /**
     * Constant for the thick thin medium border style
     */
    protected static final byte[] BORDER_STYLE_THICK_THIN_MED = "\\brdrtnthmg".getBytes();
    /**
     * Constant for the thin thick medium border style
     */
    protected static final byte[] BORDER_STYLE_THIN_THICK_MED = "\\brdrthtnmg".getBytes();
    /**
     * Constant for the thin thick thin medium border style
     */
    protected static final byte[] BORDER_STYLE_THIN_THICK_THIN_MED = "\\brdrtnthtnmg".getBytes();
    /**
     * Constant for the thick thin large border style
     */
    protected static final byte[] BORDER_STYLE_THICK_THIN_LARGE = "\\brdrtnthlg".getBytes();
    /**
     * Constant for the thin thick large border style
     */
    protected static final byte[] BORDER_STYLE_THIN_THICK_LARGE	= "\\brdrthtnlg".getBytes();
    /**
     * Constant for the thin thick thin large border style
     */
    protected static final byte[] BORDER_STYLE_THIN_THICK_THIN_LARGE = "\\brdrtnthtnlg".getBytes();
    /**
     * Constant for the wavy border style
     */
    protected static final byte[] BORDER_STYLE_WAVY = "\\brdrwavy".getBytes();
    /**
     * Constant for the double wavy border style
     */
    protected static final byte[] BORDER_STYLE_DOUBLE_WAVY = "\\brdrwavydb".getBytes();
    /**
     * Constant for the striped border style
     */
    protected static final byte[] BORDER_STYLE_STRIPED = "\\brdrdashdotstr".getBytes();
    /**
     * Constant for the embossed border style
     */
    protected static final byte[] BORDER_STYLE_EMBOSS = "\\brdremboss".getBytes();
    /**
     * Constant for the engraved border style
     */
    protected static final byte[] BORDER_STYLE_ENGRAVE = "\\brdrengrave".getBytes();

    /**
     * Constant for a row border
     */
    protected static final int ROW_BORDER = 1;
    /**
     * Constant for a cell border
     */
    protected static final int CELL_BORDER = 2;
    
    /**
     * This border is no border :-)
     */
    protected static final int NO_BORDER = 0;
    /**
     * Constant for a left border
     */
    protected static final int LEFT_BORDER = 1;
    /**
     * Constant for a top border
     */
    protected static final int TOP_BORDER = 2;
    /**
     * Constant for a right border
     */
    protected static final int RIGHT_BORDER = 4;
    /**
     * Constant for a bottom border
     */
    protected static final int BOTTOM_BORDER = 8;
    /**
     * Constant for a box (left, top, right, bottom) border 
     */
    protected static final int BOX_BORDER = 15;
    /**
     * Constant for a vertical line
     */
    protected static final int VERTICAL_BORDER = 16;
    /**
     * Constant for a horizontal line
     */
    protected static final int HORIZONTAL_BORDER = 32;
    
    /**
     * Constant for a border with no border
     */
    public static final int BORDER_NONE = 0;
    /**
     * Constant for a single border
     */
    public static final int BORDER_SINGLE = 1;
    /**
     * Constant for a double thick border
     */
    public static final int BORDER_DOUBLE_THICK = 2;
    /**
     * Constant for a shadowed border
     */
    public static final int BORDER_SHADOWED = 3;
    /**
     * Constant for a dotted border
     */
    public static final int BORDER_DOTTED = 4;
    /**
     * Constant for a dashed border
     */
    public static final int BORDER_DASHED = 5;
    /**
     * Constant for a hairline border
     */
    public static final int BORDER_HAIRLINE = 6;
    /**
     * Constant for a double border
     */
    public static final int BORDER_DOUBLE = 7;
    /**
     * Constant for a dot dash border
     */
    public static final int BORDER_DOT_DASH = 8;
    /**
     * Constant for a dot dot dash border
     */
    public static final int BORDER_DOT_DOT_DASH = 9;
    /**
     * Constant for a triple border
     */
    public static final int BORDER_TRIPLE = 10;
    /**
     * Constant for a thick thin border
     */
    public static final int BORDER_THICK_THIN = 11;
    /**
     * Constant for a thin thick border
     */
    public static final int BORDER_THIN_THICK = 12;
    /**
     * Constant for a thin thick thin border
     */
    public static final int BORDER_THIN_THICK_THIN = 13;
    /**
     * Constant for a thick thin medium border
     */
    public static final int BORDER_THICK_THIN_MED = 14;
    /**
     * Constant for a thin thick medium border
     */
    public static final int BORDER_THIN_THICK_MED = 15;
    /**
     * Constant for a thin thick thin medium border
     */
    public static final int BORDER_THIN_THICK_THIN_MED = 16;
    /**
     * Constant for a thick thin large border
     */
    public static final int BORDER_THICK_THIN_LARGE = 17;
    /**
     * Constant for a thin thick large border
     */
    public static final int BORDER_THIN_THICK_LARGE = 18;
    /**
     * Constant for a thin thick thin large border
     */
    public static final int BORDER_THIN_THICK_THIN_LARGE = 19;
    /**
     * Constant for a wavy border
     */
    public static final int BORDER_WAVY = 20;
    /**
     * Constant for a double wavy border
     */
    public static final int BORDER_DOUBLE_WAVY = 21;
    /**
     * Constant for a striped border
     */
    public static final int BORDER_STRIPED = 22;
    /**
     * Constant for an embossed border
     */
    public static final int BORDER_EMBOSS = 23;
    /**
     * Constant for an engraved border
     */
    public static final int BORDER_ENGRAVE = 24;
    
    /**
     * The type of this RtfBorder
     */
    private int borderType = ROW_BORDER;
    /**
     * The position of this RtfBorder
     */
    private int borderPosition = NO_BORDER;
    /**
     * The style of this RtfBorder
     */
    private int borderStyle = BORDER_NONE;
    /**
     * The width of this RtfBorder
     */
    private int borderWidth = 20;
    /**
     * The colour of this RtfBorder
     */
    private RtfColor borderColor = null;
    
    /**
     * Makes a copy of the given RtfBorder
     * 
     * @param doc The RtfDocument this RtfBorder belongs to
     * @param borderType The border type of this RtfBorder
     * @param border The RtfBorder to copy
     */
    protected RtfBorder(RtfDocument doc, int borderType, RtfBorder border) {
        super(doc);
        this.borderType = borderType;
        this.borderPosition = border.getBorderPosition();
        this.borderStyle = border.getBorderStyle();
        this.borderWidth = border.getBorderWidth();
        this.borderColor = new RtfColor(this.document, border.getBorderColor());
    }
    
    /**
     * Constructs a RtfBorder
     * 
     * @param doc The RtfDocument this RtfBorder belongs to
     * @param borderType The type of border this RtfBorder is
     * @param borderPosition The position of this RtfBorder
     * @param borderStyle The style of this RtfBorder
     * @param borderWidth The width of this RtfBorder
     * @param borderColor The colour of this RtfBorder
     */
    protected RtfBorder(RtfDocument doc, int borderType, int borderPosition, int borderStyle, float borderWidth, Color borderColor) {
        super(doc);
        this.borderType = borderType;
        this.borderPosition = borderPosition;
        this.borderStyle = borderStyle;
        if(borderWidth > 2) {
            borderWidth = 2;
        }
        this.borderWidth = (int) (borderWidth * TWIPS_FACTOR);
        if(this.borderWidth == 0) {
            this.borderStyle = BORDER_NONE;
        }
        if(borderColor == null) {
            this.borderColor = new RtfColor(this.document, new Color(0, 0, 0));
        } else {
            this.borderColor = new RtfColor(this.document, borderColor);
        }
    }
    
    /**
     * Writes the RtfBorder settings
     * 
     * @return A byte array with the RtfBorder settings
     */
    public byte[] write() {
        if(this.borderStyle == BORDER_NONE || this.borderPosition == NO_BORDER || this.borderWidth == 0) {
            return new byte[0];
        }
        
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            if(this.borderType == ROW_BORDER) {
                switch(this.borderPosition) {
                    case LEFT_BORDER:
                        result.write(ROW_BORDER_LEFT);
                    	break;
                    case TOP_BORDER:
                        result.write(ROW_BORDER_TOP);
                        break;
                    case RIGHT_BORDER:
                        result.write(ROW_BORDER_RIGHT);
                        break;
                    case BOTTOM_BORDER:
                        result.write(ROW_BORDER_BOTTOM);
                        break;
                    case HORIZONTAL_BORDER:
                        result.write(ROW_BORDER_HORIZONTAL);
                        break;
                    case VERTICAL_BORDER:
                        result.write(ROW_BORDER_VERTICAL);
                        break;
                    default:
                        return new byte[0];
                }
                result.write(writeBorderStyle());
                result.write(BORDER_WIDTH);
                result.write(intToByteArray(this.borderWidth));
                result.write(BORDER_COLOR_NUMBER);
                result.write(intToByteArray(this.borderColor.getColorNumber()));
                result.write('\n');
            } else if(this.borderType == CELL_BORDER) {
                switch(this.borderPosition) {
                    case LEFT_BORDER:
                        result.write(CELL_BORDER_LEFT);
                    	break;
                    case TOP_BORDER:
                        result.write(CELL_BORDER_TOP);
                        break;
                    case RIGHT_BORDER:
                        result.write(CELL_BORDER_RIGHT);
                        break;
                    case BOTTOM_BORDER:
                        result.write(CELL_BORDER_BOTTOM);
                        break;
                    default:
                        return new byte[0];
                }
                result.write(writeBorderStyle());
                result.write(BORDER_WIDTH);
                result.write(intToByteArray(this.borderWidth));
                result.write(BORDER_COLOR_NUMBER);
                result.write(intToByteArray(this.borderColor.getColorNumber()));
                result.write('\n');
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        return result.toByteArray();
    }
    
    /**
     * Writes the style of this RtfBorder
     * 
     * @return A byte array containing the style of this RtfBorder
     */
    private byte[] writeBorderStyle() {
        switch(this.borderStyle) {
            case BORDER_NONE                    : return new byte[0];
            case BORDER_SINGLE 					: return BORDER_STYLE_SINGLE;
            case BORDER_DOUBLE_THICK	 		: return BORDER_STYLE_DOUBLE_THICK;
            case BORDER_SHADOWED 				: return BORDER_STYLE_SHADOWED;
            case BORDER_DOTTED   				: return BORDER_STYLE_DOTTED;
            case BORDER_DASHED   				: return BORDER_STYLE_DASHED;
            case BORDER_HAIRLINE   				: return BORDER_STYLE_HAIRLINE;
            case BORDER_DOUBLE 		  			: return BORDER_STYLE_DOUBLE;
            case BORDER_DOT_DASH   				: return BORDER_STYLE_DOT_DASH;
            case BORDER_DOT_DOT_DASH			: return BORDER_STYLE_DOT_DOT_DASH;
            case BORDER_TRIPLE					: return BORDER_STYLE_TRIPLE;
            case BORDER_THICK_THIN				: return BORDER_STYLE_THICK_THIN;
            case BORDER_THIN_THICK				: return BORDER_STYLE_THIN_THICK;
            case BORDER_THIN_THICK_THIN			: return BORDER_STYLE_THIN_THICK_THIN;
            case BORDER_THICK_THIN_MED			: return BORDER_STYLE_THICK_THIN_MED;
            case BORDER_THIN_THICK_MED			: return BORDER_STYLE_THIN_THICK_MED;
            case BORDER_THIN_THICK_THIN_MED		: return BORDER_STYLE_THIN_THICK_THIN_MED;
            case BORDER_THICK_THIN_LARGE		: return BORDER_STYLE_THICK_THIN_LARGE;
            case BORDER_THIN_THICK_LARGE		: return BORDER_STYLE_THIN_THICK_LARGE;
            case BORDER_THIN_THICK_THIN_LARGE	: return BORDER_STYLE_THIN_THICK_THIN_LARGE;
            case BORDER_WAVY					: return BORDER_STYLE_WAVY;
            case BORDER_DOUBLE_WAVY				: return BORDER_STYLE_DOUBLE_WAVY;
            case BORDER_STRIPED					: return BORDER_STYLE_STRIPED;
            case BORDER_EMBOSS					: return BORDER_STYLE_EMBOSS;
            case BORDER_ENGRAVE					: return BORDER_STYLE_ENGRAVE;
            default                             : return BORDER_STYLE_SINGLE;
        }
    }
    
    /**
     * Gets the colour of this RtfBorder
     * 
     * @return Returns RtfColor of this RtfBorder
     */
    protected RtfColor getBorderColor() {
        return borderColor;
    }

    /**
     * Gets the position of this RtfBorder
     * @return Returns the position of this RtfBorder
     */
    protected int getBorderPosition() {
        return borderPosition;
    }

    /**
     * Gets the style of this RtfBorder
     * 
     * @return Returns the style of this RtfBorder
     */
    protected int getBorderStyle() {
        return borderStyle;
    }

    /**
     * Gets the type of this RtfBorder
     * 
     * @return Returns the type of this RtfBorder
     */
    protected int getBorderType() {
        return borderType;
    }

    /**
     * Gets the width of this RtfBorder
     * 
     * @return Returns the width of this RtfBorder
     */
    protected int getBorderWidth() {
        return borderWidth;
    }
}
