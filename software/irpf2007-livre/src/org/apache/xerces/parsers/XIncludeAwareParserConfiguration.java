/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.parsers;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xinclude.XIncludeHandler;
import org.apache.xerces.xinclude.XIncludeNamespaceSupport;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLDocumentSource;

/**
 * This class is the configuration used to parse XML 1.0 and XML 1.1 documents
 * and provides support for XInclude. This is the default Xerces configuration.
 * 
 * @author Michael Glavassevich, IBM
 * 
 * @version $Id: XIncludeAwareParserConfiguration.java 447239 2006-09-18 05:08:26Z mrglavas $
 */
public class XIncludeAwareParserConfiguration extends XML11Configuration {
    
    /** Feature identifier: allow notation and unparsed entity events to be sent out of order. */
    protected static final String ALLOW_UE_AND_NOTATION_EVENTS =
        Constants.SAX_FEATURE_PREFIX + Constants.ALLOW_DTD_EVENTS_AFTER_ENDDTD_FEATURE;
    
    /** Feature identifier: fixup base URIs. */
    protected static final String XINCLUDE_FIXUP_BASE_URIS =
        Constants.XERCES_FEATURE_PREFIX + Constants.XINCLUDE_FIXUP_BASE_URIS_FEATURE;
    
    /** Feature identifier: fixup language. */
    protected static final String XINCLUDE_FIXUP_LANGUAGE =
        Constants.XERCES_FEATURE_PREFIX + Constants.XINCLUDE_FIXUP_LANGUAGE_FEATURE;
    
    /** Feature identifier: XInclude processing */
    protected static final String XINCLUDE_FEATURE = 
        Constants.XERCES_FEATURE_PREFIX + Constants.XINCLUDE_FEATURE;
    
    /** Property identifier: error reporter. */
    protected static final String XINCLUDE_HANDLER =
        Constants.XERCES_PROPERTY_PREFIX + Constants.XINCLUDE_HANDLER_PROPERTY;
    
    /** Property identifier: error reporter. */
    protected static final String NAMESPACE_CONTEXT =
        Constants.XERCES_PROPERTY_PREFIX + Constants.NAMESPACE_CONTEXT_PROPERTY;
    
    //
    // Components
    //
    
    /** XInclude handler. */
    protected XIncludeHandler fXIncludeHandler;
    
    /** Non-XInclude NamespaceContext. */
    protected NamespaceSupport fNonXIncludeNSContext;
    
    /** XInclude NamespaceContext. */
    protected XIncludeNamespaceSupport fXIncludeNSContext;
    
    /** Current NamespaceContext. */
    protected NamespaceContext fCurrentNSContext;
    
    /** Flag indicating whether XInclude processsing is enabled. */
    protected boolean fXIncludeEnabled = false;
    
    /** Default constructor. */
    public XIncludeAwareParserConfiguration() {
        this(null, null, null);
    } // <init>()
    
    /** 
     * Constructs a parser configuration using the specified symbol table. 
     *
     * @param symbolTable The symbol table to use.
     */
    public XIncludeAwareParserConfiguration(SymbolTable symbolTable) {
        this(symbolTable, null, null);
    } // <init>(SymbolTable)
    
    /**
     * Constructs a parser configuration using the specified symbol table and
     * grammar pool.
     * <p>
     *
     * @param symbolTable The symbol table to use.
     * @param grammarPool The grammar pool to use.
     */
    public XIncludeAwareParserConfiguration(
            SymbolTable symbolTable,
            XMLGrammarPool grammarPool) {
        this(symbolTable, grammarPool, null);
    } // <init>(SymbolTable,XMLGrammarPool)
    
