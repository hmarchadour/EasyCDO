package fr.hma.easycdo.server;

public class DBMSDesc {

	public enum DBMS {
		H2_IN_MEMORY, H2, MYSQL, POSGRESQL
	}

	public static DBMSDesc INMEMORY = new DBMSDesc(DBMS.H2_IN_MEMORY, null, -1, null, null, "inmemory");

	private DBMS dbms;
	private String host;
	private int port;
	private String login;
	private String pwd;
	private String repositoryName;

	public DBMSDesc(DBMS dbms, String host, int port, String login, String pwd, String repositoryName) {
		this.dbms = dbms;
		this.host = host;
		this.port = port;
		this.login = login;
		this.pwd = pwd;
		this.repositoryName = repositoryName;
	}

	public DBMS getDBMS() {
		return dbms;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getDbLogin() {
		return login;
	}

	public String getDbPwd() {
		return pwd;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

}
