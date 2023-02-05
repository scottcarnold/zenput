package org.xandercat.swing.zenput.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class takes a series of dependencies and breaks them into one or more ordered 
 * dependency chains.  Dependency chains are ordered from least dependent to most 
 * dependent.  Dependency chains are essentially directed graphs from one or more most 
 * dependent nodes to one or more least dependent nodes.  If a cyclic dependency is 
 * detected, an IllegalStateException is thrown at the time the dependency is added;
 * when this happens, the DependencyChain object will be left in an inconsistent state.
 * 
 * Due to the cross-linked nature of nodes in the chain, the clear() method should always
 * be called when the DependencyChain object is no longer needed.  clear() can also be
 * called in order to reuse the same DependencyChain for a new set of dependencies.
 * 
 * The class used for objects in the chain should implement proper equals and 
 * hashCode methods.
 * 
 * @author Scott Arnold
 *
 * @param <T>	type of object that has the dependency relationship
 */
public class DependencyChain<T> {

	private static final Logger log = LogManager.getLogger(DependencyChain.class);
	
	public static class Link<T> {
		
		private T object;
		private List<Link<T>> parents = new ArrayList<Link<T>>();
		private List<Link<T>> children = new ArrayList<Link<T>>();
		private int order;
		
		public Link(T object) {
			this.object = object;
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Link)) {
				return false;
			}
			return object.equals(((Link<?>) obj).object);
		}
		@Override
		public int hashCode() {
			return object.hashCode();
		}
	}
	
	private List<List<Link<T>>> chains = new ArrayList<List<Link<T>>>();
	private Set<Link<T>> workingSet;
	private Comparator<Link<T>> linkOrderComparator = new Comparator<Link<T>>() {
		@Override
		public int compare(Link<T> o1, Link<T> o2) {
			return o1.order - o2.order;
		}
	};
	
	public void add(T object, List<T> dependsOn) {
		for (T dependOn : dependsOn) {
			add(object, dependOn);
		}
	}
	
	public void add(T object, T dependsOn) {
		Link<T> objectLink = new Link<T>(object);
		Link<T> dependsOnLink = new Link<T>(dependsOn);
		// find and extract any chains that already contain either the object or what it depends on 
		List<List<Link<T>>> mergeChains = new ArrayList<List<Link<T>>>();
		for (Iterator<List<Link<T>>> iter = chains.iterator(); iter.hasNext();) {
			List<Link<T>> chain = iter.next();
			if (chain.contains(objectLink) || chain.contains(dependsOnLink)) {
				mergeChains.add(chain);
				iter.remove();
			}
		}
		List<Link<T>> chain = null;
		if (mergeChains.size() == 0) {
			// create a new chain
			log.debug("Creating a new chain for dependency " + object.toString() + " on " + dependsOn.toString());
			chain = new ArrayList<Link<T>>();
		} else if (mergeChains.size() == 1) {
			// use the discovered chain
			log.debug("Found one chain for dependency " + object.toString() + " on " + dependsOn.toString());
			chain = mergeChains.get(0);
		} else {
			// merge interconnected chains
			log.debug("Merging " + mergeChains.size() + " chains for dependency " + object.toString() + " on " + dependsOn.toString());
			List<Link<T>> mergedChain = new ArrayList<Link<T>>();
			for (List<Link<T>> mergeChain : mergeChains) {
				for (Link<T> mergeLink : mergeChain) {
					if (mergedChain.contains(mergeLink)) {
						Link<T> mergeInto = mergedChain.get(mergedChain.indexOf(mergeLink));
						mergeInto.parents.addAll(mergeLink.parents);
						mergeInto.children.addAll(mergeLink.children);
					} else {
						mergedChain.add(mergeLink);
					}
				}
			}
			chain = mergedChain;
		}
		// add the chain back in to the set of chains
		this.chains.add(chain);
		// replace constructed links with existing links, or if not existing, add them to the chain
		if (chain.contains(objectLink)) {
			objectLink = chain.get(chain.indexOf(objectLink));
		} else {
			chain.add(objectLink);
		}
		if (chain.contains(dependsOnLink)) {
			dependsOnLink = chain.get(chain.indexOf(dependsOnLink)); 
		} else {
			chain.add(dependsOnLink);
		}
		// set up the dependency (if not already set up)
		if (!objectLink.parents.contains(dependsOnLink)) {
			objectLink.parents.add(dependsOnLink);
			dependsOnLink.children.add(objectLink);
		}
		if (cycleExists(objectLink)) {
			throw new IllegalStateException("Cyclic dependency detected.");
		}
	}
	
	private boolean cycleExists(Link<T> from) {
		boolean cycleExists = false;
		Iterator<Link<T>> iter = from.parents.iterator();
		while (!cycleExists && iter.hasNext()) {
			Link<T> to = iter.next();
			cycleExists = cycleExists(from, to);
		}
		return cycleExists;
	}
		
	private boolean cycleExists(Link<T> from, Link<T> to) {
		if (from.equals(to)) {
			return true;
		}
		for (Link<T> nextTo : to.parents) {
			if (cycleExists(from, nextTo)) {
				return true;
			}
		}
		return false;
	}
	
	public Set<List<T>> getOrderedChains() {
		Set<List<T>> orderedChains = new HashSet<List<T>>();
		log.debug("Ordering " + chains.size() + " chain(s)...");
		for (List<Link<T>> chain : chains) {
			orderedChains.add(getOrderedChain(chain));
		}
		return orderedChains;
	}
	
	private List<T> getOrderedChain(List<Link<T>> chain) {
		workingSet = new HashSet<Link<T>>();
		crawlLink(chain.get(0), 0);
		workingSet = null;
		Collections.sort(chain, linkOrderComparator);
		List<T> orderedChain = new ArrayList<T>();
		for (Link<T> link : chain) {
			orderedChain.add(link.object);
		}
		return orderedChain;
	}
	
	private void crawlLink(Link<T> link, int order) {
		if (workingSet.contains(link)) {
			if (order < link.order) {
				link.order = order;
				for (Link<T> parent : link.parents) {
					crawlLink(parent, order-1);
				}
			}
		} else {
			link.order = order;
			workingSet.add(link);
			for (Link<T> parent : link.parents) {
				crawlLink(parent, order-1);
			}
			for (Link<T> child : link.children) {
				crawlLink(child, order+1);
			}
		}
	}
	
	public void clear() {
		for (List<Link<T>> chain : chains) {
			for (Link<T> link : chain) {
				link.children = null;
				link.parents = null;
				link.object = null;
			}
			chain.clear();
		}
		chains.clear();
	}
}
