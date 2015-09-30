
//Description: this is the AnagramSolver Class that uses a dictionary
//to find all combination of words that have the same letters as a given
//phrase by the user.
//
import java.util.*;

public class AnagramSolver {
	private Map<String, LetterInventory> dictionary;
	private List<String> alterList;

	//pre: assume the dictionary is a nonempty collection of nonempty sequences of 
	//letters and contains no duplicate words
	//post: construct an anagram solver with the given dictionary
	public AnagramSolver(List<String> list) {
		dictionary = new HashMap<String, LetterInventory>();
		for (String word : list) {
			dictionary.put(word, new LetterInventory(word));
		}
		alterList = list;
	}

	//pre: max is equal or greater than zero(throw IllegalArgumentException if not)
	//post: prints out all combinations of words from the dictionary that are 
	//anagrams of s. and include at most max number of words. if max = 0 then unlimited 
	//number of words
	public void print(String s, int max) {
		if (max < 0) {
			throw new IllegalArgumentException();
		}
		LetterInventory userInput = new LetterInventory(s); 
		List<String> reviseDictionary = new ArrayList<String>();
		for (String word: alterList) {
			if (userInput.subtract(dictionary.get(word)) != null) { 
				reviseDictionary.add(word);
			}
		}
		List<String> word = new ArrayList<String>();
		explore (userInput,reviseDictionary, max, word); 
	}
	
	//post: trys all combination of words from the dictionary by using the steps of 
	//choose, explore, unchoose
	private void explore (LetterInventory userInput, List<String> reviseDictionary, 
								 int max, List<String> word ) {
		if (userInput.isEmpty()) {
			System.out.println(word);
		} else if (word.size() != max || max == 0) {
			for (String value: reviseDictionary) {
				if (userInput.subtract(dictionary.get(value)) != null) {
					userInput = userInput.subtract(dictionary.get(value));
					word.add(value);
					explore(userInput, reviseDictionary, max, word);
					word.remove(word.size() - 1);
					userInput = userInput.add(dictionary.get(value));
				}
			}
		}
	}
}
