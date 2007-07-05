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
package net.sf.jasperreports.engine.base;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRSortField;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseField.java,v 1.28 2006/04/19 10:17:59 teodord Exp $
 */
public class JRBaseSortField implements JRSortField, Serializable
{


	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 *
	 */
	protected String name = null;
	protected byte order = SORT_ORDER_ASCENDING;


	/**
	 *
	 */
	protected JRBaseSortField()
	{
	}
	
	
	/**
	 *
	 */
	protected JRBaseSortField(JRSortField sortField, JRBaseObjectFactory factory)
	{
		factory.put(sortField, this);
		
		name = sortField.getName();
		order = sortField.getOrder();
	}
		

	/**
	 *
	 */
	public String getName()
	{
		return name;
	}
		
	/**
	 *
	 */
	public byte getOrder()
	{
		return order;
	}
		
	/**
	 *
	 */
	public void setOrder(byte order)
	{
		this.order = order;
	}
	
}
