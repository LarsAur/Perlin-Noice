public class Vector {

      private double[] values;

      public Vector(double[] values){
            this.values = values;
      }

      public Vector(Vector vector){
            this.values = vector.values;
      }

      public double dot(Vector vector){
            if(values.length != vector.values.length){
                  throw new IllegalArgumentException("Unequal vector dim");
            }
            double dot = 0;
            for(int i = 0; i < values.length; i++){
                  dot += vector.values[i] * values[i];
            }
            return dot;
      }

      public double get(int index){
            return values[index];
      }

      public void scale(double s){
            for(int i = 0; i < values.length; i++){
                  values[i] *= s;
            }
      }

      @Override
      public String toString() {
            String s = "[";
            for(int i = 0; i < values.length; i++){
                  if(i != values.length - 1){
                        s += values[i] + ", ";
                  }else{
                        s += values[i] + "]";
                  }
            }
            return s;
      }
}
