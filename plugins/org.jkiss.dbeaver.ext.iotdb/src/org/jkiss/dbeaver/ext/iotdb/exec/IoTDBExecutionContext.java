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
package org.jkiss.dbeaver.ext.iotdb.exec;

import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.ext.iotdb.model.IoTDBDatabase;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.exec.DBCExecutionContextDefaults;
import org.jkiss.dbeaver.model.exec.DBCExecutionPurpose;
import org.jkiss.dbeaver.model.exec.DBCSession;
import org.jkiss.dbeaver.model.impl.AbstractExecutionContext;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.struct.DBSInstance;
import org.jkiss.dbeaver.model.struct.rdb.DBSSchema;

public class IoTDBExecutionContext extends AbstractExecutionContext implements DBCExecutionContextDefaults<IoTDBDatabase, DBSSchema>{

	public IoTDBExecutionContext(DBPDataSource dataSource, String purpose) {
		super(dataSource, purpose);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DBSInstance getOwnerInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DBCSession openSession(DBRProgressMonitor monitor, DBCExecutionPurpose purpose, String task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkContextAlive(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InvalidateResult invalidateContext(DBRProgressMonitor monitor, boolean closeOnFailure) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IoTDBDatabase getDefaultCatalog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBSSchema getDefaultSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsCatalogChange() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsSchemaChange() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDefaultCatalog(DBRProgressMonitor monitor, IoTDBDatabase catalog, DBSSchema schema)
			throws DBCException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultSchema(DBRProgressMonitor monitor, DBSSchema schema) throws DBCException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean refreshDefaults(DBRProgressMonitor monitor, boolean useBootstrapSettings) throws DBException {
		// TODO Auto-generated method stub
		return false;
	}

}
