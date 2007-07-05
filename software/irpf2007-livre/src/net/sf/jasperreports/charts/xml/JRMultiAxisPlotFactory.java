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
package net.sf.jasperreports.charts.xml;

import net.sf.jasperreports.charts.design.JRDesignMultiAxisPlot;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignChart;
import net.sf.jasperreports.engine.xml.JRBaseFactory;

import org.xml.sax.Attributes;


/**
 * @author Barry Klawans (bklawans@users.sourceforge.net)
 * @version $Id: JRMultiAxisPlotFactory.java 1387 2006-09-06 00:33:40 +0300 (Wed, 06 Sep 2006) bklawans $
 */
public class JRMultiAxisPlotFactory extends JRBaseFactory
{
    
	/**
	 *
	 */
	public Object createObject(Attributes atts) throws JRException
	{
		JRDesignChart chart = (JRDesignChart)digester.peek();
		JRDesignMultiAxisPlot multiAxisPlot = (JRDesignMultiAxisPlot)chart.getPlot();
		multiAxisPlot.setChart(chart);

        
		return multiAxisPlot;
	}
}
