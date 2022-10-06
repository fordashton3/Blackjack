// Ashton Ford
// 4/4/2022

import java.util.Scanner;
public class BlackJack 
{
	
	@SuppressWarnings("unused")
	public static void main(String[] args) 
	{
		Scanner input = new Scanner(System.in);
		Scanner data = new Scanner(System.in);
		
		int bet = 0, splitBet, splitTotal = 0, handTotal = 0, dealerTotal = 0, handIndex = 0, dealerIndex = 0, splitIndex = 0, deckIndex = 0, used = 0, account = 20;
		int deckValue[] = new int[52], handValue[] = new int[6], splitHandValue[] = new int[6] , dealerValue[] = new int [6];
		boolean finish = false, placeBet = true, stay = false, doubleDown = false, split = false, bust = false, splitable = false, blackJack = false;
		String deck[] = new String[52], hand[] = new String[6], splitHand[] = new String[6], dealer[] = new String[6], playAgain = "", valid, action;
		
		welcome();
		
		// Initiates and shuffles the deck
		initDeck(deck, deckValue);		
		shuffle(deck, deckValue);
		System.out.println("\t\t..... Shuffling .....\n************************************************");
		
		// The loop that contains the game functions
		for (int i = 0; i < 52 && !(playAgain.equalsIgnoreCase("q")); i += used)
		{
			if (stay == false && bust == false)
			{
				placeBet = false;
				
				do // Asks how many chips you would like to bet at the start of the turn
				{
					do
					{
						System.out.print("How many chips would you like to bet? ");
						valid = input.nextLine();
						data = new Scanner(valid);
						System.out.println();
						
					} while (!data.hasNextInt());
					
					bet = data.nextInt();
				
				} while(!(bet <= account));
					
				
				
				
				for (int j = 0; j < 2; j++) // Draws the hand for both you and the dealer
				{
					draw(deck, deckValue, hand, handValue, handIndex);
					handIndex++;
					used++;
					draw(deck, deckValue, dealer, dealerValue, dealerIndex);
					dealerIndex++;
					used++;
				}
				
				if (handValue[0] + handValue[1] == 21)
					blackJack = true;
			
			}
			
			if (blackJack == false)
			{
				printHand(dealer, hand, splitHand, split); // Shows what is in your and the dealer's hands
				
				do
				{
					do
					{
						System.out.print("What action would you like to take (Hit[h], Double Down[d], Split[p], or Stay[s])? ");
						action = input.nextLine();
						
					} while(!action.equalsIgnoreCase("h") && !action.equalsIgnoreCase("d") && !action.equalsIgnoreCase("s") && !action.equalsIgnoreCase("p"));
					
					/* These if else statememts show what happens when each action is taken */
					if (action.equalsIgnoreCase("d")) // Double Down logic
					{
						doubleDown(deck, deckValue, hand, handValue, handIndex);
						handIndex = 5; //sets the index to max so you can't draw
						bet *= 2;
						used++;
						doubleDown = true;
						finish = true;
					}
					
					else if (action.equalsIgnoreCase("p") && hand[0].charAt(0) == hand[1].charAt(0)) // Split logic
					{
						if (bet * 2 <= account)
						{
							split(deck, deckValue, hand, handValue, splitHand, splitHandValue, handIndex, splitIndex);
							handIndex++;
							splitIndex++;
							splitBet = bet;
							used += 2;
							split = true;
						}
						
						else if (bet * 2 > account)
							System.out.println("You do not have enough funds to split your hand");
						else
							System.out.println("ERROR");
						
					}
					else if (action.equalsIgnoreCase("h")) // Hit logic
					{
						draw(deck, deckValue, hand, handValue, handIndex);
						handIndex++;
						used ++;
					}
					else if (handTotal > 21)
					{
						bust = true;
						finish = true;
					}
					else
					{
						finish = true;
					}
						
	
					handTotal = handTotal(handValue, handTotal);
					
				} while((finish != true || !action.equalsIgnoreCase("s")) && doubleDown != true && stay != false);
				
			}
			
			else if (bust == false && blackJack == true && dealerTotal != handTotal)
			{
				printHand(dealer, hand, splitHand, split);
				System.out.println("Congratualions! You got a BlackJack!\tYou earned " + bet * 2.5 + " points!");
				account += bet*2.5;
			}
			
			else if (bust == false && blackJack == false && dealerTotal < handTotal)
			{
				printHand(dealer, hand, splitHand, split);
				System.out.println("Congratualions!\tYou earned " + bet + " points!");
				account += bet;
			}
			
			else if (bust == false && blackJack == false && dealerTotal == handTotal)
			{
				printHand(dealer, hand, splitHand, split);
				System.out.println("You tied with the dealer.\tYou earned " + 0 + " points!");
			}
			
			else 
			{
				printHand(dealer, hand, splitHand, split);
				System.out.println("Sorry, you lost.\tYou lost " + bet + " points!");
				account -= bet;
			}
			
			
		}
		
		input.close();
		data.close();
	}
	
	/* This method welcomes the players and tells them the rules */
	public static void welcome()
	{
		System.out.print("Welcome to the BlackJack!\n*****************************************\n" +
				"\t\tPlay against the Dealer\n" +
				"1. Press H to hit\n" +
				"2. Press D to double down\n" +
				"3. Press S to stay your hand\n" +
				"4. Press P if you would like to split if both cards are the same value\n" +
				"5. You can onlly split your hand once\n" +
				"*********************************************************************************************************************\n");
	}
	
