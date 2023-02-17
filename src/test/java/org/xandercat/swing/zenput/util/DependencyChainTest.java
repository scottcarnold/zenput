package org.xandercat.swing.zenput.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DependencyChainTest {

	private DependencyChain<String> dc;
	private Set<List<String>> chains;
	
	@BeforeEach
	public void init() {
		dc = new DependencyChain<String>();
	}
	
	@Test
	public void testEmptyChains() {
		chains = dc.getOrderedChains();
		assertNotNull(chains);
		assertEquals(0, chains.size());
	}
	
	@Test
	public void testLongChainWithMerge() {
		dc.add("F", "E");
		dc.add("C", "B");
		dc.add("E", "D");
		dc.add("D", "C");
		dc.add("B", "A");
		chains = dc.getOrderedChains();
		assertNotNull(chains);
		assertEquals(1, chains.size());
		List<String> chain = chains.iterator().next();
		assertArrayEquals(new String[] {"A",  "B", "C", "D", "E", "F"}, chain.toArray());	
	}
	
	@Test
	public void testMultipleChildrenChain() {
		dc.add("F", "A");
		dc.add("E", "A");
		dc.add("D", "A");
		dc.add("C", "A");
		dc.add("B", "A");
		chains = dc.getOrderedChains();
		assertNotNull(chains);
		assertEquals(1, chains.size());
		List<String> chain = chains.iterator().next();
		assertEquals(6, chain.size());
		assertEquals("A", chain.get(0));
	}
	
	@Test
	public void testCyclicDependencyCheck() {
		assertThrows(IllegalStateException.class, () -> {
			dc.add("B", "A");
			dc.add("C", "B");
			dc.add("A", "C");			
		});
	}
	
	@Test
	public void testMultipleChains() {
		dc.add("F", "E");
		dc.add("E", "D");
		dc.add("E2", "D");
		dc.add("C", "B");
		dc.add("B", "A");
		chains = dc.getOrderedChains();
		assertNotNull(chains);
		assertEquals(2, chains.size());
		Iterator<List<String>> iter = chains.iterator();
		List<String> chain1 = iter.next();
		List<String> chain2 = null;
		if (chain1.size() == 4) {
			chain2 = iter.next();
		} else {
			chain2 = chain1;
			chain1 = iter.next();
		}
		assertNotNull(chain1);
		assertNotNull(chain2);
		assertEquals(4, chain1.size());
		assertEquals("D", chain1.get(0));
		int fIdx = chain1.indexOf("F");
		int eIdx = chain1.indexOf("E");
		assertTrue(fIdx > eIdx);
		assertArrayEquals(new String[] {"A", "B", "C"}, chain2.toArray());
	}
}
