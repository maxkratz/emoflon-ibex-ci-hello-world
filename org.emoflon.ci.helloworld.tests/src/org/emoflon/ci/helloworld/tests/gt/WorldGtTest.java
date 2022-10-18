package org.emoflon.ci.helloworld.tests.gt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.emoflon.ci.helloworld.gt.HelloworldApp;
import org.emoflon.ci.helloworld.gt.api.GtAPI;
import org.emoflon.ci.helloworld.gt.api.matches.AddCountryToWorldMatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import helloworld.HelloworldFactory;
import helloworld.World;

public class WorldGtTest {

	private List<AddCountryToWorldMatch> matches;
	private World world;
	private GtAPI api;

	@BeforeEach
	public void setUp() {
		matches = new LinkedList<>();
		world = HelloworldFactory.eINSTANCE.createWorld();
		api = new HelloworldApp(world).initAPI();
		api.addCountryToWorld().subscribeAppearing(matches::add);
	}

	@AfterEach
	public void tearDown() {
		api.terminate();
		matches.clear();
		world = null;
	}

	@Test
	public void testAddCountryRule() {
		// No matches, yet
		assertFalse(api.findCountry().hasMatches());

		// Add a country
		api.addCountry().apply();

		// Must find one match
		assertEquals(1, api.findCountry().countMatches());
	}

	@Test
	public void testAddCountryToWorldRule() {
		// No matches, yet
		assertTrue(matches.isEmpty());
		assertFalse(api.addCountryToWorld().hasMatches());

		// Add Germany
		api.addGermany().apply();

		// Must find one match
		assertTrue(api.addCountryToWorld().hasMatches());
		assertFalse(matches.isEmpty());

		// Apply match
		api.addCountryToWorld().apply(matches.get(0));

		// Check world
		assertFalse(world.getCountries().isEmpty());
		assertEquals(1, world.getCounter());
		assertEquals("germany", world.getCountries().get(0).getName());
		assertEquals(42, world.getCountries().get(0).getPopulation());
	}

}
