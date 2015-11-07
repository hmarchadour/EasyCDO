package fr.hma.easycdo.perf.benchmarks;

import java.util.concurrent.TimeUnit;

import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import fr.hma.easycdo.client.ClientRegistry;
import fr.hma.easycdo.perf.Utils;
import fr.hma.easycdo.server.CDOFeatureDesc;
import fr.hma.easycdo.server.ServerRegistry;

/**
 * Insert 1M without warmup
 */

@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1)
@Warmup(iterations = 2)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
@Threads(value = Threads.MAX)
@OutputTimeUnit(value = TimeUnit.NANOSECONDS)
public class GetBranches {

	private IRepository repository;
	private CDOSession session;

	@Setup(Level.Trial)
	public void setup() {
		repository = ServerRegistry.INSTANCE.createRepository(Utils.H2, new CDOFeatureDesc(true, true));
		session = ClientRegistry.INSTANCE.createSession(Utils.H2.getRepositoryName());
		CDOBranch mainBranch = session.getBranchManager().getMainBranch();
		CDOBranch[] branches = mainBranch.getBranches();
		if (branches.length <= 0) {
			for (int i = 0; i < 10; i++) {
				CDOBranch masterX = mainBranch.createBranch("Master" + i);
				for (int j = 0; j < 10; j++) {
					CDOBranch masterXY = masterX.createBranch("Master" + i + "" + j);
				}
			}
		}
	}

	@TearDown(Level.Trial)
	public void teardown() {
		session.close();
		IPluginContainer.INSTANCE.clearElements();
	}

	@Benchmark
	public void internalBranchManager() throws ConcurrentAccessException, CommitException {
		CDOBranch mainBranch = repository.getBranchManager().getMainBranch();
		String name = mainBranch.getName();
	}

	@Benchmark
	public void internalBranchManager2() throws ConcurrentAccessException, CommitException {
		CDOBranch mainBranch = repository.getBranchManager().getMainBranch();
		CDOBranch[] branchXs = mainBranch.getBranches();
		for (CDOBranch branchX : branchXs) {
			CDOBranch[] branchXYs = branchX.getBranches();
			for (CDOBranch branchXY : branchXYs) {

			}
		}
	}

	@Benchmark
	public void sessionBranchManager() throws ConcurrentAccessException, CommitException {
		CDOBranch mainBranch = session.getBranchManager().getMainBranch();
		String name = mainBranch.getName();
	}

	@Benchmark
	public void sessionBranchManager2() throws ConcurrentAccessException, CommitException {
		CDOBranch mainBranch = session.getBranchManager().getMainBranch();
		CDOBranch[] branchXs = mainBranch.getBranches();
		for (CDOBranch branchX : branchXs) {
			CDOBranch[] branchXYs = branchX.getBranches();
			for (CDOBranch branchXY : branchXYs) {

			}
		}
	}

}
