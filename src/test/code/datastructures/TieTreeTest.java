package test.code.datastructures;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.code.datastructure.TieTree;

class TieTreeTest {
	
	List<String> strings = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	TieTree<String> tree;
	
	@BeforeEach
	void setUp() throws Exception {
		tree = new TieTree<String>();
		map.put("map", "map_mapped_value");
		map.put("arag1", "arag1_mapped_value");
		strings.add("");
		strings.add(" ");	
		strings.add("AA");	
		strings.add("AaA");
		strings.add("B_1");
		strings.add("AbA");
		strings.add("Around");
		strings.add("Best");
		strings.add("arag");
		strings.add("araga");
		strings.add("Directory");
		strings.add("Direct");
		strings.add("directer");
		strings.add("Directive");
		strings.add("ZA111111");
		strings.add("ZB111111");
		strings.add("ZAA111111");
		strings.add("ZCA111111");
		strings.add("GEZELLIG\\1");
		strings.add("GEZELLIG\\2");
		strings.add("GEZELLIG\\3");
		strings.add("GEZELLIG\\THUIS\\1");
		strings.add("GEZELLIG\\THUIS\\2");
		strings.add("GEZELLIG\\THUIS\\3");
		tree.addEntries(strings);
		tree.addEntries(map);
	}

	@Test
	@DisplayName("Finding true prefix, where the prefix is shorter than any key")
	void test1() {	
		String prefix = "Dir";		
		assertEquals(true, tree.findPrefix(prefix));
	}
	
	@Test
	@DisplayName("Verifying whether the object (String) retrieved equals the object (String) stored")
	void map2() {		
		String prefix = "Best";		
		assertEquals(true, tree.findPrefix(prefix));
		assertEquals(null,tree.getObject(prefix));		
	}
	
	@Test
	@DisplayName("Verifying retrieval if the key does not lead to an attached object")
	void map3() {		
		String prefix = "Best";		
		assertEquals(true, tree.findPrefix(prefix));
		assertEquals(null,tree.getObject(prefix));		
	}
	
	@Test
	@DisplayName("Verifying retrieval if a not existant key is used (key is a prefix of an entered key)")
	void map4() {		
		String prefix = "G";		
		assertEquals(true, tree.findPrefix(prefix));
		assertEquals(null,tree.getObject(prefix));	
	}
	
	@Test
	@DisplayName("Verifying retrieval if a not existant key is used (key is not a prefix of an entered key)")
	void map() {		
		String prefix = "AAA";		
		assertEquals(false, tree.findPrefix(prefix));
		assertEquals(null,tree.getObject(prefix));	
	}
	
	@Test
	@DisplayName("Finding true prefix and counting # of returned prefixed strings")
	void test4() {
		String prefix = "Dir";		
		assertEquals(3, tree.getAllStrings(prefix).size());
	}
	
	@Test
	@DisplayName("Finding # true prefix after additional/seperate add")
	void test9() {
		List<String> additionalStrings = new ArrayList<String>();
		additionalStrings.add("Dirham");
		tree.addEntries(additionalStrings);
		String prefix = "Dir";		
		assertEquals(4, tree.getAllStrings(prefix).size());
	}
	
	@Test
	@DisplayName("Finding a prefix when the prefix is a complete key")
	void test2() {
		String prefix = "arag";		
		assertEquals(true, tree.findPrefix(prefix));
	}
	
	@Test
	@DisplayName("Finding a prefix when the prefix is a complete key, where the key used itself has a previous key as it's prefix")
	void test12() {
		String prefix = "araga";		
		assertEquals(true, tree.findPrefix(prefix));
	}
	
	@Test
	@DisplayName("Finding a prefix when the prefix is a complete key, counting the # of returned values")
	void test5() {
		String prefix = "arag";		
		assertEquals(3, tree.getAllStrings(prefix).size());
	}
	
	@Test
	@DisplayName("Not finding a prefix when the prefix is not added to the tree")
	void test3() {
		String prefix = "Arag";		
		assertEquals(false, tree.findPrefix(prefix));
	}
	
	@Test
	@DisplayName("Not finding a prefix when the prefix is not added to the tree, checking the # of finds")
	void test6() {
		String prefix = "Arag";		
		assertEquals(0, tree.getAllStrings(prefix).size());
	}
	
	@Test
	@DisplayName("Verifying if the returned prefixes keys are exactly the same as the prefixed keys entered into the tree")
	void test91() {
		String prefix = "Z";
		assertTrue(tree.getAllStrings(prefix).contains("ZB111111"));
		assertTrue(tree.getAllStrings(prefix).contains("ZA111111"));
		assertTrue(tree.getAllStrings(prefix).contains("ZCA111111"));
		assertTrue(tree.getAllStrings(prefix).contains("ZAA111111"));
		assertEquals(4,tree.getAllStrings(prefix).size());
	}
	
	@Test
	@DisplayName("Not finding a prefix when the prefix is not added to the tree, checking the # of finds")
	void test111() {
		String prefix = "";	
		assertEquals(25, tree.getAllStrings(prefix).size());
	}
	
	
	@Test
	@DisplayName("Randomly created N strings of varying byte length and add these to the tree.")
	void test1111() {
		int timesToRun = 1000;
		List<String> randomlyAddedStrings = new ArrayList<String>();
		String abc = "abcdefghijklmnopqrstuvwxyz1234567890-+//!@#$%^&*()\\";
		Random random = new Random();
		for(int g = 0;g<timesToRun;g++) {
			StringBuilder sb = new StringBuilder();
			int count = random.nextInt(500);
		    for (int i = 0; i < count ; i++) {
		        sb.append(abc.charAt(random.nextInt(abc.length())));
		    }		  
		    randomlyAddedStrings.add(sb.toString());
		}
		tree.addEntries(randomlyAddedStrings);
		assertTrue(25<=tree.getAllStrings("").size());	
		assertTrue(25+timesToRun>=tree.getAllStrings("").size());
	}	
	
}
