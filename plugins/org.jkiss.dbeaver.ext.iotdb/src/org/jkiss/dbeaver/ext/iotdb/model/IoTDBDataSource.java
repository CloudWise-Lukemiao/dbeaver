package org.jkiss.dbeaver.ext.iotdb.model;

import java.sql.SQLException;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import org.jkiss.code.NotNull;

import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.ModelPreferences;
import org.jkiss.dbeaver.ext.iotdb.model.IoTDBDataSource;

import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.DBPObjectStatisticsCollector;

import org.jkiss.dbeaver.model.exec.jdbc.JDBCPreparedStatement;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCResultSet;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCSession;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCStatement;

import org.jkiss.dbeaver.model.impl.jdbc.JDBCDataSource;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCUtils;
import org.jkiss.dbeaver.model.impl.jdbc.cache.JDBCBasicDataTypeCache;
import org.jkiss.dbeaver.model.impl.jdbc.cache.JDBCObjectCache;
import org.jkiss.dbeaver.model.impl.jdbc.struct.JDBCDataType;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.sql.SQLDialect;
import org.jkiss.dbeaver.model.struct.DBSDataType;
import org.jkiss.dbeaver.model.struct.DBSObject;
import org.jkiss.dbeaver.model.struct.DBSObjectFilter;


public class IoTDBDataSource extends JDBCDataSource implements DBPObjectStatisticsCollector{
	 protected IoTDBDataSource(DBPDataSourceContainer container, SQLDialect dialect) {
		super(container, dialect);
		 dataTypeCache = new JDBCBasicDataTypeCache<>(this); 
		// TODO Auto-generated constructor stub
	}
	 private final CatalogCache catalogCache = new CatalogCache();
	 private final JDBCBasicDataTypeCache<IoTDBDataSource, JDBCDataType> dataTypeCache;
	private static final Log log = Log.getLog(IoTDBDataSource.class);
    private volatile boolean hasStatistics; 
    private int lowerCaseTableNames = 1;

	    private static Map<String, String> dataTypeMap = new HashMap<>();
	   
	    static {
	        dataTypeMap.put(String.class.getName(), "Text");
	        dataTypeMap.put(Integer.class.getName(), "Int32");
	        dataTypeMap.put(Long.class.getName(), "Int64");
	        dataTypeMap.put(Float.class.getName(), "Float");
	        dataTypeMap.put(Double.class.getName(), "Double");
	        dataTypeMap.put(Boolean.class.getName(), "Boolean");

	    }

	    public IoTDBDataSource(DBRProgressMonitor monitor, DBPDataSourceContainer container)
	            throws DBException {
	            super(monitor, container, new IoTDBSQLDialect());
	            dataTypeCache = new JDBCBasicDataTypeCache<>(this);
	            hasStatistics = !container.getPreferenceStore().getBoolean(ModelPreferences.READ_EXPENSIVE_STATISTICS);
	        }
		@Override
		public DBPDataSource getDataSource() {
			// TODO Auto-generated method stub
			return this;
		}
		 public Collection<IoTDBDatabase> getCatalogs() {
		        return catalogCache.getCachedObjects();
		    }

		@Override
		public Collection<? extends DBSDataType> getLocalDataTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DBSDataType getLocalDataType(String typeName) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Collection<? extends IoTDBDatabase> getChildren(DBRProgressMonitor monitor) throws DBException {
			// TODO Auto-generated method stub
			 return getCatalogs();
			
		}
		 public IoTDBDatabase getCatalog(String name) {
		        return catalogCache.getCachedObject(name);
		    }

		@Override
		public IoTDBDatabase getChild(DBRProgressMonitor monitor, String childName) throws DBException {
			// TODO Auto-generated method stub
			return getCatalog(childName);
			
		}

		@Override
		public Class<? extends DBSObject> getPrimaryChildType(DBRProgressMonitor monitor) throws DBException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void cacheStructure(DBRProgressMonitor monitor, int scope) throws DBException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isStatisticsCollected() {
			// TODO Auto-generated method stub
			return false;
		}
		 int getLowerCaseTableNames() {
		        return lowerCaseTableNames;
		    }
		@Override
		public void collectObjectStatistics(DBRProgressMonitor monitor, boolean totalSizeOnly, boolean forceRefresh)
				throws DBException {
			// TODO Auto-generated method stub
			
		}
		   public boolean supportsCheckConstraints() {
		       return true;
		    }
		
		 @Override
		    public void initialize(@NotNull DBRProgressMonitor monitor)
		        throws DBException {
		        super.initialize(monitor);
		        dataTypeCache.getAllObjects(monitor, this);
		        catalogCache.getAllObjects(monitor, this);
		    }
		
		    static class CatalogCache extends JDBCObjectCache<IoTDBDataSource, IoTDBDatabase> {
		        @NotNull
		        @Override
		        protected JDBCStatement prepareObjectsStatement(@NotNull JDBCSession session, @NotNull IoTDBDataSource owner) throws SQLException {
		            StringBuilder catalogQuery = new StringBuilder("SHOW STORAGE GROUP");
		            DBSObjectFilter catalogFilters = owner.getContainer().getObjectFilter(IoTDBDatabase.class, null, false);
		            if (catalogFilters != null) {
		                JDBCUtils.appendFilterClause(catalogQuery, catalogFilters, "name", true);
		            }
		            JDBCPreparedStatement dbStat = session.prepareStatement(catalogQuery.toString());
		            if (catalogFilters != null) {
		                JDBCUtils.setFilterParameters(dbStat, 1, catalogFilters);
		            }
		            return dbStat;
		        }

		        @Override
		        protected IoTDBDatabase fetchObject(@NotNull JDBCSession session, @NotNull IoTDBDataSource owner, @NotNull JDBCResultSet resultSet) throws SQLException, DBException {
		        	return new IoTDBDatabase(owner, resultSet);
		        }

		    }
		
	   
}
