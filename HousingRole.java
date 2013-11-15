import java.io.*;
import java.util.*;
import java.util.concurrent.*;



public class HousingRole extends Role { //implements Housing

    //data

	Timer timer=new Timer();
	private final int cleaningTime=5000;
	double rent=0;
	private String choice;
	public enum AgentState {nothing, hungry, checkFood, cooking, eating, cleaning, paying}
	private AgentState state =AgentState.nothing;
	public enum AgentEvent {nothing, getFood, cookFood, eatFood, cleanHome, payRent}
	private AgentEvent event=AgentEvent.nothing;
	private Map<String, Food> foods;
	private class Food {
		String choice;
		int cookingTime;
		int eatingTime;
		int amount;
		
		Food(String c, int cookTime, int eatTime,int number){
			this.choice=c;
			this.cookingTime=cookTime;
			this.amount=number;
			this.eatingTime=eatTime;
		}	
		
		public void increase(int number){
			amount=amount+number;
			//print("increased amount ="+ number);
		}
		
		public void decrease(){
			amount--;
			//print("decreased amount");
		}
	}
	
	
    public void setFood(){	
    foods =Collections.synchronizedMap(new HashMap<String, Food>());
    foods.put("Steak",new Food("Steak",5000,8000,0));
    foods.put("Chicken",new Food("Chicken",4000,8000,0));
    foods.put("Salad",new Food("Salad",3000,8000,0));
    foods.put("Pizza", new Food("Pizza",4000,8000,0));
    }
	
    // Messages
    
    public void msgGotHungry(){
        state=AgentState.hungry;
        event=AgentEvent.getFood;
    }

    public void msgCookFood(){
        event=AgentEvent.cookFood;
    }

    public void msgFinishedCooking(){
        event=AgentEvent.eatFood;
    }

    public void msgFinishedEating(){
        event=AgentEvent.nothing;
    }

    public void msgCleanHome(){
        event=AgentEvent.cleanHome;
    }

    public void msgFinishedCleaning(){
        event=AgentEvent.nothing;
    }

    public void msgPayRent(double amount){
        event=AgentEvent.payRent;
        rent=amount;
    }

   
    //Scheduler
    protected boolean pickAndExecuteAnAction() {
    	
        if (state == AgentState.hungry && event== AgentEvent.getFood) { 
    	    state=AgentState.checkFood;
    	    goToRefrigerator();
    	    return true;
        }
    	if (state==AgentState.checkFood && event==AgentEvent.cookFood) {
    	    state=AgentState.cooking;
    	    goToCookArea();
    	    return true;
    	}
    	if (state==AgentState.cooking & event==AgentEvent.eatFood) {
    	    state=AgentState.eating;
    	    eat();
    	    return true;
    	}
    	if (state==AgentState.eating && event==AgentEvent.nothing) {
    	    state=AgentState.nothing;
    	    return true;
    	}
    	if (state==AgentState.nothing && event==AgentEvent.cleanHome) {
    	   state=AgentState.cleaning;
    	   clean();
    	   return true;
    	}
    	if (state==AgentState.cleaning && event==AgentEvent.nothing) {
    	    state=AgentState.nothing;
    	    return true;
    	}
    	if (state==AgentState.nothing && event==AgentEvent.payRent) {
    	    state=AgentState.paying;
            payRent();
            return true;
    	}
    	return false;
    }
    //Actions
    private void goToRefrigerator(){
    	System.out.print("GoToRefrigertor");
        int rand = 0 + (int)(Math.random() * ((3 - 0) + 1));
        if (rand==0) { 
			choice="Steak";}
		if (rand==1) { 
			choice="Chicken";}
		if (rand==2) { 
		    choice="Salad";}
		if (rand==3) { 
		    choice="Pizza";}
        if (foods.get(choice).amount > 0) { 
        	msgCookFood();
        }
        else { 
        	myPerson.msgFridgeIsLowOn(choice); 
        }
    }  

    private void goToCookArea(){
        System.out.print("Cooking");
        timer.schedule(new TimerTask() {
			public void run() { 
				msgFinishedCooking();
			}
        }, foods.get(choice).cookingTime);
	
    }

    private void eat(){
    	System.out.print("Eating");
        timer.schedule(new TimerTask() {
			public void run() { 
				msgFinishedEating(); 
			}
        }, foods.get(choice).eatingTime);
			
    }

    private void clean(){
    	System.out.print("Cleaning");
    	timer.schedule(new TimerTask() {
			public void run() {
				msgFinishedCleaning(); 
			}
    	}, cleaningTime);
			
    }

    private void payRent(){
    	System.out.print("GoToRentMailBox");
        myPerson.Wallet -= rent;
        myPerson.home.owner.msgRentPaid(rent);
    }
}
