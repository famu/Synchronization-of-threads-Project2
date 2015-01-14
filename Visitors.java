import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.naming.BinaryRefAddr;

public class Visitors extends Thread {
	public Visitors(int i) {//constructor
		vID=i;
		setName("Visitor-" + i);
	}
	Random rand = new Random();
	public static int[] arrivalTimes = new int[15];
	public static int theater_capacity=6;
	public boolean gotSeat = false;
	private static int group_size = 15;
	private static int visitor_counter = 0;// used for sub groups
	private static int sub_group_counter = 1; // 
	public int vID;
	/*It is neccessary to keep in mind that all semaphores are 
	 * initially acquired by clock which releases at the right
	 * time.
	 */
	public static Semaphore permitSeat1 = new Semaphore(1);	   //for first session.
	public static Semaphore permitSeat2 = new Semaphore(1);	   //for 2nd session.

	public synchronized void run() {
		try {this.sleep(4);}//so that Clock can acquire neccessary semaphores.
		catch (InterruptedException e) { e.printStackTrace();}
		msg("arrives Ellis Island and waits in the lobby.");
		try {permitSeat1.acquire();}//initially acquied by clock but also ensures mutual exclusion 
		catch (InterruptedException e) { e.printStackTrace();} 
			if(theater_capacity>0){
				theater_capacity-=1;	
				gotSeat= true;
					
			}else{
				gotSeat= false;		
			}	
		if(!gotSeat){
			permitSeat1.release(); //because no need to wait anymore for the first session
			msg(" couldn't get seat and waits for the 2nd movie session.");
			try {permitSeat2.acquire();}//initially acquired by clock but also ensures mutual exclusion.
			catch (InterruptedException e) { e.printStackTrace();}
					if(theater_capacity>0){
						theater_capacity-=1;	
						gotSeat= true;
						permitSeat2.release(); //allowing next visitor to acquire seat	
					}else{
						gotSeat= false;
						permitSeat2.release(); //no need to keep hold those who could get seat in 1st session.
					}
					if(!gotSeat){
						msg(" couldn't get seat in 2nd movie session.");
						try {this.sleep(1);} //so that Speaker can acquire 'permitMuseum' to hold visitors. 
						catch (InterruptedException e) { e.printStackTrace();}
						try {Speaker.permitMuseum.acquire();}// Speaker notifies one of the visitors that no more movie sessions so proceed  
						catch (InterruptedException e) { e.printStackTrace();}			
						msg(" goes to Museum without attending any movie session.");
						Speaker.permitMuseum.release();//visitor notifies next visitor to move on to visit the museum.
					}else{
						msg(" got seat in 2nd session.");
						try {this.sleep(rand.nextInt(4000));} //meanwhile presentation is going on.
						catch (InterruptedException e) { e.printStackTrace();}
						try {Speaker.permitMovie2.acquire();}//because movie can't begin until speaker ends presentation. 
						catch (InterruptedException e) { e.printStackTrace();}
						try {Clock.permitMuseum2.acquire();}//visitors can't go to Museum untill clock ends session.
						catch (InterruptedException e) { e.printStackTrace();}
						msg(" proceed to visit the Museum after attending 2nd movie session.");
						Speaker.permitMovie2.release();//presentation ends so visitors are allowed to watch movie
						Clock.permitMuseum2.release();//allowing other visitors to proceed 
					}
		}else{
			msg(" got seat in 1st session.");
			permitSeat1.release();//so that others could take available seat.
			try {this.sleep(rand.nextInt(4000));}//random sleep time for prensentation 
			catch (InterruptedException e) { e.printStackTrace();}
			try {Speaker.permitMovie1.acquire();}//movie can't begin until speaker releases 'permitMovie1' 
			catch (InterruptedException e) { e.printStackTrace();}
			try {Clock.permitMuseum1.acquire();}//Museum isn't allowed until clock ends session. 
			catch (InterruptedException e) { e.printStackTrace();}
			msg(" proceed to visit the Museum after attending 1st movie session.");
			Speaker.permitMovie1.release(); //indication of the end of presentation
			Clock.permitMuseum1.release(); //notifying next visitor to proceed.
		}
		try {Clock.endDay.acquire();}//visitor have to wait until clock signals the end of the day
		catch (InterruptedException e) { e.printStackTrace();}
		visitor_counter+=1; 
		if(visitor_counter<5){ //group of 5 should leave
			msg(" forms group "+ sub_group_counter);
			Clock.endDay.release();//letting know others to form group
		}else{
			msg(" forms group "+ sub_group_counter + " and leave" );
			visitor_counter=0;//for every group.
			sub_group_counter+=1;//group 2 and 3 form
			Clock.endDay.release();//letting others to fomr next group
		}
	}
	//prints message with integrated task of age() function. 
	public void msg(String m) {
		System.out.println("[" + ((System.currentTimeMillis() - Project2.time))	+ "]" 
	                          + getName() + ":" + m);
	}
}

	
