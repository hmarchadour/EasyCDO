package fr.hma.easycdo.perf.process.read;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.view.CDOView;

import fr.hma.easycdo.demo.Root;

public class ReadRootObject extends OpenView {

	public ReadRootObject(CDOSession session, String resourceName) {
		super(session, resourceName);
	}

	@Override
	protected void process(CDOView view, String resourceName) throws Throwable {
		view.getResource(resourceName);
		CDOResource resource = view.getResource(resourceName);
		Root root = (Root) resource.getContents().get(0);
		String id = root.getID();
	}
}
