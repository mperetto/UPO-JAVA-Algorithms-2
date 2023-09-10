package upo.greedy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Greedy {
	
	/** Calcola una codifica di Huffman per i caratteri contenuti nel vettore characters, date le frequenze 
	 * contenute in f. f[i] contiene la frequenza (in percentuale, 0<=f[i]<=100) del carattere characters[i] 
	 * nel testo per il quale si vuole calcolare la codifica.
	 * </br>CONSIGLIO: potete estendere o usare un vostro grafo non pesato non orientato per rappresentare la 
	 * foresta di Huffman.
	 * </br>CONSIGLIO2: potete implementate una PriorityQueue dall'interfaccia in upo.additionalstructures,
	 * oppure aggiungere al grafo del primo consiglio delle priorit�.
	 * 
	 * @param characters i caratteri dell'alfabeto per i quali calcolare la codifica.
	 * @param f le frequenze dei caratteri in characters nel dato testo.
	 * @return una Map che mappa ciascun carattere in una stringa che rappresenta la sua codifica secondo 
	 * l'algoritmo visto a lezione.
	 */
	public static Map<Character,String> getHuffmanCodes(Character[] characters, int[] f) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/** Trova il massimo insieme di intervalli disgiunti, tra tutti quelli identificati da [starting[i], ending[i]],
	 * (0<=i<=starting.length) utilizzando l'algoritmo Greedy. Il risultato contiene gli indici degli intervalli selezionati.
	 * Ad esempio, con starting=[2,5,6] ed ending=[5,7,8] il risultato sar� uguale a [0,2] perch� il massimo insieme
	 * di intervalli disgiunti � {[2,5],[6,8]}.
	 * 
	 * @param starting il vettore dei tempi di inizio degli intervalli
	 * @param ending il vettore dei tempi di fine degli intervalli
	 * @return un vettore contenente gli indici del massimo insieme di intervalli disgiunti
	 */
	public static Integer[] getMaxDisjointIntervals(Integer[] starting, Integer[] ending) {
		
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		// Associo il tempo di fine dell'intervallo alla sua posizione
		// nell'array prima dell'ordinamento
		for (int i = 0; i < ending.length; i++) {
			map.put(ending[i], i);
		}
		
		for (int i = 0; i < ending.length - 1; i++) {
            for (int j = 0; j < ending.length - i - 1; j++) {
                if (ending[j] > ending[j + 1]) {
                    int temp = ending[j];
                    ending[j] = ending[j + 1];
                    ending[j + 1] = temp;

                    temp = starting[j];
                    starting[j] = starting[j + 1];
                    starting[j + 1] = temp;
                }
            }
        }
		
		ArrayList<Integer> sol = new ArrayList<Integer>();
		Integer lastEnding = -1;
		for (int i = 0; i < ending.length; i++) {
			if (starting[i] > lastEnding) {
				lastEnding = ending[i];
				sol.add(map.get(lastEnding));
			}
		}
		
		Integer[] result = new Integer[sol.size()];
		result = sol.toArray(result);
		
		return result;
	}
	
	/** Trova lo scheduling massimale, utilizzando l'algoritmo di Moore, tra i job identificati dai vettori duration e deadline
	 * (duration[i] e deadline[i] sono, rispettivamente, la durata e la scadenza del job L_i). Il risultato contiene, nell'ordine
	 * selezionato dall'algoritmo, gli indici dei job nello scheduling massimale.
	 * 
	 * @param duration il vettore delle durate
	 * @param deadline il vettore delle scadenze
	 * @return un vettore contenente gli indici dei job in uno scheduling massimale
	 */
	public static Integer[] getMooreMaxJobs(Integer[] duration, Integer[] deadline) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}