    /**
     * Constructs a parser configuration using the specified symbol table,
     * grammar pool, and parent settings.
     * <p>
     *
     * @param symbolTable    The symbol table to use.
     * @param grammarPool    The grammar pool to use.
     * @param parentSettings The parent settings.
     */
    public XIncludeAwareParserConfiguration(
            SymbolTable symbolTable,
            XMLGrammarPool grammarPool,
            XMLComponentManager parentSettings) {
        super(symbolTable, grammarPool, parentSettings);
        
        final String[] recognizedFeatures = {
                ALLOW_UE_AND_NOTATION_EVENTS,
                XINCLUDE_FIXUP_BASE_URIS,
                XINCLUDE_FIXUP_LANGUAGE
        };
        addRecognizedFeatures(recognizedFeatures);
        
        // add default recognized properties
        final String[] recognizedProperties =
        { XINCLUDE_HANDLER, NAMESPACE_CONTEXT };
        addRecognizedProperties(recognizedProperties);
        
        setFeature(ALLOW_UE_AND_NOTATION_EVENTS, true);
        setFeature(XINCLUDE_FIXUP_BASE_URIS, true);
        setFeature(XINCLUDE_FIXUP_LANGUAGE, true);
        
        fNonXIncludeNSContext = new NamespaceSupport();
        fCurrentNSContext = fNonXIncludeNSContext;
        setProperty(NAMESPACE_CONTEXT, fNonXIncludeNSContext);
    }
    
    
    /** Configures the pipeline. */
    protected void configurePipeline() {
        super.configurePipeline();
        if (fXIncludeEnabled) {
            // If the XInclude handler was not in the pipeline insert it.
            if (fXIncludeHandler == null) {
                fXIncludeHandler = new XIncludeHandler();
                // add XInclude component
                setProperty(XINCLUDE_HANDLER, fXIncludeHandler);
                addCommonComponent(fXIncludeHandler);
                fXIncludeHandler.reset(this);
            }
            // Setup NamespaceContext
            if (fCurrentNSContext != fXIncludeNSContext) {
                if (fXIncludeNSContext == null) {
                    fXIncludeNSContext = new XIncludeNamespaceSupport();
                }
                fCurrentNSContext = fXIncludeNSContext;
                setProperty(NAMESPACE_CONTEXT, fXIncludeNSContext);
            }
            //configure DTD pipeline
            fDTDScanner.setDTDHandler(fDTDProcessor);
            fDTDProcessor.setDTDSource(fDTDScanner);
            fDTDProcessor.setDTDHandler(fXIncludeHandler);
            fXIncludeHandler.setDTDSource(fDTDProcessor);
            fXIncludeHandler.setDTDHandler(fDTDHandler);
            if (fDTDHandler != null) {
                fDTDHandler.setDTDSource(fXIncludeHandler);
            }
            
            // configure XML document pipeline: insert after DTDValidator and 
            // before XML Schema validator
            XMLDocumentSource prev = null;
            if (fFeatures.get(XMLSCHEMA_VALIDATION) == Boolean.TRUE) {
                // we don't have to worry about fSchemaValidator being null, since
                // super.configurePipeline() instantiated it if the feature was set
                prev = fSchemaValidator.getDocumentSource();
            }
            // Otherwise, insert after the last component in the pipeline
            else {
                prev = fLastComponent;
                fLastComponent = fXIncludeHandler;
            }
            
            XMLDocumentHandler next = prev.getDocumentHandler();
            prev.setDocumentHandler(fXIncludeHandler);
            fXIncludeHandler.setDocumentSource(prev);
            if (next != null) {
                fXIncludeHandler.setDocumentHandler(next);
                next.setDocumentSource(fXIncludeHandler);
            }
        }
        else {
            // Setup NamespaceContext
            if (fCurrentNSContext != fNonXIncludeNSContext) {
                fCurrentNSContext = fNonXIncludeNSContext;
                setProperty(NAMESPACE_CONTEXT, fNonXIncludeNSContext);
            }
        }
    } // configurePipeline()
    
    protected void configureXML11Pipeline() {
        super.configureXML11Pipeline();
        if (fXIncludeEnabled) {
            // If the XInclude handler was not in the pipeline insert it.
            if (fXIncludeHandler == null) {
                fXIncludeHandler = new XIncludeHandler();
                // add XInclude component
                setProperty(XINCLUDE_HANDLER, fXIncludeHandler);
                addCommonComponent(fXIncludeHandler);
                fXIncludeHandler.reset(this);
            }
            // Setup NamespaceContext
            if (fCurrentNSContext != fXIncludeNSContext) {
                if (fXIncludeNSContext == null) {
                    fXIncludeNSContext = new XIncludeNamespaceSupport();
                }
                fCurrentNSContext = fXIncludeNSContext;
                setProperty(NAMESPACE_CONTEXT, fXIncludeNSContext);
            }
            // configure XML 1.1. DTD pipeline
            fXML11DTDScanner.setDTDHandler(fXML11DTDProcessor);
            fXML11DTDProcessor.setDTDSource(fXML11DTDScanner);
            fXML11DTDProcessor.setDTDHandler(fXIncludeHandler);
            fXIncludeHandler.setDTDSource(fXML11DTDProcessor);
            fXIncludeHandler.setDTDHandler(fDTDHandler);
            if (fDTDHandler != null) {
                fDTDHandler.setDTDSource(fXIncludeHandler);
            }
            
            // configure XML document pipeline: insert after DTDValidator and 
            // before XML Schema validator
            XMLDocumentSource prev = null;
            if (fFeatures.get(XMLSCHEMA_VALIDATION) == Boolean.TRUE) {
                // we don't have to worry about fSchemaValidator being null, since
                // super.configurePipeline() instantiated it if the feature was set
                prev = fSchemaValidator.getDocumentSource();
            }
            // Otherwise, insert after the last component in the pipeline
            else {
                prev = fLastComponent;
                fLastComponent = fXIncludeHandler;
            }
            
            XMLDocumentHandler next = prev.getDocumentHandler();
            prev.setDocumentHandler(fXIncludeHandler);
            fXIncludeHandler.setDocumentSource(prev);
            if (next != null) {
                fXIncludeHandler.setDocumentHandler(next);
                next.setDocumentSource(fXIncludeHandler);
            }
        }
        else {
            // Setup NamespaceContext
            if (fCurrentNSContext != fNonXIncludeNSContext) {
                fCurrentNSContext = fNonXIncludeNSContext;
                setProperty(NAMESPACE_CONTEXT, fNonXIncludeNSContext);
            }
        }
    } // configureXML11Pipeline()
    
    public boolean getFeature(String featureId)
        throws XMLConfigurationException {
        if (featureId.equals(PARSER_SETTINGS)) {
            return fConfigUpdated;
        }
        else if (featureId.equals(XINCLUDE_FEATURE)) {
            return fXIncludeEnabled;
        }
        return super.getFeature0(featureId);
        
    } // getFeature(String):boolean
    
    public void setFeature(String featureId, boolean state)
        throws XMLConfigurationException {
        if (featureId.equals(XINCLUDE_FEATURE)) {
            fXIncludeEnabled = state;
            fConfigUpdated = true;
            return;
        }
        super.setFeature(featureId,state);
    }
    
}
