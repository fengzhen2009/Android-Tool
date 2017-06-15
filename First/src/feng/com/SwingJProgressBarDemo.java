package feng.com;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
/**
* Created by MyWorld on 2016/3/24.
*/
public class SwingJProgressBarDemo {
   public static void main(String[] args) {
       JFrame frame = new JFrame("JProgressBarDemo");
       frame.setSize(400, 200);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setLayout(new FlowLayout());
       final JProgressBar progressBar = new JProgressBar();
       progressBar.setOrientation(JProgressBar.HORIZONTAL);
       progressBar.setSize(200, 100);
       progressBar.setMinimum(0);
       progressBar.setMaximum(100);
       progressBar.setStringPainted(true);
       frame.add(progressBar);
       JButton btn = new JButton("Start ProgressBar");
       btn.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       for (int i = 0; i <= 10; i++) {
                           progressBar.setValue(i * 10);
                           try {
                               TimeUnit.SECONDS.sleep(1);
                           } catch (InterruptedException e1) {
                               e1.printStackTrace();
                           }
                       }
                   }
               }).start();
           }
       });        
       frame.add(btn);
       frame.setLocationRelativeTo(null);
       frame.setVisible(true);
   }}