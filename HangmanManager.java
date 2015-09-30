
//Description: This is the HangmanManager Class that keeps track of the state 
//of the game of hangman.
//
import java.util.*;

public class HangmanManager {
	private Set<String> currentSet;//current list of words from the dictionary
	private SortedSet<Character> character;//current list of alphabetic characters guessed
	private int currentGuess;//current number of guesses left
	private String pattern;// current pattern of the word
	
	//pre:length of word is greater than 0, max is greater or equal to 0
	//(throw IllegalArgumentException if not)
	//post:uses the parameter values to initialize the state of the game. 
	//current list of words should be initially be all words from the dictionary file
	//that are of the given length. initialize the pattern with the given length
	//and initialize all all characters with "-"
	public HangmanManager(List<String> dictionary, int length, int max) {
		if (length < 1 || max < 0) {
			throw new IllegalArgumentException();
		} 
		character = new TreeSet<Character>();
		currentSet = new TreeSet<String>();
		currentGuess = max;
		for (String s : dictionary) {
			if (s.length() == length) {
				currentSet.add(s);
			}
		}
		pattern = "-";
		for (int i = 0; i < length - 1; i++) {
			pattern += " -";
		}
	}
	
	//post: return the current list of words being considered from the dictionary. 
	public Set<String> words() {
		return currentSet;
	}
	
	//post: return the current number of guesses the player has left.
	public int guessesLeft() {
		return currentGuess;
	}
	
	//post: return the current list of characters that the player has already guessed.
	public SortedSet <Character> guesses() {
		return character;
	}
	
	//pre:current list of word being considered is not empty
	//(throw IllegalStateException if not)
	//post:returns the current pattern to be displayed for the game, into account
	//guesses that have been made. letters that have not been guessed are displayed 
	//as a dash with space seperating the letter. there is no leading or trailing spaces
	public String pattern() {
		if (currentSet.isEmpty()) {
			throw new IllegalStateException();
		}
		return pattern;
	}
	
	//pre:current number of guesses must be greater than zero, current list of word 
	//being considered is not empty (throw IllegalStateException if not)
	//current list of word being considered is empty, characters guessed has already 
	//been guessed (throw IllegalArgumentException if not)
	//post:records the next guess made by the user. chooses the set of words to use 
	//going forward. returns the number of occurrences of the guessed letter in the 
	//new pattern, and appropriately update the number of current guesses left. 
	public int record(char guess) {
		if (currentGuess < 1 || currentSet.isEmpty()) {
			throw new IllegalStateException();
		}
		if (!currentSet.isEmpty() && character.contains(guess)) {
			throw new IllegalArgumentException();
		} 
		character.add(guess);
		sortMap();
		int count = 0;
		for (int i = 0; i < pattern.length(); i++) {
			if (pattern.charAt(i) == guess) {
				count++;
			}
		}
		if (count == 0) {
			currentGuess -= 1;
		}
		return count;	
	}
	
	//post:sorts the word from the current list of words considered, into multiple 
	//lists of patterns where the guessed character occur at each index 
	private void sortMap() {
		Map<String, Set<String>> sort = new TreeMap<String, Set<String>>();
		for (String word : currentSet) {
			String keys = "";
			for (int i = 0; i < word.length(); i++) {
				if(character.contains(word.charAt(i))) {
					keys += word.charAt(i) + " ";	
				} else {
					keys += "- ";
				}
			}
			keys = keys.substring(0, keys.length() - 1);
			if (!sort.containsKey(keys)) {
				sort.put(keys, new TreeSet<String>());
			}
			sort.get(keys).add(word);
		}
		sortKey(sort); 
	}
	
	//post:from all the sorted words, picks the pattern with the most
	//words to choose from going forward.
	private void sortKey(Map<String, Set<String>> sort) {
		String key1 = "";
		int max = 0;
		for (String set: sort.keySet()) {
			if(max < sort.get(set).size()) {
				max = sort.get(set).size();
				key1 = set;
			} 
		}
		pattern = key1;
		currentSet = sort.get(key1);
	}
}
