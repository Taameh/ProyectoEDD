package TDADiccionario;

import java.util.Iterator;

import Exceptions.InvalidEntryException;
import Exceptions.InvalidKeyException;
import Exceptions.InvalidPositionException;
import TDALista.*;

public class DiccionarioHashAbierto<K,V> implements Dictionary<K,V> {
	
	protected PositionList<Entrada<K,V>> [] arregloBuckets;
	protected int n; //CANTIDAD DE ENTRADAS
	protected int N; //TAMANIO DEL ARREGLO
	protected static final float factor = 0.5f;
	
	@SuppressWarnings("unchecked")
	public DiccionarioHashAbierto() {
		N = 11;
		arregloBuckets = (PositionList<Entrada<K,V>> []) new DoublyLinkedList[N];
		for (int i = 0;i < N;i++)
			arregloBuckets[i] = new DoublyLinkedList<Entrada<K,V>>();
		n = 0;
	}

	@Override
	public int size() {
		
		return n;
	}

	@Override
	public boolean isEmpty() {
		
		return n == 0;
	}
	
	private int hashThisKey(K key) throws InvalidKeyException {
		if (key == null) throw new InvalidKeyException("ERROR, UNA CLAVE NULA NO CORRESPONDE");
		return key.hashCode()%N;
	}

	/**
	 * Busca una entrada con clave igual a una clave dada y la devuelve, si no existe retorna nulo.
	 * @param key Clave a buscar.
	 * @return Entrada encontrada.
	 * @throws InvalidKeyException si la clave pasada por par�metro es inv�lida.
	 */
	public Entry<K, V> find(K key) throws InvalidKeyException {
		
		if (key == null)
			throw new InvalidKeyException("key invalida");
		
		Entry<K, V> ret = null;
		int clave = hashThisKey(key);
		Iterator<Entrada<K, V>> it = arregloBuckets[clave].iterator();
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
			throw new InvalidKeyException("Key Invalida");
		
		PositionList<Entry<K,V>> l = new DoublyLinkedList<Entry<K,V>>();
		
		for (Entrada<K, V> s : arregloBuckets[hashThisKey(key)]) {
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
		
		PositionList<Entrada<K,V>> l = arregloBuckets[hashThisKey(key)];
		Entrada<K,V> nueva = new Entrada<K,V>(key,value);
		arregloBuckets[hashThisKey(key)].addLast(nueva);
		n++;
		
		return nueva;
	}

	@Override
	public Entry<K, V> remove(Entry<K, V> e) throws InvalidEntryException {
		
		if (e == null)
			throw new InvalidEntryException("ENTRADA NULA");
		
		Entry<K, V> salida = null;
		
		try {
				PositionList<Entrada<K, V>> l = arregloBuckets[hashThisKey(e.getKey())];
				Position<Entrada<K, V>> cursor = null;
				Iterator<Position<Entrada<K, V>>> it = l.positions().iterator();
		
				while (it.hasNext() && salida == null) {
					cursor = it.next();
					if (cursor.element().equals(e)) {
						salida = cursor.element();
							l.remove(cursor);
							n--;
						}
					}
			}catch(InvalidKeyException | InvalidPositionException f) { 
				f.printStackTrace();
			}
		
		
		
		if (salida == null)
			throw new InvalidEntryException("La entrada no se encuentra en el diccionario");

		return salida;
	}

	@Override
	public Iterable<Entry<K, V>> entries() {
		PositionList<Entry<K, V>> lista = new DoublyLinkedList<Entry<K, V>>();
		for (int i = 0; i < N; i++) {
			for (Entry<K, V> en : arregloBuckets[i]) {
				lista.addLast(en);
			}
		}
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	private void rehash() {
		Iterable<Entry<K, V>> entradas = entries();
		N = proximo_primo(N * 2);
		arregloBuckets = (PositionList<Entrada<K, V>>[]) new DoublyLinkedList[N];
		n = 0;
		for (int i = 0; i < N; i++)
			arregloBuckets[i] = new DoublyLinkedList<Entrada<K, V>>();
		for (Entry<K, V> e : entradas)
			try {
				insert(e.getKey(), e.getValue());
			} catch (InvalidKeyException ex) {
				ex.getMessage();
			}

	}

	private int proximo_primo(int n) {
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