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

package org.apache.xerces.impl.dv.util;

import org.apache.xerces.xs.datatypes.ByteList;
import org.apache.xerces.xs.XSException;

/**
 * Implementation of <code>org.apache.xerces.xs.datatypes.ByteList</code>.
 *
 * @xerces.internal 
 * 
 * @author Ankit Pasricha, IBM
 * 
 * @version $Id: ByteListImpl.java 446747 2006-09-15 21:46:20Z mrglavas $
 */
public class ByteListImpl implements ByteList {

    // actually data stored in a byte array
    protected final byte[] data;
    
    // canonical representation of the data
    protected String canonical;
    
    public ByteListImpl(byte[] data) {
        this.data = data;
    }
    
    /**
     * The number of <code>byte</code>s in the list. The range of 
     * valid child object indices is 0 to <code>length-1</code> inclusive. 
     */
    public int getLength() {
        return data.length;
    }

    /**
     * Checks if the <code>byte</code> <code>item</code> is a 
     * member of this list. 
     * @param item  <code>byte</code> whose presence in this list 
     *   is to be tested. 
     * @return  True if this list contains the <code>byte</code> 
     *   <code>item</code>. 
     */
    public boolean contains(byte item) {
        for (int i = 0; i < data.length; ++i) {
            if (data[i] == item) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the <code>index</code>th item in the collection. The index 
     * starts at 0. 
     * @param index  index into the collection. 
     * @return  The <code>byte</code> at the <code>index</code>th 
     *   position in the <code>ByteList</code>. 
     * @exception XSException
     *   INDEX_SIZE_ERR: if <code>index</code> is greater than or equal to the 
     *   number of objects in the list.
     */
    public byte item(int index) 
        throws XSException {
        
        if(index < 0 || index > data.length - 1) {
            throw new XSException(XSException.INDEX_SIZE_ERR, null);
        }
        return data[index];
    }
    
}

