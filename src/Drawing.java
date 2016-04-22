/**
 * Created by kenvi on 15/11/13.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.Semaphore;
import javax.swing.*;

public class Drawing {
    JFrame jframe = new JFrame();
    public JPanel GImage = null;
    public JButton button = null;
    public String code = null;
    public Semaphore lock;
    public String name;
    public Drawing(String name, Semaphore lock) {
        this.lock = lock;
        this.name = name;
        try {
            lock.acquire();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        initFrame();
    }
    // 初始化窗口
    public void initFrame() {
        jframe.setAlwaysOnTop(true);

        // 利用JPanel添加背景图片
        GImage = new JPanel() {
            protected void paintComponent(Graphics g) {
                ImageIcon icon = new ImageIcon("/tmp/"+ name + ".png");
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, icon.getIconWidth(),
                        icon.getIconHeight(), icon.getImageObserver());
                jframe.setSize(400,300);
            }

        };

        final JTextField textField = new JTextField();

        button = new JButton("确定");

        button.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                code = textField.getText().trim();
                lock.release();
            }
        });

        jframe.setLayout(new GridLayout(3, 1));
        jframe.setTitle("测试背景图片");
        jframe.add(GImage);
        jframe.add(textField);
        jframe.add(button);

        jframe.pack();
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {

    }
    public String getCode(){
        return code;
    }
    public void close() {
        jframe.dispose();
        GImage=null;
    }
}
