package chess;
import com.github.bhlangonijr.chesslib.Side;

import processing.core.*;
public class ChessServer extends PApplet {
    public Server server;
    public static final int port = 10002;
    public int clients = 0;
    Side takenSide;
    public static void main(String[] args) {
    	String[] processingArgs = {"ChessServer"};
    	ChessServer mySketch = new ChessServer();
    	PApplet.runSketch(processingArgs, mySketch);
    }
    public void setup() {
    	server = new Server(this, port);
    }
    public void draw() {
    	  Client thisClient = server.available();
    	  try {
    	    if (thisClient != null) {
    	    	byte[] data = thisClient.readBytes();
    	    	if(data != null) {
    	      server.write(data);
    	    	}
    	    }
    	  } 
    	  catch (Exception e) {
    	  }
    	}
    public void serverEvent(Server s, Client c) {
    	clients++;
    	c.write(clients);
    }
}
