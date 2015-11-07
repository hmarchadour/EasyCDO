package fr.hma.easycdo.client;

import org.eclipse.emf.cdo.session.CDOSession;

import fr.hma.easycdo.client.internal.ClientRegistryImpl;

public interface ClientRegistry {
	ClientRegistry INSTANCE = ClientRegistryImpl.init();

	CDOSession createSession(String repositoryName);
}
