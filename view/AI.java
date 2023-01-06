package view;

import java.util.ArrayList;
import chessComponent.*;

import chessComponent.SquareComponent;
import model.ChessColor;
import model.ChessboardPoint;
import java.io.IOException;

public class AI
{
    private int[] dxList=new int[]{0,1,-1,0,0};
    private int[] dyList=new int[]{0,0,0,1,-1};
    private double[] Levelscore=new double[20];
    private final Chessboard board;
    private final double fail=-1.74256952;
    private int X,Y,DX,DY;
    private double ans=-100.0;
    //记录每层的最大分数


    public AI(Chessboard chessboard) 
    {
        this.board = chessboard;
    }
    public int getX()
    {
        return this.X;
    }

    public int getY()
    {
        return this.Y;
    }

    public int getDX()
    {
        return this.DX;
    }

    public int getDY()
    {
        return this.DY;
    }

    public double max(double A,double B)
    {
        if(A>B) return A;
        return B;
    }

    public void swapMyChess(SquareComponent A,SquareComponent B)
    {
        int t1=A.getChessboardPoint().getX(),t2=A.getChessboardPoint().getY();
        int t3=B.getChessboardPoint().getX(),t4=B.getChessboardPoint().getY();

       Chessboard.getChessComponents()[t1][t2]=B;
       Chessboard.getChessComponents()[t3][t4]=A;

       A.setChessboardPoint(new ChessboardPoint(t3, t4));
       B.setChessboardPoint(new ChessboardPoint(t1,t2));
    }

