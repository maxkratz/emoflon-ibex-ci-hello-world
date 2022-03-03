package org.emoflon.ci.helloworld.tests.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import helloworld.HelloworldFactory;
import helloworld.World;

/**
 * This class tests the generated code of the class World (defined within the
 * metamodel).
 */
public class WorldTests {

	@Test
	public void testHelloworldFactoryInstanceNotNull() {
		assertNotNull(HelloworldFactory.eINSTANCE);
	}

	@Test
	public void testGetterSetter() {
		final World w = HelloworldFactory.eINSTANCE.createWorld();
		w.setCounter(73);
		assertEquals(73, w.getCounter());
	}

}
