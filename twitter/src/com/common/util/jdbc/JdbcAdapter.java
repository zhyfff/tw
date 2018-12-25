package com.common.util.jdbc;



import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;






public class JdbcAdapter implements Serializable {
	// ???{?f?[?^?^?C?v
	private DataSource ds; // ?f?[?^?\?[?X

	private Connection m_conn = null;

	private String[] m_columnNames = null;

	public static Log log = LogFactory.getLog(JdbcAdapter.class);

	//private static final Logit sqlLog = Logit.getInstance("backsql");
	

	
	   private PreparedStatement addParams(PreparedStatement pstmt, List params) throws Exception {
	        if (params != null) {
	        	//log.info("Parameters = ");
	            for (int i = 0; i < params.size(); i++) {
	                if (params.get(i) instanceof SQLParameterBean) {
	                    SQLParameterBean param = (SQLParameterBean)params.get(i);
	                    switch(param.getType()) {
	                    case Types.DECIMAL : {
	                        pstmt.setBigDecimal(i + 1, new BigDecimal(param.getValue()));
	                        //log.info("    " + (i + 1) + " DECIMAL   " + param.getValue());
	                        break;
	                    }
	                    case Types.INTEGER : {
	                        pstmt.setInt(i + 1, Integer.parseInt(param.getValue()));
	                        //log.info("    " + (i + 1) + " INTEGER   " + param.getValue());
	                        break;
	                    }
	                    case Types.TIMESTAMP : {
	                    	//log.info("    " + (i + 1) + " TIMESTAMP " + param.getValue());
	                        pstmt.setTimestamp(i + 1, Timestamp.valueOf(param.getValue()));
	                        break;
	                    }
	                    default : {
	                    	pstmt.setString(i + 1, param.getValue());
	                    	//log.info("    " + (i + 1) + " VARCHAR   " + param.getValue());
	                        break;
	                    }
	                    }
	                } else {
	                    throw new Exception("Bad type, not a SQLParameterBean");
	                }
	            }
	        }
	        return pstmt;
	    }
    
