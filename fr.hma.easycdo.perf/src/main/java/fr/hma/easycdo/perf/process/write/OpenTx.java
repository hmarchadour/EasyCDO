package fr.hma.easycdo.perf.process.write;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;

import fr.hma.easycdo.perf.process.AbstractProcess;

public abstract class OpenTx extends AbstractProcess {

	public OpenTx(CDOSession session, String resourceName) {
		super(session, resourceName);
	}

	@Override
	protected void process(CDOSession session, String resourceName) throws Throwable {
		CDOTransaction tx = session.openTransaction(session.getBranchManager().getMainBranch());
		try {
			CDOResource resource;
			if (tx.hasResource(resourceName)) {
				resource = tx.getResource(resourceName);
			} else {
				resource = tx.createResource(resourceName);
			}
			process(tx, resource);
		} finally {
			tx.close();
		}
	}

	protected abstract void process(CDOTransaction tx, CDOResource resource) throws Throwable;
}
