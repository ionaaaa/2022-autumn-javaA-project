package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;

public class AdvisorChessComponent extends ChessComponent
{
    public AdvisorChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size,int rank)
    {
        super(chessboardPoint, location, chessColor, clickController, size,2);
        if (this.getChessColor() == ChessColor.RED) {
            name = "士";
        } else {
            name = "仕";
        }
    }
}