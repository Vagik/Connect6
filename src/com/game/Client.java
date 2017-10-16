package com.game;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Client extends JPanel{

    private static int [][]board = new int[19][19];
    private static JFrame frame = new JFrame("Connect6");
    private static int click_x, click_y;
    private static int x,y;
    private static int myColor;

    private Client() {}

    public static void createGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(528, 550);
        frame.add(new Board());
        frame.setVisible(true);
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                click_x = e.getX();
                click_y = e.getY();
            }
        });
    }

    public static void main(String[] args) {
        createGUI();


        try {
            Registry registry = LocateRegistry.getRegistry(null);
            GameLogic stub = (GameLogic) registry.lookup("Server");
            myColor =   stub.getColor();

            while (true) {
                try {
                    x = (click_x - 14 + 10) / 27;                                                        //Номер строки
                    y = (click_y - 14 + 10) / 27 - 1;                                                    //Номер столбца

                    if (x >= 0 && x <= 18 && y >= 0 && y <= 18) {
                        board[x][y] = myColor;
                        stub.setPoint(x, y, myColor);

                        frame.add(new Stone(14 + 27 * x - 10, 14 + 27 * y - 10, myColor));    //14 - начальное смещение, 27 - расстояние между ячейками, 10 - половина размера камня
                        frame.setVisible(true);

                        click_x = -100;
                        click_y = -100;
                    }
                   Thread.sleep(10);
                }
                catch (Exception e) {
                    System.err.println("SetPoint exception: " + e.toString());
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}