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
package net.sf.jasperreports.engine.fill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportReturnValue;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRFillBand.java 1485 2006-11-14 20:23:17 +0200 (Tue, 14 Nov 2006) teodord $
 */
public class JRFillBand extends JRFillElementContainer implements JRBand
{


	/**
	 *
	 */
	private JRBand parent = null;

	private boolean isPrintWhenTrue = true;

	/**
	 *
	 */
	private boolean isNewPageColumn = false;
	private boolean isFirstWholeOnPageColumn = false;
	private Map isNewGroupMap = new HashMap();

	private Set nowEvaluationTimes;

	/**
	 *
	 */
	protected JRFillBand(
		JRBaseFiller filler,
		JRBand band,
		JRFillObjectFactory factory
		)
	{
		super(filler, band, factory);

		parent = band;

		if (deepElements.length > 0)
		{
			for(int i = 0; i < deepElements.length; i++)
			{
				deepElements[i].setBand(this);
			}
		}

		initElements();

		initConditionalStyles();

		nowEvaluationTimes = new HashSet();
	}


	/**
	 *
	 */
	protected void setNewPageColumn(boolean isNew)
	{
		this.isNewPageColumn = isNew;
	}


	/**
	 *
	 */
	protected boolean isNewPageColumn()
	{
		return isNewPageColumn;
	}


	/**
	 * Decides whether this band is the for whole band on the page/column.
	 *
	 * @return whether this band is the for whole band on the page/column
	 */
	protected boolean isFirstWholeOnPageColumn()
	{
		return isFirstWholeOnPageColumn;
	}


	/**
	 *
	 */
	protected void setNewGroup(JRGroup group, boolean isNew)
	{
		isNewGroupMap.put(group, isNew ? Boolean.TRUE : Boolean.FALSE);
	}


	/**
	 *
	 */
	protected boolean isNewGroup(JRGroup group)
	{
		Boolean value = (Boolean)isNewGroupMap.get(group);

		if (value == null)
		{
			value = Boolean.FALSE;
		}

		return value.booleanValue();
	}


	/**
	 *
	 */
	public int getHeight()
	{
		return (parent != null ? parent.getHeight() : 0);
	}

	/**
	 *
	 */
	public boolean isSplitAllowed()
	{
		return parent.isSplitAllowed();
	}

	/**
	 *
	 */
	public void setSplitAllowed(boolean isSplitAllowed)
	{
	}

	/**
	 *
	 */
	public JRExpression getPrintWhenExpression()
	{
		return (parent != null ? parent.getPrintWhenExpression() : null);
	}

	/**
	 *
	 */
	protected boolean isPrintWhenExpressionNull()
	{
		return (getPrintWhenExpression() == null);
	}

	/**
	 *
	 */
	protected boolean isPrintWhenTrue()
	{
		return isPrintWhenTrue;
	}

	/**
	 *
	 */
	protected void setPrintWhenTrue(boolean isPrintWhenTrue)
	{
		this.isPrintWhenTrue = isPrintWhenTrue;
	}

	/**
	 *
	 */
	protected boolean isToPrint()
	{
		return
			(isPrintWhenExpressionNull() ||
			 (!isPrintWhenExpressionNull() &&
			  isPrintWhenTrue()));
	}


	/**
	 *
	 */
	protected void evaluatePrintWhenExpression(
		byte evaluation
		) throws JRException
	{
		boolean isPrintTrue = false;

		JRExpression expression = getPrintWhenExpression();
		if (expression != null)
		{
			Boolean printWhenExpressionValue = (Boolean)filler.evaluateExpression(expression, evaluation);
			if (printWhenExpressionValue == null)
			{
				isPrintTrue = false;
			}
			else
			{
				isPrintTrue = printWhenExpressionValue.booleanValue();
			}
		}

		setPrintWhenTrue(isPrintTrue);
	}



	/**
	 *
	 */
	protected JRPrintBand refill(
		int availableStretchHeight
		) throws JRException
	{
		rewind();

		return fill(availableStretchHeight);
	}


	/**
	 *
	 */
	protected JRPrintBand fill() throws JRException
	{
		return fill(0, false);
	}


	/**
	 *
	 */
	protected JRPrintBand fill(
		int availableStretchHeight
		) throws JRException
	{
		return fill(availableStretchHeight, true);
	}


	/**
	 *
	 */
	protected JRPrintBand fill(
		int availableStretchHeight,
		boolean isOverflowAllowed
		) throws JRException
	{
		filler.fillContext.ensureMasterPageAvailable();

		if (
			Thread.currentThread().isInterrupted()
			|| filler.isInterrupted()
			)
		{
			// child fillers will stop if this parent filler was marked as interrupted
			filler.setInterrupted(true);

			throw new JRFillInterruptedException();
		}

		filler.setBandOverFlowAllowed(isOverflowAllowed);

		initFill();

		if (isNewPageColumn && !isOverflow)
		{
			isFirstWholeOnPageColumn = true;
		}
		
		resetElements();

		prepareElements(availableStretchHeight, isOverflowAllowed);

		stretchElements();

		moveBandBottomElements();

		removeBlankElements();

		isFirstWholeOnPageColumn = isNewPageColumn && isOverflow;
		isNewPageColumn = false;
		isNewGroupMap = new HashMap();

		JRPrintBand printBand = new JRPrintBand();
		fillElements(printBand);

		return printBand;
	}


	protected int getContainerHeight()
	{
		return getHeight();
	}


	protected boolean isVariableUsedInSubreportReturns(String variableName)
	{
		boolean used = false;
		JRElement[] bandElements = getElements();
		if (bandElements != null)
		{
			elementsLoop:
			for (int i = 0; i < bandElements.length; i++)
			{
				JRElement element = bandElements[i];
				if (element instanceof JRSubreport)
				{
					JRSubreport subreport = (JRSubreport) element;
					JRSubreportReturnValue[] returnValues = subreport.getReturnValues();
					if (returnValues != null)
					{
						for (int j = 0; j < returnValues.length; j++)
						{
							JRSubreportReturnValue returnValue = returnValues[j];
							if (returnValue.getToVariable().equals(variableName))
							{
								used = true;
								break elementsLoop;
							}
						}
					}
				}
			}
		}

		return used;
	}


	protected void addNowEvaluationTime(JREvaluationTime evaluationTime)
	{
		nowEvaluationTimes.add(evaluationTime);
	}


	protected void addNowEvaluationTimes(JREvaluationTime[] evaluationTimes)
	{
		for (int i = 0; i < evaluationTimes.length; i++)
		{
			nowEvaluationTimes.add(evaluationTimes[i]);
		}
	}


	protected boolean isNowEvaluationTime(JREvaluationTime evaluationTime)
	{
		return nowEvaluationTimes.contains(evaluationTime);
	}


	protected int getId()
	{
		return System.identityHashCode(this);
	}


	protected void evaluate(byte evaluation) throws JRException
	{
		evaluateConditionalStyles(evaluation);
		super.evaluate(evaluation);
	}

}
