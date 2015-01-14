import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Clock extends Thread{
	public Clock() {
		setName("Clock");
	}
			public static Semaphore endDay = new Semaphore(1);
			public static Semaphore lock3 = new Semaphore(1); //
			public static Semaphore permitSession1 = new Semaphore(1);
			public static Semaphore permitSession2 = new Semaphore(1);
			public static Semaphore permitMuseum1 = new Semaphore(1);
			public static Semaphore permitMuseum2 = new Semaphore(1);
		/*Initially, It is very important for Clock to acquire certain semaphores,
		 *so Clock can release them at the right time to ensure required course
		 *of activities. 
		 * */ 
		public synchronized void run() {//acquiring of semaphores starts in the beginning. 
			try {Visitors.permitSeat1.acquire();} catch (InterruptedException e) { e.printStackTrace();}
			try {Speaker.permitPresentation1.acquire();} catch (InterruptedException e) { e.printStackTrace();}
			try {Visitors.permitSeat2.acquire();} catch (InterruptedException e) { e.printStackTrace();}
			try {Speaker.permitPresentation2.acquire();} catch (InterruptedException e) { e.printStackTrace();}
			try {permitMuseum1.acquire();} catch (InterruptedException e) { e.printStackTrace();}
			try {permitMuseum2.acquire();} catch (InterruptedException e) { e.printStackTrace();}
			try {endDay.acquire();} catch (InterruptedException e) { e.printStackTrace();}
			//above are the semphores that have to be acquired by Clock.  		
			try {this.sleep(1500);} catch (InterruptedException e) { e.printStackTrace();} //mean while all visitors can come into the lobby.
			msg(" sets session 1 and notifies all visitors to enter the room.");
			Visitors.permitSeat1.release(); //allowing visitors to take seats.
			try {this.sleep(2000);} catch (InterruptedException e) { e.printStackTrace();} //Mean while visitors can take available seats.
			Speaker.permitPresentation1.release();//allowing speaker to start his presentation.
			try {this.sleep(3000);} //Mean while visitors can listen to speaker and watching movie
			catch (InterruptedException e) { e.printStackTrace();} 
			msg(" ends session one, announces break and notifies visitors to proceed visiting the Museum.");
			permitMuseum1.release();//allowing visitors to visit museum.
			//break time
			try {this.sleep(1000);} catch (InterruptedException e) { e.printStackTrace();}//
			//break time ends
			msg(" sets session 2 and notifies all remaining visitors to enter the room.");
			Visitors.theater_capacity=6; //restoring theater capacity
			Visitors.permitSeat2.release();//allowing visitors to take seat for 2nd session.
			try {this.sleep(2000);} //Mean while visitors can take available seats
			catch (InterruptedException e) { e.printStackTrace();} 
			Speaker.permitPresentation2.release();//allows speaker to start presentation.
			try {this.sleep(3000);} catch (InterruptedException e) { e.printStackTrace();} //Mean while visitors are watching movie
			msg(" ends session 2 and and notifies visitors to proceed visiting.");
			permitMuseum2.release();//allowing next 6 visitors to enter museum.
			try {this.sleep(3000);} //Mean while visitors can visit Museum
			catch (InterruptedException e) { e.printStackTrace();} 			
			msg(" signals the end of the day.");
			endDay.release();//signals the end of the day
			try {this.sleep(4000);} //Mean while visitors can gather and leave the museum.
			catch (InterruptedException e) { e.printStackTrace();}
			msg(" Museum closed.");
		}
		
//prints time of activity along with the supplied message.
public void msg(String m) {
	System.out.println("[" + ((System.currentTimeMillis() - Project2.time)) + "]" 
						+ getName() + ":" + m);
	}
}
