/**
 * Source: https://dzone.com/articles/design-patterns-strategy
 */
public class Client {
    public static void main(String[] args) {
        CompressionContext ctx = new CompressionContext();
        //we could assume context is already set by preferences
        ctx.setCompressionStrategy(new ZipCompressionStrategy());
        //get a list of files...
        ctx.createArchive(fileList);
    }
}