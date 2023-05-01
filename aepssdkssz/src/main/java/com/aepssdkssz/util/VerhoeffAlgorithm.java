package com.aepssdkssz.util;

public class VerhoeffAlgorithm {
        static int[][] d;
        static int[][] p;
        static int[] inv;

        public static boolean validateVerhoeff(final String num) {
        int c = 0;
        final int[] myArray = StringToReversedIntArray(num);
        for (int i = 0; i < myArray.length; ++i) {
            c = VerhoeffAlgorithm.d[c][VerhoeffAlgorithm.p[i % 8][myArray[i]]];
        }
        return c == 0;
    }

    private static int[] StringToReversedIntArray(final String num) {
        int[] myArray = new int[num.length()];
        for (int i = 0; i < num.length(); ++i) {
            myArray[i] = Integer.parseInt(num.substring(i, i + 1));
        }
        myArray = Reverse(myArray);
        return myArray;
    }

        private static int[] Reverse(final int[] myArray) {
        final int[] reversed = new int[myArray.length];
        for (int i = 0; i < myArray.length; ++i) {
            reversed[i] = myArray[myArray.length - (i + 1)];
        }
        return reversed;
    }

        static {
        VerhoeffAlgorithm.d = new int[][] { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, { 1, 2, 3, 4, 0, 6, 7, 8, 9, 5 }, { 2, 3, 4, 0, 1, 7, 8, 9, 5, 6 }, { 3, 4, 0, 1, 2, 8, 9, 5, 6, 7 }, { 4, 0, 1, 2, 3, 9, 5, 6, 7, 8 }, { 5, 9, 8, 7, 6, 0, 4, 3, 2, 1 }, { 6, 5, 9, 8, 7, 1, 0, 4, 3, 2 }, { 7, 6, 5, 9, 8, 2, 1, 0, 4, 3 }, { 8, 7, 6, 5, 9, 3, 2, 1, 0, 4 }, { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 } };
        VerhoeffAlgorithm.p = new int[][] { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, { 1, 5, 7, 6, 2, 8, 3, 0, 9, 4 }, { 5, 8, 0, 3, 7, 9, 6, 1, 4, 2 }, { 8, 9, 1, 6, 0, 4, 3, 5, 2, 7 }, { 9, 4, 5, 3, 1, 2, 6, 8, 7, 0 }, { 4, 2, 8, 6, 5, 7, 3, 9, 0, 1 }, { 2, 7, 9, 3, 8, 0, 6, 4, 1, 5 }, { 7, 0, 4, 6, 9, 1, 3, 2, 5, 8 } };
        VerhoeffAlgorithm.inv = new int[] { 0, 4, 3, 2, 1, 5, 6, 7, 8, 9 };
    }

}