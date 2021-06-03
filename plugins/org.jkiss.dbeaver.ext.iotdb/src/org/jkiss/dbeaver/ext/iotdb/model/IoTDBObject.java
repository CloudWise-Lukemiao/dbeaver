package org.jkiss.dbeaver.ext.iotdb.model;

import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.model.DBPNamedObject;
import org.jkiss.dbeaver.model.DBPObjectWithLongId;
import org.jkiss.dbeaver.model.struct.DBSObject;

public interface IoTDBObject extends DBPNamedObject, DBSObject, DBPObjectWithLongId{
	  @NotNull
	  IoTDBDataSource getDataSource();

}
