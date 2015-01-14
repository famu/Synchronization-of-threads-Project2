import java.util.concurrent.Semaphore;

public class Speaker extends Thread {
	public Speaker() {//constructor
		setName("Speaker ");
	}
			public static Semaphore permitPresentation1 = new Semaphore(1);
			public static Semaphore permitPresentation2 = new Semaphore(1);
			public static Semaphore permitMovie1 = new Semaphore(1);
			public static Semaphore permitMovie2 = new Semaphore(1);
			public static Semaphore permitMuseum = new Semaphore(1);
   /*three of the semaphores have to be acquired by speaker so
    * speaker could let visitors know when presentation ends and
    * when the movie starts.  
    */
	public synchronized void run(){
		try {permitMuseum.acquire();} catch (InterruptedException e) { e.printStackTrace();}//for those visitors who missed session.
		try {permitMovie1.acquire();} catch (InterruptedException e) { e.printStackTrace();}//for 1st session
		try {permitMovie2.acquire();} catch (InterruptedException e) { e.printStackTrace();}//for 2nd session
		msg(" arrives and waits for the show to start");
		try {permitPresentation1.acquire();}//released by Clock 
		catch (InterruptedException e) { e.printStackTrace();}
		msg(" starts presentation before 1st movie session.");
		try {this.sleep(1000);} //Meanwhile visitors watch presentation.
		catch (InterruptedException e) { e.printStackTrace();}
		msg(" ends 1st presentation and lets visitors watch movie.");
		permitMovie1.release();//allows visitors to watch movie.
		try {permitPresentation2.acquire();} catch (InterruptedException e) { e.printStackTrace();}
		msg(" lets the remaining visitors know that there's no movie sessions any more so proceed to visit the Museum.");
		permitMuseum.release();//allows visitors to move on if they missed both sessions.
		msg(" starts presentation before 2nd movie session.");
		try {this.sleep(1000);}//Meanwhile visitors watch presentation. 
		catch (InterruptedException e) { e.printStackTrace();}
		msg(" ends 2nd presentation and watches movie with visitors.");
		permitMovie2.release();//allows visitors to watch movie after he ends presentation.
		try {Clock.endDay.acquire();}//has to wait for the end of the day. 
		catch (InterruptedException e) { e.printStackTrace();}
		msg(" leaves the Museum.");
		Clock.endDay.release();//lets visitors know that day has ended.
	}
	
	//prints message with integrated task of age() function. 
		public void msg(String m) {
			System.out.println("[" + ((System.currentTimeMillis() - Project2.time)) + "]" 
					+ getName() + ":" + m); //getName is built-in one
		}	
}
