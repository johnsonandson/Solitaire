//Sophia Babayev / 12/16/2025 / Add all user interactions to the screen
package resources;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;




public class GUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener{
private Card card1;
private Card card2;
Durak game;
	
JPanel south; // player one hand
JPanel north; // player two hand
JPanel east; // graveyard area
JPanel west; //deck area
JPanel center; // play area

JPanel playerOne;
JPanel playerTwo;


private static final int CARD_WIDTH = 120;
private static final int CARD_HEIGHT = 150;
private static final int HAND_OVERLAP = CARD_WIDTH / 4;


private JLabel trumpLabel;
private JLabel endGame;
private JLabel turnIndicator; 
private JPanel graveyardPanel;

private JToggleButton viewHandsButtonSouth;
private JToggleButton viewHandsButtonNorth;
private JButton endTurn;
private JButton takeCardsSouth;
private JButton takeCardsNorth;






    // Pre: game not null.
    // Post: Sets up the GUI window and panels.
    public GUI(Durak game){
	   this.game= game;
        //Create and set up the window.
       setTitle("Durak");
       setSize(900,550);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   setResizable(false);
       
       // this supplies the background
       try {
		System.out.println(getClass().toString());
		Image blackImg = ImageIO.read(getClass().getResource("background.jpg"));
		setContentPane(new ImagePanel(blackImg));

       }catch(IOException e) {
    	   e.printStackTrace();
       }
	   
	   for(Card c: game.deck){
		c.addMouseListener(this);
	   }
	
    		Container cp = getContentPane();
   			cp.setLayout(new BorderLayout());

			// SOUTH: Player One (local player) hand area information
       		south = new JPanel(new BorderLayout());
       		south.setOpaque(false);
    		south.setPreferredSize(new Dimension(0, 150));
       		playerOne = new JPanel();
       		playerOne.setOpaque(false);
       		playerOne.setPreferredSize(new Dimension(600, 120));
       		playerOne.add(new JLabel("Player One"));
       		south.add(playerOne, BorderLayout.CENTER);
			south.setBorder(BorderFactory.createLineBorder(Color.BLACK));
       		cp.add(south, BorderLayout.SOUTH);

    		// Button panel for south
    		JPanel southButtonPanel = new JPanel();
    		southButtonPanel.setLayout(new BoxLayout(southButtonPanel, BoxLayout.Y_AXIS));
    		southButtonPanel.setOpaque(false);
    		southButtonPanel.setPreferredSize(new Dimension(120, 0));
    		
	   		viewHandsButtonSouth = new JToggleButton("Show Hands");
            viewHandsButtonSouth.setToolTipText("Toggle to show/hide both players' hands");
            viewHandsButtonSouth.addActionListener(this);
            southButtonPanel.add(viewHandsButtonSouth);
            
            takeCardsSouth = new JButton("Take Cards");
            takeCardsSouth.setToolTipText("Take all cards from the table (defender only)");
            takeCardsSouth.addActionListener(this);
            southButtonPanel.add(takeCardsSouth);
            
            south.add(southButtonPanel, BorderLayout.EAST);


			// NORTH: Player Two area (opponent) information
            north = new JPanel(new BorderLayout());
            north.setOpaque(false);
            north.setPreferredSize(new Dimension(0, 150));
            playerTwo = new JPanel();
            playerTwo.setOpaque(false);
            playerTwo.setPreferredSize(new Dimension(600, 120));
            playerTwo.add(new JLabel("Player Two"));
            north.add(playerTwo, BorderLayout.CENTER);
			north.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cp.add(north, BorderLayout.NORTH);

            // Button panel for north
            JPanel northButtonPanel = new JPanel();
            northButtonPanel.setLayout(new BoxLayout(northButtonPanel, BoxLayout.Y_AXIS));
            northButtonPanel.setOpaque(false);
            northButtonPanel.setPreferredSize(new Dimension(120, 0));
            
	   		viewHandsButtonNorth = new JToggleButton("Show Hands");
            viewHandsButtonNorth.setToolTipText("Toggle to show/hide both players' hands");
            viewHandsButtonNorth.addActionListener(this);
            northButtonPanel.add(viewHandsButtonNorth);
            
            takeCardsNorth = new JButton("Take Cards");
            takeCardsNorth.setToolTipText("Take all cards from the table (defender only)");
            takeCardsNorth.addActionListener(this);
            northButtonPanel.add(takeCardsNorth);
            
            north.add(northButtonPanel, BorderLayout.EAST);


			// EAST: Graveyard information
            east = new JPanel(new BorderLayout());
            east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
            east.setPreferredSize(new Dimension(160, 0));
            east.add(new JLabel("Graveyard"));
            
            graveyardPanel = new JPanel();
            graveyardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, -HAND_OVERLAP, 0));
            graveyardPanel.setOpaque(false);
            graveyardPanel.setPreferredSize(new Dimension(160, 150));
            east.add(graveyardPanel);
            
            turnIndicator = new JLabel("Player 1 Attacking", SwingConstants.CENTER);
            turnIndicator.setPreferredSize(new Dimension(160, 30));
            turnIndicator.setFont(new Font("Serif", Font.BOLD, 12));
            east.add(turnIndicator);
			
			endTurn = new JButton("End Turn");
			endTurn.setOpaque(false);
			endTurn.setPreferredSize(new Dimension(160, 30));
			endTurn.addActionListener(this);
			east.add(endTurn);
            
            
			east.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cp.add(east, BorderLayout.EAST);

			// West: Deck information
            west = new JPanel(new BorderLayout());
            west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));
            west.setPreferredSize(new Dimension(160, 0));
            west.add(new JLabel("Deck"));
			west.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cp.add(west, BorderLayout.WEST);
			trumpLabel = new JLabel("", SwingConstants.CENTER);
            trumpLabel.setPreferredSize(new Dimension(160, 60));
            trumpLabel.setFont(new Font("Serif", Font.BOLD, 36));
            west.add(trumpLabel);
			


			// CENTER: play area information
            center = new JPanel();
            center.setPreferredSize(new Dimension(800, 400)); // workspace for table
            center.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			center.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			endGame=new JLabel();
			center.add(endGame);
            cp.add(center, BorderLayout.CENTER);

   			
	   		//cards in flow layout - 4 layered panes in center panel
			center.setLayout(new FlowLayout());
			


			this.setVisible(true);
			updateDisplay();
		}




		// Pre: stackIn not null.
		// Post: Returns a JLayeredPane displaying the stack of cards.
		public JLayeredPane drawPile(Stack<Card> stackIn) {
				JLayeredPane pane = new JLayeredPane();
				Object[] cards = stackIn.toArray(); 
				pane.setPreferredSize(new Dimension(120, 170));
				pane.setBorder(BorderFactory.createLineBorder(Color.red));
				for (int i = 0; i < cards.length; i++) {
					Card c = (Card) cards[i]; // cast each element back to Card
					int x = 0;
					int y = i * 35;
					
					c.setBounds(x, y, 120, 170);
					pane.add(c, Integer.valueOf(i));
			
				}
				
				return pane;
		}
			
			
		// Pre: hand and panel not null.
		// Post: Renders the hand on the panel, face up or down.
		private void renderPlayerHand(java.util.List<Card> hand, JPanel panel, boolean faceUp) {
			panel.removeAll();
		
		// Adjust overlap based on number of cards
		int overlap = HAND_OVERLAP;
		if (hand.size() > 8) {
			// More cards = closer together
			overlap = Math.max(20, CARD_WIDTH - (600 / hand.size()));
		}
		
		// Use null layout or FlowLayout with gaps; simplest: FlowLayout with small horizontal gap
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, -overlap, 0));

		for (Card c : hand) {
			if (faceUp) c.show(); else c.hide();
			// Ensure the Card component uses the consistent size
			c.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
			c.setSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
			// Add mouse listener so user can interact
			c.addMouseListener(this);
			panel.add(c);
		}
		panel.revalidate();
		panel.repaint();
	}


	   // Pre: None.
	   // Post: Updates the GUI to reflect the current game state.
	   private void updateDisplay() {
        //ask the server for "the game"
        //TODO: add a if statement that says "only do the stuff below once you receive a new Durak object for game from server."
    	if(0!=1){	
            if (game == null) {
				
				return;
	 		} 
            if (game.isGameOver==1){
                endGame.setText("Player One Wins!");
            }
            else if(game.isGameOver==2){
                endGame.setText("Player Two Wins!");
            }
			west.removeAll();
			west.add(new JLabel("Deck"));
			Stack<Card> deckPile = new Stack();
			game.getTrumpCard().show();
            if(!game.deck.isEmpty()){
                deckPile.add(game.getTrumpCard());
            }

            String gameOverMsg = game.gameOver();
            if (!gameOverMsg.isEmpty()) {
                JOptionPane.showMessageDialog(this, gameOverMsg);
            }
			
            if(game.deck.size()>1)
                deckPile.add(game.getTopDeckCard());
			west.add(drawPile(deckPile));

			boolean southFaceUp = viewHandsButtonSouth != null && viewHandsButtonSouth.isSelected();
            boolean northFaceUp = viewHandsButtonNorth != null && viewHandsButtonNorth.isSelected();
			
            renderPlayerHand(game.getHand1(), playerOne, southFaceUp);
            renderPlayerHand(game.getHand2(), playerTwo, northFaceUp);
            
            // Update turn indicator
            if (game.isPlayer1Attacking()) {
                turnIndicator.setText("Player 1 Attacking");
            } else {
                turnIndicator.setText("Player 2 Attacking");
            }
            
            // Update graveyard display
            graveyardPanel.removeAll();
            for (Card c : game.getGraveyard()) {
                c.hide();  // Show cards face down in graveyard
                c.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
                graveyardPanel.add(c);
            }
            graveyardPanel.revalidate();
            graveyardPanel.repaint();
            
			center.removeAll();
			for(Stack<Card> stack:game.getColumns()){
				JLayeredPane playArea = new JLayeredPane();
				playArea.setPreferredSize(new Dimension(120, 200));
				playArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				int yoffset=0;
				for(Card c: stack){

					c.show();
					c.setBounds(0, yoffset, 120,170);
					c.addMouseListener(this);  // Add mouse listener so cards can be clicked for defense
					playArea.add(c, 0);
					yoffset+=50;
				}
				
				center.add(playArea);
			}
			repaint();
        }
			
    	}
       

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
		
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		
		
	}

	// Pre: None.
	// Post: Handles mouse release events for card selection and actions.
	@Override
    public void mouseReleased(MouseEvent arg0) {
		Card selectedCard = (Card)arg0.getComponent();
        
        // Validate that the selected card belongs to the correct player
        boolean cardInHand1 = game.getHand1().contains(selectedCard);
        boolean cardInHand2 = game.getHand2().contains(selectedCard);
        
        // Get the current attacking and defending players
        int attackingPlayer = game.getAttackingPlayer();
        int defendingPlayer = game.getDefendingPlayer();
        
      
        
        

        // Handle card selection and actions
        if (card1 == null) {
            // First card selected
            card1 = selectedCard;
            card1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
            repaint();
            return;
        } else {
            // Second card selected - determine the action
            card2 = selectedCard;

            // Determine if this is an attack or defense move
            boolean c1InAttackerHand = (attackingPlayer == 1 && cardInHand1) || (attackingPlayer == 2 && cardInHand2);
            boolean isCard2OnTable = false;
            
            if (card2 != null) {
                for (Stack<Card> column : game.getColumns()) {
                    if (column.contains(card2)) {
                        isCard2OnTable = true;
                        break;
                    }
                }
            }
            //MOVE TO DURAK-TELL DURAK WHAT THE MOVE IS
            // Switch-based action handler
            if (c1InAttackerHand && !isCard2OnTable) { 
                // Attacker playing a new attack card
                System.out.println("Player " + attackingPlayer + " attacking with " + card1);
                //the actual server needs to know stuff step
                game.doMove(card1, null);
                //TODO send game to server
            } else if (!c1InAttackerHand && isCard2OnTable) {
                // Defender playing a defense card against an attack card
                System.out.println("Player " + defendingPlayer + " defending against " + card2 + " with " + card1);
                game.doMove(card1, card2);

            } else {
                System.out.println("Invalid move!");
            }
            
            // Clear borders and reset
            card1.setBorder(BorderFactory.createEmptyBorder());
            updateDisplay();
        }
        
        card1 = null;
        card2 = null;
    }


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	

	// Pre: None.
	// Post: Handles action events from buttons.
	@Override
    public void actionPerformed(ActionEvent arg0) {
        // south toggle controls only playerOne
        if (arg0.getSource() == viewHandsButtonSouth) {
            boolean on = viewHandsButtonSouth.isSelected();
            String text;
			if (on) {
				text = "Hide Hand2";
			} else {
				text = "Show Hand2";
			}
			viewHandsButtonSouth.setText(text);
            updateDisplay();
            return;
        }

        // north toggle controls only playerTwo
        if (arg0.getSource() == viewHandsButtonNorth) {
            boolean on = viewHandsButtonNorth.isSelected();
            String text;
			if (on) {
				text = "Hide Hand1";
			} else {
				text = "Show Hand1";
			}
			viewHandsButtonNorth.setText(text);
            updateDisplay();
            return;
        }

        // Player 1 take cards button
        if (arg0.getSource() == takeCardsSouth) {
            if (game.getDefendingPlayer() == 1) {
                game.defenderTakesCards();
                game.endTurn();
                updateDisplay();
            } else {
                System.out.println("Only the defending player can take cards!");
            }
            return;
        }

        // Player 2 take cards button
        if (arg0.getSource() == takeCardsNorth) {
            if (game.getDefendingPlayer() == 2) {
                game.defenderTakesCards();
                game.endTurn();
                updateDisplay();
            } else {
                System.out.println("Only the defending player can take cards!");
            }
            return;
        }

		if (arg0.getSource() == endTurn) {
			game.endTurn();
			updateDisplay();
			return;
		}

       
    }




    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
	
}
