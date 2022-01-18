/*

                 $$\                     $$\   $$\ $$\
                 $$ |                    $$$\  $$ |\__|
            $$$$$$$ | $$$$$$\ $$\    $$\ $$$$\ $$ |$$\  $$$$$$\
           $$  __$$ |$$  __$$\\$$\  $$  |$$ $$\$$ |$$ |$$  __$$\
           $$ /  $$ |$$$$$$$$ |\$$\$$  / $$ \$$$$ |$$ |$$$$$$$$ |
           $$ |  $$ |$$   ____| \$$$  /  $$ |\$$$ |$$ |$$   ____|
           \$$$$$$$ |\$$$$$$$\   \$  /   $$ | \$$ |$$ |\$$$$$$$\
            \_______| \_______|   \_/    \__|  \__|\__| \_______|

    ____________________________________________________________________

    RandomScreenshot! version 1.0 by devNie

    This app is retrieves a random screenshot from the service LightShot
    and presents it to the user on a graphical interface.

*/

package com.github.devnie.randomscreenshots;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.github.devnie.randomscreenshots.Main.fallbackImage;
import static com.github.devnie.randomscreenshots.Main.getImageUrl;


public class Window {
    private static JFrame window;
    private static JPanel content;
    private static final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    private final static ImageIcon image = new ImageIcon();


    public static void create() {
        window = new JFrame("Random Screenshots! " + Main.Version);
        window.setLayout(new GridBagLayout());
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);

        content = new JPanel();
        content.setLayout(new GridBagLayout());


        GridBagConstraints gbc = new GridBagConstraints();  // Define shorthand for GridBagConstrains object and standardized constrains
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        gbc.insets = new Insets(15, 15, 15, 15);


        JLabel currentURL = new JLabel(Main.currentLink);   // Add a label showing the source of the image
        currentURL.setFont(new Font("Impact", Font.BOLD, 32));
        currentURL.setCursor(new Cursor(Cursor.HAND_CURSOR));
        currentURL.setToolTipText("Click to copy");

        UIManager.put("ToolTip.background", new Color(240,240,240));    // label tooltip style
        UIManager.put("ToolTip.foreground", new Color(170,170,170));
        UIManager.put("ToolTip.border",new LineBorder(new Color(170,170,170),1));
        UIManager.put("ToolTip.font" , new Font("",Font.BOLD,14));

        currentURL.addMouseListener(new MouseAdapter() {    // add copy to clip board functionality
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    currentURL.setForeground(new Color(81,81,81));  // Change the color to a lighter grey (default is  51,51,51)

                    StringSelection strContent = new StringSelection(currentURL.getText());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(strContent, null);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    currentURL.setForeground(new Color(51, 51, 51));  // Change back to normal font color when LMB has been released
                }
            }
        });

        gbc.gridy = 1;
        content.add(currentURL, gbc);


        JButton button = new JButton("Show Me Something Random!");  // Add a "new image" button
        button.setFont(new Font("Impact", Font.PLAIN, 20));
        button.setFocusPainted(false);

        button.addActionListener(e -> { // generates a new url and refreshes the content pane
            Main.getRandUrl();
            currentURL.setText(Main.currentLink);
            BufferedImage img;
            try {
                img = Window.getImg(getImageUrl(Main.currentLink));

            }catch (IOException exception){
                img = (BufferedImage) fallbackImage;
            }
            Window.setImage(img);
            content.revalidate();
            content.repaint();
            window.pack();
            window.setLocationRelativeTo(null);
        });

        gbc.gridy = 3;
        content.add(button, gbc);


        JLabel img = new JLabel(image); // add the scrapped image (defined last because of different insets)
        gbc.gridy = 2;
        gbc.insets = new Insets(32, 32, 32, 32);
        content.add(img, gbc);


        window.add(content);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }


    public static BufferedImage getImg(String url) throws IOException {
        BufferedImage bffImg;
        assert url != null;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        conn.setRequestProperty (   // circumvents 403 by setting correct user agent info
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31"
        );

        bffImg = ImageIO.read(conn.getInputStream());
        conn.disconnect();

        return bffImg;

    }

     static void setImage(BufferedImage bffImg) {
        if (bffImg.getWidth() > (screen.getWidth()*0.8) || (bffImg.getHeight() > screen.getHeight()*0.8)) { // prevents images larger than 2/3 of either screen dimension

            double significanceY = (screen.getHeight()*0.8) / bffImg.getHeight(); // for sake of readability (calculates factor that will bring the image width or height to max size of 0.6 screen dimension)
            double significanceX = (screen.getWidth()*0.8) / bffImg.getWidth();

            double scaleFactor = Math.min(significanceY, significanceX);  // Scale factor (more significant image dimension)

            double newX = bffImg.getWidth()* scaleFactor;
            double newY = bffImg.getHeight()* scaleFactor;

            image.setImage(bffImg.getScaledInstance((int)newX, (int)newY, Image.SCALE_SMOOTH)); // scale image

        } else {
            image.setImage(bffImg);
        }
    }

}
