/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2018 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.pdfchain.bql.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This class handles bracket related logic for BQL
 */
class BQLBracketMatcher {

    /**
     * Test whether two tokens are matching brackets
     *
     * @param left  left bracket token
     * @param right right bracket token
     * @return true iff the left bracket token matches the right bracket token, false otherwise
     */
    private static boolean isMatchingBracket(BQLTokenizer.Token left, BQLTokenizer.Token right) {
        String[] lOps = {"[", "(", "{"};
        String[] rOps = {"]", ")", "}"};
        for (int i = 0; i < lOps.length; i++) {
            if (left.getText().equals(lOps[i]))
                return right.getText().equals(rOps[i]);
        }
        return false;
    }

    /**
     * Builds Map&lt;Integer, Integer&gt; such that any bracket position can be queried, and the matching bracket position returned
     *
     * @param tokens input tokens
     * @return Map of matching bracket positions
     */
    static Map<Integer, Integer> matchingBrackets(List<BQLTokenizer.Token> tokens) {
        Map<Integer, Integer> retval = new HashMap<>();
        Stack<Object> tmp = new Stack<>();
        for (int i = 0; i < tokens.size(); i++) {
            BQLTokenizer.Token t = tokens.get(i);
            if (t.getType() == BQLTokenizer.Type.LEFT_BRACKET)
                tmp.push(new Object[]{t, i});
            else if (t.getType() == BQLTokenizer.Type.RIGHT_BRACKET) {
                if (tmp.isEmpty())
                    throw new IllegalArgumentException("Mismatched parenthesis");

                Object[] objs = (Object[]) tmp.pop();
                BQLTokenizer.Token l = (BQLTokenizer.Token) objs[0];
                Integer leftPos = (Integer) objs[1];
                if (!isMatchingBracket(l, t))
                    throw new IllegalArgumentException("Mismatched parenthesis");
                retval.put(leftPos, i);
                retval.put(i, leftPos);
            }
        }
        return retval;
    }
}
