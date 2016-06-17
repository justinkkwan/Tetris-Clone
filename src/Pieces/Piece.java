package Pieces;

/**
 * Created by Justin Kwan on 03/06/2016.
 */
public class Piece {

    private static final int[][][] table = new int[][][]{
            //L piece:
            {
                    {             2,0,
                        0,1, 1,1, 2,1},

                    {       1,0,
                            1,1,
                            1,2, 2,2},

                    {       0,1, 1,1, 2,1,
                            0,2},

                    {   0,0, 1,0,
                             1,1,
                             1,2}
            },
            //J piece:
            {
                    {0,0, 0,1, 1,1, 2,1},

                    {1,0, 2,0, 1,1, 1,2},

                    {0,1, 1,1, 2,1, 2,2},

                    {1,0, 1,1, 0,2, 1,2}
            },
            //S piece:
            {
                    {0,1, 2,0, 1,0, 1,1},

                    {1,0, 1,1, 2,1, 2,2},

                    {0,2, 2,1, 1,1, 1,2},

                    {0,0, 0,1, 1,1, 1,2}
            },
            //Z piece:
            {
                    {0,0, 1,0, 1,1, 2,1},

                    {2,0, 1,1, 2,1, 1,2},

                    {0,1, 1,1, 1,2, 2,2},

                    {1,0, 0,1, 1,1, 0,2}
            },
            //T piece:
            {
                    {1,0, 0,1, 1,1, 2,1},

                    {1,0, 1,1, 2,1, 1,2},

                    {0,1, 1,1, 2,1, 1,2},

                    {1,0, 0,1, 1,1, 1,2}
            },
            //O piece:
            {
                    {1,0, 2,0, 1,1, 2,1},

                    {1,0, 2,0, 1,1, 2,1},

                    {1,0, 2,0, 1,1, 2,1},

                    {1,0, 2,0, 1,1, 2,1}
            },
            //I piece:
            {
                    {0,1, 1,1, 2,1, 3,1},

                    {2,0, 2,1, 2,2, 2,3},

                    {0,2, 1,2, 2,2, 3,2},

                    {1,0, 1,1, 1,2, 1,3}
            }
    };

    /**
     * @param p  the type of piece
     * @param b  the orientation of the piece
     *
     * @return   the an array that represents where the 4 blocks are
     */
    public static int[] lookup(Pieces p, int b){
        int[] positions = new int[8];
        System.arraycopy(table[p.getIndex()][b], 0, positions, 0, 8);
        return positions;
    }


}
