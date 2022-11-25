package org.emoflon.ibex.ci.helloworld.gt;

import org.eclipse.emf.common.util.URI;
import org.emoflon.ibex.ci.helloworld.gt.api.GtHiPEApp;

import helloworld.World;

public class HelloworldApp extends GtHiPEApp {
	
	public HelloworldApp(final World world) {
		super(EmoflonGtAppUtils.createTempDir().normalize().toString() + "/");
		EmoflonGtAppUtils.extractFiles(workspacePath);
		if (world.eResource() == null) {
			createModel(URI.createURI("model.xmi"));
			resourceSet.getResources().get(0).getContents().add(world);
		} else {
			resourceSet = world.eResource().getResourceSet();
		}
	}
	
}
