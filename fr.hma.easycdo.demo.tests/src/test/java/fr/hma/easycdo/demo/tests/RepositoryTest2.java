package fr.hma.easycdo.demo.tests;

import java.util.Set;

import org.eclipse.emf.cdo.common.id.CDOID.ObjectType;
import org.eclipse.emf.cdo.common.model.CDOPackageInfo;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.hma.easycdo.client.ClientRegistry;
import fr.hma.easycdo.demo.DemoFactory;
import fr.hma.easycdo.demo.Root;
import fr.hma.easycdo.server.CDOFeatureDesc;
import fr.hma.easycdo.server.DBMSDesc;
import fr.hma.easycdo.server.ServerRegistry;

public class RepositoryTest2 {

	private static final String TEST_RESOURCE_NAME = "/test";
	private IRepository repository;
	private CDOSession session;

	@Before
	public void setup() throws ConcurrentAccessException, CommitException {
		repository = ServerRegistry.INSTANCE.createRepository(DBMSDesc.INMEMORY, new CDOFeatureDesc(true, true));
		session = ClientRegistry.INSTANCE.createSession(DBMSDesc.INMEMORY.getRepositoryName());
		CDOPackageInfo[] packageInfos = repository.getPackageRegistry().getPackageInfos();
		CDOTransaction tx = session.openTransaction(session.getBranchManager().getMainBranch());
		CDOResource resource = tx.createResource(TEST_RESOURCE_NAME);
		Root root = DemoFactory.eINSTANCE.createRoot();
		root.setID("root");
		resource.getContents().add(root);
		tx.commit();
		tx.close();
	}

	@After
	public void teardown() {
		session.close();
		IPluginContainer.INSTANCE.clearElements();
	}

	@Test
	public void getEPackage() {
		Set<ObjectType> types = repository.getStore().getObjectIDTypes();
		for (ObjectType type : types) {
			System.out.println(type.getDeclaringClass());
		}
		CDOPackageInfo[] packageInfos = repository.getPackageRegistry().getPackageInfos();
		for (CDOPackageInfo cdoPackageInfo : packageInfos) {
			EPackage ePackage = cdoPackageInfo.getEPackage();
			System.out.println(ePackage.getName());
		}
	}

	@Test
	public void getEPackage2() {
		CDOPackageInfo[] packageInfos = session.getPackageRegistry().getPackageInfos();
		for (CDOPackageInfo cdoPackageInfo : packageInfos) {
			EPackage ePackage = cdoPackageInfo.getEPackage();
			EList<EClassifier> eClassifiers = ePackage.getEClassifiers();
			for (EClassifier eClassifier : eClassifiers) {
				if (eClassifier instanceof EClass) {
					EClass eClass = (EClass) eClassifier;
					System.out.println(eClass.getEIDAttribute());
				}
			}
		}
	}

}
