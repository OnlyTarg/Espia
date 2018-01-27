
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by CleBo on 19.01.2018.
 */
public class Main extends JFrame {

    JFrame mainWindow;
    JButton b1,b2,b3;
    JLabel label;
    public class CreateClient extends Thread{
        String name;
        public CreateClient(String name) throws IOException {
            this.name = name;
        }

        @Override
        public void run() {
            if (name.equals("121")){
                try {
                    new Server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (name.equals("КПП")){
                try {
                    new Client("КПП");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (name.equals("КТП")){
                try {
                    new Client("КТП");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



        }
    }

    private void createwindow() {
        mainWindow = new JFrame();
        mainWindow.setTitle("PConrol");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(227, 300);
        mainWindow.setVisible(true);
        mainWindow.setResizable(false);
    }
    private void createButtons() {
        mainWindow.setLayout(null);

        b1 = new JButton("121");
        b2 = new JButton("КПП");
        b3 = new JButton("КТП");
        label = new JLabel("Оберіть користувача:");

        b1.addActionListener(Listener(b1));
        b2.addActionListener(Listener(b2));
        b3.addActionListener(Listener(b3));



        Font fontGlobal = new Font("Times new Roman",Font.BOLD,20);
        b1.setFont(fontGlobal);
        b2.setFont(fontGlobal);
        b3.setFont(fontGlobal);
        label.setFont(fontGlobal);

        b1.setBounds(10, 70, 200, 50);
        b2.setBounds(10, 130, 200, 50);
        b3.setBounds(10, 190, 200, 50);
        label.setBounds(10, 10, 200, 50);

        b1.setBackground(Color.YELLOW);
        b2.setBackground(Color.YELLOW);
        b3.setBackground(Color.YELLOW);

        mainWindow.add(b1);
        mainWindow.add(b2);
        mainWindow.add(b3);
        mainWindow.add(label);

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Main main = new Main();
        main.createwindow();
        main.createButtons();

    }
    private ActionListener Listener (JButton b) {
        /*Создаю слушатель для кнопок
        При нажатии на кнопку, она изменяет цвет на противоположный,
        в зависимости от того какой цвет был установлен до нажатия.
        Работает только в офлайне
       */

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.setVisible(false);
                try {
                    CreateClient newClient = new CreateClient(b.getText());
                    newClient.start();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }



            }
        };
        return actionListener;
    }
}