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
package net.sf.jasperreports.view.save;

import java.io.File;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.view.JRSaveContributor;

/**
 * @author Flavius Sana (flavius_sana@users.sourceforge.net)
 * @version $Id: JRRtfSaveContributor.java 1229 2006-04-19 13:27:35 +0300 (Wed, 19 Apr 2006) teodord $
 */
public class JRRtfSaveContributor extends JRSaveContributor 
{

	/**
	 * 
	 */
	private static final String EXTENSION_RTF = ".rtf";
	public static final JRRtfSaveContributor INSTANCE = new JRRtfSaveContributor(); 

	/**
	 * 
	 */
	public static JRRtfSaveContributor getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * 
	 */
	public boolean accept(File file)
	{
		if(file.isDirectory()){
			return true;
		}
		return file.getName().toLowerCase().endsWith(EXTENSION_RTF);
	}

	
	/**
	 * 
	 */
	public String getDescription()
	{
		return "RTF (*.rtf)";
	}
	
	/**
	 * 
	 */
	public void save(JasperPrint jasperPrint, File file) throws JRException
	{
		if(!file.getName().endsWith(EXTENSION_RTF))
		{
			file = new File(file.getAbsolutePath() + EXTENSION_RTF);
		}
		
		if (
			!file.exists() ||
			JOptionPane.OK_OPTION == 
				JOptionPane.showConfirmDialog(
					null, 
					MessageFormat.format(
						java.util.ResourceBundle.getBundle("net/sf/jasperreports/view/viewer").getString("file.exists"),
						new Object[]{file.getName()}
						), 
					java.util.ResourceBundle.getBundle("net/sf/jasperreports/view/viewer").getString("save"), 
					JOptionPane.OK_CANCEL_OPTION
					)
			)
		{
			JRRtfExporter exporter = new JRRtfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);
			exporter.exportReport();
		}
	}
}
