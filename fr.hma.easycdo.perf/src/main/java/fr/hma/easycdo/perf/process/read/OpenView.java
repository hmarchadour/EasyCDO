package fr.hma.easycdo.perf.process.read;

import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.view.CDOView;

import fr.hma.easycdo.perf.process.AbstractProcess;

public abstract class OpenView extends AbstractProcess {

	public OpenView(CDOSession session, String resourceName) {
		super(session, resourceName);
	}

	@Override
	protected void process(CDOSession session, String resourceName) throws Throwable {
		CDOView view = session.openView(session.getBranchManager().getMainBranch());
		try {
			process(view, resourceName);
		} finally {
			view.close();
		}
	}

	protected abstract void process(CDOView view, String resourceName) throws Throwable;
}
