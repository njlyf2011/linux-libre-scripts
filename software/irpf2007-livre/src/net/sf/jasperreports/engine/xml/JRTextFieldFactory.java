/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine.xml;

import java.util.Collection;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.xml.sax.Attributes;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRTextFieldFactory.java 1355 2006-08-04 17:31:54 +0300 (Fri, 04 Aug 2006) lucianc $
 */
public class JRTextFieldFactory extends JRBaseFactory
{


	/**
	 *
	 */
	private static final String ATTRIBUTE_isStretchWithOverflow = "isStretchWithOverflow";
	private static final String ATTRIBUTE_evaluationTime = "evaluationTime";
	private static final String ATTRIBUTE_evaluationGroup = "evaluationGroup";
	private static final String ATTRIBUTE_pattern = "pattern";
	private static final String ATTRIBUTE_isBlankWhenNull = "isBlankWhenNull";
	private static final String ATTRIBUTE_hyperlinkType = "hyperlinkType";
	private static final String ATTRIBUTE_hyperlinkTarget = "hyperlinkTarget";
	private static final String ATTRIBUTE_bookmarkLevel = "bookmarkLevel";


	/**
	 *
	 */
	public Object createObject(Attributes atts)
	{
		JRXmlLoader xmlLoader = (JRXmlLoader)digester.peek(digester.getCount() - 1);
		Collection groupEvaluatedTextFields = xmlLoader.getGroupEvaluatedTextFields();
		JasperDesign jasperDesign = (JasperDesign)digester.peek(digester.getCount() - 2);

		JRDesignTextField textField = new JRDesignTextField(jasperDesign);

		String isStretchWithOverflow = atts.getValue(ATTRIBUTE_isStretchWithOverflow);
		if (isStretchWithOverflow != null && isStretchWithOverflow.length() > 0)
		{
			textField.setStretchWithOverflow(Boolean.valueOf(isStretchWithOverflow).booleanValue());
		}

		Byte evaluationTime = (Byte)JRXmlConstants.getEvaluationTimeMap().get(atts.getValue(ATTRIBUTE_evaluationTime));
		if (evaluationTime != null)
		{
			textField.setEvaluationTime(evaluationTime.byteValue());
		}
		if (textField.getEvaluationTime() == JRExpression.EVALUATION_TIME_GROUP)
		{
			groupEvaluatedTextFields.add(textField);
			
			String groupName = atts.getValue(ATTRIBUTE_evaluationGroup);
			if (groupName != null)
			{
				JRDesignGroup group = new JRDesignGroup();
				group.setName(groupName);
				textField.setEvaluationGroup(group);
			}
		}
		
		textField.setPattern(atts.getValue(ATTRIBUTE_pattern));

		String isBlankWhenNull = atts.getValue(ATTRIBUTE_isBlankWhenNull);
		if (isBlankWhenNull != null && isBlankWhenNull.length() > 0)
		{
			textField.setBlankWhenNull(Boolean.valueOf(isBlankWhenNull));
		}

		String hyperlinkType = atts.getValue(ATTRIBUTE_hyperlinkType);
		if (hyperlinkType != null)
		{
			textField.setLinkType(hyperlinkType);
		}

		Byte hyperlinkTarget = (Byte)JRXmlConstants.getHyperlinkTargetMap().get(atts.getValue(ATTRIBUTE_hyperlinkTarget));
		if (hyperlinkTarget != null)
		{
			textField.setHyperlinkTarget(hyperlinkTarget.byteValue());
		}
		
		String bookmarkLevelAttr = atts.getValue(ATTRIBUTE_bookmarkLevel);
		if (bookmarkLevelAttr != null)
		{
			textField.setBookmarkLevel(Integer.parseInt(bookmarkLevelAttr));
		}

		return textField;
	}
	

}
