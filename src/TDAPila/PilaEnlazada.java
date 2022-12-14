package TDAPila;

import Auxiliares.Nodo;
import Exceptions.EmptyStackException;

/**
 * Clase PilaEnlaza
 * Modela la interfaz Stack
 * @author Maxi Fernandez - Tomas Arroyo
 *	
 * @param <E> tipo del elemento
 */
public class PilaEnlazada<E> implements Stack<E>{
	private Nodo<E> top;
	private int size = 0;
	
	/**
	 * Crea una instancia de PilaEnlaza vacia
	 */
	public PilaEnlazada() {
		top = null;
		size = 0;
	}
	
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public E top() throws EmptyStackException {
		if (size == 0) {
			throw new EmptyStackException("La Pila esta vacia");
		}
		return top.getElement();
	}

	@Override
	public void push(E element) {
		Nodo<E> n = new Nodo(element, top);
		top = n;
		size ++;
	}

	@Override
	public E pop() throws EmptyStackException {
		if (size == 0) {
			throw new EmptyStackException("La Pila esta vacia");
		}
		Nodo <E> toReturn = top;
		top = top.getNext();
		size--;
		return toReturn.getElement();
	}

}
