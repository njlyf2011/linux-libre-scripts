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

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ObjectCreationFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseFactory.java 1348 2006-07-19 17:20:47 +0300 (Wed, 19 Jul 2006) lucianc $
 */
public abstract class JRBaseFactory implements ObjectCreationFactory
{


	/**
	 *
	 */
	protected transient Digester digester = null;


	/**
	 *
	 */
	public Digester getDigester() 
	{
		return this.digester;
	}
	

	/**
	 *
	 */
	public void setDigester(Digester digester) 
	{
		this.digester = digester;
	}
		

}
