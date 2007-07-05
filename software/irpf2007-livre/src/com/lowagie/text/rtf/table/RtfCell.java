/*
 * $Id: RtfCell.java,v 1.18 2006/09/14 23:10:56 xlv Exp $
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.List;
import com.lowagie.text.Paragraph;
import com.lowagie.text.rtf.RtfBasicElement;
import com.lowagie.text.rtf.RtfExtendedElement;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.style.RtfColor;
import com.lowagie.text.rtf.style.RtfParagraphStyle;
import com.lowagie.text.rtf.text.RtfParagraph;


/**
 * The RtfCell wraps a Cell, but can also be added directly to a Table.
 * The RtfCell is an extension of Cell, that supports a multitude of different
 * borderstyles.
 * 
 * @version $Id: RtfCell.java,v 1.18 2006/09/14 23:10:56 xlv Exp $
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 * @author Steffen Stundzig
 * @author Benoit Wiart
 * @see com.lowagie.text.rtf.table.RtfBorder
 */
public class RtfCell extends Cell implements RtfExtendedElement {

    /**
     * This cell is not merged
     */
    private static final int MERGE_NONE = 0;
    /**
     * This cell is the parent cell of a vertical merge operation
     */
    private static final int MERGE_VERT_PARENT = 1;
    /**
     * This cell is a child cell of a vertical merge operation
     */
    private static final int MERGE_VERT_CHILD = 2;

    /**
     * The parent RtfRow of this RtfCell
     */
    private RtfRow parentRow = null;
    /**
     * The content of this RtfCell
     */
    private ArrayList content = null;
    /**
     * The right margin of this RtfCell
     */
    private int cellRight = 0;
    /**
     * The width of this RtfCell
     */
    private int cellWidth = 0;
    /**
     * The borders of this RtfCell
     */
    private RtfBorderGroup borders = null;
    /**
     * The vertical alignment of this RtfCell
     */
    private int verticalAlignment = Cell.ALIGN_MIDDLE;
    /**
     * The background color of this RtfCell
     */
    private RtfColor backgroundColor = null;
    /**
     * The padding of this RtfCell
     */
    private int cellPadding = 0;
    /**
     * The merge type of this RtfCell
     */
    private int mergeType = MERGE_NONE;
    /**
     * The number of columns spanned by this RtfCell
     */
    private int colspan = 1;
    /**
     * The number of rows spanned by this RtfCell
     */
    private int rowspan = 1;
    /**
     * The RtfDocument this RtfCell belongs to
     */
    private RtfDocument document = null;
    /**
     * Whether this RtfCell is in a header
     */
    private boolean inHeader = false;
    /**
     * Whether this RtfCell is a placeholder for a removed table cell.
     */
    private boolean deleted = false;

    /**
     * Constructs an empty RtfCell
     */
    public RtfCell() {
        super();
        this.borders = new RtfBorderGroup();
    }
    
    /**
     * Constructs a RtfCell based upon a String
     * 
     * @param content The String to base the RtfCell on
     */
    public RtfCell(String content) {
        super(content);
        this.borders = new RtfBorderGroup();
    }
    
    /**
     * Constructs a RtfCell based upon an Element
     * 
     * @param element The Element to base the RtfCell on
     * @throws BadElementException If the Element is not valid
     */
    public RtfCell(Element element) throws BadElementException {
        super(element);
        this.borders = new RtfBorderGroup();
    }
    
    /**
     * Constructs a RtfCell based upon certain Properties
     * 
     * @param properties The Properties for this RtfCell
     */
    public RtfCell(Properties properties) {
        super(properties);
        this.borders = new RtfBorderGroup();
    }
    
    /**
     * Constructs a deleted RtfCell.
     * 
     * @param deleted Whether this RtfCell is actually deleted.
     */
    protected RtfCell(boolean deleted) {
        super();
        this.deleted = deleted;
    }
    
    /**
     * Constructs a RtfCell based on a Cell.
     * 
     * @param doc The RtfDocument this RtfCell belongs to
     * @param row The RtfRow this RtfCell lies in
     * @param cell The Cell to base this RtfCell on
     */
    protected RtfCell(RtfDocument doc, RtfRow row, Cell cell) {
        this.document = doc;
        this.parentRow = row;
        importCell(cell);
    }

