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
package org.jkiss.dbeaver.ext.iotdb;

import java.util.List;

import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.ext.iotdb.model.IoTDBDataSource;

import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.connection.DBPConnectionConfiguration;
import org.jkiss.dbeaver.model.connection.DBPDriver;
import org.jkiss.dbeaver.model.connection.DBPNativeClientLocation;
import org.jkiss.dbeaver.model.connection.DBPNativeClientLocationManager;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCDataSourceProvider;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.utils.CommonUtils;

public class IoTDBDataSourceProvider extends JDBCDataSourceProvider implements DBPNativeClientLocationManager {

    private static final Log log = Log.getLog(IoTDBDataSourceProvider.class);

	@Override
	public long getFeatures() {
		// TODO Auto-generated method stub
		return 0;
	}
	 @Override
	    public String getConnectionURL(DBPDriver driver, DBPConnectionConfiguration connectionInfo)
	    {
	        StringBuilder url = new StringBuilder();
	        url.append("jdbc:iotdb://")
	            .append(connectionInfo.getHostName());
	        if (!CommonUtils.isEmpty(connectionInfo.getHostPort())) {
	            url.append(":").append(connectionInfo.getHostPort());
	        }
	        url.append("/");
	        if (!CommonUtils.isEmpty(connectionInfo.getDatabaseName())) {
	            url.append(connectionInfo.getDatabaseName());
	        }

	        return url.toString();
	    }


	@Override
	public DBPDataSource openDataSource(DBRProgressMonitor monitor, DBPDataSourceContainer container)
			throws DBException {
		// TODO Auto-generated method stub
		return new IoTDBDataSource(monitor, container);
		
	}



	@Override
	public List<DBPNativeClientLocation> findLocalClientLocations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBPNativeClientLocation getDefaultLocalClientLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProductName(DBPNativeClientLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProductVersion(DBPNativeClientLocation location) {
		// TODO Auto-generated method stub
		return null;
	}



}