    private PreparedStatement addParams(PreparedStatement pstmt, Map params) throws Exception {
        if (params != null) {
        	//log.info("Parameters = ");
            Iterator keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String)keys.next();
                int index = Integer.parseInt(key);
                pstmt.setObject(index, params.get(key));
                //log.info("    " + index + " OBJECT " + params.get(key));
            }
        }
        return pstmt;
    }
    
    public Vector dbSelect(String sql, List params) throws Exception {
        return executeQuery(sql, params, -1, -1, true);
    }
    
    public Vector dbSelect(String sql, List params, int start, int end) throws Exception {
        return executeQuery(sql, params, start, end, true);
    }
    
    public Vector dbSelect(String sql, Map params) throws Exception {
        return executeQuery(sql, params, -1, -1, true);
    }
    
    public Vector dbSelect(String sql, Map params, int start, int end) throws Exception {
        return executeQuery(sql, params, start, end, true);
    }
    
    private Vector executeQuery(String sql, Map params, int start, int end, boolean bUseTrans) throws Exception {
        PreparedStatement pstmt;
        ResultSet rset;
        Connection conn = null;
        Vector rows = new Vector();
        ResultSetMetaData metaData;
        try {
            if (sql.indexOf("select") != -1 || sql.indexOf("SELECT") != -1) {
                conn = getConnection();
//                //log.info("SQL = " + sql);
                pstmt = conn.prepareStatement(sql);
                pstmt = addParams(pstmt, params);
                rset = pstmt.executeQuery();
                metaData = rset.getMetaData();
                int numberOfColumns = metaData.getColumnCount();
                this.m_columnNames = new String[numberOfColumns];
                for (int i = 1; i < numberOfColumns + 1; i++) {
                    m_columnNames[i - 1] = metaData.getColumnName(i);
                }
                String[] columnNames = new String[numberOfColumns];
                if (start != -1) {
                    for (int dmyCount = 0; dmyCount < start; dmyCount++) {
                        if (rset.next() == false)
                            break;
                    }
                }
                int rowCount = 0;
                while (rset.next()) {
                    if (end != -1 && end == rowCount) {
                        break;
                    }
                    Vector newRow = new Vector();
                    for (int i = 1; i <= columnNames.length; i++) {
                        newRow.addElement(rset.getObject(i));
                    }
                    rows.addElement(newRow);
                    rowCount++;
                }
                rset.close();
                pstmt.close();
                close(conn);
            }
        } catch (Exception e) {
            if (conn != null) {
                close(conn);
            }
            log.error(e);;
            throw e;
        }
        return rows;
    }
    
    private Vector executeQuery(String sql, List params, int start, int end, boolean bUseTrans) throws Exception {
        PreparedStatement pstmt;
        ResultSet rset;
        Connection conn = null;
        Vector rows = new Vector();
        ResultSetMetaData metaData;
        try {
            if (sql.indexOf("select") != -1 || sql.indexOf("SELECT") != -1) {
                conn = getConnection();
//                //log.info("SQL = " + sql);
                pstmt = conn.prepareStatement(sql);
                pstmt = addParams(pstmt, params);
                rset = pstmt.executeQuery();
                metaData = rset.getMetaData();
                int numberOfColumns = metaData.getColumnCount();
                this.m_columnNames = new String[numberOfColumns];
                for (int i = 1; i < numberOfColumns + 1; i++) {
                    m_columnNames[i - 1] = metaData.getColumnName(i);
                }
                String[] columnNames = new String[numberOfColumns];
                if (start != -1) {
                    for (int dmyCount = 0; dmyCount < start; dmyCount++) {
                        if (rset.next() == false)
                            break;
                    }
                }
                int rowCount = 0;
                while (rset.next()) {
                    if (end != -1 && end == rowCount) {
                        break;
                    }
                    Vector newRow = new Vector();
                    for (int i = 1; i <= columnNames.length; i++) {
                        newRow.addElement(rset.getObject(i));
                    }
                    rows.addElement(newRow);
                    rowCount++;
                }
                rset.close();
                pstmt.close();
                close(conn);
            }
        } catch (Exception e) {
            if (conn != null) {
                close(conn);
            }
            log.error(e);;
            throw e;
        }
        return rows;
    }
    
    public void insertBlob(String sql, String infoId, String infoUrl , InputStream in) throws Exception {
        PreparedStatement pstmt;
        Connection conn = null;
        try {
            if (sql.indexOf("insert") != -1 || sql.indexOf("insert") != -1) {
                conn = getConnection();
//                //log.info("SQL = " + sql);
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, infoId);
                pstmt.setString(2, infoUrl);
                pstmt.setBinaryStream(3, in, in.available());  
                pstmt.executeUpdate();  
                
                pstmt.close();
                pstmt = null;
                close(conn);
            }
        } catch (Exception e) {
            if (conn != null) {
                close(conn);
            }
            log.error(e);;
            
            throw e;
        } finally {
	    	   try {  
	    	       //鍏抽棴娴� 
	    	       if(in!=null) in.close();  
	    	   } catch (IOException e) {  
	    	       // TODO Auto-generated catch block  
	    	       log.error(e);;  
	    	   }  

	           

        }

    }
  
  
    
    public InputStream getInfoBlob(String sql) throws Exception {
        PreparedStatement pstmt;
        Connection conn = null;
        ResultSet rs=null;  
        InputStream  in = null;
        try {
            if (sql.indexOf("select") != -1 || sql.indexOf("SELECT") != -1) {
                conn = getConnection();
//                //log.info("SQL = " + sql);
                pstmt = conn.prepareStatement(sql);
                rs=pstmt.executeQuery(sql); 
                rs.next(); 
//                in=rs.getBinaryStream(1);
                Blob b =rs.getBlob(1);
                in = b.getBinaryStream();
                
                
                pstmt.close();
                close(conn);
            }
        } catch (Exception e) {
            if (conn != null) {
                close(conn);
            }
            log.error(e);;
        } finally {
	    	   try {  
	    	       //鍏抽棴娴� 
	    	       if(in!=null) in.close();  
	    	   } catch (IOException e) {  
	    	       // TODO Auto-generated catch block  
	    	       log.error(e);;  
	    	   }  

        }
        return in;
    }
    
    public int dbUpdate(String sql, List params) {
        return executeSQL(sql, params);
    }

    public int dbInsert(String sql, List params) {
        return executeSQL(sql, params);
    }
    
    public int dbUpdate(String sql, Map params) {
        return executeSQL(sql, params);
    }

    public int dbInsert(String sql, Map params) {
        return executeSQL(sql, params);
    }
    
    private int executeSQL(String sql, List params) {
  //  	sqlLog.info(sql);
        PreparedStatement pstmt;
        Connection conn = null;
        int result = -1;
        try {
            conn = getConnection();
            //log.info("SQL = " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt = addParams(pstmt, params);
            result = pstmt.executeUpdate();
            Commit(conn);
            pstmt.close();
        } catch (SQLException sqle) {
        	rollback(conn);
            log.error(sqle);
        } catch (Exception e) {
        	rollback(conn);
            log.error(e);;
        }finally{
            pstmt = null;
            if (conn != null) {
                close(conn);
            }
        }
        return result;
    }
    
    private int executeSQL(String sql, Map params) {
    //	sqlLog.info(sql);
        PreparedStatement pstmt;
        Connection conn = null;
        int result = -1;
        try {
            conn = getConnection();
            //log.info("SQL = " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt = addParams(pstmt, params);
            result = pstmt.executeUpdate();
            Commit(conn);
            pstmt.close();
        } catch (SQLException sqle) {
        	rollback(conn);
            log.error(sqle);
        } catch (Exception e) {
        	rollback(conn);
            log.error(e);;
        }finally{
            pstmt = null;
            if (conn != null) {
                close(conn);
            }
        }
        return result;
    }
    
    // add by shangcm 2006/11/30 for PreparedStatement end


	/** ?R???X?g???N?^?[ */
	public JdbcAdapter() {
		ds = null;
	}

	/**
	 * ?C?j?V?????C?Y
	 */
	public boolean init(DataSource dss) {
		try {
			this.ds = dss;
			return true;
		} catch (Exception e) {
			log.error(e);;
		}
		return false;
	}

	public boolean init(DataSource dss, Connection conn) {
		try {
			this.ds = dss;
			this.m_conn = conn;
			return true;
		} catch (Exception e) {
			log.error(e);;
		}
		return false;
	}

	/**
	 * ?R?l?N?V??????????
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			if (m_conn == null) {
				conn = ds.getConnection();
			}
			else
				conn = m_conn;

			conn.setAutoCommit(false);
		} catch (Exception e) {
			log.error(e);;
		}
		return conn;
	}

	/**
	 * ?R?l?N?V?????????f
	 */
	public void close(Connection conn) {
		try {
			conn.close();
			return;
		} catch (SQLException sqle) {
			log.error(sqle);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				conn = null;
			}
		}
	}

	/**
	 * ?Z???N?g?????s?????B
	 * 
	 * @return Vector ????
	 * @param String
	 *            query SQL
	 */
	public Vector dbSelect(String query) {
		//log.info("sql = " + query);
		return dbSelect(query, -1, -1);
	}

	/**
	 * ?Z???N?g?????s?????B
	 * 
	 * @return Vector ????
	 * @param String
	 *            query SQL
	 * @param int
	 *            start ?J?n?s
	 * @param int
	 *            end ?I???s
	 */
	public Vector dbSelect(String query, int start, int end) {
		// ////executeQuery()???????o??///////////////////
		return executeQuery(query, start, end);
	}

	/**
	 * ?? ?A?b?v?f?[?g
	 * 
	 * @return boolean true / flase
	 * @param String
	 *            query SQ
	 */
	public boolean dbUpdate(String query) {
	///	sqlLog.info("JdbcAdapter updateSql: " + query);
		//log.info("sql = " + query);
		return executeSQL(query);
	}


	
	/**
	 * ?C???T?[?g
	 * 
	 * @return boolean true / flase
	 * @param String
	 *            query SQL
	 */
	public boolean dbInsert(String query) {
	//	sqlLog.info("JdbcAdapter insertSql: " + query);
		//log.info("sql = " + query);
		return executeSQL(query);
	}

	/**
	 * ???[???o?b?N
	 */
	public void rollback(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			log.error(e);;
		}
	}

	/**
	 * ?R?~?b?g
	 */
	public void Commit(Connection conn) {
		try {
			conn.commit(); // conn.rollback();
		} catch (SQLException e) {
			log.error(e);;
		}
	}

	/**
	 * ?????????????s
	 */
	synchronized public Vector executeQuery(String query) {
		
		return executeQuery(query, -1, -1);
	}

	/**
	 * ?????????????s
	 */
	private Vector executeQuery(String query, int start, int end) {

		return executeQuery(query, start, end, true);
	}

	/**
	 * ?????????????s
	 */
	
	private Vector executeQuery(String query, int start, int end,
			boolean bUseTrans) {
		Statement stmt = null; // ?X?e?[?g?????g
		ResultSet rset = null; // ???R?[?h?Z?b?g
		Connection conn = null;
		Vector rows = new Vector(); // ?s
		ResultSetMetaData metaData; // ???????????^?f?[?^
		try {
			// ///?J?n?s???s?????L???b?`///////////////////////////////////////
			// ?N?G???[??SELECT?????????????????????????????????s,???????O?????????s/////
			if (query.indexOf("select") != -1 || query.indexOf("SELECT") != -1) {
				conn = getConnection();
				stmt = conn.createStatement();
				rset = stmt.executeQuery(query);
				metaData = rset.getMetaData();
				// ////?J??????int?z?????????E?J?????????X?g?????O?z????????///////
				int numberOfColumns = metaData.getColumnCount();
				this.m_columnNames = new String[numberOfColumns];// ?J?????l?[???W??
				for (int i = 1; i < numberOfColumns + 1; i++) {
					m_columnNames[i - 1] = metaData.getColumnName(i);
//					log.info("Name:"+m_columnNames[i - 1]);
				}
				String[] columnNames = new String[numberOfColumns];// ?J?????l?[???W??
				// ////?_?~?[?J?E???g?????[?v??????////////////////////////////////
				if (start != -1) {
					for (int dmyCount = 0; dmyCount < start; dmyCount++) {
						if (rset.next() == false)
							break;
					}
				}
				// ////?s?x?N?^?[???f?[?^???i?[/////
				int rowCount = 0;
				while (rset.next()) {
					if (end != -1 && end == end) {
						break;
					}
					Vector newRow = new Vector();
					for (int i = 1; i <= columnNames.length; i++) {
						newRow.addElement(rset.getObject(i));
					}
					rows.addElement(newRow);
				}
				// ?X?e?[?g?????g???N???[?Y????//
				rset.close();
				stmt.close();
				close(conn);
			}
		} catch (Exception e) {
			if (conn != null) {
				close(conn);
			}
		//	logger.error(e);
			log.error(e);;
		//	sqlLog.error(e);
		}
		return rows;
	}

	   public void beginTransaction() {
	        try {
	            m_conn.setAutoCommit(false);
	        } catch (Exception e) {
	            log.error(e);;
	        }
	    }
	   
	    public void Commit() {
	        try {
	            m_conn.commit(); // conn.rollback();
	        } catch (SQLException e) {
	            log.error(e);;
	        }
	    }
	/**
	 * ?X?V?n?f?[?^?x?[?X????
	 */
	private boolean executeSQL(String query) {
		Statement stmts = null; // ?X?e?[?g?????g
		Connection conn = null;
		boolean rebool = false;
		try {
			conn = getConnection();
			// ?s????SQL???????????????A???[???o?b?N
			stmts = conn.createStatement();
			if (stmts.executeUpdate(query) > 0) {
				Commit(conn);
				stmts.close();
				rebool = true;
			} else {
				rollback(conn);
				stmts.close();
			}
		}catch(MySQLIntegrityConstraintViolationException e){
			log.info("该条数据已存在！");
		} catch (SQLException sqle) {
			log.error(sqle);
		//	logger.error(LogUtil.getTrace(sqle));
			
		
		} catch (Exception e) {
	//		logger.error("Exception"+e);
			log.error(e);;
			
		} finally {
			stmts = null;
			if (conn != null) {
				close(conn);
			}
		}
		return rebool;
	}

	/**
	 * @return Returns the m_columnNames.
	 */
	public String[] getM_columnNames() {
		return m_columnNames;
	}

	/**
	 * @param names
	 *            The m_columnNames to set.
	 */
	public void setM_columnNames(String[] names) {
		m_columnNames = names;
	}
	
	
	/**
	 * 鏂板缓琛�
	 * @param String query SQ
	 */
	public  void dbCreate(String query) {
	//	sqlLog.info("JdbcAdapter updateSql: " + query);	
			 execute2SQL(query);
	}
	
	/**
	 * 鏂板缓琛ㄧ殑鎵ц鍔ㄤ綔
	 */
	private boolean execute2SQL(String query) {
		Statement stmts = null; 
		Connection conn = null;
		boolean rebool = false;
		try {
			conn = getConnection();
			stmts = conn.createStatement();
			if (stmts.executeUpdate(query) > 0) {
				Commit(conn);
				stmts.close();
				rebool = true;
			} else {
				rollback(conn);
				stmts.close();
			}
		}catch(MySQLSyntaxErrorException e){
//			log.info("表已经存在了！");
		} catch (SQLException sqle) {
			log.error(sqle);
		} catch (Exception e) {
			log.error(e);;
			
		} finally {
			stmts = null;
			if (conn != null) {
				close(conn);
			}
		}
		return rebool;
	}
	
	//鑾峰彇琛ㄥ悕
	public Vector<String>  GetMySqlTables() {
		
		Connection conn = null;
		ResultSet rs = null;
		Vector<Vector<String>> rows = null;
		Vector<String> newRow = new Vector<String>();
		try {
		         conn = getConnection();	         		   
		         java.sql.DatabaseMetaData meta = conn.getMetaData();
		         rs = meta.getTables(null, null, null, new String[]{"TABLE", "VIEW" });
		         while (rs.next()) {
		           String tableOrViewName = rs.getString("TABLE_NAME");
		           
		           
		           String columnName;
		           String columnType;
		        //   Thread.sleep(1000);
		           
		           ResultSet colRet = meta.getColumns(null,"%", tableOrViewName,"%");
		           while(colRet.next()) {
		            columnName = colRet.getString("COLUMN_NAME");
//		            columnType = colRet.getString("TYPE_NAME");
//		            int datasize = colRet.getInt("COLUMN_SIZE");
//		            int digits = colRet.getInt("DECIMAL_DIGITS");
//		            int nullable = colRet.getInt("NULLABLE");  
//		            log.info(columnName+" "+columnType+" "+datasize+" "+digits+" "+
//		              nullable);
		         //   log.info("getTableNames(): tableOrViewName="+tableOrViewName);
		            if(columnName.indexOf("TJRQ") != -1){
		            	
		             newRow.addElement(tableOrViewName);		           
				     log.info("getTableNames(): tableOrViewName="+tableOrViewName);
		            	
		            }
		           }
		         }


		       //  describe ho_ftp_task
		     
		        rs.close();
		        conn.close();

		} catch (Exception e) {
			if (conn != null) {
				close(conn);
			}
			
			log.error(e);;

		}
		return newRow;
	}
	
	//瀵煎嚭琛ㄥ啓鍒版枃浠�
	public boolean BackUpFile(String query) {
		Statement stmts = null; // ?X?e?[?g?????g
		Connection conn = null;
		boolean rebool = false;
		try {
			conn = getConnection();
			// ?s????SQL???????????????A???[???o?b?N
			stmts = conn.createStatement();
			stmts.execute(query);
            
		    rebool = true;
		} catch (SQLException sqle) {
			log.error(sqle);
		//	logger.error(LogUtil.getTrace(sqle));
			
		
		} catch (Exception e) {
	//		logger.error("Exception"+e);
			log.error(e);;
			
		} finally {
			stmts = null;
			if (conn != null) {
				close(conn);
			}
		}
		return rebool;
	}

}