	/* This method sets the deck of cards from A - K for what the cards look like and their values */
	public static void initDeck(String[] deck, int[] deckValue)		
	{
		for (int k = 0; k < 52; k ++)
			deck[k] = "";
		
		for (int i = 0; i < 4; i ++)
		{
			for (int j = 0; j < 13; j++)
			{
				deckValue[13 * i + j] = j + 1;
				
				if (j >= 10)
					deckValue[13 * i + j] = 10;
			}
		}
			
				
		
		for (int i = 0; i < 13; i ++) 
		{
			for (int j = 0; j < 4; j++)
			{
	
				if (i == 10)
					deck[13 * j + i] += "J";
				else if (i == 11)
					deck[13 * j + i] += "Q";
				else if (i == 12)
					deck[13 * j + i] += "K";
				else if (i == 0)
					deck[13 * j + i] += "A";			
				else
					deck[13 * j + i] += i + 1;
				
				if (j == 0)
					deck[13 * j + i] += "♥";
				else if (j == 1)
					deck[13 * j + i] += "♦";
				else if (j == 2)
					deck[13 * j + i] += "♣";
				else 
					deck[13 * j + i] += "♠";
			}
				
		}
			
	}
	
	/* This method initiates the hands of the player and dealer */
	public static void intitHand(String[] hand, int[] handValue, String[] dealer, int[] dealerValue, String[] splitHand, int[] splitHandValue)
	{
		for (int i = 0; i < 6; i++)
		{
			hand[i] = " ";
			handValue[i] = 0;
			dealer[i] = " ";
			dealerValue[i] = 0;
			splitHand[i] = " ";
			splitHandValue[i] = 0;
			
		}
		
	}
	
	/* This method prints the contents of both the deck and it's values for testing */
	public static void printDeck(String[] deck, int[] deckValue)	
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 13; j++)
				System.out.print(deck[i * 13 + j] + " " + deckValue[i * 13 + j] + "\t");
			System.out.println();
		}
	}
	
	/* This method prints the contents of both the deck and it's values for testing */
	public static void printHand(String[] dealer, String[] hand,String[] splitHand, boolean split)	
	{
		System.out.print("\n***********************************************************\n");
	
		System.out.print("Dealer:");	
		
			for (int j = 0; j < 6; j++)
			{
				if (dealer[j] != null)
					System.out.print("\t" + dealer[j]);
			}
			
		System.out.print("\nYou   :");
			
			for (int j = 0; j < 6; j++)
			{
				if (hand[j] != null)
					System.out.print("\t" + hand[j]);
			}
		
			if (split == true)
			{
			System.out.print("\nSplit :");
				
				for (int j = 0; j < 6; j++)
				{
					if (splitHand[j] != null)
						System.out.print("\t" + hand[j]);
				}
				
			}
			
		System.out.println("\n***********************************************************");
	}
	
	/* This method switches the value of two cards in both decks, the shown card and the card value */
	public static void swap(String[] deck, int[] deckValue, int p1, int p2)
	{
		
		String temp = deck[p1];
		deck[p1] = deck[p2];
		deck[p2] = temp;
		
		int tempInt = deckValue[p1];
		deckValue[p1] = deckValue[p2];
		deckValue[p2] = tempInt;
		
	}
	
	/* This method shuffles two random cards in the shown deck and deck values */
	public static void shuffle(String[] deck, int[] deckValue)
	{
		
		for (int i = 0; i < deck.length; i ++) 
		{
			int card1 = (int)(Math.random() * deck.length - 1);
			int card2 = (int)(Math.random() * deck.length - 1);
			
			swap(deck, deckValue, card1, card2);
			
		}
		
	}
	
	/* Gives the total for the number of each card added up in your hand */
	public static int handTotal(int[] handValue, int handTotal)
	{
		for (int i = 0; i < 6; i++)
			handTotal += handValue[i];
		
		return handTotal;
	}
	
	/* Gives the total for the number of each card added up in the deck */
	public static int deckTotal(int[] deckValue)
	{
		int deckTotal = 0;
		
		for (int i = 0; i < 52; i++)
			deckTotal += deckValue[i];
		
		return deckTotal;
		
	}
	
	/* This method determines what happens when a card is drawn from the deck */
	public static void draw(String[] deck, int[] deckValue, String[] hand, int[] handValue, int handIndex)
	{
		hand[handIndex] = deck[0];
		handValue[handIndex] = deckValue[0];
		deck[0] = "";
		deckValue[0] = 0;
		
		for (int i = 0; i < 52; i++)
		{
			if (i != 51)
			{
				deck[i] = deck[i+1];
				deckValue[i] = deckValue[i+1];
			}
			
		}
		
	}

	/* This method determines what happens when the player hits the deck */
	public static void doubleDown(String[] deck, int[] deckValue, String[] hand, int[] handValue, int handIndex)
	{
		draw(deck, deckValue, hand, handValue, handIndex);		
	}
	
	/* This method determines what happens when the player hits the deck */
	public static void split(String[] deck, int[] deckValue, String[] hand, int[] handValue, String[] splitHand, int[] splitHandValue ,int handIndex, int splitIndex)
	{
		
		handIndex = 1;
		splitHand[0] = hand[1];
		draw(deck, deckValue, hand, handValue, handIndex);
		
		splitIndex = 1;
		draw(deck, deckValue, splitHand, splitHandValue, splitIndex);
		
	}
		
}
