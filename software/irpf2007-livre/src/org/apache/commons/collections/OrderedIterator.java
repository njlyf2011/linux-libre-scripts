/*
 *  Copyright 2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.collections;

import java.util.Iterator;

/**
 * Defines an iterator that operates over an ordered collection.
 * <p>
 * This iterator allows both forward and reverse iteration through the collection.
 *  
 * @since Commons Collections 3.0
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 *
 * @author Stephen Colebourne
 */
public interface OrderedIterator extends Iterator {

    /**
     * Checks to see if there is a previous element that can be iterated to.
     *
     * @return <code>true</code> if the iterator has a previous element
     */
    boolean hasPrevious();

    /**
     * Gets the previous element from the collection.
     *
     * @return the previous element in the iteration
     * @throws java.util.NoSuchElementException if the iteration is finished
     */
    Object previous();

}
