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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Random;

public class Main {

    public static final String Version = "1.0";
    public static final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static String currentLink;


    public static void getRandUrl() {   // random link generation
        char[] index = new char[6];

        for (int i = 0; i < index.length; i++) {    // first two characters of the key are letters
            if (i <= 1) {
                index[i] = alphabet[new Random().nextInt(alphabet.length)];
            } else {
                index[i] = Character.forDigit(new Random().nextInt(10), 10);
            }
        }

        currentLink = ("https://prnt.sc/"+String.valueOf(index));
    }


    public static String getImageUrl(String url) {  // Image scrapper
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            doc = null;
        }

        assert doc != null;
        return doc.select("#screenshot-image").attr("src");
    }


    public static void main(String[] args) { // App initialization
        getRandUrl();
        Window.setImage(getImageUrl(currentLink));
        Window.create();
    }
}
