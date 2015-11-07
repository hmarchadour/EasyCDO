package fr.hma.easycdo.demo.tests;

import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.hma.easycdo.client.ClientRegistry;
import fr.hma.easycdo.server.CDOFeatureDesc;
import fr.hma.easycdo.server.DBMSDesc;
import fr.hma.easycdo.server.ServerRegistry;

public class RepositoryTest {

	private static final String TEST_RESOURCE_NAME = "/test";
	private IRepository repository;
	private CDOSession session;

	@Before
	public void setup() {
		repository = ServerRegistry.INSTANCE.createRepository(DBMSDesc.INMEMORY, new CDOFeatureDesc(true, true));
		session = ClientRegistry.INSTANCE.createSession(DBMSDesc.INMEMORY.getRepositoryName());
	}

	@After
	public void teardown() {
		session.close();
		IPluginContainer.INSTANCE.clearElements();
	}

	@Test
	public void simpleCreate() {
		Object[] elements = repository.getElements();
		for (Object object : elements) {
			System.out.println(object);
		}
		CDOBranch mainBranch = repository.getBranchManager().getMainBranch();
	}

}
