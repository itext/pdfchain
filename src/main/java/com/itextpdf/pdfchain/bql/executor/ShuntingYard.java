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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * ShuntingYard implementation for BQL
 * A Shunting Yard algorithm will take an expression in infix notation and convert it to postfix.
 * Postfix is a lot easier to process for building abstract syntax trees.
 */
class ShuntingYard {

    /**
     * @param infix List of infix tokens
     * @return List of postfix tokens
     */
    static List<BQLTokenizer.Token> postfix(List<BQLTokenizer.Token> infix) {
        Map<Integer, Integer> matchingBrackets = BQLBracketMatcher.matchingBrackets(infix);
        Stack<BQLTokenizer.Token> stk = new Stack<>();
        List<BQLTokenizer.Token> output = new ArrayList<>();

        for (int i = 0; i < infix.size(); i++) {
            BQLTokenizer.Token t = infix.get(i);

            if (t.getType() == BQLTokenizer.Type.WHITESPACE)
                continue;

            // if the token is a number or literal, or variable, push it to the output queue
            if (t.getType() == BQLTokenizer.Type.NUMBER || t.getType() == BQLTokenizer.Type.STRING || t.getType() == BQLTokenizer.Type.VARIABLE) {
                output.add(t);
                continue;
            }

            // if the token is an operator then:
            if (t.getType() == BQLTokenizer.Type.OPERATOR) {
                while (!stk.isEmpty() && stk.peek().getType() == BQLTokenizer.Type.OPERATOR
                        && ((isLeftAssociative(t) && precedence(t) <= precedence(stk.peek()))
                        || (!isLeftAssociative(t) && precedence(t) < precedence(stk.peek())))) {
                    output.add(stk.pop());
                }
                stk.push(t);
                continue;
            }

            // if the token is a right array parenthesis, process the array and put it on the stack
            if (t.getType() == BQLTokenizer.Type.LEFT_BRACKET && t.getText().equalsIgnoreCase("[")) {
                List<String> arr = new ArrayList<>();
                for (int j = i + 1; j < matchingBrackets.get(i); j++) {
                    BQLTokenizer.Token tmp = infix.get(j);
                    if (tmp.getType() != BQLTokenizer.Type.COMMA && tmp.getType() != BQLTokenizer.Type.WHITESPACE)
                        arr.add(infix.get(j).getText());
                }
                i = matchingBrackets.get(i);
                BQLTokenizer.Token arrToken = new BQLTokenizer.Token(arr.toArray(new String[arr.size()]));
                output.add(arrToken);
                continue;
            }

            // if the token is a left parenthesis, push it onto the stack
            if (t.getType() == BQLTokenizer.Type.LEFT_BRACKET) {
                stk.push(t);
                continue;
            }

            // if the token is a right parenthesis
            if (t.getType() == BQLTokenizer.Type.RIGHT_BRACKET) {
                while (!stk.isEmpty() && stk.peek().getType() != BQLTokenizer.Type.LEFT_BRACKET) {
                    output.add(stk.pop());
                }
                if (!stk.isEmpty() && stk.peek().getType() == BQLTokenizer.Type.LEFT_BRACKET) {
                    stk.pop();
                }
            }
        }
        while (!stk.isEmpty()) {
            BQLTokenizer.Token t = stk.pop();
            if (t.getType() == BQLTokenizer.Type.LEFT_BRACKET || t.getType() == BQLTokenizer.Type.RIGHT_BRACKET)
                throw new IllegalArgumentException("Mismatched parenthesis in expression");
            output.add(t);
        }
        return output;
    }

    /**
     * Get the precedence of a certain operator
     * Precedence dictates what gets executed first in ambiguous statements such as "A + B * C"
     *
     * @param token input token
     * @return precedence of the token
     */
    private static int precedence(BQLTokenizer.Token token) {
        String[] operators = {  // high level
                "*",
                // comparison operators
                ">",
                ">=",
                "<",
                "<=",
                // equality operators
                "==",
                "!=",
                // logical operators
                "AND",
                "OR",
                // display operators
                "SELECT",
                "WHERE",
                "SORT"
        };
        for (int i = 0; i < operators.length; i++) {
            if (token.getText().equalsIgnoreCase(operators[i]))
                return operators.length - i;
        }
        return (int) Math.pow(operators.length, 2);
    }

    /**
     * Get whether an operator is left associative or not
     * Associativity helps structuring statements such as "A - B - C - D"
     * which can be interpreted as either "(((A - B) - C) - D)" or "(A - (B - (C - D)))"
     *
     * @param token input token
     * @return true iff the token is a left associative operator
     */
    private static boolean isLeftAssociative(BQLTokenizer.Token token) {
        return true;
    }

}
