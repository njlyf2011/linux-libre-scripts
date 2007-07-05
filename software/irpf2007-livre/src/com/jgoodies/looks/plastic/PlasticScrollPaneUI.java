/*
 * Copyright (c) 2001-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.looks.plastic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalScrollPaneUI;

import com.jgoodies.looks.Options;

/**
 * The JGoodies Plastic L&amp;Fl implementation of <code>ScrollPaneUI</code>.
 * Installs an etched border if the client property 
 * <code>Options.IS_ETCHED_KEY</code> is set.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.3 $
 * 
 * @see com.jgoodies.looks.Options#IS_ETCHED_KEY
 */
public final class PlasticScrollPaneUI extends MetalScrollPaneUI {

    /**
     * Holds the listener that handles changes in the etched border property.
     */
    private PropertyChangeListener borderStyleChangeHandler;

    public static ComponentUI createUI(JComponent b) {
        return new PlasticScrollPaneUI();
    }

    protected void installDefaults(JScrollPane scrollPane) {
        super.installDefaults(scrollPane);
        installEtchedBorder(scrollPane);
    }


    // Managing the Etched Property *******************************************

    public void installListeners(JScrollPane scrollPane) {
        super.installListeners(scrollPane);
        borderStyleChangeHandler = new BorderStyleChangeHandler();
        scrollPane.addPropertyChangeListener(Options.IS_ETCHED_KEY, borderStyleChangeHandler);
    }

    protected void uninstallListeners(JComponent c) {
        ((JScrollPane) c).removePropertyChangeListener(Options.IS_ETCHED_KEY,
                borderStyleChangeHandler);
        super.uninstallListeners(c);
    }

    protected void installEtchedBorder(JScrollPane scrollPane) {
        Object value = scrollPane.getClientProperty(Options.IS_ETCHED_KEY);
        boolean hasEtchedBorder = Boolean.TRUE.equals(value);
        LookAndFeel.installBorder(scrollPane, 
                hasEtchedBorder 
                    ? "ScrollPane.etchedBorder"
                    : "ScrollPane.border");
    }
    
    private class BorderStyleChangeHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            JScrollPane scrollPane = (JScrollPane) evt.getSource();
            installEtchedBorder(scrollPane);
        }

    }

}