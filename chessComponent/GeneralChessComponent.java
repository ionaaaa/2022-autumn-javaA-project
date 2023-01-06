package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;

public class GeneralChessComponent extends ChessComponent
{
    public GeneralChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size,int rank)
    {
        super(chessboardPoint, location, chessColor, clickController, size,1);
        if (this.getChessColor() == ChessColor.RED) {
            name = "帅";
        } else {
            name = "将";
        }
    }
}