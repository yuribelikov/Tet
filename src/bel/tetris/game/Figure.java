package bel.tetris.game;

import java.awt.*;

class Figure
{
  private final static int TYPE_UNDEFINED = 0;
  private final static int TYPE_LSTAIR = 1;
  private final static int TYPE_RSTAIR = 2;
  private final static int TYPE_PODIUM = 3;
  private final static int TYPE_LCORNER = 4;
  private final static int TYPE_RCORNER = 5;
  private final static int TYPE_SQUARE = 6;
  private final static int TYPE_LINE = 7;
  private final static int NUMBER = 7;
  final static int SIZE = 4;   // figure width/height

  private static int[] lastFigures = new int[2];
  private int type;
  private boolean[][][] contents;    // [rotation][y][x]
  private int rotation = 0;
  Point pos;


  Figure(Point startPos)
  {
    type = TYPE_UNDEFINED;
    while (type == TYPE_UNDEFINED || (type == lastFigures[0] && type == lastFigures[1]))
      type = (int) Math.round(NUMBER * Math.random());

    lastFigures[0] = lastFigures[1];
    lastFigures[1] = type;
    pos = new Point(startPos.x, startPos.y);
//    type = TYPE_LINE;
    createContents();
  }

  boolean[][] getCurrContents()
  {
    return contents[rotation];
  }

  void rotate()
  {
    rotation--;
    if (rotation < 0)
      rotation = 3;
  }

  void rotateBack()
  {
    rotation++;
    if (rotation > 3)
      rotation = 0;
  }

  private void createContents()
  {
    contents = new boolean[4][SIZE][SIZE];
    switch (type)
    {
      case TYPE_LSTAIR:
        contents[0][1][2] = contents[0][1][3] =                     //  xx
        contents[0][2][1] = contents[0][2][2] = true;            // xx

        contents[1][1][1] =                                              // x
        contents[1][2][1] = contents[1][2][2] =                // xx
        contents[1][3][2] = true;                                      //  x

        contents[2][1][2] = contents[2][1][3] =                //  xx
        contents[2][2][1] = contents[2][2][2] = true;            // xx

        contents[3][1][1] =                                          // x
        contents[3][2][1] = contents[3][2][2] =                // xx
        contents[3][3][2] = true;                                    //  x
        break;

      case TYPE_RSTAIR:
        contents[0][1][0] = contents[0][1][1] =                // xx
        contents[0][2][1] = contents[0][2][2] = true;            //  xx

        contents[1][1][2] =                                          //  x
        contents[1][2][1] = contents[1][2][2] =                // xx
        contents[1][3][1] = true;                                    // x

        contents[2][1][0] = contents[2][1][1] =                // xx
        contents[2][2][1] = contents[2][2][2] = true;            //  xx

        contents[3][1][2] =                                          //  x
        contents[3][2][1] = contents[3][2][2] =                // xx
        contents[3][3][1] = true;                                    // x
        break;

      case TYPE_PODIUM:
        contents[0][1][2] =                                          //   x
        contents[0][2][1] = contents[0][2][2] =                //  xx
        contents[0][3][2] = true;                                    //   x

        contents[1][1][1] =                                                              //  x
        contents[1][2][0] = contents[1][2][1] = contents[1][2][2] = true;      // xxx

        contents[2][1][1] =                                          //  x
        contents[2][2][1] = contents[2][2][2] =                //  xx
        contents[2][3][1] = true;                                    //  x

        contents[3][1][0] = contents[3][1][1] = contents[3][1][2] =            // xxx
        contents[3][2][1] = true;                                                        //  x
        break;

      case TYPE_LCORNER:
        contents[0][1][1] = contents[0][1][2] =                //  xx
        contents[0][2][2] =                                          //   x
        contents[0][3][2] = true;                                    //   x

        contents[1][1][2] =                                                              //   x
        contents[1][2][0] = contents[1][2][1] = contents[1][2][2] = true;      // xxx

        contents[2][1][1] =                                          //  x
        contents[2][2][1] =                                          //  x
        contents[2][3][1] = contents[2][3][2] = true;            //  xx

        contents[3][1][0] = contents[3][1][1] = contents[3][1][2] =            // xxx
        contents[3][2][0] = true;                                                        // x
        break;

      case TYPE_RCORNER:
        contents[0][1][1] = contents[0][1][2] =                //  xx
        contents[0][2][1] =                                          //  x
        contents[0][3][1] = true;                                    //  x

        contents[1][1][0] = contents[1][1][1] = contents[1][1][2] =            // xxx
        contents[1][2][2] = true;                                                        //   x

        contents[2][1][2] =                                          //   x
        contents[2][2][2] =                                          //   x
        contents[2][3][1] = contents[2][3][2] = true;            //  xx

        contents[3][1][0] =                                                              // x
        contents[3][2][0] = contents[3][2][1] = contents[3][2][2] = true;      // xxx
        break;

      case TYPE_SQUARE:
        for (int n = 0; n < 4; n++)
          contents[n][1][1] = contents[n][1][2] = contents[n][2][1] = contents[n][2][2] = true;
        break;

      case TYPE_LINE:
        for (int i = 0; i < 4; i++)
          contents[0][2][i] = contents[2][2][i] = contents[1][i][2] = contents[3][i][2] = true;
        break;
    }
  }

  public String toString()
  {
    return "Figure : " + type + " [" + 90 * rotation + "], " + pos;
  }
}
