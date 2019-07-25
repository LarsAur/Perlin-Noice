import java.util.Random;

public class GradientGrid{

      private Hashmap2D<Integer, Integer, Vector> gradients = new Hashmap2D<>();

      private static final Vector[] predefinedVectorSet = new Vector[]{
              new Vector(new double[]{1/Math.sqrt(2), 1/Math.sqrt(2)}),
              new Vector(new double[]{1/-Math.sqrt(2), 1/Math.sqrt(2)}),
              new Vector(new double[]{1/-Math.sqrt(2), 1/-Math.sqrt(2)}),
              new Vector(new double[]{1/Math.sqrt(2), 1/-Math.sqrt(2)}),
              new Vector(new double[]{1, 0}),
              new Vector(new double[]{0, 1}),
              new Vector(new double[]{-1, 0}),
              new Vector(new double[]{0, -1})
      };

      private long seed;

      public GradientGrid(long seed){
            this.seed = seed;
      }

      public Vector get(int x, int y){
            if(!gradients.containsKey(x, y)){
                gradients.put(x, y, vectorChunkCoord(x, y));
            }
            return gradients.get(x, y);
      }

      private Vector vectorChunkCoord(int cx, int cy) {
            long chunkSeed = (long) ((Math.pow(seed - 1, cx) % 1997) * (Math.pow(seed + 1, cy) % 1999));
            Random random = new Random(chunkSeed);
            return predefinedVectorSet[random.nextInt(predefinedVectorSet.length)];
      }

}
