public class Vector2D {

      private double[] values;

      public Vector2D(double vx, double vy){
            this.values = values;
      }

      public Vector2D(Vector2D vector2D){
            this.values = vector2D.values;
      }

      public double get(int index){
            return values[index];
      }
}
