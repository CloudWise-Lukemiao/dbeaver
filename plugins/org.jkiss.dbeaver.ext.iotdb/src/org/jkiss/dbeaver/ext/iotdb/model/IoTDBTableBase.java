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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.model.DBFetchProgress;
import org.jkiss.dbeaver.model.DBPDataSource;
import org.jkiss.dbeaver.model.DBPEvaluationContext;
import org.jkiss.dbeaver.model.DBPNamedObject;
import org.jkiss.dbeaver.model.DBPNamedObject2;
import org.jkiss.dbeaver.model.DBPRefreshableObject;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.data.DBDDataFilter;
import org.jkiss.dbeaver.model.data.DBDDataReceiver;
import org.jkiss.dbeaver.model.data.DBDPseudoAttribute;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.exec.DBCExecutionSource;
import org.jkiss.dbeaver.model.exec.DBCResultSet;
import org.jkiss.dbeaver.model.exec.DBCSession;
import org.jkiss.dbeaver.model.exec.DBCStatement;
import org.jkiss.dbeaver.model.exec.DBCStatementType;
import org.jkiss.dbeaver.model.exec.DBCStatistics;
import org.jkiss.dbeaver.model.exec.DBExecUtils;
import org.jkiss.dbeaver.model.exec.jdbc.JDBCStatement;
import org.jkiss.dbeaver.model.impl.jdbc.JDBCUtils;
import org.jkiss.dbeaver.model.impl.jdbc.cache.JDBCStructCache;
import org.jkiss.dbeaver.model.impl.jdbc.struct.JDBCTable;
import org.jkiss.dbeaver.model.messages.ModelMessages;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.sql.SQLDialect;
import org.jkiss.dbeaver.model.sql.SQLUtils;
import org.jkiss.dbeaver.model.struct.DBSAttributeBase;
import org.jkiss.dbeaver.model.struct.DBSDataManipulator.ExecuteBatch;
import org.jkiss.dbeaver.model.struct.DBSEntity;
import org.jkiss.dbeaver.model.struct.DBSEntityAssociation;
import org.jkiss.dbeaver.model.struct.DBSEntityAttribute;
import org.jkiss.dbeaver.model.struct.DBSObject;
import org.jkiss.dbeaver.model.struct.rdb.DBSCatalog;
import org.jkiss.dbeaver.model.struct.rdb.DBSSchema;
import org.jkiss.dbeaver.model.struct.rdb.DBSTableConstraint;
import org.jkiss.dbeaver.model.struct.rdb.DBSTableIndex;

