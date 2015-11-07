package fr.hma.easycdo.perf.process.write;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;

import fr.hma.easycdo.demo.DemoFactory;
import fr.hma.easycdo.demo.Root;

public class CreateARootItem extends OpenTx {

	public CreateARootItem(CDOSession session, String resourceName) {
		super(session, resourceName);
	}

	@Override
	protected void process(CDOTransaction tx, CDOResource resource) throws Throwable {
		Root root = DemoFactory.eINSTANCE.createRoot();
		resource.getContents().add(root);
		tx.commit();
	}
}
