package fr.hma.easycdo.perf.process.read;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.view.CDOView;

public class ReadResource extends OpenView {

	public ReadResource(CDOSession session, String resourceName) {
		super(session, resourceName);
	}

	@Override
	protected void process(CDOView view, String resourceName) throws Throwable {
		view.getResource(resourceName);
		CDOResource resource = view.getResource(resourceName);
		String name = resource.getName();
	}
}
