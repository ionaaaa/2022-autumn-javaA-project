package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;

/**
 * 这个类表示棋盘上的空棋子的格子
 */
public class EmptySlotComponent extends SquareComponent {

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ClickController listener, int size, int rank) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size, rank);
    }

    @Override
    public boolean canMoveTo(SquareComponent[][] chessboard, ChessboardPoint destination, SquareComponent selected) {
        return false;
    }

}
