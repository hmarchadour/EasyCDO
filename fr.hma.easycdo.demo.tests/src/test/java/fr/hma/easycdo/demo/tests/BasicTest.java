package fr.hma.easycdo.demo.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.eclipse.emf.cdo.CDOState;
import org.eclipse.emf.cdo.common.commit.CDOCommitInfo;
import org.eclipse.emf.cdo.common.commit.CDOCommitInfoHandler;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.hma.easycdo.client.ClientRegistry;
import fr.hma.easycdo.server.CDOFeatureDesc;
import fr.hma.easycdo.server.DBMSDesc;
import fr.hma.easycdo.server.ServerRegistry;

public class BasicTest {

	private static final String TEST_RESOURCE_NAME = "/test";
	private IRepository repository;
	private CDOSession session;

	@Before
	public void setup() {
		repository = ServerRegistry.INSTANCE.createRepository(DBMSDesc.INMEMORY, new CDOFeatureDesc(true, true));
		session = ClientRegistry.INSTANCE.createSession(DBMSDesc.INMEMORY.getRepositoryName());
		IListener listener = new IListener() {

			@Override
			public void notifyEvent(IEvent event) {
				System.out.println(event);
			}
		};
		repository.addListener(listener);
		CDOCommitInfoHandler handler = new CDOCommitInfoHandler() {

			@Override
			public void handleCommitInfo(CDOCommitInfo commitInfo) {
				System.out.println(commitInfo);
			}
		};
		repository.addCommitInfoHandler(handler);
	}

	@After
	public void teardown() {
		session.close();
	}

	@Test
	public void simpleCreate() {
		assertNotNull(repository);
		assertNotNull(session);
	}

	@Test
	public void createResource() throws ConcurrentAccessException, CommitException {
		CDOTransaction tx = session.openTransaction(session.getBranchManager().getMainBranch());
		assertFalse(tx.hasResource(TEST_RESOURCE_NAME));
		CDOResource resource = tx.createResource(TEST_RESOURCE_NAME);
		assertEquals(resource.cdoState(), CDOState.NEW);
		tx.commit();
		assertEquals(resource.cdoState(), CDOState.CLEAN);
		tx.close();
	}

	@Test
	public void createThenModifyResource() throws ConcurrentAccessException, CommitException {
		CDOTransaction tx = session.openTransaction(session.getBranchManager().getMainBranch());
		assertFalse(tx.hasResource(TEST_RESOURCE_NAME));
		CDOResource resource = tx.createResource(TEST_RESOURCE_NAME);
		tx.commit();
		resource.setName(TEST_RESOURCE_NAME);
		assertEquals(resource.cdoState(), CDOState.DIRTY);
		tx.rollback();
		assertEquals(resource.cdoState(), CDOState.CLEAN);
		tx.close();
	}

}
