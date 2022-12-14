package TDADiccionario;

import java.util.Iterator;

import Exceptions.InvalidEntryException;
import Exceptions.InvalidKeyException;
import Exceptions.InvalidPositionException;
import TDALista.*;
import Auxiliares.Entrada;
import Auxiliares.Entry;
import Auxiliares.Position;

/**
 * Clase DiccionarioHashAbierto 
 * implementa un diccionario utilizando una tabla de hash abierta
 * @author Maxi Fernandez - Tomas Arroyo
 *
 * @param <K> representa la clave
 * @param <V> representa el valor
 */
public class DiccionarioHashAbierto<K,V> implements Dictionary<K,V> {
	
	protected PositionList<Entrada<K,V>> [] buckets;
	protected int n; //CANTIDAD DE ENTRADAS
	protected int N; //TAMANIO DEL ARREGLO
	protected static final float factor = 0.5f;
	
	/**
	 * Crea una nueva instancia de DiccionarioHashAbierto vacia
	 */
	@SuppressWarnings("unchecked")
	public DiccionarioHashAbierto() {
		N = 11;
		buckets = (PositionList<Entrada<K,V>> []) new ListaDoblementeEnlazada[N];
		for (int i = 0;i < N;i++)
			buckets[i] = new ListaDoblementeEnlazada<Entrada<K,V>>();
		n = 0;
	}

	@Override	public int size() {
		
		return n;
	}

	@Override
	public boolean isEmpty() {
		
		return n == 0;
	}
	
	/**
	 * Funcion Hash. Computa el valor de la Key
	 * @param key clave a aplicarle hash
	 * @return valor resultante de aplicarle hash a la key
	 * @throws InvalidKeyException si la key es nula
	 */
	private int hashThisKey(K key) throws InvalidKeyException {
		if (key == null) throw new InvalidKeyException("ERROR, UNA CLAVE NULA NO CORRESPONDE");
		return key.hashCode()%N;
	}


	public Entry<K, V> find(K key) throws InvalidKeyException {
		
		if (key == null)
			throw new InvalidKeyException("key invalida");
		
		Entry<K, V> ret = null;
		int clave = hashThisKey(key);
		Iterator<Entrada<K, V>> it = buckets[clave].iterator();
		boolean esta = false;

		Entrada<K, V> act = it.hasNext() ? it.next() : null;
		while (!esta && act != null) {
			if (key.equals(act.getKey())) {
				esta = true;
				ret = act;
			} else {
				act = it.hasNext() ? it.next() : null;
			}
		}
		return ret;
	}

	@Override
	public Iterable<Entry<K, V>> findAll(K key) throws InvalidKeyException {
		
		if (key == null)
			throw new InvalidKeyException("La key recibida es nula");
		
		PositionList<Entry<K,V>> l = new ListaDoblementeEnlazada<Entry<K,V>>();
		
		for (Entrada<K, V> s : buckets[hashThisKey(key)]) {
			if (s.getKey().equals(key)) {
				l.addLast(s);
			}
		}
		return l;
	}

	@Override
	public Entry<K, V> insert(K key, V value) throws InvalidKeyException {
		
		if (n / N >= factor)
			rehash();
		
		PositionList<Entrada<K,V>> l = buckets[hashThisKey(key)];
		Entrada<K,V> newEntry = new Entrada<K,V>(key,value);
		buckets[hashThisKey(key)].addLast(newEntry);
		n++;
		
		return newEntry;
	}

	@Override
	public Entry<K, V> remove(Entry<K, V> e) throws InvalidEntryException {
		
		if (e == null)
			throw new InvalidEntryException("La entrada recibida es nula.");
		
		Entry<K, V> toReturn = null;
		
		try {
				PositionList<Entrada<K, V>> l = buckets[hashThisKey(e.getKey())];
				Position<Entrada<K, V>> cursor = null;
				Iterator<Position<Entrada<K, V>>> it = l.positions().iterator();
		
				while (it.hasNext() && toReturn == null) {
					cursor = it.next();
					if (cursor.element().equals(e)) {
						toReturn = cursor.element();
							l.remove(cursor);
							n--;
						}
					}
			}catch(InvalidKeyException | InvalidPositionException f) { 
				f.printStackTrace();
			}
		
		
		
		if (toReturn == null)
			throw new InvalidEntryException("La entrada no se encuentra en el diccionario");

		return toReturn;
	}

	@Override
	public Iterable<Entry<K, V>> entries() {
		PositionList<Entry<K, V>> it = new ListaDoblementeEnlazada<Entry<K, V>>();
		for (int i = 0; i < N; i++) {
			for (Entry<K, V> en : buckets[i]) {
				it.addLast(en);
			}
		}
		return it;
	}
	
	/**
	 * Redimensionar. crea un nuevo diccionario de mayor tamanio que el original
	 * e inserta en el todas las entradas del diccionario original
	 */
	@SuppressWarnings("unchecked")
	private void rehash() {
		Iterable<Entry<K, V>> entries = entries();
		N = sigPrimo(N * 2);
		buckets = (PositionList<Entrada<K, V>>[]) new ListaDoblementeEnlazada[N];
		n = 0;
		for (int i = 0; i < N; i++)
			buckets[i] = new ListaDoblementeEnlazada<Entrada<K, V>>();
		for (Entry<K, V> e : entries)
			try {
				insert(e.getKey(), e.getValue());
			} catch (InvalidKeyException ex) {
				ex.getMessage();
			}

	}
	
	/**
	 * Consulta si el siguiente de n es primo
	 * @param n numero a consultar
	 * @return retorna el siguiente primo del numero pasado por parametro 
	 * o retorna el numero pasado por parametro en caso de que sea primo
	 */
	private int sigPrimo(int n) {
		boolean es = false;
		n++;
		while (!es) {
			if (esPrimo(n))
				es = true;
			else
				n++;

		}
		return n;
	}
	
	/**
	 * Consulta si el nuemero pasado por parametro es primo
	 * @param n numero a consultar
	 * @return retorna verdadero si el numero pasado por parametro es primo, falso caso contrario
	 */
	private boolean esPrimo(int n) {
		boolean es = false;
		int divisor = 2;
		while (divisor < n && !es) {
			if (n % divisor == 0)
				es = true;
			else
				divisor++;

		}

		return es;
	}

}
