/**
 */
package fr.hma.easycdo.demo;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link fr.hma.easycdo.demo.NodeContainer#getNodes <em>Nodes</em>}</li>
 * </ul>
 *
 * @see fr.hma.easycdo.demo.DemoPackage#getNodeContainer()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface NodeContainer extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Nodes</b></em>' containment reference list.
	 * The list contents are of type {@link fr.hma.easycdo.demo.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nodes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nodes</em>' containment reference list.
	 * @see fr.hma.easycdo.demo.DemoPackage#getNodeContainer_Nodes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getNodes();

} // NodeContainer
