package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;

public class MinisterChessComponent extends ChessComponent
{
    public MinisterChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size,int rank)
    {
        super(chessboardPoint, location, chessColor, clickController, size,3);
        if (this.getChessColor() == ChessColor.RED) {
            name = "象";
        } else {
            name = "相";
        }
    }
}