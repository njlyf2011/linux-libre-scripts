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

package org.apache.xerces.impl.dv.dtd;

import org.apache.xerces.impl.dv.*;

/**
 * NOTATIONValidator defines the interface that data type validators must obey.
 * These validators can be supplied by the application writer and may be useful as
 * standalone code as well as plugins to the validator architecture.
 *
 * @xerces.internal  
 * 
 * @author Jeffrey Rodriguez, IBM
 * @author Sandy Gao, IBM
 * 
 * @version $Id: NOTATIONDatatypeValidator.java 446749 2006-09-15 21:51:23Z mrglavas $
 */
 public class NOTATIONDatatypeValidator implements DatatypeValidator {
 
    // construct a NOTATION datatype validator
    public NOTATIONDatatypeValidator() {
    }

    /**
     * Checks that "content" string is valid NOTATION value.
     * If invalid a Datatype validation exception is thrown.
     * 
     * @param content       the string value that needs to be validated
     * @param context       the validation context
     * @throws InvalidDatatypeException if the content is
     *         invalid according to the rules for the validators
     * @see InvalidDatatypeValueException
     */
    public void validate(String content, ValidationContext context) throws InvalidDatatypeValueException {
    }

}
