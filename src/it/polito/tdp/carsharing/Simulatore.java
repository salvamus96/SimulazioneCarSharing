package it.polito.tdp.carsharing;

import java.util.PriorityQueue;

public class Simulatore {
	
// definizione di un enumerazione (raccolta di costanti sotto un nome) di tipi di eventi gestiti dal simulatore
	public enum EventType{
		CUSTOMER_IN, // arriva un nuovo cliente
		CAR_RETURNED // viene restituita un'auto
	
	}

	
	// la classe event deve avere un compareTo per essere inserita in una CODA PRIORITARIA
	public class Event implements Comparable <Event> {
		private int minuti; // minuti a partire dall'inzio della simulazione
		private EventType tipo;
		
		public Event(int minuti, EventType tipo) {
			super();
			this.minuti = minuti;
			this.tipo = tipo;
		}
		
		public int getMinuti() {
			return minuti;
		}
		
		public EventType getTipo() {
			return tipo;
		}

		@Override
		public int compareTo(Event other) {
			return this.minuti - other.minuti;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + minuti;
			result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Event other = (Event) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (minuti != other.minuti)
				return false;
			if (tipo != other.tipo)
				return false;
			return true;
		}

		private Simulatore getOuterType() {
			return Simulatore.this;
		}

		@Override
		public String toString() {
			return "Event [minuti=" + minuti + ", tipo=" + tipo + "]";
		}
		
		
	}
	
	
	// Coda degli eventi
	private PriorityQueue<Event> queue = new PriorityQueue<>();
	
	// Parametri della simulazione, condizioni a contorno secondo cui il simulatore lavora 
	// (impostati all'inizio ma sono costanti durante la simulazione)
	private int NC = 20; // numero di auto disponibili
	private int T_IN = 10; // tempo di arrivo dei clienti (10 min)
	private int T_TRAVEL_BASE = 60; // durata minima del viaggio
	private int T_TRAVEL_DURATA = 3; // quanti periodi di durata base può durare un viaggio
	
	// Modello del mondo: indica lo stato del sistema (queste variabili evolvono in continuazione)
	private int disponibili; // numero di auto disponibili

	// Valori da calcolare (output), non influenzano la simulazione
	private int clientiArrivati; // cleinti arrivati al noleggio
	private int clientiInsoddisfatti; // numero di clienti insoddisfatti
	
	// # 1 INIZIALIZZAZIONE DELLA SIMULAZIONE
	/**
	 * Crea la coda iniziale di eventi ed inizializza correttamente tutte le variabili di simulazione
	 * @param durataMax durata complessiva della simulazione, in minuti
	 */
	public void init(int durataMax) {
		
		// inizializza la coda degli eventi
		this.queue.clear();
		int time = 0;
		while (time <= durataMax) {
			Event e = new Event (time, EventType.CUSTOMER_IN);
			this.queue.add(e);
			time = time + this.T_IN;
		}
		
		// inizializza le variabili di simulazione
		this.disponibili = this.NC;
		this.clientiArrivati = 0;
		this.clientiInsoddisfatti = 0;
	}
	
	// # 2 AVVIO DELLA SIMULAZIONE
	public void run () {
		Event e;
		while((e = this.queue.poll()) != null){
			processEvent(e);
		}
		
	}
	
	private void processEvent(Event e) {
		System.out.println(e);
		
		switch(e.getTipo()) {
		
		case CUSTOMER_IN: 
			this.clientiArrivati ++;
			if (this.disponibili > 0) {
				// cliente soddisfatto
				this.disponibili --;
												// numero intero tra 1 e DURATA troncato nella parte intera
				int durata = T_TRAVEL_BASE * (1 + (int)(Math.random() * T_TRAVEL_DURATA));
				Event rientro = new Event (e.getMinuti() + durata, EventType.CAR_RETURNED);
				this.queue.add(rientro);
			}
			else
				// cliente insoddisfatto
				this.clientiInsoddisfatti ++;
			break;
			
		case CAR_RETURNED:
			this.disponibili ++;
			break;
		
		}
	}

	public int getNC() {
		return NC;
	}
	public void setNC(int nC) {
		NC = nC;
	}
	public int getT_IN() {
		return T_IN;
	}
	public void setT_IN(int t_IN) {
		T_IN = t_IN;
	}
	public int getT_TRAVEL_BASE() {
		return T_TRAVEL_BASE;
	}
	public void setT_TRAVEL_BASE(int t_TRAVEL_BASE) {
		T_TRAVEL_BASE = t_TRAVEL_BASE;
	}
	public int getT_TRAVEL_DURATA() {
		return T_TRAVEL_DURATA;
	}
	public void setT_TRAVEL_DURATA(int t_TRAVEL_DURATA) {
		T_TRAVEL_DURATA = t_TRAVEL_DURATA;
	}
	public int getClientiArrivati() {
		return clientiArrivati;
	}
	public int getClientiInsoddisfatti() {
		return clientiInsoddisfatti;
	}
	
	
	
	
	
}
