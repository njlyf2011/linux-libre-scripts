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

package org.apache.xerces.impl.dv;

/**
 * A runtime exception that's thrown if an error happens when the application
 * tries to get a DV factory instance.
 *
 * @xerces.internal 
 *
 * @version $Id: DVFactoryException.java 446751 2006-09-15 21:54:06Z mrglavas $
 */
public class DVFactoryException extends RuntimeException {
    
    /** Serialization version. */
    static final long serialVersionUID = -3738854697928682412L;
    
    public DVFactoryException() {
        super();
    }

    public DVFactoryException(String msg) {
        super(msg);
    }
}
