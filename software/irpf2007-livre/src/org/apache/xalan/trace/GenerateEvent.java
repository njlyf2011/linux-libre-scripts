/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: GenerateEvent.java,v 1.12 2005/02/04 22:20:38 mcnamara Exp $
 */
package org.apache.xalan.trace;

import org.apache.xalan.transformer.TransformerImpl;
import org.xml.sax.Attributes;

/**
 * Event generated by the XSL processor after it generates a new node in the result tree.
 * This event responds to and is modeled on the SAX events that are sent to the
 * formatter listener FormatterToXXX)classes.
 *
 * @see org.apache.xml.utils.DOMBuilder
 * @see org.apache.xml.serializer.ToHTMLStream
 * @see org.apache.xml.serializer.ToTextStream
 * @see org.apache.xml.serializer.ToXMLStream
 *
 * @xsl.usage advanced
 */
public class GenerateEvent implements java.util.EventListener
{

  /**
   * The XSLT Transformer, which either directly or indirectly contains most needed information.
   *
   * @see org.apache.xalan.transformer.TransformerImpl
   */
  public TransformerImpl m_processor;

  /**
   * The type of SAX event that was generated, as enumerated in the EVENTTYPE_XXX constants below.
   */
  public int m_eventtype;


  /**
   * Character data from a character or cdata event.
   */
  public char m_characters[];

  /**
   * The start position of the current data in m_characters.
   */
  public int m_start;

  /**
   * The length of the current data in m_characters.
   */
  public int m_length;

  /**
   * The name of the element or PI.
   */
  public String m_name;

  /**
   * The string data in the element (comments and PIs).
   */
  public String m_data;

  /**
   * The current attribute list.
   */
  public Attributes m_atts;

  /**
   * Constructor for startDocument, endDocument events.
   *
   * @param processor The XSLT TransformerFactory instance.
   * @param eventType One of the EVENTTYPE_XXX constants.
   */
  public GenerateEvent(TransformerImpl processor, int eventType)
  {
    m_processor = processor;
    m_eventtype = eventType;
  }

  /**
   * Constructor for startElement, endElement events.
   *
   * @param processor The XSLT TransformerFactory Instance.
   * @param eventType One of the EVENTTYPE_XXX constants.
   * @param name The name of the element.
   * @param atts The SAX attribute list.
   */
  public GenerateEvent(TransformerImpl processor, int eventType, String name,
                       Attributes atts)
  {

    m_name = name;
    m_atts = atts;
    m_processor = processor;
    m_eventtype = eventType;
  }

  /**
   * Constructor for characters, cdate events.
   *
   * @param processor The XSLT TransformerFactory instance.
   * @param eventType One of the EVENTTYPE_XXX constants.
   * @param ch The char array from the SAX event.
   * @param start The start offset to be used in the char array.
   * @param length The end offset to be used in the chara array.
   */
  public GenerateEvent(TransformerImpl processor, int eventType, char ch[],
                       int start, int length)
  {

    m_characters = ch;
    m_start = start;
    m_length = length;
    m_processor = processor;
    m_eventtype = eventType;
  }

  /**
   * Constructor for processingInstruction events.
   *
   * @param processor The instance of the XSLT processor.
   * @param eventType One of the EVENTTYPE_XXX constants.
   * @param name The name of the processing instruction.
   * @param data The processing instruction data.
   */
  public GenerateEvent(TransformerImpl processor, int eventType, String name,
                       String data)
  {

    m_name = name;
    m_data = data;
    m_processor = processor;
    m_eventtype = eventType;
  }

  /**
   * Constructor for comment and entity ref events.
   *
   * @param processor The XSLT processor instance.
   * @param eventType One of the EVENTTYPE_XXX constants.
   * @param data The comment or entity ref data.
   */
  public GenerateEvent(TransformerImpl processor, int eventType, String data)
  {

    m_data = data;
    m_processor = processor;
    m_eventtype = eventType;
  }
}
