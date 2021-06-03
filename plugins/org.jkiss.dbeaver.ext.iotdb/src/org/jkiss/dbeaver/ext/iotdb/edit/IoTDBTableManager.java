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
package org.jkiss.dbeaver.ext.iotdb.edit;


import java.util.Map;

import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.ext.iotdb.model.IoTDBDatabase;
import org.jkiss.dbeaver.ext.iotdb.model.IoTDBTableBase;
import org.jkiss.dbeaver.model.edit.DBECommandContext;
import org.jkiss.dbeaver.model.edit.DBEObjectRenamer;
import org.jkiss.dbeaver.model.impl.sql.edit.struct.SQLTableManager;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.struct.DBSObject;
import org.jkiss.dbeaver.model.struct.cache.DBSObjectCache;

public class IoTDBTableManager extends SQLTableManager<IoTDBTableBase, IoTDBDatabase> implements DBEObjectRenamer<IoTDBTableBase> {

	@Override
	public Class<?>[] getChildTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBSObjectCache<? extends DBSObject, IoTDBTableBase> getObjectsCache(IoTDBTableBase object) {
		// TODO Auto-generated method stub
		return object.getContainer().getTableCache();
	}

	@Override
	public void renameObject(DBECommandContext commandContext, IoTDBTableBase object, String newName)
			throws DBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected IoTDBTableBase createDatabaseObject(DBRProgressMonitor monitor, DBECommandContext context,
			Object container, Object copyFrom, Map<String, Object> options) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

}
