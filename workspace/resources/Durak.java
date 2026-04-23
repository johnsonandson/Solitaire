//Sophia Babayev / 12/16/2025 / Durak Game Implementation
package resources;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
//Either Make this the server, or make Durak in server. GUI tells durak what the action is, Durak 


public class Durak implements serializable {
    

    public static final int LISTENING_PORT = 9876;
    
	public static void main(String[] args) {
		Durak game = new Durak();
		GUI gui = new GUI(game);
//pre condition: chat server is initialized
//post condition: server listens to port

    //pre condition: chat is initialized
    //Post condition: server records the port and shuts down when hit with an error or disconnect
    
        ServerSocket listener;
        Socket connection;
        try{
            listener=new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port "+LISTENING_PORT);
            while(true){
                connection=listener.accept();
                ConnectionHandler h=new ConnectionHandler(connection);
                h.start();

            }
        }
        catch(Exception e){
            System.out.println("Server down"+"\n"+"error"+e);
            return;
        }
    }
    /**
     *  Defines a thread that handles the connection with one
     *  client.
     */

    //pre conditon: socket is initialized
    //post condition: Initialize ConnectionHandler to set streams and add handlers
    private static class ConnectionHandler extends Thread {
        private static volatile ArrayList<ConnectionHandler> handlers;
        Socket client;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        
        
        ConnectionHandler(Socket socket) {
            client = socket;
            if (handlers==null){
                handlers=new ArrayList<ConnectionHandler>();
            }
            handlers.add(this);
            try {
                oos=new ObjectOutputStream(client.getOutputStream());
                ois=new ObjectInputStream(client.getInputStream());
            }
            catch(Exception e){
                
            }

        }
    }
    
	// Pre: None.
	// Post: Sets up a new game with deck, hands, and trump.
	public Durak() {
        columns = new ArrayList<>();
        hand1 = new ArrayList<>();
        hand2 = new ArrayList<>();
        graveyard = new ArrayList<>();
        deck = new LinkedList<>();

        Deck();
        shuffleDeck();
        pickTrump();          // sets trumpCard (bottom card of deck)
        dealInitialHands(6);
		//shortCutEnd();
        while(true){
        //TODO: Listener, add connections
        }
    }



    

    //TODO: Add connections
	ArrayList<Stack <Card>> columns;
	Queue<Card> deck;
	ArrayList<Card> graveyard;
	public int isGameOver = 0;
	boolean isPlayer1Attacking = true;
    boolean turn;
    boolean loser;

    
    private ArrayList<Card> hand1;
    private ArrayList<Card> hand2;


    
    public void swapHand(){
        ArrayList<Card> temp = hand1;
        hand1 = hand2;
        hand2 = temp;
    }

    // Pre: None.
    // Post: Returns player 1's hand. 
	public java.util.List<Card> getHand1() {
        return java.util.Collections.unmodifiableList(hand1);
    }

    // Pre: None.
    // Post: Returns player 2's hand. 
    public java.util.List<Card> getHand2() {
        return java.util.Collections.unmodifiableList(hand2);
    }

    // Pre: None.
    // Post: Returns the graveyard. 
	public java.util.List<Card> getGraveyard() {
        return java.util.Collections.unmodifiableList(graveyard);
    }

    // Pre: None.
    // Post: Returns true if player 1 is attacking, false otherwise.
	public boolean isPlayer1Attacking() {
		return isPlayer1Attacking;
	}

    // Pre: None.
    // Post: Returns 1 if player 1 is attacking, 2 if player 2 is attacking.
	public int getAttackingPlayer() {
		return isPlayer1Attacking ? 1 : 2;
	}

    // Pre: None.
    // Post: Returns 1 if player 1 is defending, 2 if player 2 is defending.
	public int getDefendingPlayer() {
		return isPlayer1Attacking ? 2 : 1;
	}

	private Card trumpCard;


    // Pre: None.
    // Post: Returns the table columns.
    public List<Stack<Card>> getColumns() {
        return Collections.unmodifiableList(columns);
    }




