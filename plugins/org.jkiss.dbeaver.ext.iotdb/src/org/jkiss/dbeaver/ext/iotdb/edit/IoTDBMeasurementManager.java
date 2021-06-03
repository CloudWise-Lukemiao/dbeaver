package org.jkiss.dbeaver.ext.iotdb.edit;

import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.ext.generic.edit.GenericTableManager;
import org.jkiss.dbeaver.ext.generic.model.GenericTableBase;
import org.jkiss.dbeaver.model.edit.DBECommandContext;
import org.jkiss.dbeaver.model.edit.DBEObjectRenamer;
import org.jkiss.dbeaver.model.impl.sql.edit.struct.SQLTableManager;

public class IoTDBMeasurementManager extends GenericTableManager implements DBEObjectRenamer<GenericTableBase>{

	@Override
	public void renameObject(DBECommandContext commandContext, GenericTableBase object, String newName)
			throws DBException {
		// TODO Auto-generated method stub
		 processObjectRename(commandContext, object, newName);
		
	}

}
