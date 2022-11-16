package TDAColaCP;

import java.util.Comparator;

/**
 * Clase Default_Comparator
 * @author Maxi Fernandez - Tomas Arroyo
 *
 * @param <K>
 */
public class Default_Comparator<K extends Comparable<K>> implements Comparator<K>{

	@Override
	public int compare(K a, K b) {
		return a.compareTo(b);
	}
}