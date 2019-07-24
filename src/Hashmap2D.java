import java.util.HashMap;
import java.util.Map;

public class Hashmap2D<K1, K2, V> {

      private final Map<K1, Map<K2, V>> hmap2d;

      public Hashmap2D(){
            hmap2d = new HashMap<K1, Map<K2, V>>();
      }

      public void put(K1 k1, K2 k2, V v){
            Map<K2, V> map;
            if(hmap2d.containsKey(k1)){
                  map = hmap2d.get(k1);
            }else {
                  map = new HashMap<K2, V>();
                  hmap2d.put(k1, map);
            }

            map.put(k2, v);
      }

      public V get(K1 k1, K2 k2){
            if(hmap2d.containsKey(k1)){
                  return hmap2d.get(k1).get(k2);
            }else{
                  return null;
            }
      }

      public boolean containsKey(K1 k1, K2 k2){
            return hmap2d.containsKey(k1) && hmap2d.get(k1).containsKey(k2);
      }

}
