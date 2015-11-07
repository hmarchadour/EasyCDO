package fr.hma.easycdo.perf.benchmarks;

import java.util.concurrent.TimeUnit;

import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.cdo.util.ConcurrentAccessException;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import fr.hma.easycdo.client.ClientRegistry;
import fr.hma.easycdo.demo.Root;
import fr.hma.easycdo.perf.Utils;
import fr.hma.easycdo.perf.process.CustomUtils;
import fr.hma.easycdo.perf.process.read.ReadAllObjects;
import fr.hma.easycdo.perf.process.read.ReadAllObjectsWithoutPrefetch;
import fr.hma.easycdo.server.CDOFeatureDesc;
import fr.hma.easycdo.server.ServerRegistry;

/**
 * Insert 1M without warmup
 */
@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
@OutputTimeUnit(value = TimeUnit.SECONDS)
@Fork(jvmArgs = "-Xmx4G")
public class MillionBaby {

	private IRepository repository;
	private CDOSession session;

	@Setup(Level.Trial)
	public void setup() throws ConcurrentAccessException, CommitException {
		repository = ServerRegistry.INSTANCE.createRepository(Utils.H2, new CDOFeatureDesc(true, true));
		session = ClientRegistry.INSTANCE.createSession(Utils.H2.getRepositoryName());
		CDOView view = session.openView(session.getBranchManager().getMainBranch());
		if (!view.hasResource(CustomUtils.TEST_RESOURCE_NAME)) {
			Root root = CustomUtils.populate(3, 100);
			CustomUtils.initSession(Utils.H2.getRepositoryName(), root);
		}
		view.close();
	}

	@TearDown(Level.Trial)
	public void teardown() {
		session.close();
		IPluginContainer.INSTANCE.clearElements();
	}

	@Benchmark
	public void readAllObjectsWithoutPrefetch() throws Exception {
		new ReadAllObjectsWithoutPrefetch(session, CustomUtils.TEST_RESOURCE_NAME).call();
	}

	@Benchmark
	public void readAllObjects() throws Exception {
		new ReadAllObjects(session, CustomUtils.TEST_RESOURCE_NAME).call();
	}

}
