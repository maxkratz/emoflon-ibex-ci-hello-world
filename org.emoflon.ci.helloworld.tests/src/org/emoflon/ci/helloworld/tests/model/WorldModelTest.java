package org.emoflon.ci.helloworld.tests.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.emoflon.smartemf.runtime.collections.LinkedSmartESet;
import org.junit.jupiter.api.Test;

import helloworld.HelloworldFactory;
import helloworld.World;

/**
 * This class tests the generated code of the class World (defined within the
 * metamodel).
 */
public class WorldModelTest {

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

	@Test
	public void testNoCountries() {
		final World w = HelloworldFactory.eINSTANCE.createWorld();

		// No countries in newly created world
		assertEquals(0, w.getCountries().size());
	}

}
