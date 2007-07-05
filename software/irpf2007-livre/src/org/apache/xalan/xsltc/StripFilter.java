/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
 * $Id: StripFilter.java,v 1.4 2004/02/16 20:54:58 minchau Exp $
 */

package org.apache.xalan.xsltc;


/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public interface StripFilter {
    public boolean stripSpace(DOM dom, int node, int type);
}
