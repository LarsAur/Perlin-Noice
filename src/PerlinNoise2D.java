import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class PerlinNoise2D {

      private long seed;
      private Random rand;
      private int chunkSize;

      public PerlinNoise2D(long seed, int chunkSize) {
            this.seed = seed;
            this.chunkSize = chunkSize;
            this.rand = new Random(seed);
      }

      public double noise(int x, int y) {
            int chunkx = Math.floorDiv(x, chunkSize);
            int chunky = Math.floorDiv(y, chunkSize);

            Vector ulg = chunkVectorHash(chunkx, chunky);               // upper left gradient
            Vector llg = chunkVectorHash(chunkx, chunky + 1);           // lower left gradient
            Vector urg = chunkVectorHash(chunkx + 1, chunky);           // upper right gradient
            Vector lrg = chunkVectorHash(chunkx + 1, chunky + 1);       // lower right gradient

            int inChunkx = mod(x, chunkSize);                           // mod with range [0, chunksize)
            int inChunky = mod(y, chunkSize);

            double fraqx = inChunkx / (double) chunkSize;               // fraction of x within the chunk (0=left, 1=right)
            double fraqy = inChunky / (double) chunkSize;               // fraction of y within the chunk (0=up, 1=down)

            double fadex = fade(fraqx); // faded of x within the chunk (0=left, 1=right)
            double fadey = fade(fraqy); // faded of y within the chunk (0=up, 1=down)

            Vector uld = new Vector(new double[]{fraqx, fraqy});          // upper left distance
            Vector lld = new Vector(new double[]{fraqx, fraqy - 1});      // lower left distance
            Vector urd = new Vector(new double[]{fraqx - 1, fraqy});      // upper right distance
            Vector lrd = new Vector(new double[]{fraqx - 1, fraqy - 1});  // lower right distance

            double dotul = ulg.dot(uld);
            double dotll = llg.dot(lld);
            double dotur = urg.dot(urd);
            double dotlr = lrg.dot(lrd);

            double lxInter = dotll + fadex * (dotlr - dotll); // lower x interpolation with fade
            double uxInter = dotul + fadex * (dotur - dotul); // upper x interpolation with fade
            double biInter = uxInter + fadey * (lxInter - uxInter); // bilinear interpolation with fade

            return (biInter + 1) / 2; // Normalizing noise value to [0, 1]
      }

      private Vector chunkVectorHash(int cx, int cy) {

            int k1 = cx < 0 ? -cx * 2 - 1 : cx * 2; // Mapping Integers to Natural numbers (f : Z -> N)
            int k2 = cy < 0 ? -cy * 2 - 1 : cy * 2;
            long chunkSeed = (((k1 + k2) * (k1 + k2 + 1)) / 2 + k2) * seed; // Cantor pairing function, seed should not be zero

            // Integer hash function
            // https://burtleburtle.net/bob/hash/integer.html
            chunkSeed = (chunkSeed ^ 0xdeadbeef) + (chunkSeed << 4);  // deadbeef <3
            chunkSeed = chunkSeed ^ (chunkSeed >> 10);
            chunkSeed = chunkSeed + (chunkSeed << 7);
            chunkSeed = chunkSeed ^ (chunkSeed >> 13);

            return predefinedVectorSet[mod(chunkSeed, predefinedVectorSet.length)];
      }

      private static final Vector[] predefinedVectorSet = new Vector[]{
              new Vector(new double[]{1, 1}),
              new Vector(new double[]{-1, 1}),
              new Vector(new double[]{-1, -1}),
              new Vector(new double[]{1, -1}),
              new Vector(new double[]{Math.sqrt(2), 0}),
              new Vector(new double[]{-Math.sqrt(2), 0}),
              new Vector(new double[]{0, -Math.sqrt(2)}),
              new Vector(new double[]{0, Math.sqrt(2)})
      };

      private double fade(double t) {
            return 6 * Math.pow(t, 5) - 15 * Math.pow(t, 4) + 10 * Math.pow(t, 3);
      }

      private int mod(long x, int m) {
            return (int) ((x % m) + m) % m; // mod function with range [0, m) no negative numbers as with % operator
      }

      public static void main(String[] args) {
            int chunkSize = 64;
            long seed = System.nanoTime();
            PerlinNoise2D pn = new PerlinNoise2D(seed, chunkSize);

            int size = 512;
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
            WritableRaster raster = image.getRaster();

            for (int y = 0; y < size; y++) {
                  for (int x = 0; x < size; x++) {
                        int p = (int) ((pn.noise(x, y)) * 255);
                        raster.setPixel(x, y, new int[]{p});
                  }
            }

            try {
                  // retrieve image
                  File outputfile = new File("noise.png");
                  ImageIO.write(image, "png", outputfile);
            } catch (IOException e) {
            }

      }


}
