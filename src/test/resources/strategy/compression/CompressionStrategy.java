/**
 * Source: https://dzone.com/articles/design-patterns-strategy
 */
public interface CompressionStrategy {
  public void compressFiles(ArrayList<File> files);
}