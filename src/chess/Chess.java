package chess;
import processing.core.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.game.*;
import com.github.bhlangonijr.chesslib.move.*;
import com.github.bhlangonijr.chesslib.pgn.*;
import com.github.bhlangonijr.chesslib.unicode.*;
import com.github.bhlangonijr.chesslib.util.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
public class Chess extends PApplet {
	public static Board board;
	public String letters = "abcdefgh";
	public int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8};
	public static int tileSize = 100;
	public int darkSquare;
	public int lightSquare;
	public int darkColor;
	public int lightColor;
	public int dSquareColor;
	public int lSquareColor;
	public int dHighlightColor;
	public int lHighlightColor;
	public int dHoverColor;
	public int lHoverColor;
	public char wKing = 'K', bKing = 'k', wQueen = 'Q', bQueen = 'q', wPawn = 'P', bPawn = 'p', wRook = 'R', bRook = 'r', wKnight = 'N', bKnight = 'n', wBishop = 'B', bBishop = 'b';
	public PImage iwKing, ibKing, iwQueen, ibQueen, iwPawn, ibPawn, iwRook, ibRook, iwKnight, ibKnight, iwBishop, ibBishop;
	public String emptyFen = "8/8/8/8/8/8/8/8 b - - 0 1";
	public char[] pieceOrder = {'p', 'n', 'b', 'r', 'q', 'k', 'P', 'N', 'B', 'R', 'Q', 'K'};
	public List<String> fens = new ArrayList<String>();
	public boolean click = false;
	public int x = 0, y = 0;
	public int x1 = 0, y1 = 0;
	public int opx = 0, opy = 0;
	public int opx1 = 0, opy1 = 0;
	public int startx = 0, starty = 0;
	public boolean dragging = false;
	public boolean highlightSquares = true;
	public TextField textbox, seedbox;
	public boolean isHost = false;
	public boolean isPlaying = false;
	public String code = "";
	public ChessServer sketch;
	public boolean setClient = true;
	public static int[] seeds;
	public static int index;
	public boolean setSeed = true;
	public static char[][] permutation = {
			  {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'},
			  {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			  {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			  {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
			  {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
			  };
	public static Square[][] boardRepresentationWhite = {
			{Square.A8, Square.B8, Square.C8, Square.D8, Square.E8, Square.F8, Square.G8, Square.H8},
			{Square.A7, Square.B7, Square.C7, Square.D7, Square.E7, Square.F7, Square.G7, Square.H7},
			{Square.A6, Square.B6, Square.C6, Square.D6, Square.E6, Square.F6, Square.G6, Square.H6},
			{Square.A5, Square.B5, Square.C5, Square.D5, Square.E5, Square.F5, Square.G5, Square.H5},
			{Square.A4, Square.B4, Square.C4, Square.D4, Square.E4, Square.F4, Square.G4, Square.H4},
			{Square.A3, Square.B3, Square.C3, Square.D3, Square.E3, Square.F3, Square.G3, Square.H3},
			{Square.A2, Square.B2, Square.C2, Square.D2, Square.E2, Square.F2, Square.G2, Square.H2},
			{Square.A1, Square.B1, Square.C1, Square.D1, Square.E1, Square.F1, Square.G1, Square.H1}
			};
	public static Square[][] boardRepresentationBlack = {
			{Square.H1, Square.G1, Square.F1, Square.E1, Square.D1, Square.C1, Square.B1, Square.A1},
			{Square.H2, Square.G2, Square.F2, Square.E2, Square.D2, Square.C2, Square.B2, Square.A2},
			{Square.H3, Square.G3, Square.F3, Square.E3, Square.D3, Square.C3, Square.B3, Square.A3},
			{Square.H4, Square.G4, Square.F4, Square.E4, Square.D4, Square.C4, Square.B4, Square.A4},
			{Square.H5, Square.G5, Square.F5, Square.E5, Square.D5, Square.C5, Square.B5, Square.A5},
			{Square.H6, Square.G6, Square.F6, Square.E6, Square.D6, Square.C6, Square.B6, Square.A6},
			{Square.H7, Square.G7, Square.F7, Square.E7, Square.D7, Square.C7, Square.B7, Square.A7},
			{Square.H8, Square.G8, Square.F8, Square.E8, Square.D8, Square.C8, Square.B8, Square.A8}
			};
	public final int CHESSCOM = 69;
	public final int LICHESS = 420;
	public int colorScheme = LICHESS;
	public static Square[][] boardRepresentation;
	public Side perspective = Side.WHITE;
	public Client client;
	public Side takenSide;
	List<int[]> flaggedSquares = new ArrayList<int[]>();
	List<Arrow> arrows = new ArrayList<Arrow>();
	public float scl = 0;
	public Button host, joiner;
	public boolean displayJoinCode = false;
	public boolean startServer = true;
public void settings() {
	size(800, 800, P2D);
  }
public static void main(String[] args) {
	String[] processingArgs = {"Chess"};
	Chess mySketch = new Chess();
	PApplet.runSketch(processingArgs, mySketch);
}
public void setup() {
	Random rand = new Random(69420);
	seeds = toIntArray(loadStrings("seeds.txt"));
	index = floor(random(70000));
	randomSeed(index);
	//just an array of 44 characters
	char[] arr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', '#', '$', '%', '-', '&', '_', '+'};
	for(int i = 0; i < arr.length; i++) {
		arr[i] = Character.toUpperCase(arr[i]);
	}
	//scrambles the contents randomly
	arr = scrambleContents(arr);
	//turn it into an 11x4 array
	char[][] arr2 = decodeBoard(arr, 11, 4);
	char[][] codes = new char[11][4];
	//4 chars in each column of the permutation table
	//these 4 characters in each column determine encoding characters for numbers in the ip address
	//example: 1 -> a, 6, $, 9
	//used to encode and decode ip addresses
	for(int i = 0; i < codes.length; i++) {
		codes[i][0] = arr2[i][0];
		codes[i][1] = arr2[i][1];
		codes[i][2] = arr2[i][2];
		codes[i][3] = arr2[i][3];
	}
	//codes array is used to create the permutation table
	for(int i = 0; i < codes.length; i++) {
		for(int j = 0; j < 4; j++) {
			permutation[j+1][i] = codes[i][j];
		}
	}
	board = new Board();
	//sets the color scheme and player perspective
	setPerspective(Side.WHITE);
	if(colorScheme == CHESSCOM) {
	lightSquare = color(234, 233, 210);
    darkSquare = color(119, 148, 88);
	lightColor = color(210, 209, 189);
	darkColor = color(106, 135, 77);
	lSquareColor = color(246, 246, 105);
	dSquareColor = color(186, 202, 43);
	lHoverColor = color(174, 177, 135);
    dHoverColor = color(132, 121, 78);
    lHighlightColor = color(235, 126, 106);
    dHighlightColor = color(212, 109, 80);
	} else {
		lightSquare = color(240, 217, 181);
	    darkSquare = color(181, 136, 99);
		lightColor = color(130, 151, 105);
		darkColor = color(100, 111, 64);
		lSquareColor = color(205, 210, 106);
		dSquareColor = color(170, 162, 58);
		lHoverColor = color(174, 177, 135);
	    dHoverColor = color(132, 121, 78);
	    lHighlightColor = color(235, 126, 106);
	    dHighlightColor = color(212, 109, 80);
	}
	//load image data
	  iwKing = loadImage("data/wK.png");
	  iwQueen = loadImage("data/wQ.png");
	  iwRook = loadImage("data/wR.png");
	  iwBishop = loadImage("data/wB.png");
	  iwKnight = loadImage("data/wN.png");
	  iwPawn = loadImage("data/wP.png");
	  ibKing = loadImage("data/bK.png");
	  ibQueen = loadImage("data/bQ.png");
	  ibRook = loadImage("data/bR.png");
	  ibBishop = loadImage("data/bB.png");
	  ibKnight = loadImage("data/bN.png");
	  ibPawn = loadImage("data/bP.png");
	  iwKing.resize(tileSize, tileSize);
	  ibKing.resize(tileSize, tileSize);
	  iwQueen.resize(tileSize, tileSize);
	  ibQueen.resize(tileSize, tileSize);
	  iwPawn.resize(tileSize, tileSize);
	  ibPawn.resize(tileSize, tileSize);
	  iwRook.resize(tileSize, tileSize);  
	  ibRook.resize(tileSize, tileSize); 
	  iwKnight.resize(tileSize, tileSize);
	  ibKnight.resize(tileSize, tileSize);
	  iwBishop.resize(tileSize, tileSize);  
	  ibBishop.resize(tileSize, tileSize);
	  surface.setLocation(100, 100);
	  surface.setResizable(false);
	  //calculates the "scale" of arrows, or how far they should be extended before intersecting the
	  //arrow's triangle. messy and a silly thing to do, but it is used for arrow transparency
	  scl = calcScl(tileSize/4, tileSize/5);
	  //creates the GUI elements
	  textbox = new TextField(this, new PVector(width/2 + 148, height/2 - 12), new PVector(100, 30));
	  seedbox = new TextField(this, new PVector(width/2 + 148, height/2 + 36), new PVector(100, 30));
	  host = new Button(this, "Host a Game");
	  host.setBackground(color(0, 255, 0));
	  host.setBounds((int) (width/3.5f), (int) (height/2), 150, 50);
	  joiner = new Button(this, "Join a Game");
	  joiner.setBackground(color(255, 0, 0));
	  joiner.setBounds((int) (width/2f), (int) (height/2), 150, 50);
  }
public void updatePermutationTable(int seed) {
	//this just updates the permutation table. see line 104 for a detailed explanation
	index = seed;
	randomSeed(index);
	char[] arr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', '#', '$', '%', '-', '&', '_', '+'};
	for(int i = 0; i < arr.length; i++) {
		arr[i] = Character.toUpperCase(arr[i]);
	}
	arr = scrambleContents(arr);
	char[][] arr2 = decodeBoard(arr, 11, 4);
	char[][] codes = new char[11][4];
	for(int i = 0; i < codes.length; i++) {
		codes[i][0] = arr2[i][0];
		codes[i][1] = arr2[i][1];
		codes[i][2] = arr2[i][2];
		codes[i][3] = arr2[i][3];
	}
	for(int i = 0; i < codes.length; i++) {
		for(int j = 0; j < 4; j++) {
			permutation[j+1][i] = codes[i][j];
		}
	}
}
public char[] scrambleContents(char[] arr) {
	//creates an array of indices in the array using the non repeating random number generator.
	//"scrambles" them according to the indices randomly generated within the array.
	char[] ret = new char[arr.length];
	int[] indices = nonRepeatingRNG(0, arr.length);
	for(int i = 0; i < arr.length; i++) {
		ret[i] = arr[indices[i]];
	}
	return ret;
}
public char randomChar() {
	//returns a random alphanumeric character
	boolean letter = random(0, 1) >= 0.5f;
	return letter ? Character.forDigit(floor(random(0, 10)), 10) : Character.toUpperCase(randomLetter());
}
public int[] toIntArray(String[] arr) {
	//turns an array of string integers into an array of integers ("69420" -> 69420)
	int[] ret = new int[arr.length];
	for(int i = 0; i < arr.length; i++) {
		ret[i] = Integer.parseInt(arr[i]);
	}
	return ret;
}
public String[] toStringArray(int[] arr) {
	//turns an array of integers into an array of string integers (69420 -> "69420")
	String[] ret = new String[arr.length];
	for(int i = 0; i < arr.length; i++) {
		ret[i] = "" + arr[i];
	}
	return ret;
}
public void setPerspective(Side side) {
	//sets the player's perspective and defines the array of squares represented as the board
	//also sets up the numbers and letters used to mark the board squares
	perspective = side;
	boardRepresentation = perspective == Side.WHITE ? boardRepresentationWhite : boardRepresentationBlack;
	if(perspective == Side.BLACK) {
		for(int i = numbers.length-1; i >= 0; i--) {
			numbers[i] = i+1;
		}
		letters = "hgfedcba";
	} else {
		for(int i = 0; i < numbers.length; i++) {
			numbers[i] = i+1;
		}
		letters = "abcdefgh";
	}
}
//main loop of the game
public void draw() {
	background(49, 46, 43);
    showBoard();
    if(isPlaying) {
    	//turns the board into a character array to display images
    char[][] map = decodeBoard(board.toStringFromViewPoint(perspective).toCharArray(), 8, 9);
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
        	//displays images according to the piece on the current square in the loop
          if (map[j][i] != ' ') {
            if (map[j][i] == bPawn) {
              image(ibPawn, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == bKnight) {
              image(ibKnight, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == bBishop) {
              image(ibBishop, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == bRook) {
              image(ibRook, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == bQueen) {
              image(ibQueen, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == bKing) {
              image(ibKing, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == wPawn) {
              image(iwPawn, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == wKnight) {
              image(iwKnight, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == wBishop) {
              image(iwBishop, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == wRook) {
              image(iwRook, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == wQueen) {
              image(iwQueen, (i*tileSize), (j*tileSize));
            } else if (map[j][i] == wKing) {
              image(iwKing, (i*tileSize), (j*tileSize));
            }
          }
        }
      }   
    //this checks if the opponent has made a move
    if(client.available() > 0) {
   byte[] move = client.readBytes(); 
   //just checks if all the requisite arguments are there
   if(move.length > 3) {
   if(decodeInt(move[5]) != perspective) doOpponentMove(move[0], move[1], move[2], move[3], move[4]);
   }
   client.clear();
      }
    } else {
    	//if not playing currently, this is the gui and server connection section
    	if(!displayJoinCode) {
    		//display the buttons to give an option of whether to join a game or host a game
    	host.show();
    	joiner.show();
    	} else {
    		//if the server isn't already instantiated, no need to do it again
          if(sketch != null) {
        	  //if the server has more than 1 client, then that means we can start the game
    		if(sketch.clients > 1) {
    			isPlaying = true;
    			client.side = random(0, 1) > 0.5f ? Side.WHITE : Side.BLACK;
    			setPerspective(client.side);
    			//sends a byte to the opponent pertaining to the side they are playing so that the opponent 
    			//plays the opposite side
    			client.write(new byte[] {(byte) (client.side == Side.WHITE ? 69 : 70)});
    		  }
    		}
    		if(isHost) {
    			if(startServer) {
    				//if you're the host, then this code starts the server
    				sketch = new ChessServer();
    				PApplet.runSketch(new String[] {"ChessServer"}, sketch);
    				try {
    					//creates a new client to connect to the server on your ip address.
						client = new Client(this, InetAddress.getLocalHost().getHostAddress(), ChessServer.port);
//    					client = new Client(this, "127.0.0.1", ChessServer.port);
					} catch (Exception e) {
						e.printStackTrace();
					}
    				//generates a code based on the permutation table generated earlier
    				code += generateCode();
    				startServer = false;
    			} else {
    				//gui
    				pushStyle();
    				textSize(24);
    				fill(0);
    				stroke(0);
    				textAlign(CENTER, CENTER);
    				text("Send this code to your friends: " + code, width/2, height/2);
    				text("Game server seed: " + index, width/2, height/2 + 30);
    				popStyle();
    			}
    		} else {
    			//more gui
    			pushStyle();
				textSize(24);
				fill(0);
				stroke(0);
				textAlign(CENTER, CENTER);
				text("Input game code here:", width/2, height/2);
				textbox.show();
				text("Input seed here:", width/2, height/2 + 48);
				seedbox.show();
				popStyle();
	    		byte val = 0;
	    		//if you finish typing in the textfield to enter the seed, then update the permutation table based on it
	    		if(seedbox.text != "" && !seedbox.selected && !textbox.selected && setSeed) {
	    			updatePermutationTable(Integer.parseInt(seedbox.text));
	    			setSeed = false;
	    		}
	    		//if you finish typing in the code for the game then connect to the server after decoding it into an ip address
	    		if(textbox.text != "" && !textbox.selected && !seedbox.selected && setClient) {
	    			client = new Client(this, decodeCode(textbox.text), ChessServer.port);
	    			setClient = false;
	    		}
	    		//sets your color based on the other player's color (opposite of theirs)
	    		if(client != null) {
	    			byte[] data = client.readBytes();
	    			if(data != null) {
	    		val = data[0];
	    		if(val != 0) {
	    			isPlaying = true;
	    			if(val == 69) {
	    				client.side = Side.BLACK;
	    				setPerspective(client.side);
	    			} else if(val == 70) {
	    				client.side = Side.WHITE;
	    				setPerspective(client.side);
	    			}
	    		}
	    		client.clear();
	    			}
	    		}
    		}
    	}
    }
  }
public int[] nonRepeatingRNG(int min, int max) {
	//non repeating random number generator
	 int[] list = new int[max];
	 ArrayList<Integer> remaining = new ArrayList<Integer>();
	 for(int i = 0; i < max; i++) {
	   remaining.add(i, i);
	 }
	 for(int i = 0; i < list.length; i++) {
	  int rand = floor(random((remaining.size() - 1) + 1 - min) + min);
	  int chosen = remaining.get(rand);
	  remaining.remove(rand);
	  list[i] = chosen;
	 }
	 return list;
	}
public int countOccurrences(String s, char c) {
	//counts occurrences of a character c in string s
	int occurrences = 0;
	for(int i = 0; i < s.length(); i++) {
		if(s.charAt(i) == c) occurrences++;
	}
	return occurrences;
}
public String generateCode() {
	//generates a code based on the server's ip address
	String str = "";
	String ip = client.ip();
	int len = ip.length();
	int dotLoop = 4;
	int[] occurrences = new int[10];
	for(int i = 0; i < occurrences.length; i++) {
		occurrences[i] = countOccurrences(ip, (char)(i+'0'));
	}
	//uses the permutation table to turn an ip address into an encrypted address
	for(int i = 0; i < len; i++) {
		char target = ip.charAt(i);
		if(target == '.') {
			str += Character.toUpperCase(permutation[dotLoop][10]);
			dotLoop--;
		} else {
			int asDigit = Integer.parseInt(Character.toString(target));
			str += Character.toUpperCase(permutation[occurrences[asDigit]][asDigit]);
			occurrences[asDigit]--;
		}
	}
	return str;
}
public String decodeCode(String c) {
	//decodes the code input by the person joining a server into an ip address to connect to
	//of course, it uses the permutation table
	String str = "";
	for(int i = 0; i < c.length(); i++) {
		str += characterMatchPermutation(c.charAt(i));
	}
	return str;
}
public char characterMatchPermutation(char c) {
	//does char c match one of the characters in the permutation table? 
	//if so, return the character at the top of the column it's in
	for(int i = 1; i < permutation.length; i++) {
		for(int j = 0; j < permutation[i].length; j++) {
			if(permutation[i][j] == c) {
				return permutation[0][j];
			}
		}
	}
	return c;
}
public char randomLetter() {
	//returns a random letter
	String abcs = "abcdefghijklmnopqrstuvwxyz";
	return abcs.charAt(floor(random(0, 26)));
}
public void mousePressed() {
	//function is called when the mouse is pressed
	//the player uses it to make moves
	if(isPlaying) {
	if(mouseButton == LEFT) {
		flaggedSquares.clear();
		arrows.clear();
		if(board.getSideToMove() == perspective) {
	if(!click) {
		x = constrain(round(mouseX/tileSize), 0, 7);
		y = constrain(round(mouseY/tileSize), 0, 7);
		if(board.getPiece(boardRepresentation[y][x]) != null) click = true;
	} else {
	x1 = constrain(round(mouseX/tileSize), 0, 7);
	y1 = constrain(round(mouseY/tileSize), 0, 7);
	Piece p = perspective == Side.WHITE ? Piece.WHITE_QUEEN : Piece.BLACK_QUEEN;
	boolean pawn = board.getPiece(boardRepresentation[y][x]) == Piece.WHITE_PAWN || board.getPiece(boardRepresentation[y][x]) == Piece.BLACK_PAWN;
	boolean pawnPromotion = pawn && y1 == 0 && y == 1;
	Move move;
	if(!pawnPromotion) {
		move = new Move(boardRepresentation[y][x], boardRepresentation[y1][x1]);
	} else {
		move = new Move(boardRepresentation[y][x], boardRepresentation[y1][x1], p);
	}
	if(board.isMoveLegal(move, true)) {
	//plays the move on the board and sends it to the server
	board.doMove(move);
	client.write(new byte[]{(byte) x, (byte) y, (byte) x1, (byte) y1, pieceToInt(move.promotion), (byte) (perspective == Side.WHITE ? 1 : 0)});
	click = false;
	  } else {
		  x = constrain(x1, 0, 7);
		  y = constrain(y1, 0, 7);
	    }
	  }
	}
  } else {
	  //adds flagged squares when the right mouse button is pressed
	  int[] square = new int[2];
	    square[0] = constrain(round(mouseX/tileSize), 0, 7);
		square[1] = constrain(round(mouseY/tileSize), 0, 7);
		if(!isInFlaggedSquares(square)) {
			flaggedSquares.add(square);
		} else {
			flaggedSquares.remove(flaggedSquares.indexOf(getSquare(square)));
		}
      }
	} else {
		if(host.mouseEntered) {
			displayJoinCode = true;
			isHost = true;
		} else if(joiner.mouseEntered) {
			displayJoinCode = true;
			isHost = false;
		}
	}
}
public void mouseDragged() {
	//if the mouse is dragged, create an arrow
	//*only if the mouse is dragged AND the right mouse button is pressed
	if(!dragging) {
		int[] square = new int[2];
	    square[0] = constrain(round(mouseX/tileSize), 0, 7);
		square[1] = constrain(round(mouseY/tileSize), 0, 7);
		if(getSquare(square) != null) {
	startx = constrain(round(mouseX/tileSize), 0, 7);
	starty = constrain(round(mouseY/tileSize), 0, 7);
	dragging = true;
		}
	}
}
public boolean equals(int[] a, int[] b) {
	//memory addresses between 2 arrays are different
	//this function compares their contents
	return a[0] == b[0] && a[1] == b[1];
}
public void mouseReleased() {
	//makes an arrow if the right mouse button is released after dragging it
	if(mouseButton == RIGHT) {
		dragging = false;
		int a = constrain(round(mouseX/tileSize), 0, 7);
		int b = constrain(round(mouseY/tileSize), 0, 7);
		PVector c = new PVector(startx, starty);
		PVector d = new PVector(a, b);
		Arrow arrow = new Arrow(startx, starty, a, b, isKnightMove(startx, starty, a, b), cardinal(startx, starty, a, b), diagonal(startx, starty, a, b));
		if(!c.equals(d)) {
			if(!isInArrows(arrow)) {
			arrows.add(arrow);
			} else {
				arrows.remove(getArrow(arrow));
			}
		}
	  }
}
public boolean cardinal(int a, int b, int a1, int b1) {
	//is there a cardinal direction specified from the absolute value of
	//the change in x and y? (N, E, S, W)
	PVector inc = new PVector(abs(a - a1), abs(b - b1));
	if(inc.x == 0 && inc.y != 0) return true;
	if(inc.y == 0 && inc.x != 0) return true;
	return false;
}
public boolean diagonal(int a, int b, int a1, int b1) {
	//is the change in x and y equal?
	return abs(a - a1) == abs(b - b1);
}
public float calcScl(float d, float weight) {
	//THIS APPLYS FOR D = 25 AND WEIGHT = 20 WITH TILESIZE = 100
	//to get the scale get the endpoints of the arrow. thats 50, or halfway inside the nearest tile. 
	//the size of the triangle is 25, and the tip of it is 12.5 (half that).
	//we add this to the midpoint, putting the value at 67.5.
	//subtract the size of the triangle (25) from that to get 37.5.
	//this is where the arrow line should end.
	//the weight of the arrow line is 20, so subtract that from it to get 17.5.
	//take the size divided by it (25/17.5) to get 1.4285715 as the scale.
	
	//the following algorithm condenses this
	return d / ((tileSize/2) - (d/2) - weight);
}
public void drawArrow(Arrow arrow) {
	//d is the triangle width, weight is the size of the line
	float d = tileSize/4;
	float weight = tileSize/5;
	//slope between the start and endpoints of the arrow
	PVector slope = new PVector(arrow.end.x - arrow.start.x, arrow.end.y - arrow.start.y);
	//normalizes the slope vector to a unit vector
	slope.normalize();
	//multiplies it by the scale calculated in calcScl()
	slope.mult(scl);
	slope.x = abs(slope.x);
	slope.y = abs(slope.y);
	int fillCol = color(255, 128, 0, 160);
	//slope v2
	PVector inc = new PVector(abs(arrow.start.x - arrow.end.x), abs(arrow.start.y - arrow.end.y));
	//this chunk of code determines where to start and end the arrow lines
	float d1 = arrow.tileDist.x < 0 ? (-d/scl) : (d/scl);
	float d2 = arrow.tileDist.y < 0 ? (-d/scl) : (d/scl);
	if(arrow.isCardinalArrow) {
		d1 *= scl;
		d2 *= scl;
	}
	if(arrow.tileDist.x == 0) d1 = 0;
	if(arrow.tileDist.y == 0) d2 = 0;
	pushStyle();
	if(!arrow.isKnightArrow) {
	//a is the angle that the triangle is rotated by (slope of the arrow converted to an angle)
    float a = atan2(arrow.start.x-arrow.end.x, arrow.end.y-arrow.start.y);
	pushMatrix();
	stroke(fillCol);
	strokeWeight(weight);
	strokeCap(SQUARE);
	if(arrow.isCardinalArrow || arrow.isDiagonalArrow) {
        line(arrow.start.x, arrow.start.y, arrow.end.x + d1, arrow.end.y + d2);
	} else {
		d1 *= slope.x;
		d2 *= slope.y;
		line(arrow.start.x, arrow.start.y, arrow.end.x + d1, arrow.end.y + d2);
	}
	noStroke();
	fill(fillCol);
  translate(arrow.end.x, arrow.end.y);
	rotate(a);
	fill(fillCol);
	triangle(-d, -d, d, -d, 0, d/2);
	popMatrix();
	} else {
		//draws a knight arrow
		if(inc.x < inc.y) {
		float a = atan2(arrow.start.x-arrow.end.x, 0);
		float a2 = arrow.tileDist.x > 0 ? weight/2 : -weight/2;
		float a3 = arrow.tileDist.y > 0 ? weight/2 : -weight/2;
		pushMatrix();
		stroke(fillCol);
		strokeWeight(weight);
		strokeCap(SQUARE);
	    line(arrow.start.x, arrow.start.y, arrow.start.x, arrow.end.y + a2);
		noStroke();
		fill(fillCol);
	  translate(arrow.end.x, arrow.end.y);
		rotate(a);
		fill(fillCol);
		triangle(-d, -d, d, -d, 0, d/2);
		popMatrix();
		pushMatrix();
		stroke(fillCol);
		strokeWeight(weight);
		float d11 = arrow.tileDist.x < 0 ? -d : d;
	  line(arrow.start.x + a3, arrow.end.y, arrow.end.x + d11, arrow.end.y);
		noStroke();
		fill(fillCol);
		popMatrix();
		} else {
			float a = atan2(0, arrow.end.y-arrow.start.y);
			float a2 = arrow.tileDist.x > 0 ? weight/2 : -weight/2;
			float a3 = arrow.tileDist.y > 0 ? weight/2 : -weight/2;
			pushMatrix();
			stroke(fillCol);
			strokeWeight(weight);
			strokeCap(SQUARE);
		  line(arrow.start.x, arrow.start.y, arrow.end.x + a2, arrow.start.y);
			noStroke();
			fill(fillCol);
		  translate(arrow.end.x, arrow.end.y);
			rotate(a);
			fill(fillCol);
			triangle(-d, -d, d, -d, 0, d/2);
			popMatrix();
			
			pushMatrix();
			stroke(fillCol);
			strokeWeight(weight);
			float d21 = arrow.tileDist.y < 0 ? -d : d;
		  line(arrow.end.x, arrow.start.y + a3, arrow.end.x, arrow.end.y + d21);
			noStroke();
			fill(fillCol);
			popMatrix();
		}
	}
	popStyle();
}
public boolean isKnightMove(int a, int b, int a1, int b1) {
	//does the distance between vector <a, b> and <a1, b1> resemble that of a knight move
	//(2 squares in 1 direction, 2 squares in the other)
	return (abs(a - a1) == 2 && abs(b - b1) == 1) || (abs(a - a1) == 1 && abs(b - b1) == 2);
}
boolean isInFlaggedSquares(int[] square) {
	//compares contents of int[] square and all the squares in the flagged squares 
	for(int[] sq : flaggedSquares) {
		if(sq[0] == square[0] && sq[1] == square[1]) return true;
	} 
	return false;
}
boolean isInArrows(Arrow arrow) {
	//same concept as isInFlaggedSquares() but with arrows
	for(Arrow a : arrows) {
		if(arrow.start.equals(a.start) && arrow.end.equals(a.end) && a.isKnightArrow == arrow.isKnightArrow) return true;
	} 
	return false;
}
int[] getSquare(int[] square) {
	//if int[] square is equal to any of the squares in the flagged squares, then return its clone in the
	//flagged squares arraylist. used for the removal of squares in the flagged squares
	for(int[] sq : flaggedSquares) {
		if(sq[0] == square[0] && sq[1] == square[1]) return sq;
	} 
	return null;
}
Arrow getArrow(Arrow arrow) {
	//same concept as getSquare() but with arrows
	for(Arrow a : arrows) {
		if(arrow.start.equals(a.start) && arrow.end.equals(a.end) && a.isKnightArrow == arrow.isKnightArrow) return a;
	} 
	return null;
}
public void doOpponentMove(int ox, int oy, int ox1, int oy1, int p) {
	//does the opponent's move based on input received from the client
	try {
	Square[][] Board = perspective == Side.WHITE ? boardRepresentationBlack : boardRepresentationWhite;
	opx = sevenToZero(ox);
	opy = sevenToZero(oy);
	opx1 = sevenToZero(ox1);
	opy1 = sevenToZero(oy1);
	Move move = new Move(Board[oy][ox], Board[oy1][ox1], intToPiece(p));
	board.doMove(move);
	} catch (Exception e) {
		
	}
}
public int sevenToZero(int num) {
	return 7 - num;
}
public Piece intToPiece(int piece) {
	switch(piece) {
	case 3:
		return Piece.WHITE_QUEEN;
	case 2:
		return Piece.WHITE_ROOK;
	case 1: 
		return Piece.WHITE_BISHOP;
	case 0:
		return Piece.WHITE_KNIGHT;
	case -1:
		return Piece.BLACK_QUEEN;
	case -2:
		return Piece.BLACK_ROOK;
	case -3:
		return Piece.BLACK_BISHOP;
	case -4:
		return Piece.BLACK_KNIGHT;
		default:
			return Piece.NONE;
	}
}
public byte pieceToInt(Piece piece) {
	switch(piece) {
	case WHITE_QUEEN:
		return 3;
	case WHITE_ROOK:
		return 2;
	case WHITE_BISHOP:
		return 1;
	case WHITE_KNIGHT:
		return 0;
	case BLACK_QUEEN:
		return -1;
	case BLACK_ROOK:
		return -2;
	case BLACK_BISHOP:
		return -3;
	case BLACK_KNIGHT:
		return -4;
		default:
			return -5;
	}
}
boolean isGameOver(Board board) {
	if(board.isDraw()) {
		println("Draw");
		return true;
	}
	if(board.isMated()) {
		if(board.getSideToMove() == Side.WHITE) {
		  println("Black won by Checkmate");
		} else if(board.getSideToMove() == Side.BLACK) {
		  println("White won by Checkmate");
		}
		return true;
	}
	return false;
}
public long perft(Board board, int depth, int ply) {
    if (depth == 0) {
        return 1;
    }
    long nodes = 0;      
    List<Move> moves = board.legalMoves();
    for (Move move : moves) {
        board.doMove(move);
        nodes += perft(board, depth - 1, ply + 1);
        board.undoMove();
    }
    return nodes;
}
public void showBoard() {
	  noStroke();
	  textSize(tileSize/5);
	  for (int i = 0; i<8; i++) {
	    for (int j = 0; j<8; j++) { 
	      if ((i+j)%2 == 0) fill(lightSquare);
	      else fill(darkSquare);
	      rect((i*tileSize), (j*tileSize), tileSize, tileSize);
	    }
	  }
	  //displays files and ranks
	  if(perspective == Side.WHITE) {
      for (int x = 1; x <= 8; x++) {
          int num = numbers[numbers.length-x];
          textSize(tileSize/5);
          if (x % 2 == 0) {
            fill(lightSquare);
          } else {
            fill(darkSquare);
          }
          text(num, tileSize/15, ((x*(tileSize))-(tileSize*0.8f)));
        }
        for (int y = 0; y < 8; y++) {
          char letter = letters.charAt(y);
          if (y % 2 == 0) {
            fill(lightSquare);
          } else {
            fill(darkSquare);
          }
          text(letter, ((y*(tileSize))+(tileSize*0.8f)), ((8*tileSize)-(tileSize*0.125f)));
        }
	  } else {
		  for (int x = 0; x < numbers.length; x++) {
		        int num = numbers[x];
		        textSize(tileSize/5);
		        if (x % 2 != 0) {
		          fill(lightSquare);
		        } else {
		          fill(darkSquare);
		        }
		        text(num, tileSize/15, ((x*(tileSize))+(tileSize*0.2f)));
		      }
		      for (int y = 0; y < 8; y++) {
		        char letter = letters.charAt(y);
		        if (y % 2 == 0) {
		          fill(lightSquare);
		        } else {
		          fill(darkSquare);
		        }
		        text(letter, ((y*(tileSize))+(tileSize*0.8f)), ((8*tileSize)-(tileSize*0.125f)));
		      }
	  }
	  //shows on the board what the legal moves are
	  List<int[]> moves = movesFromSquare(boardRepresentation[y][x]);
	  for(int[] move : moves) {
		  int i = move[0];
		  int j = move[1];
			  if(board.getPiece(boardRepresentation[j][i]) == Piece.NONE) {
				  noStroke();
			  if ((i+j) % 2 == 0) {
	              fill(lightColor);
	            } else {
	              fill(darkColor);
	            }
              ellipse(((i*tileSize)+(tileSize/2)), ((j*tileSize)+(tileSize/2)), tileSize/4f, tileSize/4f);
			  } else {
	              noFill();
	              strokeWeight(0.08f*tileSize);
	              if ((i+j) % 2 == 0) {
	                stroke(lightColor);
	              } else {
	                stroke(darkColor);
	              }
	              ellipse(((i*tileSize)+(tileSize/2)), ((j*tileSize)+(tileSize/2)), 0.92f*tileSize, 0.92f*tileSize);
			  }
	  }
	  //highlights the current selected square
		  noStroke();
		  if(board.getSideToMove() != perspective) {
		  highlight(x, y);
		  highlight(x1, y1);
		  if(board.getPiece(boardRepresentation[y1][x1]) != Piece.NONE) {
	        	if ((x + y) % 2 == 0) {
		            fill(lSquareColor);
		          } else {
		            fill(dSquareColor);
		          }
		          rect(x*tileSize, y*tileSize, tileSize, tileSize);
	        }
		  }
		  //highlights the square the opponent moved to
		  if(board.getSideToMove() == perspective) {
		  highlight(opx, opy);
		  highlight(opx1, opy1);
		  if(board.getPiece(boardRepresentation[opy1][opx1]) != Piece.NONE) {
	        	if ((opx + opy) % 2 == 0) {
		            fill(lSquareColor);
		          } else {
		            fill(dSquareColor);
		          }
		          rect(opx*tileSize, opy*tileSize, tileSize, tileSize);
	        }
		  }
		  //highlights the square the player is hovering over
		  if(colorScheme == LICHESS) {
		  int i = constrain(round(mouseX/tileSize), 0, 7);
		  int j = constrain(round(mouseY/tileSize), 0, 7);
		  boolean inList = false;
		  for(int[] move : moves) {
			  if(move[0] == i && move[1] == j) {
				  inList = true;
				  break;
			  }
		  }
		  if(inList) {
	              if ((i + j) % 2 == 0) {
	                fill(lHoverColor);
	              } else {
	                fill(dHoverColor);
	              }
	              rect((i*tileSize), (j*tileSize), tileSize, tileSize);
		    }
		  }
		  //highlights the squares that the player has flagged
		  for(int[] square : flaggedSquares) {
			  highlight(square[0], square[1], lHighlightColor, dHighlightColor);
		  }
		  for(Arrow arrow : arrows) {
			  drawArrow(arrow);
		  }
	}
public Side decodeInt(int x) {
	return x == 1 ? Side.WHITE : Side.BLACK;
}
public Side opposite(Side side) {
	return side == Side.WHITE ? Side.BLACK : Side.WHITE;
}
public void highlight(int x2, int y2) {
	//if square at x2 and y2 is empty, highlight it
	if (board.getPiece(boardRepresentation[y2][x2]) != Piece.NONE) {
	if ((x2 + y2) % 2 == 0) {
        fill(lSquareColor);
      } else {
        fill(dSquareColor);
      }
      rect(x2*tileSize, y2*tileSize, tileSize, tileSize);
	}
}
public void highlight(int x2, int y2, int col1, int col2) {
	//highlight the square at x2, y2 with colors col1 and col2 regardless of it being empty or not
	if ((x2 + y2) % 2 == 0) {
        fill(col1);
      } else {
        fill(col2);
      }
      rect(x2*tileSize, y2*tileSize, tileSize, tileSize);
}
List<int[]> movesFromSquare(Square sq) {
	//get a list of legal moves from Square sq
	List<int[]> moves = new ArrayList<int[]>();
	List<Move> legalMoves = board.legalMoves();
	for(Move move : legalMoves) {
		if(move.from == sq) moves.add(squareToIntArray(move.to));
	}
	return moves;
}
public int[] squareToIntArray(Square sq) {
	for(int i = 0; i < boardRepresentation.length; i++) {
		for(int j = 0; j < boardRepresentation[i].length; j++) {
			if(boardRepresentation[j][i] == sq) return new int[]{i, j};
		}
	}
	return null;
}
public char[][] decodeBoard( final char[] array, final int rows, final int cols ) {
    if (array.length != (rows*cols))
        throw new IllegalArgumentException("Invalid array length");

    char[][] bidi = new char[rows][cols];
    for ( int i = 0; i < rows; i++ )
        System.arraycopy(array, (i*cols), bidi[i], 0, cols);

    return bidi;
}
public void printArr(char[][] b) {
	  for (int j = 0; j < b.length; j++) {
	    String rank = "";
	    for (int i = 0; i < b[j].length; i++) {
	      rank += "|" + b[j][i] + "|";
	    }
	    println(rank);
	  }
	}
public void printBoard(char[][] b) {
	  for (int j = 0; j < b.length; j++) {
	    String rank = "";
	    for (int i = 0; i < b[j].length; i++) {
	      if (b[j][i] == bKing) {
	        rank += "|k|";
	      } else if (b[j][i] == bQueen) {
	        rank += "|q|";
	      } else if (b[j][i] == bRook) {
	        rank += "|r|";
	      } else if (b[j][i] == bBishop) {
	        rank += "|b|";
	      } else if (b[j][i] == bKnight) {
	        rank += "|n|";
	      } else if (b[j][i] == bPawn) {
	        rank += "|p|";
	      }
	      if (b[j][i] == ' ') {
	        rank += "| |";
	      }
	      if (b[j][i] == wKing) {
	        rank += "|K|";
	      } else if (b[j][i] == wQueen) {
	        rank += "|Q|";
	      } else if (b[j][i] == wRook) {
	        rank += "|R|";
	      } else if (b[j][i] == wBishop) {
	        rank += "|B|";
	      } else if (b[j][i] == wKnight) {
	        rank += "|N|";
	      } else if (b[j][i] == wPawn) {
	        rank += "|P|";
	      }
	    }
	    println(rank);
	  }
	}
}
