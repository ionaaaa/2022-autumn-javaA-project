package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;

public class CannonChessComponent extends ChessComponent
{
    public CannonChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size,int rank)
    {
        super(chessboardPoint, location, chessColor, clickController, size,0);
        if (this.getChessColor() == ChessColor.RED) {
            name = "炮";
        } else {
            name = "炮";
        }
    }
}