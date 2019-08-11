import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class PerlinNoice2D {

      private long seed;
      private Random rand;
      private int chunkSize;

      public PerlinNoice2D(long seed, int chunkSize) {
            this.seed = seed;
            this.chunkSize = chunkSize;
            this.rand = new Random(seed);
      }

      public double noice(int x, int y) {
            int chunkx = Math.floorDiv(x, chunkSize);
            int chunky = Math.floorDiv(y, chunkSize);

            Vector ulg = chunkVectorHash(chunkx, chunky);               // upper left gradient
            Vector llg = chunkVectorHash(chunkx, chunky + 1);           // lower left gradient
            Vector urg = chunkVectorHash(chunkx + 1, chunky);           // upper right gradient
            Vector lrg = chunkVectorHash(chunkx + 1, chunky + 1);       // lower right gradient

            int inChunkx = ((x % chunkSize) + chunkSize) % chunkSize;   // mod with range [0, chunksize)
            int inChunky = ((y % chunkSize) + chunkSize) % chunkSize;
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

            //Faded Lerp

            double lxInter = dotll + fadex * (dotlr - dotll); // lower x interpolation with fade
            double uxInter = dotul + fadex * (dotur - dotul) ; // upper x interpolation with fade
            double biInter = uxInter + fadey * (lxInter - uxInter); // bilinear interpolation with fade

            return biInter;
      }

      private Vector chunkVectorHash(int cx, int cy) {
            long chunkSeed = (long) ((Math.pow(seed - 1, cx) % 1997) * (Math.pow(seed + 1, cy) % 1999));
            Random random = new Random(chunkSeed);
            return predefinedVectorSet[random.nextInt(predefinedVectorSet.length)];
      }

      private static final Vector[] predefinedVectorSet = new Vector[]{
              new Vector(new double[]{1, 0}),
              new Vector(new double[]{0, 1}),
              new Vector(new double[]{-1, 0}),
              new Vector(new double[]{0, -1})
      };

      private double fade(double t) {
            return 6 * Math.pow(t, 5) - 15 * Math.pow(t, 4) + 10 * Math.pow(t, 3);
      }

      public static void main(String[] args) {
            Random random = new Random();
            PerlinNoice2D pn = new PerlinNoice2D(random.nextLong(), 16);

            BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_BYTE_GRAY);
            WritableRaster raster = image.getRaster();

            for (int y = 0; y < 128; y++) {
                  for (int x = 0; x < 128; x++) {
                        int p = (int)((pn.noice(x, y) + 1) * 127);
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
