package fr.hma.easycdo.perf.process.read;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import fr.hma.easycdo.demo.NamedElement;

public class ReadAllObjects extends OpenView {

	public ReadAllObjects(CDOSession session, String resourceName) {
		super(session, resourceName);
	}

	@Override
	protected void process(CDOView view, String resourceName) throws Throwable {
		CDOResource resource = view.getResource(resourceName);
		resource.cdoPrefetch(CDORevision.DEPTH_INFINITE);
		TreeIterator<EObject> allContents = resource.getAllContents();
		while (allContents.hasNext()) {
			CDOObject eObject = (CDOObject) allContents.next();
			NamedElement namedElement = (NamedElement) resource.getContents().get(0);
			String id = namedElement.getID();
		}
	}
}