public class IoTDBTableBase  extends JDBCTable<IoTDBDataSource, IoTDBDatabase>
implements DBPNamedObject2,DBPRefreshableObject, IoTDBSourceObject{
	 private static final Log log = Log.getLog(IoTDBTableBase.class);
	 private void readRequiredMeta(DBRProgressMonitor monitor)
		        throws DBCException
		    {
		        try {
		            getAttributes(monitor);
		        }
		        catch (DBException e) {
		            throw new DBCException("Can't cache table columns", e);
		        }
		    }
	 protected void appendSelectSource(DBRProgressMonitor monitor, StringBuilder query, String tableAlias, DBDPseudoAttribute rowIdAttribute) {
	        if (rowIdAttribute != null) {
	            // If we have pseudo attributes then query gonna be more complex
	            query.append(tableAlias).append(".*"); //$NON-NLS-1$
	            query.append(",").append(rowIdAttribute.translateExpression(tableAlias));
	            if (rowIdAttribute.getAlias() != null) {
	                query.append(" as ").append(rowIdAttribute.getAlias());
	            }
	        } else {
	            if (tableAlias != null) {
	                query.append(tableAlias).append(".");
	            }
	            query.append("*"); //$NON-NLS-1$
	        }
	    }
	@Override
	public DBCStatistics readData(DBCExecutionSource source, DBCSession session, DBDDataReceiver dataReceiver,
			DBDDataFilter dataFilter, long firstRow, long maxRows, long flags, int fetchSize) throws DBCException {
		// TODO Auto-generated method stub
		 DBCStatistics statistics = new DBCStatistics();
	        boolean hasLimits = firstRow >= 0 && maxRows > 0;

	        DBPDataSource dataSource = session.getDataSource();
	        DBRProgressMonitor monitor = session.getProgressMonitor();
	        try {
	            readRequiredMeta(monitor);
	        } catch (DBException e) {
	        	log.warn(e);
	           
	        }

	        DBDPseudoAttribute rowIdAttribute = (flags & FLAG_READ_PSEUDO) != 0 ?
	            DBUtils.getRowIdAttribute(this) : null;

	        // Always use alias if we have criteria or ROWID.
	        // Some criteria doesn't work without alias
	        // (e.g. structured attributes in Oracle requires table alias)
	        String tableAlias = null;
	        if ((dataFilter != null && dataFilter.hasConditions()) || rowIdAttribute != null) {
	            {
	                if (dataSource.getSQLDialect().supportsAliasInSelect()) {
	                    tableAlias = "";
	                }
	            }
	        }

	        if (rowIdAttribute != null && tableAlias == null) {
	            log.warn("Can't query ROWID - table alias not supported");
	            rowIdAttribute = null;
	        }

	        StringBuilder query = new StringBuilder(100);
	        String path=getFullyQualifiedName(DBPEvaluationContext.DML);
	        String measure=path.substring(0,path.lastIndexOf("."));
	        String filed=path.substring(path.lastIndexOf(".")+1);
	        query.append("SELECT ").append(filed);
	       
	        query.append(" FROM ").append(measure);
	        if (tableAlias != null) {
	            query.append(" ").append(tableAlias); //$NON-NLS-1$
	        }
	        SQLUtils.appendQueryConditions(dataSource, query, tableAlias, dataFilter);
	        SQLUtils.appendQueryOrder(dataSource, query, tableAlias, dataFilter);

	        String sqlQuery = query.toString();
	        statistics.setQueryText(sqlQuery);
	        System.out.println(sqlQuery);
	        monitor.subTask(ModelMessages.model_jdbc_fetch_table_data);

	        try (DBCStatement dbStat = DBUtils.makeStatement(
	            source,
	            session,
	            DBCStatementType.SCRIPT,
	            sqlQuery,
	            firstRow,
	            maxRows))
	        {
	            if (monitor.isCanceled()) {
	                return statistics;
	            }
	            if (dbStat instanceof JDBCStatement && (fetchSize > 0 || maxRows > 0)) {
	                DBExecUtils.setStatementFetchSize(dbStat, firstRow, maxRows, fetchSize);
	            }

	            long startTime = System.currentTimeMillis();
	            boolean executeResult = dbStat.executeStatement();
	            statistics.setExecuteTime(System.currentTimeMillis() - startTime);
	            if (executeResult) {
	                DBCResultSet dbResult = dbStat.openResultSet();
	                if (dbResult != null && !monitor.isCanceled()) {
	                    try {
	                        dataReceiver.fetchStart(session, dbResult, firstRow, maxRows);

	                        DBFetchProgress fetchProgress = new DBFetchProgress(session.getProgressMonitor());
	                        while (dbResult.nextRow()) {
	                            if (fetchProgress.isCanceled() || (hasLimits && fetchProgress.isMaxRowsFetched(maxRows))) {
	                                // Fetch not more than max rows
	                                break;
	                            }
	                            dataReceiver.fetchRow(session, dbResult);
	                            fetchProgress.monitorRowFetch();
	                        }
	                        fetchProgress.dumpStatistics(statistics);
	                    } finally {
	                        // First - close cursor
	                        try {
	                            dbResult.close();
	                        } catch (Throwable e) {
	                            log.error("Error closing result set", e); //$NON-NLS-1$
	                        }
	                        // Then - signal that fetch was ended
	                        try {
	                            dataReceiver.fetchEnd(session, dbResult);
	                        } catch (Throwable e) {
	                            log.error("Error while finishing result set fetch", e); //$NON-NLS-1$
	                        }
	                    }
	                }
	            }
	            return statistics;
	        } finally {
	            dataReceiver.close();
	        }
		
	}
	@Override
	public ExecuteBatch insertData(DBCSession session, DBSAttributeBase[] attributes, DBDDataReceiver keysReceiver,
			DBCExecutionSource source) throws DBCException {
		// TODO Auto-generated method stub
		return super.insertData(session, attributes, keysReceiver, source);
	}
	@Override
	public ExecuteBatch deleteData(DBCSession session, DBSAttributeBase[] keyAttributes, DBCExecutionSource source)
			throws DBCException {
		// TODO Auto-generated method stub
		return super.deleteData(session, keyAttributes, source);
	}
	protected IoTDBTableBase(IoTDBDatabase container, boolean persisted) {
		super(container, persisted);
		// TODO Auto-generated constructor stub
	}
	protected IoTDBTableBase(
			IoTDBDatabase catalog,
	        ResultSet dbResult)
	    {
	        super(catalog, JDBCUtils.safeGetString(dbResult, 1), true);
	    }
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getFullyQualifiedName(DBPEvaluationContext context) {
		// TODO Auto-generated method stub
		DBPNamedObject dbp=this;
		
		 return getFullQualifiedName1(null,
		            getContainer(),
		            this);
		
		 
	}
	 public static String getFullQualifiedName1(@Nullable DBPDataSource dataSource, @NotNull DBPNamedObject ... path)
	    {
	        StringBuilder name = new StringBuilder(20 * path.length);
	        if (dataSource  == null) {
	            // It is not SQL identifier, let's just make it simple then
	        	int i=0;
	            for (DBPNamedObject namePart : path) {
	            	i++;
	                if (i==2) {
	                name.append(namePart.getName());
	                break;
	                }
	            }
	        } 
	        return name.toString();
	    }
	@Override
	public DBSObject refreshObject(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		return getContainer().tableCache.refreshObject(monitor, getContainer(), this);
	}

	@Override
	public JDBCStructCache<IoTDBDatabase, ? extends DBSEntity, ? extends DBSEntityAttribute> getCache() {
		// TODO Auto-generated method stub
		 return getContainer().getTableCache();
	}
	@Override
	public String getObjectDefinitionText(DBRProgressMonitor monitor, Map<String, Object> options) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isView() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Collection<? extends DBSTableIndex> getIndexes(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Collection<? extends DBSTableConstraint> getConstraints(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<? extends DBSEntityAttribute> getAttributes(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DBSEntityAttribute getAttribute(DBRProgressMonitor monitor, String attributeName) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Collection<? extends DBSEntityAssociation> getAssociations(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Collection<? extends DBSEntityAssociation> getReferences(DBRProgressMonitor monitor) throws DBException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setObjectDefinitionText(String sourceText) throws DBException {
		// TODO Auto-generated method stub
		
	}

	

}
