/**
 * @title CS340 Main.java
 * @author  Muhammad Faisal
 */

import javax.swing.*;
import java.awt.Component;
import java.util.*;


public class Project2  {	
	public static long time = System.currentTimeMillis();
	public static int Num_visitors=15;
	static Visitors[] Visitor= new Visitors[15];
	static Clock clock;
	static Speaker speaker;
	public static void main(String[] args){
//	all initializations of threads are in main function
		
		clock = new Clock();
		clock.start();
		
		for(int i=0;i<Num_visitors;i++){ 
			Visitor[i] = new Visitors(i+1);
			Visitor[i].start();
		}
    
		speaker = new Speaker();
		speaker.start();
		
	}
}
