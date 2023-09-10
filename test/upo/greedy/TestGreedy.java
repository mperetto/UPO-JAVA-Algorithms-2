package upo.greedy;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class TestGreedy {

	@Test
	void testGetMaxDisjointIntervals() {
		Integer[] starting = {2, 5, 6};
		Integer[] ending = {5, 7, 8};
		
		Integer[] sol = Greedy.getMaxDisjointIntervals(starting, ending);
		
		Integer[] expected = {0, 2};
        Assert.assertArrayEquals(expected, sol);
        
        starting = new Integer[] {6, 5, 2};
        ending = new Integer[] {8, 7, 5};
        
        sol = Greedy.getMaxDisjointIntervals(starting, ending);
        
        expected = new Integer[] {2, 0};
        Assert.assertArrayEquals(expected, sol);
	}

}
