package fr.hma.easycdo.server;

import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.net4j.util.container.IManagedContainer;

import fr.hma.easycdo.server.internal.ServerRegistryImpl;

public interface ServerRegistry {

	ServerRegistry INSTANCE = ServerRegistryImpl.init();

	IRepository createRepository(DBMSDesc dbmsDesc, CDOFeatureDesc cdoDesc);

	IManagedContainer getContainer();
}
