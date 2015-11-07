package fr.hma.easycdo.client.internal;

import org.eclipse.emf.cdo.net4j.CDONet4jSession;
import org.eclipse.emf.cdo.net4j.CDONet4jSessionConfiguration;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.server.net4j.CDONet4jServerUtil;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.jvm.IJVMConnector;
import org.eclipse.net4j.jvm.JVMUtil;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;

import fr.hma.easycdo.client.ClientRegistry;

public class ClientRegistryImpl implements ClientRegistry {

	private static ClientRegistry singleton = null;

	public static ClientRegistry init() {
		if (singleton == null) {
			singleton = new ClientRegistryImpl();
		}
		return singleton;
	}

	private IManagedContainer container;

	private IJVMConnector jvmConnector;

	public ClientRegistryImpl() {
		container = IPluginContainer.INSTANCE;
		Net4jUtil.prepareContainer(container);
		JVMUtil.prepareContainer(container);
		TCPUtil.prepareContainer(container);
		CDONet4jServerUtil.prepareContainer(container);
		CDONet4jUtil.prepareContainer(container);

		jvmConnector = JVMUtil.getConnector(container, "default");
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		jvmConnector.close();
		LifecycleUtil.deactivate(jvmConnector);

	}

	@Override
	public CDOSession createSession(String repositoryName) {
		return genericCreateSession(repositoryName);
	}

	protected CDOSession genericCreateSession(String repositoryName) {
		CDONet4jSessionConfiguration configuration = CDONet4jUtil.createNet4jSessionConfiguration();
		configuration.setConnector(jvmConnector);
		configuration.setRepositoryName(repositoryName);
		configuration.setSignalTimeout(-1);
		CDONet4jSession session = configuration.openNet4jSession();
		session.options().getNet4jProtocol().setTimeout(-1);
		return session;

	}

}
