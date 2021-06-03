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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.ext.iotdb.model.IoTDBDatabase.UniqueKeyCache.CheckConstraintCache;
import org.jkiss.dbeaver.model.DBPObjectStatistics;
import org.jkiss.dbeaver.model.DBPObjectStatisticsCollector;
import org.jkiss.dbeaver.model.DBPRefreshableObject;
import org.jkiss.dbeaver.model.DBPSaveableObject;
import org.jkiss.dbeaver.model.DBPScriptObject;
import org.jkiss.dbeaver.model.DBPScriptObjectExt2;
import org.jkiss.dbeaver.model.DBPSystemObject;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCPreparedStatement;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCResultSet;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCSession;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCStatement;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCConstants;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCUtils;
import org.jkiss.dbeaver.model.impl.jdbc.cache.JDBCCompositeCache;
import org.jkiss.dbeaver.model.impl.jdbc.cache.JDBCStructLookupCache;
import org.jkiss.dbeaver.model.meta.Association;
import org.jkiss.dbeaver.model.preferences.DBPPropertySource;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;

import org.jkiss.dbeaver.model.struct.DBSEntityConstraintType;
import org.jkiss.dbeaver.model.struct.DBSObject;

import org.jkiss.dbeaver.model.struct.rdb.DBSCatalog;
import org.jkiss.dbeaver.model.struct.rdb.DBSProcedure;
import org.jkiss.dbeaver.model.struct.rdb.DBSProcedureContainer;


