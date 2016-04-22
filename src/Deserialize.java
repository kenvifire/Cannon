import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Created by kenvi on 15/11/26.
 */
public class Deserialize {
    public static void main(String args[]) throws Exception {
        File file = new File("payload.bin");
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        in.readObject();
    }
}
