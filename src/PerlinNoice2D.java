import java.util.Random;

public class PerlinNoice {

      Vector2D[][] grad;
      int sizex, sizey;
      long seed;
      Random rand;

      Vector2D[] predefinedVectorSet = new Vector2D[]{
              new Vector2D()
      };

      public PerlinNoice(int sizex, int sizey, long seed) {
            this.sizex = sizex;
            this.sizey = sizey;
            this.seed = seed;
            rand = new Random(seed);


            grad = new RandomUnitVector[sizex][sizey];

            //TODO Use precomputed gradient vectors in hash lookup table to reduce process of creating gradient vectors
            for (int y = 0; y < sizey; y++) {
                  for (int x = 0; x < sizey; x++) {
                        grad[x][y] = new RandomUnitVector(2, rand.nextLong());
                  }
            }
      }

      public static void main(String[] args) {
            PerlinNoice pn = new PerlinNoice(100, 100, 0);
      }


}
