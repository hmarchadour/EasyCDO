package fr.hma.easycdo.server;

public class CDOFeatureDesc {

	private boolean supportingAudits;

	private boolean supportingBranches;

	public CDOFeatureDesc(boolean supportingAudits, boolean supportingBranches) {
		this.supportingAudits = supportingAudits;
		this.supportingBranches = supportingBranches;
	}

	public boolean isSupportingAudits() {
		return supportingAudits;
	}

	public boolean isSupportingBranches() {
		return supportingBranches;
	}

}
