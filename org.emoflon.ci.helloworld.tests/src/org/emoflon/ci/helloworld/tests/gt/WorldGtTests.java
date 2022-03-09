package org.emoflon.ci.helloworld.tests.gt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import org.emoflon.ci.helloworld.gt.EmoflonGtHiPEApp;
import org.emoflon.ci.helloworld.gt.api.GtAPI;
import org.emoflon.ci.helloworld.gt.api.matches.AddCountryToWorldMatch;
import org.junit.jupiter.api.Test;

import helloworld.Country;
import helloworld.HelloworldFactory;
import helloworld.World;

public class WorldGtTests {

	private List<AddCountryToWorldMatch> matches;
	private World world;
	private GtAPI api;

	private void setUpModel() {
		matches = new LinkedList<>();
		world = HelloworldFactory.eINSTANCE.createWorld();
		api = new EmoflonGtHiPEApp(world).initAPI();
		api.addCountryToWorld().subscribeAppearing(m -> {
			matches.add(m);
		});
	}

	@Test
	public void testIncrementRule() {
		setUpModel();

		// Prerequisites: No countries in world 
		assertEquals(0, world.getCounter()); 
		assertTrue(world.getCountries().isEmpty()); 
 
		// Add a country 
		final Country c = HelloworldFactory.eINSTANCE.createCountry(); 
		c.setName("germany"); 
		c.setPopulation(99); 
 
		assertFalse(matches.isEmpty()); 
		api.addCountryToWorld().apply(matches.get(0)); 
		assertEquals(1, world.getCounter()); 
		assertEquals(1, world.getCountries().size()); 
		assertTrue(world.getCountries().contains(c)); 
	}
	
}
