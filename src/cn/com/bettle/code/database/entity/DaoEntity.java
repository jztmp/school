package cn.com.bettle.code.database.entity;

public class DaoEntity {

	private String name;
	
	private boolean sqlMap = false;
	
	private String dbtype;
	
	private String technology;
	
	private String dbid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public boolean isSqlMap() {
		return sqlMap;
	}

	public void setSqlMap(boolean sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	
}