	// Pre: None.
	// Post: Builds a 36-card deck for Durak.
	private void Deck() {
        ArrayList<Card> temp = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (int value = 6; value <=14; value++) {
                // Create card images will be loaded in Card constructor
				Card c = new Card(value, suit);
				c.hide();
                temp.add(c);
            }
        }
        // Place into the queue 
        deck = new LinkedList<>(temp);
    }

	// Pre: Deck must be set up.
	// Post: Shuffles the deck.
	private void shuffleDeck() {
		ArrayList<Card> temp = new ArrayList<>(deck);
		Collections.shuffle(temp);
		deck = new LinkedList<>(temp);
	}

	// Pre: None.
	// Post: Returns top card without removing it, or null if empty.
	public Card getTopDeckCard(){
		return deck.peek();
	}

	// Pre: Deck must be set up.
	// Post: Removes 20 cards from deck for quick testing.
    public void shortCutEnd(){
        for(int i=0; i<20; i++){
            deck.poll();
        }
    }

	// Pre: Deck must be set up.
	// Post: Picks the trump card from the bottom of the deck.
	private void pickTrump() {
		if (deck.isEmpty()) {
			trumpCard = null;
			return;
		}
		
		ArrayList<Card> list = new ArrayList<>(deck);
		trumpCard = list.get(list.size() - 1); // bottom card is trump 
	}

	

	// Pre: None.
	// Post: Returns the trump card, or null if not set.
	public Card getTrumpCard() {
        return trumpCard;
    }


	// Pre: None.
	// Post: Returns the suit of the trump card, or null if not set.
	public Card.Suit getTrumpSuit() {
    	if (trumpCard == null) {
        	return null;
    	} else {
        	return trumpCard.suit;
    	}
	}

	// Pre: None.
	// Post: Returns the deck as a list. 
	public java.util.List<Card> getDeckAsList() {
        return new ArrayList<>(deck);
    }

	

	// Pre: None.
	// Post: Returns winner message if game over, else empty string.
	public String gameOver(){
		if(hand1.isEmpty() && deck.isEmpty()) {
            isGameOver = 1;
            return "Player 1 wins! Player 2 is the Durak.";
        }else if (hand2.isEmpty() && deck.isEmpty()) {
            isGameOver = 1;
			return "Player 2 wins! Player 1 is the Durak.";
		}
        else {
            isGameOver = 0;
            return "";
        }
	}


	// Pre: Deck must be set up.
	// Post: Deals initial hands to players.
	private void dealInitialHands(int handSize) {
        for (int i = 0; i < handSize; i++) {
            if (!deck.isEmpty()) hand1.add(deck.poll());
            if (!deck.isEmpty()) hand2.add(deck.poll());
        }
    }


	// Pre: None.
	// Post: Returns the top card without removing it, or null if empty.
	public Card peekTopDeckCard() {
        return deck.peek();
    }




	// Pre: None.
	// Post: Draws cards to fill hands to target size.
	public void replenishHandsTo(int targetSize) {
        while (hand1.size() < targetSize && !deck.isEmpty()) {
            hand1.add(deck.poll());
        }
        while (hand2.size() < targetSize && !deck.isEmpty()) {
            hand2.add(deck.poll());
        }
        // If deck becomes empty, trumpCard stays whatever it was (you already pick it)
    }



	// Pre: attack and defense not null.
	// Post: Returns true if defense beats attack.
    public boolean canDefend(Card attack, Card defense) {
        // if (attack == null || defense == null) return false;

        Card.Suit trump = getTrumpSuit();

        // assume Card has int field 'value' and Card.Suit field 'suit'
        int aRank = attack.value;    
        int dRank = defense.value;   

        // same suit, higher rank
        if (defense.suit == attack.suit && dRank > aRank) return true;

        // defense is trump, attack is not
        if (defense.suit == trump && attack.suit != trump) return true;

        return false;
    }


	// Pre: card not null.
	// Post: Returns true if card can attack.
    public boolean isAttacking(Card card){
        if(card == null) return false;
        

        return true;
    }

	// Pre: None.
	// Post: Returns true if all table columns have 2 cards.
    public boolean canEndTurn(){
            for (Stack<Card> column : columns) {
                if(column.size() != 2){
                    return false;
                }
            }
            return true;
    }

	// Pre: canEndTurn() true.
	// Post: Ends turn, moves cards to graveyard, switches player, replenishes hands.
    public void endTurn(){
        if(!canEndTurn()){
            return;
        }

        // Move all cards from table to graveyard
        for (Stack<Card> column : columns) {
            while (!column.isEmpty()) {
                graveyard.add(column.pop());
            }
        }

      

        // Clear the table
        columns.clear();
        // Switch turns
        isPlayer1Attacking = !isPlayer1Attacking;
        // Replenish hands
        replenishHandsTo(6);
        // Check for game over
        gameOver();
    }

	// Pre: None.
	// Post: Defender takes all table cards, switches turn, replenishes hands.
    public void defenderTakesCards() {
        // Defender takes all cards from the table into their hand
        ArrayList<Card> defendingHand = isPlayer1Attacking ? hand2 : hand1;
        
        for (Stack<Card> column : columns) {
            while (!column.isEmpty()) {
                defendingHand.add(column.pop());
            }
        }
        columns.clear();
        // Switch turns
        isPlayer1Attacking = !isPlayer1Attacking;
        // Replenish hands
        replenishHandsTo(6);
        // Check for game over
        gameOver();
    }

	// Pre: card not null.
	// Post: Returns true if card can attack (first or matches table rank).
    public boolean isValidAttackCard(Card card) {
        // First card can be anything, subsequent cards must match values on table
        if (columns.isEmpty()) {
            return true;  // First attack card
        }
        
        // Get all values currently on the table
        ArrayList<Integer> tableValues = new ArrayList<>();
        for (Stack<Card> column : columns) {
            if (!column.isEmpty()) {
                tableValues.add(column.peek().value);  // Get top card value
            }
        }
        
        // Attack card must match one of the values on the table
        return tableValues.contains(card.value);
    }



	// Pre: c1 in a hand, c2 on table if defending.
	// Post: Performs attack or defense if valid.
    public void doMove(Card c1, Card c2) {
        // Determine which hand c1 came from
        boolean c1InHand1 = hand1.contains(c1);
        boolean c1InHand2 = hand2.contains(c1);

        // Determine if c2 is an attack card on the table
        boolean isCard2OnTable = false;
        if (c2 != null) {
            for (Stack<Card> column : columns) {
                if (column.contains(c2)) {
                    isCard2OnTable = true;
                    break;
                }
            }
            System.out.println("c1InHand1: " + c1InHand1 + ", c1InHand2: " + c1InHand2);
        }
        

        // If Player 1 is attacking
        if (isPlayer1Attacking && c1InHand1) {
            // Validate that attack card is valid
            if (!isValidAttackCard(c1)) {
                System.out.println("Invalid attack! Card value must match cards on table.");
                return;
            }
            // Player 1 attacks with c1 (c2 will be null for new attacks)
            Stack<Card> column = new Stack<>();
            column.push(c1);
            columns.add(column);
            hand1.remove(c1);
            System.out.println("Player 1 attacked with " + c1);
            return;
        }
        
        // If Player 1 is attacking, Player 2 must defend
        if (isPlayer1Attacking && c1InHand2 && c2 != null && isCard2OnTable) {
            // Player 2 defends against c2 (the attack card) with c1
            if (canDefend(c2, c1)) {
                for (Stack<Card> column : columns) {
                    if (column.peek().equals(c2)) {
                        column.push(c1);
                        hand2.remove(c1);
                        System.out.println("Player 2 defended with " + c1);
                        return;
                    }
                }
            } else {
                System.out.println("Invalid defense!");
            }
            return;
        }
        
        // If Player 2 is attacking
        if (!isPlayer1Attacking && c1InHand2) {
            // Validate that attack card is valid
            if (!isValidAttackCard(c1)) {
                System.out.println("Invalid attack! Card value must match cards on table.");
                return;
            }
            // Player 2 attacks with c1 (c2 will be null for new attacks)
            Stack<Card> column = new Stack<>();
            column.push(c1);
            columns.add(column);
            hand2.remove(c1);
            System.out.println("Player 2 attacked with " + c1);
            return;
        }
        
        // If Player 2 is attacking, Player 1 must defend
        if (!isPlayer1Attacking && c1InHand1 && c2 != null && isCard2OnTable) {
            // Player 1 defends against c2 (the attack card) with c1
            if (canDefend(c2, c1)) {
                for (Stack<Card> column : columns) {
                    if (column.peek().equals(c2)) {
                        column.push(c1);
                        hand1.remove(c1);
                        System.out.println("Player 1 defended with " + c1);
                        return;
                    }
                }
            } else {
                System.out.println("Invalid defense!");
            }
            return;
        }
    }


    
}