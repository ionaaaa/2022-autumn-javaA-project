package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;

import java.awt.*;

public class SoldierChessComponent extends ChessComponent {

    public SoldierChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size,int rank) {
        super(chessboardPoint, location, chessColor, clickController, size,6);
        if (this.getChessColor() == ChessColor.RED) {
            name = "兵";
        } else {
            name = "卒";
        }
    }

}
