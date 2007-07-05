/* Copyright (c) 2001-2005, The HSQL Development Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the HSQL Development Group nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL HSQL DEVELOPMENT GROUP, HSQLDB.ORG,
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package org.hsqldb.lib;

import java.util.NoSuchElementException;

import org.hsqldb.store.BaseHashMap;

/**
 * @author fredt@users
 * @version 1.7.2
 * @since 1.7.2
 */
public class IntKeyLongValueHashMap extends BaseHashMap {

    public IntKeyLongValueHashMap() {
        this(16, 0.75f);
    }

    public IntKeyLongValueHashMap(int initialCapacity)
    throws IllegalArgumentException {
        this(initialCapacity, 0.75f);
    }

    public IntKeyLongValueHashMap(int initialCapacity,
                                  float loadFactor)
                                  throws IllegalArgumentException {
        super(initialCapacity, loadFactor, BaseHashMap.intKeyOrValue,
              BaseHashMap.longKeyOrValue, false);
    }

    public long get(int key) throws NoSuchElementException {

        int lookup = getLookup(key);

        if (lookup != -1) {
            return longValueTable[lookup];
        }

        throw new NoSuchElementException();
    }

    public long get(int key, int defaultValue) {

        int lookup = getLookup(key);

        if (lookup != -1) {
            return longValueTable[lookup];
        }

        return defaultValue;
    }

    public boolean get(int key, long[] value) {

        int lookup = getLookup(key);

        if (lookup != -1) {
            value[0] = longValueTable[lookup];

            return true;
        }

        return false;
    }

    public boolean put(int key, int value) {

        int oldSize = size();

        super.addOrRemove(key, value, null, null, false);

        return oldSize != size();
    }

    public boolean remove(int key) {

        int oldSize = size();

        super.addOrRemove(key, 0, null, null, true);

        return oldSize != size();
    }
}
