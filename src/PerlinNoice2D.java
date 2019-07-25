import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class PerlinNoice2D {


      private GradientGrid gradientGrid;

      private long seed;
      private Random rand;
      private int chunkSize;

      public PerlinNoice2D(long seed, int chunkSize) {
            this.seed = seed;
            this.chunkSize = chunkSize;
            this.rand = new Random(seed);
            this.gradientGrid = new GradientGrid(seed);
      }

      public double noice(int x, int y) {
            int chunkx = Math.floorDiv(x, chunkSize);
            int chunky = Math.floorDiv(y, chunkSize);

            Vector ulg = gradientGrid.get(chunkx, chunky);              // upper left gradient
            Vector llg = gradientGrid.get(chunkx, chunky + 1);          // lower left gradient
            Vector urg = gradientGrid.get(chunkx + 1, chunky);          // upper right gradient
            Vector lrg = gradientGrid.get(chunkx + 1, chunky + 1);      // lower right gradient

            int inChunkx = ((x % chunkSize) + chunkSize) % chunkSize;
            int inChunky = ((y % chunkSize) + chunkSize) % chunkSize;

            Vector uld = new Vector(new double[]{inChunkx, inChunky});                          // upper left distance
            Vector lld = new Vector(new double[]{inChunkx, inChunky - chunkSize});              // lower left distance
            Vector urd = new Vector(new double[]{inChunkx - chunkSize, inChunky});              // upper right distance
            Vector lrd = new Vector(new double[]{inChunkx - chunkSize, inChunky - chunkSize});  // lower right distance


            double dotul = ulg.dot(uld);
            double dotll = llg.dot(lld);
            double dotur = urg.dot(urd);
            double dotlr = lrg.dot(lrd);

            //Bilinear interpolation

            double fraqx = inChunkx / (double) chunkSize;
            double fraqy = inChunky / (double) chunkSize;

            double lxInter = dotll * fade(1 - fraqx) + dotlr * fade(fraqx); // lower x interpolation with fade
            double uxInter = dotul * fade(1 - fraqx) + dotur * fade(fraqx); // upper x interpolation with fade
            double biInter = uxInter * fade(1 - fraqy) + lxInter * fade(fraqy); // bilinear interpolation with fade

            return biInter;
      }

      private double fade(double t) {
            return 6 * Math.pow(t, 5) - 15 * Math.pow(t, 4) + 10 * Math.pow(t, 3);
      }



      public static void main(String[] args) {
            Random random = new Random();
            PerlinNoice2D pn = new PerlinNoice2D(random.nextLong(), 8);

            BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_BYTE_GRAY);
            WritableRaster raster = image.getRaster();

            for (int y = 0; y < 128; y++) {
                  for (int x = 0; x < 128; x++) {
                        int p = (int)((pn.noice(x, y) + 1) * 127);
                        raster.setPixel(x, y, new int[]{p});
                        //System.out.print((double)Math.round(pn.noice(x, y) * 10d) / 10d + "\t");
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
