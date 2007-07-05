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

package org.apache.xerces.impl.xs.util;

/**
 * @xerces.internal  
 * 
 * @author Henry Zongaro, IBM
 * @version $Id: XInt.java 446723 2006-09-15 20:37:45Z mrglavas $
 */

public final class XInt {

    private int fValue;

    XInt(int value) {
        fValue = value;
    }

    public final int intValue() {
        return fValue;
    }

    public final short shortValue() {
        return (short)fValue;
    }

    public final boolean equals(XInt compareVal) {
        return (this.fValue == compareVal.fValue);
    }

    public String toString() {
        return Integer.toString(fValue);
    }
}
