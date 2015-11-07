package fr.hma.easycdo.server.internal;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.eclipse.emf.cdo.internal.server.Repository;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.server.CDOServerUtil;
import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.emf.cdo.server.db.CDODBUtil;
import org.eclipse.emf.cdo.server.db.mapping.IMappingStrategy;
import org.eclipse.emf.cdo.server.mem.MEMStoreUtil;
import org.eclipse.emf.cdo.server.net4j.CDONet4jServerUtil;
import org.eclipse.emf.cdo.spi.server.InternalStore;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.db.DBUtil;
import org.eclipse.net4j.db.IDBConnectionProvider;
import org.eclipse.net4j.db.h2.H2Adapter;
import org.eclipse.net4j.db.mysql.MYSQLAdapter;
import org.eclipse.net4j.db.postgresql.PostgreSQLAdapter;
import org.eclipse.net4j.jvm.IJVMAcceptor;
import org.eclipse.net4j.jvm.JVMUtil;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.h2.jdbcx.JdbcDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import fr.hma.easycdo.server.CDOFeatureDesc;
import fr.hma.easycdo.server.DBMSDesc;
import fr.hma.easycdo.server.ServerRegistry;

public class ServerRegistryImpl implements ServerRegistry {

	private static ServerRegistry singleton = null;

	public static ServerRegistry init() {
		if (singleton == null) {
			singleton = new ServerRegistryImpl();
		}
		return singleton;
	}

	private IManagedContainer container;

	private IJVMAcceptor jvmAcceptor;

	public ServerRegistryImpl() {
		container = IPluginContainer.INSTANCE;
		Net4jUtil.prepareContainer(container);
		JVMUtil.prepareContainer(container);
		TCPUtil.prepareContainer(container);
		CDONet4jServerUtil.prepareContainer(container);
		CDONet4jUtil.prepareContainer(container);

		jvmAcceptor = JVMUtil.getAcceptor(container, "default");
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		jvmAcceptor.close();
		LifecycleUtil.deactivate(jvmAcceptor);
	}

	public IManagedContainer getContainer() {
		return container;
	}

	public IRepository createRepository(DBMSDesc dbmsDesc, CDOFeatureDesc cdoDesc) {
		IRepository repository = null;
		DataSource ds = null;
		InternalStore store = null;
		switch (dbmsDesc.getDBMS()) {
		case H2_IN_MEMORY:
			store = (InternalStore) MEMStoreUtil.createMEMStore();
			break;
		case H2:
			JdbcDataSource h2ds = new JdbcDataSource();
			h2ds.setUser(dbmsDesc.getDbLogin());
			h2ds.setPassword(dbmsDesc.getDbPwd());
			h2ds.setURL(dbmsDesc.getHost());
			h2ds.setDescription(dbmsDesc.getRepositoryName());
			ds = h2ds;
			break;

		case MYSQL:
			MysqlDataSource mysqlds = new MysqlDataSource();
			mysqlds.setUser(dbmsDesc.getDbLogin());
			mysqlds.setPassword(dbmsDesc.getDbPwd());
			mysqlds.setURL(dbmsDesc.getHost());
			ds = mysqlds;
			break;
		case POSGRESQL:
			PGSimpleDataSource pgDs = new PGSimpleDataSource();
			pgDs.setUser(dbmsDesc.getDbLogin());
			pgDs.setPassword(dbmsDesc.getDbPwd());
			pgDs.setDatabaseName(dbmsDesc.getHost());
			ds = pgDs;
		default:
			break;
		}

		if (ds != null) {
			IDBConnectionProvider dbConnectionProvider = DBUtil.createConnectionProvider(ds);
			IMappingStrategy mappingStrategy = CDODBUtil.createHorizontalMappingStrategy(cdoDesc.isSupportingAudits(),
					cdoDesc.isSupportingBranches());
			switch (dbmsDesc.getDBMS()) {
			case H2:
				H2Adapter h2Adapter = new H2Adapter();
				store = (InternalStore) CDODBUtil.createStore(mappingStrategy, h2Adapter, dbConnectionProvider);
				break;

			case MYSQL:
				MYSQLAdapter mySQLAdapter = new MYSQLAdapter();
				store = (InternalStore) CDODBUtil.createStore(mappingStrategy, mySQLAdapter, dbConnectionProvider);
				break;
			case POSGRESQL:
				PostgreSQLAdapter pgAdapter = new PostgreSQLAdapter();
				store = (InternalStore) CDODBUtil.createStore(mappingStrategy, pgAdapter, dbConnectionProvider);
				break;
			default:
				break;
			}
		}
		if (store != null) {
			// Setup Repository
			Map<String, String> props = new HashMap<String, String>();
			props.put(IRepository.Props.OVERRIDE_UUID, dbmsDesc.getRepositoryName());
			props.put(IRepository.Props.SUPPORTING_AUDITS, String.valueOf(cdoDesc.isSupportingAudits()));
			props.put(IRepository.Props.SUPPORTING_BRANCHES, String.valueOf(cdoDesc.isSupportingBranches()));

			Repository internalRepository = new Repository.Default();
			internalRepository.setName(dbmsDesc.getRepositoryName());
			internalRepository.setStore(store);
			internalRepository.setProperties(props);
			repository = internalRepository;
			CDOServerUtil.addRepository(container, repository);
		}
		return repository;
	}

}
