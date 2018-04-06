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
package com.itextpdf.pdfchain.ui.model;

import com.itextpdf.pdfchain.blockchain.Record;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JBlockchainTableModel extends DefaultTableModel {

    private List<Record> records = new ArrayList<>();
    private List<String> columns = new ArrayList<>();

    public JBlockchainTableModel() {
    }

    public JBlockchainTableModel setData(Collection<Record> records) {
        // put records
        this.records.clear();
        this.records.addAll(records);

        // determine common columns
        Map<String, Integer> columnNames = new HashMap<>();
        for (Record r : records) {
            for (String col : r.keySet()) {
                if (!columnNames.containsKey(col))
                    columnNames.put(col, 0);
                columnNames.put(col, columnNames.get(col) + 1);
            }
        }
        columns.clear();
        for (Map.Entry<String, Integer> en : columnNames.entrySet()) {
            if (en.getValue() == records.size())
                columns.add(en.getKey());
        }
        java.util.Collections.sort(columns);

        return this;
    }

    @Override
    public int getRowCount() {
        return records == null ? 0 : records.size();
    }

    @Override
    public int getColumnCount() {
        return columns == null ? 0 : columns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return records.get(rowIndex).get(columns.get(columnIndex));
    }

}