public class IoTDBDatabase implements DBSCatalog, DBPSaveableObject, DBPRefreshableObject, DBPSystemObject,
DBSProcedureContainer, DBPObjectStatisticsCollector, DBPObjectStatistics,
DBPScriptObject, DBPScriptObjectExt2{

	final TableCache tableCache = new TableCache();

	 private final IoTDBDataSource dataSource;
	 private String name;
	 private boolean persisted;
	 final UniqueKeyCache uniqueKeyCache = new UniqueKeyCache(tableCache);
	 final CheckConstraintCache checkConstraintCache = new CheckConstraintCache(tableCache);
	 //private final AdditionalInfo additionalInfo = new AdditionalInfo();
    public IoTDBDatabase(IoTDBDataSource dataSource, ResultSet dbResult)
    {
        tableCache.setCaseSensitive(false);
        this.dataSource = dataSource;
        if (dbResult != null) {
            this.name = JDBCUtils.safeGetString(dbResult, 1);
            persisted = true;
        } else {
            persisted = false;
        }
    }

	@Override
	public Collection<? extends DBSObject> getChildren(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		 return tableCache.getAllObjects(monitor, this);
	}
	 @Association
	    public Collection<IoTDBTable> getTables(DBRProgressMonitor monitor) throws DBException {
	        return tableCache.getTypedObjects(monitor, this, IoTDBTable.class);
	    }

	@Override
	public DBSObject getChild(DBRProgressMonitor monitor, String childName) throws DBException {
		// TODO Auto-generated method stub
		return tableCache.getObject(monitor, this, childName);
	}
	public TableCache getTableCache()
    {
        return tableCache;
    }
	 @Override
	    public void setPersisted(boolean persisted)
	    {
	        this.persisted = persisted;
	    }
	@Override
	public Class<? extends DBSObject> getPrimaryChildType(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cacheStructure(DBRProgressMonitor monitor, int scope) throws DBException {
		// TODO Auto-generated method stub
		  monitor.subTask("Cache tables");
	        tableCache.getAllObjects(monitor, this);
	        if ((scope & STRUCT_ATTRIBUTES) != 0) {
	            monitor.subTask("Cache table columns");
	            tableCache.loadChildren(monitor, this, null);
	        }
	        if ((scope & STRUCT_ASSOCIATIONS) != 0) {
	            monitor.subTask("Cache table constraints");
	            uniqueKeyCache.getAllObjects(monitor, this);
	            if (getDataSource().supportsCheckConstraints()) {
	                checkConstraintCache.getAllObjects(monitor, this);
	            }
	        }
		
	}

	@Override
	public DBSObject getParentObject() {
		// TODO Auto-generated method stub
		return dataSource.getContainer();
	}

	@Override
	public IoTDBDataSource getDataSource() {
		// TODO Auto-generated method stub
		return dataSource;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	 public void setName(String name)
	    {
	        this.name = name;
	    }


	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPersisted() {
		// TODO Auto-generated method stub
		 return persisted;
	}

	@Override
	public boolean supportsObjectDefinitionOption(String option) {
		// TODO Auto-generated method stub
		return false;
	}


	 @Override
	    public String getObjectDefinitionText(DBRProgressMonitor monitor, Map<String, Object> options) throws DBException {
	        
	        return null;
	    }

	@Override
	public boolean hasStatistics() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getStatObjectSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DBPPropertySource getStatProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isStatisticsCollected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void collectObjectStatistics(DBRProgressMonitor monitor, boolean totalSizeOnly, boolean forceRefresh)
			throws DBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<? extends DBSProcedure> getProcedures(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBSProcedure getProcedure(DBRProgressMonitor monitor, String uniqueName) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSystem() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DBSObject refreshObject(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		
	        tableCache.clearCache();
	       
	        uniqueKeyCache.clearCache();
	        if (getDataSource().supportsCheckConstraints()) {
	            checkConstraintCache.clearCache();
	        }
	      
	        return this;
	}

   
	  public static class TableCache extends JDBCStructLookupCache<IoTDBDatabase, IoTDBTableBase, IoTDBTableColumn> {

	        TableCache()
	        {
	            super(JDBCConstants.TABLE_NAME);
	        }

	       

	        @Override
	        protected IoTDBTableBase fetchObject(@NotNull JDBCSession session, @NotNull IoTDBDatabase owner, @NotNull JDBCResultSet dbResult)
	            throws SQLException, DBException{
	                return new IoTDBTable(owner, dbResult);	            
	        }

	        @Override
	        protected JDBCStatement prepareChildrenStatement(@NotNull JDBCSession session, @NotNull IoTDBDatabase owner, @Nullable IoTDBTableBase forTable)
	            throws SQLException{
	            StringBuilder sql = new StringBuilder();
	            sql.append("show timeseries "+owner.getName());
	            JDBCPreparedStatement dbStat = session.prepareStatement(sql.toString());
	            if (forTable != null) {
	                dbStat.setString(2, forTable.getName());
	            }
	            return dbStat;
	        }

	        @Override
	        protected IoTDBTableColumn fetchChild(@NotNull JDBCSession session, @NotNull IoTDBDatabase owner, @NotNull IoTDBTableBase table, @NotNull JDBCResultSet dbResult)
	            throws SQLException, DBException
	        {
	            return new IoTDBTableColumn(table, dbResult);
	        }

			@Override
			public JDBCStatement prepareLookupStatement(JDBCSession session, IoTDBDatabase owner, IoTDBTableBase object,
					String objectName) throws SQLException {
				StringBuilder sql = new StringBuilder("SHOW timeseries "+owner.getName());
		            return session.prepareStatement(sql.toString());
			}


			
	    }
	    /**
	     * Constraint cache implementation
	     */
	    static class UniqueKeyCache extends JDBCCompositeCache<IoTDBDatabase, IoTDBTable, IoTDBTableConstraint, IoTDBTableConstraintColumn> {
	        UniqueKeyCache(TableCache tableCache)
	        {
	            super(tableCache, IoTDBTable.class, "TABLE_NAME","CONSTRAINT_NAME");
	        }

	        @NotNull
	        @Override
	        protected JDBCStatement prepareObjectsStatement(JDBCSession session, IoTDBDatabase owner, IoTDBTable forTable)
	            throws SQLException
	        {
	            
	            return null;
	        }
	        /**
	         * Check constraint cache implementation
	         */

	        static class CheckConstraintCache extends JDBCCompositeCache<IoTDBDatabase, IoTDBTable, IoTDBTableConstraint, IoTDBTableConstraintColumn> {
	            CheckConstraintCache(TableCache tableCache)
	            {
	                super(tableCache, IoTDBTable.class, "TABLE_NAME", "CONSTRAINT_NAME");
	            }

	            @Override
	            protected JDBCStatement prepareObjectsStatement(JDBCSession session, IoTDBDatabase owner, IoTDBTable forTable) throws SQLException {
	                
	                return null;
	            }

	            @Override
	            protected IoTDBTableConstraint fetchObject(JDBCSession session, IoTDBDatabase owner, IoTDBTable parent, String checkConstraintName, JDBCResultSet resultSet) throws SQLException, DBException {

	            	return new IoTDBTableConstraint(parent, checkConstraintName, null, DBSEntityConstraintType.CHECK, true, resultSet);
	            }

	            @Override
	            protected IoTDBTableConstraintColumn[] fetchObjectRow(JDBCSession session, IoTDBTable mySQLTable, IoTDBTableConstraint forObject, JDBCResultSet resultSet) throws SQLException, DBException {
	                return new IoTDBTableConstraintColumn[0];
	            }

	            @Override
	            protected void cacheChildren(DBRProgressMonitor monitor, IoTDBTableConstraint object, List<IoTDBTableConstraintColumn> children) {

	            }


			
	        }
	       

	        @Nullable
	        @Override
	        protected IoTDBTableConstraintColumn[] fetchObjectRow(
	                JDBCSession session,
	                IoTDBTable parent, IoTDBTableConstraint object, JDBCResultSet dbResult)
	            throws SQLException, DBException
	        {
	          
	        	return null;
	        }

	        @Override
	        protected void cacheChildren(DBRProgressMonitor monitor, IoTDBTableConstraint constraint, List<IoTDBTableConstraintColumn> rows)
	        {
	            constraint.setColumns(rows);
	        }

			@Override
			protected IoTDBTableConstraint fetchObject(JDBCSession session, IoTDBDatabase owner, IoTDBTable parent,
					String childName, JDBCResultSet resultSet) throws SQLException, DBException {
				// TODO Auto-generated method stub
				return null;
			}
	    }
	  @Override
	    public String toString()
	    {
	        return name + " [" + dataSource.getContainer().getName() + "]";
	    } 

}
