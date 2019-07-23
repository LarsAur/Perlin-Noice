import java.util.Random;

public class RandomUnitVector {

      private float[] values;

      public RandomUnitVector(int dim, long seed){
            values = new float[dim];

            Random rand = new Random(seed);
            float sum = 0;
            //Randomize
            for(int i = 0; i < values.length; i++){
                  values[i] = rand.nextFloat();
                  sum += values[i];
            }
            //Normalize
            for(int i = 0; i < values.length; i++){
                  values[i] /= sum;
            }
      }

      public float get(int index){
            return values[index];
      }
}
