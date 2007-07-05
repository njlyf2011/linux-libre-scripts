/* $Id: Rule.java 471661 2006-11-06 08:09:25Z skitching $
 *
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


package org.apache.commons.digester;


import org.xml.sax.Attributes;


/**
 * Concrete implementations of this class implement actions to be taken when
 * a corresponding nested pattern of XML elements has been matched.
 * <p>
 * Writing a custom Rule is considered perfectly normal when using Digester,
 * and is encouraged whenever the default set of Rule classes don't meet your
 * requirements; the digester framework can help process xml even when the
 * built-in rules aren't quite what is needed. Creating a custom Rule is
 * just as easy as subclassing javax.servlet.http.HttpServlet for webapps,
 * or javax.swing.Action for GUI applications.
 * <p>
 * If a rule wishes to manipulate a digester stack (the default object stack,
 * a named stack, or the parameter stack) then it should only ever push
 * objects in the rule's begin method and always pop exactly the same
 * number of objects off the stack during the rule's end method. Of course
 * peeking at the objects on the stacks can be done from anywhere.
 * <p>
 * Rule objects should be stateless, ie they should not update any instance
 * member during the parsing process. A rule instance that changes state
 * will encounter problems if invoked in a "nested" manner; this can happen
 * if the same instance is added to digester multiple times or if a 
 * wildcard pattern is used which can match both an element and a child of the
 * same element. The digester object stack and named stacks should be used to
 * store any state that a rule requires, making the rule class safe under all
 * possible uses.
 */

public abstract class Rule {


    // ----------------------------------------------------------- Constructors


    /**
     * Constructor sets the associated Digester.
     *
     * @param digester The digester with which this rule is associated
     * @deprecated The digester instance is now set in the {@link Digester#addRule} method. Use {@link #Rule()} instead.
     */
    public Rule(Digester digester) {

        super();
        setDigester(digester);

    }
    
    /**
     * <p>Base constructor.
     * Now the digester will be set when the rule is added.</p>
     */
    public Rule() {}


    // ----------------------------------------------------- Instance Variables


    /**
     * The Digester with which this Rule is associated.
     */
    protected Digester digester = null;


    /**
     * The namespace URI for which this Rule is relevant, if any.
     */
    protected String namespaceURI = null;


    // ------------------------------------------------------------- Properties


    /**
     * Return the Digester with which this Rule is associated.
     */
    public Digester getDigester() {

        return (this.digester);

    }
    
    /**
     * Set the <code>Digester</code> with which this <code>Rule</code> is associated.
     */
    public void setDigester(Digester digester) {
        
        this.digester = digester;
        
    }

    /**
     * Return the namespace URI for which this Rule is relevant, if any.
     */
    public String getNamespaceURI() {

        return (this.namespaceURI);

    }


    /**
     * Set the namespace URI for which this Rule is relevant, if any.
     *
     * @param namespaceURI Namespace URI for which this Rule is relevant,
     *  or <code>null</code> to match independent of namespace.
     */
    public void setNamespaceURI(String namespaceURI) {

        this.namespaceURI = namespaceURI;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * This method is called when the beginning of a matching XML element
     * is encountered.
     *
     * @param attributes The attribute list of this element
     * @deprecated Use the {@link #begin(String,String,Attributes) begin}
     *   method with <code>namespace</code> and <code>name</code>
     *   parameters instead.
     */
    public void begin(Attributes attributes) throws Exception {

        ;  // The default implementation does nothing

    }


    /**
     * This method is called when the beginning of a matching XML element
     * is encountered. The default implementation delegates to the deprecated
     * method {@link #begin(Attributes) begin} without the 
     * <code>namespace</code> and <code>name</code> parameters, to retain 
     * backwards compatibility.
     *
     * @param namespace the namespace URI of the matching element, or an 
     *   empty string if the parser is not namespace aware or the element has
     *   no namespace
     * @param name the local name if the parser is namespace aware, or just 
     *   the element name otherwise
     * @param attributes The attribute list of this element
     * @since Digester 1.4
     */
    public void begin(String namespace, String name, Attributes attributes)
        throws Exception {

        begin(attributes);

    }


    /**
     * This method is called when the body of a matching XML element
     * is encountered.  If the element has no body, this method is
     * called with an empty string as the body text.
     *
     * @param text The text of the body of this element
     * @deprecated Use the {@link #body(String,String,String) body} method
     *   with <code>namespace</code> and <code>name</code> parameters
     *   instead.
     */
    public void body(String text) throws Exception {

        ;  // The default implementation does nothing

    }


    /**
     * This method is called when the body of a matching XML element is 
     * encountered.  If the element has no body, this method is 
     * called with an empty string as the body text.
     * <p>
     * The default implementation delegates to the deprecated method 
     * {@link #body(String) body} without the <code>namespace</code> and
     * <code>name</code> parameters, to retain backwards compatibility.
     *
     * @param namespace the namespace URI of the matching element, or an 
     *   empty string if the parser is not namespace aware or the element has
     *   no namespace
     * @param name the local name if the parser is namespace aware, or just 
     *   the element name otherwise
     * @param text The text of the body of this element
     * @since Digester 1.4
     */
    public void body(String namespace, String name, String text)
        throws Exception {

        body(text);

    }


    /**
     * This method is called when the end of a matching XML element
     * is encountered.
     * 
     * @deprecated Use the {@link #end(String,String) end} method with 
     *   <code>namespace</code> and <code>name</code> parameters instead.
     */
    public void end() throws Exception {

        ;  // The default implementation does nothing

    }


    /**
     * This method is called when the end of a matching XML element
     * is encountered. The default implementation delegates to the deprecated
     * method {@link #end end} without the 
     * <code>namespace</code> and <code>name</code> parameters, to retain 
     * backwards compatibility.
     *
     * @param namespace the namespace URI of the matching element, or an 
     *   empty string if the parser is not namespace aware or the element has
     *   no namespace
     * @param name the local name if the parser is namespace aware, or just 
     *   the element name otherwise
     * @since Digester 1.4
     */
    public void end(String namespace, String name)
        throws Exception {

        end();

    }


    /**
     * This method is called after all parsing methods have been
     * called, to allow Rules to remove temporary data.
     */
    public void finish() throws Exception {

        ;  // The default implementation does nothing

    }


}
