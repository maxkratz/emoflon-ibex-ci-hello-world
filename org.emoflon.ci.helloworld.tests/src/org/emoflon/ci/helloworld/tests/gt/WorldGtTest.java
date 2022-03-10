package org.emoflon.ci.helloworld.tests.gt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.emoflon.ci.helloworld.gt.HelloworldApp;
import org.emoflon.ci.helloworld.gt.api.GtAPI;
import org.emoflon.ci.helloworld.gt.api.matches.AddCountryToWorldMatch;
import org.junit.jupiter.api.Test;

import helloworld.HelloworldFactory;
import helloworld.World;

public class WorldGtTest {

	private List<AddCountryToWorldMatch> matches;
	private World world;
	private GtAPI api;

	private void setUp() {
		matches = new LinkedList<>();
		world = HelloworldFactory.eINSTANCE.createWorld();
		api = new HelloworldApp().initAPI();
		api.addCountryToWorld().subscribeAppearing(matches::add);
	}
	
	@Test
	public void testAddCountryRule() {
		setUp();
		
		// No matches, yet
		assertTrue(matches.isEmpty()); 
 
		// Add a country 
		api.addCountry().apply();
		
		try {
			api.getModel().getResources().get(0).save(null);
		} catch (final IOException e) {
			e.printStackTrace();
		}
 
//		assertFalse(matches.isEmpty());
		assertEquals(1, api.findCountry().countMatches());
	}
	

}
