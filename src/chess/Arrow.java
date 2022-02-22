package chess;
import processing.core.PVector;
//This is just a data structure to hold start and end positions for arrows
public class Arrow {
  public boolean isKnightArrow = false;
  public boolean isCardinalArrow = false;
  public boolean isDiagonalArrow = false;
  PVector start = new PVector(0, 0);
  PVector end = new PVector(0, 0);
  PVector tileDist = new PVector(0, 0);
  public Arrow(int x, int y, int x1, int y1, boolean knightMove, boolean cardinalArrow, boolean diagonalArrow) {
	  start = new PVector(x, y).mult(Chess.tileSize);
	  end = new PVector(x1, y1).mult(Chess.tileSize);
	  tileDist = new PVector(x - x1, y - y1);
	  start.add(Chess.tileSize/2, Chess.tileSize/2);
	  end.add(Chess.tileSize/2, Chess.tileSize/2);
	  isKnightArrow = knightMove;
	  isCardinalArrow = cardinalArrow;
	  isDiagonalArrow = diagonalArrow;
  }
  public boolean equals(Arrow b) {
	  return start.equals(b.start) && end.equals(b.end);
  }
}
