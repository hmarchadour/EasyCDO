package fr.hma.easycdo.perf;

import fr.hma.easycdo.server.DBMSDesc;
import fr.hma.easycdo.server.DBMSDesc.DBMS;

public class Utils {
	public static final DBMSDesc H2 = new DBMSDesc(DBMS.H2_IN_MEMORY, "jdbc:h2:˜/test", -1, null, null, "h2");
}
