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
package org.apache.commons.collections.bag;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.Unmodifiable;
import org.apache.commons.collections.iterators.UnmodifiableIterator;
import org.apache.commons.collections.set.UnmodifiableSet;

/**
 * Decorates another <code>Bag</code> to ensure it can't be altered.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 * 
 * @author Stephen Colebourne
 */
public final class UnmodifiableBag
        extends AbstractBagDecorator implements Unmodifiable, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -1873799975157099624L;

    /**
     * Factory method to create an unmodifiable bag.
     * <p>
     * If the bag passed in is already unmodifiable, it is returned.
     * 
     * @param bag  the bag to decorate, must not be null
     * @return an unmodifiable Bag
     * @throws IllegalArgumentException if bag is null
     */
    public static Bag decorate(Bag bag) {
        if (bag instanceof Unmodifiable) {
            return bag;
        }
        return new UnmodifiableBag(bag);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * 
     * @param bag  the bag to decorate, must not be null
     * @throws IllegalArgumentException if bag is null
     */
    private UnmodifiableBag(Bag bag) {
        super(bag);
    }

    //-----------------------------------------------------------------------
    /**
     * Write the collection out using a custom routine.
     * 
     * @param out  the output stream
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(collection);
    }

    /**
     * Read the collection in using a custom routine.
     * 
     * @param in  the input stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        collection = (Collection) in.readObject();
    }

    //-----------------------------------------------------------------------
    public Iterator iterator() {
        return UnmodifiableIterator.decorate(getCollection().iterator());
    }

    public boolean add(Object object) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    //-----------------------------------------------------------------------
    public boolean add(Object object, int count) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object object, int count) {
        throw new UnsupportedOperationException();
    }

    public Set uniqueSet() {
        Set set = getBag().uniqueSet();
        return UnmodifiableSet.decorate(set);
    }

}
