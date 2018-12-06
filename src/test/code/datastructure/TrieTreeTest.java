package test.code.datastructure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.code.datastructure.TieTree;



class TieTreeTest {
	
	List<String> strings = new ArrayList<String>();
	
	@BeforeEach
	void setUp() {
		strings.add("AA");	
		strings.add("AaA");
		strings.add("B_1");
		strings.add("AbA");
		strings.add("Around");
		strings.add("Best");
		strings.add("arag");
		strings.add("Directory");
		strings.add("Direct");
		strings.add("directer");
		strings.add("Directive");
	}

	@Test
	@DisplayName("Test prefix finding, for true prefix")
	void test1() {
		TieTree tree = new TieTree();		
		tree.addStrings(strings);
		String prefix = "Dir";		
		assertEquals(true, tree.findPrefix(prefix.toCharArray()));
	}
	
	@Test
	@DisplayName("Test matching string finding, for true prefix")
	void test4() {
		TieTree tree = new TieTree();		
		tree.addStrings(strings);
		String prefix = "Dir";		
		assertEquals(3, tree.getAllStrings(prefix.toCharArray()).size());
	}
	
	@Test
	@DisplayName("Adding another string and then finding, for true prefix")
	void test9() {
		TieTree tree = new TieTree();		
		tree.addStrings(strings);
		List<String> additionalStrings = new ArrayList<String>();
		additionalStrings.add("Dirham");
		tree.addStrings(additionalStrings);
		String prefix = "Dir";		
		assertEquals(4, tree.getAllStrings(prefix.toCharArray()).size());
	}
	
	@Test
	@DisplayName("Test prefix finding, for complete word")
	void test2() {
		TieTree tree = new TieTree();		
		tree.addStrings(strings);
		String prefix = "arag";		
		assertEquals(true, tree.findPrefix(prefix.toCharArray()));
	}
	
	@Test
	@DisplayName("Test matching string finding, for complete word")
	void test5() {
		TieTree tree = new TieTree();		
		tree.addStrings(strings);
		String prefix = "arag";		
		assertEquals(1, tree.getAllStrings(prefix.toCharArray()).size());
	}
	
	@Test
	@DisplayName("Test prefix finding,  for not existing prefix")
	void test3() {
		TieTree tree = new TieTree();		
		tree.addStrings(strings);
		String prefix = "Arag";		
		assertEquals(false, tree.findPrefix(prefix.toCharArray()));
	}
	
	@Test
	@DisplayName("Test matching string finding, for not existing prefix")
	void test6() {
		TieTree tree = new TieTree();		
		tree.addStrings(strings);
		String prefix = "Arag";		
		assertEquals(0, tree.getAllStrings(prefix.toCharArray()).size());
	}

}