    /**
     * Imports the Cell properties into the RtfCell
     * 
     * @param cell The Cell to import
     */
    private void importCell(Cell cell) {
        this.content = new ArrayList();
        
        if(cell == null) {
            this.borders = new RtfBorderGroup(this.document, RtfBorder.CELL_BORDER, this.parentRow.getParentTable().getBorders());
            return;
        }
        
        this.colspan = cell.colspan();
        this.rowspan = cell.rowspan();
        if(cell.rowspan() > 1) {
            this.mergeType = MERGE_VERT_PARENT;
        }
        if(cell instanceof RtfCell) {
            this.borders = new RtfBorderGroup(this.document, RtfBorder.CELL_BORDER, ((RtfCell) cell).getBorders());
        } else {
            this.borders = new RtfBorderGroup(this.document, RtfBorder.CELL_BORDER, cell.border(), cell.borderWidth(), cell.borderColor());
        }
        this.verticalAlignment = cell.verticalAlignment();
        if(cell.backgroundColor() == null) {
            this.backgroundColor = new RtfColor(this.document, 255, 255, 255);
        } else {
            this.backgroundColor = new RtfColor(this.document, cell.backgroundColor());
        }
        
        this.cellPadding = (int) this.parentRow.getParentTable().getCellPadding();
        
        if(cell != null) {
            Iterator cellIterator = cell.getElements();
            Paragraph container = null;
            while(cellIterator.hasNext()) {
                try {
                    Element element = (Element) cellIterator.next();
                    // should we wrap it in a paragraph
                    if(!(element instanceof Paragraph) && !(element instanceof List)) {
                        if(container != null) {
                            container.add(element);
                        } else {
                            container = new Paragraph();
                            container.setAlignment(cell.horizontalAlignment());
                            container.add(element);
                        }
                    } else {
                        if(container != null) {
                            RtfBasicElement rtfElement = this.document.getMapper().mapElement(container);
                            rtfElement.setInTable(true);
                            this.content.add(rtfElement);
                            container = null;
                        }
                        // if horizontal alignment is undefined overwrite
                        // with that of enclosing cell
                        if (element instanceof Paragraph && ((Paragraph) element).alignment() == Element.ALIGN_UNDEFINED) {
                            ((Paragraph) element).setAlignment(cell.horizontalAlignment());
                        }

                        RtfBasicElement rtfElement = this.document.getMapper().mapElement(element);
                        rtfElement.setInTable(true);
                        this.content.add(rtfElement);
                    }
                } catch(DocumentException de) {
                    de.printStackTrace();
                }
            }
            if(container != null) {
                try {
                    RtfBasicElement rtfElement = this.document.getMapper().mapElement(container);
                    rtfElement.setInTable(true);
                    this.content.add(rtfElement);
                } catch(DocumentException de) {
                    de.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Write the cell definition part of this RtfCell
     * 
     * @return A byte array with the cell definition
     */
    public byte[] writeDefinition() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            if(this.mergeType == MERGE_VERT_PARENT) {
                result.write("\\clvmgf".getBytes());
            } else if(this.mergeType == MERGE_VERT_CHILD) {
                result.write("\\clvmrg".getBytes());
            }
            switch (verticalAlignment) {
                case Element.ALIGN_BOTTOM:
                    result.write("\\clvertalb".getBytes());
                    break;
                case Element.ALIGN_CENTER:
                case Element.ALIGN_MIDDLE:
                    result.write("\\clvertalc".getBytes());
                    break;
                case Element.ALIGN_TOP:
                    result.write("\\clvertalt".getBytes());
                    break;
            }
            result.write(this.borders.write());

            if(this.backgroundColor != null) {
                result.write("\\clcbpat".getBytes());
                result.write(intToByteArray(this.backgroundColor.getColorNumber()));
            }
            result.write('\n');
            
            result.write("\\clftsWidth3".getBytes());
            result.write('\n');
            
            result.write("\\clwWidth".getBytes());
            result.write(intToByteArray(this.cellWidth));
            result.write('\n');
            
            if(this.cellPadding > 0) {
                result.write("\\clpadl".getBytes());
                result.write(intToByteArray(this.cellPadding / 2));
                result.write("\\clpadt".getBytes());
                result.write(intToByteArray(this.cellPadding / 2));
                result.write("\\clpadr".getBytes());
                result.write(intToByteArray(this.cellPadding / 2));
                result.write("\\clpadb".getBytes());
                result.write(intToByteArray(this.cellPadding / 2));
                result.write("\\clpadfl3".getBytes());
                result.write("\\clpadft3".getBytes());
                result.write("\\clpadfr3".getBytes());
                result.write("\\clpadfb3".getBytes());
            }
            result.write("\\cellx".getBytes());
            result.write(intToByteArray(this.cellRight));
            
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        return result.toByteArray();
    }
    
    /**
     * Write the content of this RtfCell
     * 
     * @return A byte array with the content of this RtfCell
     */
    public byte[] write() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            if(this.content.size() == 0) {
                result.write(RtfParagraph.PARAGRAPH_DEFAULTS);
                if(this.parentRow.getParentTable().getTableFitToPage()) {
                    result.write(RtfParagraphStyle.KEEP_TOGETHER_WITH_NEXT);
                }
                result.write(RtfParagraph.IN_TABLE);
            } else {
                for(int i = 0; i < this.content.size(); i++) {
                    RtfBasicElement rtfElement = (RtfBasicElement) this.content.get(i);
                    if(rtfElement instanceof RtfParagraph) {
                        ((RtfParagraph) rtfElement).setKeepTogetherWithNext(this.parentRow.getParentTable().getTableFitToPage());
                    }
                    result.write(rtfElement.write());
                    if(rtfElement instanceof RtfParagraph && i < (this.content.size() - 1)) {
                        result.write(RtfParagraph.PARAGRAPH);
                    }
                }
            }
            result.write("\\cell".getBytes());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        return result.toByteArray();
    }

    /**
     * Sets the right margin of this cell. Used in merge operations
     * 
     * @param cellRight The right margin to use
     */
    protected void setCellRight(int cellRight) {
        this.cellRight = cellRight;
    }
    
    /**
     * Gets the right margin of this RtfCell
     * 
     * @return The right margin of this RtfCell.
     */
    protected int getCellRight() {
        return this.cellRight;
    }
    
    /**
     * Sets the cell width of this RtfCell. Used in merge operations.
     * 
     * @param cellWidth The cell width to use
     */
    protected void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }
    
    /**
     * Gets the cell width of this RtfCell
     * 
     * @return The cell width of this RtfCell
     */
    protected int getCellWidth() {
        return this.cellWidth;
    }
    
    /**
     * Gets the number of columns this RtfCell spans
     * 
     * @return The number of columns this RtfCell spans
     */
    protected int getColspan() {
        return this.colspan;
    }
    
    /**
     * Gets the number of rows this RtfCell spans
     * 
     * @return The number of rows this RtfCell spans
     */
    protected int getRowspan() {
        return this.rowspan;
    }
    
    /**
     * Gets the cell padding of this RtfCell
     * 
     * @return The cell padding of this RtfCell
     */
    protected int getCellpadding() {
        return this.cellPadding;
    }

    /**
     * Gets the borders of this RtfCell
     * 
     * @return The borders of this RtfCell
     */
    protected RtfBorderGroup getBorders() {
        return this.borders;
    }
    
    /**
     * Set the borders of this RtfCell
     * 
     * @param borderGroup The RtfBorderGroup to use as borders
     */
    public void setBorders(RtfBorderGroup borderGroup) {
        this.borders = new RtfBorderGroup(this.document, RtfBorder.CELL_BORDER, borderGroup);
    }
    
    /**
     * Get the vertical alignment of this RtfCell
     * 
     * @return The vertical alignment of this RtfCell
     */
    protected int getVerticalAlignment() {
        return this.verticalAlignment;
    }
    
    /**
     * Get the background color of this RtfCell
     * 
     * @return The background color of this RtfCell
     */
    protected RtfColor getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * Merge this cell into the parent cell.
     * 
     * @param mergeParent The RtfCell to merge with
     */
    protected void setCellMergeChild(RtfCell mergeParent) {
        this.mergeType = MERGE_VERT_CHILD;
        this.cellWidth = mergeParent.getCellWidth();
        this.cellRight = mergeParent.getCellRight();
        this.cellPadding = mergeParent.getCellpadding();
        this.borders = mergeParent.getBorders();
        this.verticalAlignment = mergeParent.getVerticalAlignment();
        this.backgroundColor = mergeParent.getBackgroundColor();
    }

    /**
     * Sets the RtfDocument this RtfCell belongs to
     * 
     * @param doc The RtfDocument to use
     */
    public void setRtfDocument(RtfDocument doc) {
        this.document = doc;
    }
    
    /**
     * Unused
     * @param inTable
     */
    public void setInTable(boolean inTable) {
    }
    
    /**
     * Sets whether this RtfCell is in a header
     * 
     * @param inHeader <code>True</code> if this RtfCell is in a header, <code>false</code> otherwise
     */
    public void setInHeader(boolean inHeader) {
        this.inHeader = inHeader;
        for(int i = 0; i < this.content.size(); i++) {
            ((RtfBasicElement) this.content.get(i)).setInHeader(inHeader);
        }
    }

    /**
     * Transforms an integer into its String representation and then returns the bytes
     * of that string.
     *
     * @param i The integer to convert
     * @return A byte array representing the integer
     */
    private byte[] intToByteArray(int i) {
        return Integer.toString(i).getBytes();
    }
    
    /**
     * Checks whether this RtfCell is a placeholder for
     * a table cell that has been removed due to col/row spanning.
     * 
     * @return <code>True</code> if this RtfCell is deleted, <code>false</code> otherwise.
     */
    public boolean isDeleted() {
        return this.deleted;
    }
}
