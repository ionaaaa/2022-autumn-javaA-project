package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;

public class HorseChessComponent extends ChessComponent
{
    public HorseChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size,int rank)
    {
        super(chessboardPoint, location, chessColor, clickController, size,5);
        if (this.getChessColor() == ChessColor.RED) {
            name = "马";
        } else {
            name = "马";
        }
    }
}