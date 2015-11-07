package fr.hma.easycdo.perf.process;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.emf.common.util.EList;

import fr.hma.easycdo.client.ClientRegistry;
import fr.hma.easycdo.demo.DemoFactory;
import fr.hma.easycdo.demo.Node;
import fr.hma.easycdo.demo.NodeContainer;
import fr.hma.easycdo.demo.Root;

public class CustomUtils {

	public static final String TEST_RESOURCE_NAME = "/test";

	/**
	 * ^nbNodebyLevel^deep + 1
	 * 
	 * @param deep
	 * @param nbNodebyLevel
	 * @throws ConcurrentAccessException
	 * @throws CommitException
	 */
	public static void initSession(String repositoryName, Root root) throws ConcurrentAccessException, CommitException {
		CDOSession session = ClientRegistry.INSTANCE.createSession(repositoryName);
		CDOTransaction tx = session.openTransaction(session.getBranchManager().getMainBranch());
		CDOResource resource = tx.createResource(TEST_RESOURCE_NAME);
		assert resource.getContents().isEmpty();
		resource.getContents().add(root);
		tx.commit();
		tx.close();
		session.close();
	}

	public static Root populate(int deep, int nbNodebyLevel) {
		Root root = DemoFactory.eINSTANCE.createRoot();
		root.setID("root");
		recusivePopulateNode(root, 0, deep, nbNodebyLevel);
		return root;
	}

	private static void recusivePopulateNode(NodeContainer parent, int level, int deep, int nbNodebyLevel) {
		EList<Node> nodes = parent.getNodes();
		for (int i = 0; i < nbNodebyLevel; i++) {
			Node node = DemoFactory.eINSTANCE.createNode();
			node.setID("level" + level + "_" + i);
			nodes.add(node);
		}
		if (level + 1 < deep) {
			for (Node node : nodes) {
				recusivePopulateNode(node, level + 1, deep, nbNodebyLevel);
			}
		}
	}
}