    public int judge(int x,int y,int dx,int dy)
    {
        if(x<0||x>7) return -1;
        if(y<0||y>3) return -1;
        if(x+dx<0||x+dx>7) return -1;
        if(y+dy<0||y+dy>3) return -1;

        if(Chessboard.getChessComponents()[x][y] instanceof EmptySlotComponent) return -1;
        if(!Chessboard.getChessComponents()[x][y].isReversal()) return 2;

        return 1;
        //如果两个棋子不出界，那么他们可以交换或者执行下一步操作
    }
    public double search(int x,int y,int dx,int dy,double score,int step)
    {
        if(step==7) return score;

        if((step==3||step==5)&&score<Levelscore[step]-10) return fail;
        if((step==5)&&score<ans-10) return fail;

        //如果搜索了5个来回，返回对应值
        //如果当前分数过低，退出
        //有棋子出界，退出

        Levelscore[step]=max(score,Levelscore[step]);
        //保存对应层数的最大值

        //改了个static
        if(Chessboard.getChessComponents()[x][y] instanceof EmptySlotComponent) return fail;

    //    if(!Chessboard.getChessComponents()[x][y].isReversal()&&step%2==1)
    //    {
    //        return 0;
     //   }//如果这个棋子是翻面的,且轮到AI走

        if(step%2==1) //说明此时是AI走
        if(Chessboard.getChessComponents()[x][y].isReversal())
        if(Chessboard.getChessComponents()[x][y].getChessColor()==ChessColor.BLACK) //走黑棋？不对，应该走红棋
        return fail;

        if(step%2==0) //说明此时是模拟人类的下一步
        if(Chessboard.getChessComponents()[x][y].isReversal())
        if(Chessboard.getChessComponents()[x][y].getChessColor()==ChessColor.RED)
        return fail;


        SquareComponent now=Chessboard.getChessComponents()[x][y];
        SquareComponent enemy=Chessboard.getChessComponents()[x+dx][y+dy];
        //你就是我们要对付的棋子，小子
        

        //如果可以now可以移动到enemy的位置
        if(now.canMoveTo(Chessboard.getChessComponents(),new ChessboardPoint(x+dx, y+dy), now))
        {
            double nowscore=0.0;
            double site=0.0;
            //暂存后续搜索

            double res=0.0;
            if(step%2==1) res=100;
            if(step%2==0) res=-100;
            //如果后面是人类走，需要找出人类的最强应对，然后减去
            //如果后面是AI走，需要找出AI的最强应对，然后加上


            if(enemy.getRank()==1) nowscore=30.0;
            if(enemy.getRank()==2) nowscore=12.0;
            if(enemy.getRank()==3) nowscore=8.0;
            if(enemy.getRank()==4) nowscore=6.0;
            if(enemy.getRank()==5) nowscore=3.0;
            if(enemy.getRank()==6) nowscore=3.0;
            if(enemy.getRank()==0) nowscore=8.0;
            if(enemy.getRank()==-1) nowscore=0.0;

            if(enemy.getChessColor()==ChessColor.RED) nowscore=-nowscore;
            if(!enemy.isReversal()) nowscore=0;

            
            for(int i=0;i<=7;i++)
            for(int r=0;r<=3;r++)
            for(int j=1;j<=4;j++)
            {
                int t1=dxList[j],t2=dyList[j];
                if(Chessboard.getChessComponents()[i][r].getRank()==0)
                {
                    t1*=2;
                    t2*=2;
                }
                //如果这个棋子是炮的话，能且只能移动两格

                if(judge(i,r,t1,t2)==1)
                {
                    swapMyChess(Chessboard.getChessComponents()[x][y], Chessboard.getChessComponents()[x+dx][y+dy]);
                    site=search(i,r,t1,t2,nowscore+score,step+1);
                    swapMyChess(Chessboard.getChessComponents()[x][y], Chessboard.getChessComponents()[x+dx][y+dy]);
                }
                else if(judge(i,r,t1,t2)==2) 
                {
                    if(j>1) continue;
                    site=0;
                }
                //如果下一步是翻面的话，只考虑一次

                if(site==fail) continue;//如果出错，不再考虑后续情况

                else if(step%2==1&&site<res)
                {
                    res=site;
                }
                else if(step%2==0&&site>res)
                {
                    res=site;
                }
                //如果当前回合是奇数，那么后续搜索人类能找到的最强应对
                //如果当前回合是偶数，考虑后续AI的最强应对
            }
            if(res==-100||res==100) res=0;
                //特殊，如果后续无法更新，调成0
            return nowscore+res;
        }
        //连执行这一步都做不到，滚
        return score;
    }
    public void work()
    {
        for(int i=0;i<=10;i++) Levelscore[i]=-100;
        ans=-1000;
        X=Y=DX=DY=0;
        double avg=0.0,avt=0.0;
        //初始化
        for(int i=0;i<=7;i++)
        for(int r=0;r<=3;r++)
        for(int j=1;j<=4;j++)
        {
            int t1=dxList[j],t2=dyList[j];
            //System.out.printf("%d %d %d %d%n",i,r,t1,t2);
            double site=-100.0;
            if(Chessboard.getChessComponents()[i][r].getRank()==0)
            {
                t1*=2;
                t2*=2;
            }
                //如果这个棋子是炮的话，能且只能移动两格
            if(judge(i,r,t1,t2)==1) 
            {
                SquareComponent now=Chessboard.getChessComponents()[i][r];
                if(!now.canMoveTo(Chessboard.getChessComponents(),new ChessboardPoint(i+t1, r+t2), now)) continue;
                if(now.getChessColor()==ChessColor.BLACK) continue;

                site=search(i,r,t1,t2,0,1);

                avg+=site;
                avt++;
                //对于可以移动的棋子，记录其平均得分
                System.out.printf("%d %d %d %d %f%n",i,r,t1,t2,site);
            }else continue;

            if(site>=ans)
            {
                ans=site;
                X=i;
                Y=r;
                DX=t1;
                DY=t2;
                //只有回溯到最初状态，才记录
            }
        }
        System.out.printf("%f%n",ans);
        for(int i=0;i<=7;i++)
        for(int r=0;r<=3;r++)
        {
            double site=0,safe=0;
            for(int t1=-1;t1<=1;t1++)
            for(int t2=-1;t2<=1;t2++)
            {
                if(judge(i+t1,r+t2,0,0)==1)
                {
                    if(Chessboard.getChessComponents()[i+t1][r+t2].getChessColor()==ChessColor.RED) safe+=1;
                    else safe-=1;
                }
            }
            if(judge(i,r,1,0)==2) 
            {
                if(avt!=0) site=avg/avt+1+safe;
                else site=0;

                if(site>ans&&site>-1000.0)
                {
                    ans=site;
                    X=i;
                    Y=r;
                    DX=DY=0;
                }
                System.out.printf("%d %d %f%n",i,r,site);
            }//对于翻棋，价值取移动棋子的平均值
        }


        if(Chessboard.getChessComponents()[X][Y].isReversal())
        {
            board.swapChessComponents(Chessboard.getChessComponents()[X][Y],Chessboard.getChessComponents()[X+DX][Y+DY]);
            System.out.println("移动");
        }
        else
        {
            System.out.println("翻面");
            Chessboard.getChessComponents()[X][Y].setReversal(true);
            Chessboard.getChessComponents()[X][Y].repaint();
        }

        for(int i=0;i<=7;i++)
        {
            for(int r=0;r<=3;r++)
            System.out.printf(Chessboard.getChessComponents()[i][r].getName()+" ");
            System.out.println();
        }
       // for(int i=0;i<=7;i++)
     //   {
      //      for(int r=0;r<=3;r++)
      //      System.out.printf(Chessboard.getChessComponents()[i][r].getChessColor()+" ");
       //     System.out.println();
      //  }
        System.out.printf("%d %d %d %d \n",X,Y,DX,DY);
        System.out.println(Chessboard.getChessComponents()[X][Y].getName());
        System.out.printf("ans=%f%n",ans);
    }
}