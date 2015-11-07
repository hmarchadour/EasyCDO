package fr.hma.easycdo.demo.utils;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;

import fr.hma.easycdo.client.ClientRegistry;
import fr.hma.easycdo.demo.DemoFactory;
import fr.hma.easycdo.demo.Node;
import fr.hma.easycdo.demo.NodeContainer;
import fr.hma.easycdo.demo.Root;
import fr.hma.easycdo.server.CDOFeatureDesc;
import fr.hma.easycdo.server.DBMSDesc;
import fr.hma.easycdo.server.ServerRegistry;

public class CustomUtils {

	public static final String TEST_RESOURCE_NAME = "/test";

	/**
	 * ^nbNodebyLevel^deep + 1
	 * 
	 * @param deep
	 * @param nbNodebyLevel
	 * @return
	 * @throws ConcurrentAccessException
	 * @throws CommitException
	 */
	public static CDOSession initSession(int deep, int nbNodebyLevel)
			throws ConcurrentAccessException, CommitException {
		ServerRegistry.INSTANCE.createRepository(DBMSDesc.INMEMORY, new CDOFeatureDesc(true, true));
		CDOSession session = ClientRegistry.INSTANCE.createSession(DBMSDesc.INMEMORY.getRepositoryName());
		CDOTransaction tx = session.openTransaction(session.getBranchManager().getMainBranch());
		CDOResource resource = tx.createResource(TEST_RESOURCE_NAME);
		Root root = DemoFactory.eINSTANCE.createRoot();
		root.setID("root");
		populateRoot(root, deep, nbNodebyLevel);
		resource.getContents().add(root);
		tx.commit();
		tx.close();
		return session;
	}

	private static void populateRoot(Root root, int deep, int nbNodebyLevel) {
		recusivePopulateNode(root, 0, deep, nbNodebyLevel);
	}

	private static void recusivePopulateNode(NodeContainer parent, int level, int deep, int nbNodebyLevel) {
		if (level < deep) {
			for (int i = 0; i < nbNodebyLevel; i++) {
				Node node = DemoFactory.eINSTANCE.createNode();
				parent.getNodes().add(node);
				node.setID("level" + level + "_" + i);
				recusivePopulateNode(node, level + 1, deep, nbNodebyLevel);
			}
		}
	}
}
