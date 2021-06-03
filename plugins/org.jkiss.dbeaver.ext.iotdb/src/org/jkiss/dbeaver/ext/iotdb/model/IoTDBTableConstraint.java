/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2020 DBeaver Corp and others
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
package org.jkiss.dbeaver.ext.iotdb.model;

import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.model.DBPEvaluationContext;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCResultSet;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCUtils;
import org.jkiss.dbeaver.model.meta.IPropertyValueValidator;
import org.jkiss.dbeaver.model.meta.Property;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.struct.DBSEntityAttributeRef;
import org.jkiss.dbeaver.model.struct.DBSEntityConstraint;
import org.jkiss.dbeaver.model.struct.DBSEntityConstraintType;
import org.jkiss.dbeaver.model.struct.DBSEntityReferrer;

import java.util.ArrayList;
import java.util.List;

/**
 * GenericPrimaryKey
 */
public class IoTDBTableConstraint extends IoTDBTableConstraintBase {
    private List<IoTDBTableConstraintColumn> columns;
    private String checkClause;

    public IoTDBTableConstraint(IoTDBTable table, String name, String remarks, DBSEntityConstraintType constraintType, boolean persisted)
    {
        super(table, name, remarks, constraintType, persisted);
    }

    public IoTDBTableConstraint(IoTDBTable table, String name, String description, DBSEntityConstraintType constraintType, boolean persisted, JDBCResultSet resultSet) {
        super(table, name, description, constraintType, persisted, resultSet);
        this.checkClause = JDBCUtils.safeGetString(resultSet, "CHECK_CLAUSE");
    }

    // Copy constructor
    protected IoTDBTableConstraint(DBRProgressMonitor monitor, IoTDBTable table, DBSEntityConstraint source) throws DBException {
        super(table, source, false);
        if (source instanceof DBSEntityReferrer) {
            List<? extends DBSEntityAttributeRef> columns = ((DBSEntityReferrer) source).getAttributeReferences(monitor);
            if (columns != null) {
                this.columns = new ArrayList<>(columns.size());
                /*for (DBSEntityAttributeRef col : columns) {
                    if (col.getAttribute() != null) {
                        IoTDBTableColumn ownCol = table.getAttribute(monitor, col.getAttribute().getName());
                        this.columns.add(new IoTDBTableConstraintColumn(this, ownCol, col.getAttribute().getOrdinalPosition()));
                    }
                }*/
            }
        }
    }

    @Override
    public List<IoTDBTableConstraintColumn> getAttributeReferences(DBRProgressMonitor monitor)
    {
        return columns;
    }

    public void setCheckClause(String clause) {
        this.checkClause = clause;
    }

    @Property(viewable = true, editable = true, order = 4, visibleIf = MySQLCheckConstraintsValueValidator.class)
    public String getCheckClause() {
        return checkClause;
    }

    public void addColumn(IoTDBTableConstraintColumn column)
    {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        this.columns.add(column);
    }

    void setColumns(List<IoTDBTableConstraintColumn> columns)
    {
        this.columns = columns;
    }

    @NotNull
    @Override
    public String getFullyQualifiedName(DBPEvaluationContext context)
    {
        return DBUtils.getFullQualifiedName(getDataSource(),
            getTable().getContainer(),
            getTable(),
            this);
    }

    @NotNull
    @Override
    public IoTDBDataSource getDataSource()
    {
        return getTable().getDataSource();
    }

    public static class MySQLCheckConstraintsValueValidator implements IPropertyValueValidator<IoTDBTableConstraint, Object> {

        @Override
        public boolean isValidValue(IoTDBTableConstraint object, Object value) throws IllegalArgumentException {
            return object.getDataSource().supportsCheckConstraints();
        }
    }

}
