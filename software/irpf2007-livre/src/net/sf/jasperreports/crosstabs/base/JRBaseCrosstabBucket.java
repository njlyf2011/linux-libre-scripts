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
package net.sf.jasperreports.crosstabs.base;

import java.io.Serializable;

import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.fill.calculation.BucketDefinition;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;

/**
 * Base read-only implementation of {@link net.sf.jasperreports.crosstabs.JRCrosstabBucket JRCrosstabBucket}.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRBaseCrosstabBucket.java 1229 2006-04-19 13:27:35 +0300 (Wed, 19 Apr 2006) teodord $
 */
public class JRBaseCrosstabBucket implements JRCrosstabBucket, Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	protected byte order = BucketDefinition.ORDER_ASCENDING;
	protected JRExpression expression;
	protected JRExpression comparatorExpression;

	protected JRBaseCrosstabBucket()
	{
	}
	
	public JRBaseCrosstabBucket(JRCrosstabBucket bucket, JRBaseObjectFactory factory)
	{
		factory.put(bucket, this);
		
		this.order = bucket.getOrder();
		this.expression = factory.getExpression(bucket.getExpression());
		this.comparatorExpression = factory.getExpression(bucket.getComparatorExpression());
	}

	public byte getOrder()
	{
		return order;
	}

	public JRExpression getExpression()
	{
		return expression;
	}

	public JRExpression getComparatorExpression()
	{
		return comparatorExpression;
	}
}
