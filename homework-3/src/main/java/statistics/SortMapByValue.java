package statistics;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class SortMapByValue {
	
	/* 
	 * Order
	 * DESC = false, 
	 * ASC = true
	 */
	public static Map<Integer, Integer> sortByValue(Map<Integer, Integer> unsortMap, final boolean order)
    {
        List<Entry<Integer, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }
	
	public void printMap(Map<Integer, Integer> map) {
		map.forEach((key, value) -> System.out.println("(K, V): (" + key + ", " + value + ")"));
	}
	
	public Map<Integer, Integer> getSortedFirstNElement(Map<Integer, Integer> unsortMap, int N) {
		boolean desc = false;
		int count = 0;
		
		Map<Integer, Integer> sortedMap = sortByValue(unsortMap, desc);
		Map<Integer, Integer> finalMap = new LinkedHashMap<>();
		for (Integer k: sortedMap.keySet()) {
			if (count > N) {
				break;
			}
			finalMap.put(k, sortedMap.get(k));
			count++;
		}
		return finalMap;
		
	}

}
