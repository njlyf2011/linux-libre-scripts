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
package net.sf.jasperreports.crosstabs.fill;

import net.sf.jasperreports.engine.fill.JRCalculable;

/**
 * Percentage calculator interface.
 * <p>
 * Implementing this interface is required for percentage calculations on types
 * missing built-in support.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRPercentageCalculator.java 1229 2006-04-19 13:27:35 +0300 (Wed, 19 Apr 2006) teodord $
 * @see net.sf.jasperreports.crosstabs.fill.JRPercentageCalculatorFactory
 */
public interface JRPercentageCalculator
{
	/**
	 * Calculates the percentage of a value out of a total.
	 * 
	 * @param value the value
	 * @param total the total
	 * @return the percentage
	 */
	public Object calculatePercentage(JRCalculable value, JRCalculable total);
}
