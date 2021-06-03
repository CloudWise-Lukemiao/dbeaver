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


import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;

import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPNamedObject2;
import org.jkiss.dbeaver.model.DBPOrderedObject;

import org.jkiss.dbeaver.model.impl.jdbc.struct.JDBCTableColumn;
import org.jkiss.dbeaver.model.struct.DBSTypedObjectExt3;
import org.jkiss.dbeaver.model.struct.rdb.DBSTableColumn;

import java.sql.ResultSet;


/**
 * IoTDBTableColumn
 */
public class IoTDBTableColumn extends JDBCTableColumn<IoTDBTableBase> implements DBSTableColumn, DBSTypedObjectExt3, DBPNamedObject2, DBPOrderedObject
{
    protected IoTDBTableColumn(IoTDBTableBase table, boolean persisted) {
		super(table, persisted);
		// TODO Auto-generated constructor stub
	}
    
    public IoTDBTableColumn(
    		IoTDBTableBase table,
            ResultSet dbResult)
            throws DBException
        {
            super(table, true);
           // loadInfo(dbResult);
        }

	private static final Log log = Log.getLog(IoTDBTableColumn.class);

	@Override
	public DBPDataSource getDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

    
}
