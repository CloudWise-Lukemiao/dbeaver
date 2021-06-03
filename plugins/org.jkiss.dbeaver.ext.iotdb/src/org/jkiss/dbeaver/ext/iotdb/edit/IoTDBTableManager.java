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
