import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 *
 * @author Cole Petersen
 * 
 * CFBTwitterBot.java
 * 
 * Generates and tweets statements and questions about college football.
 * 
 */

public class CFBTwitterBot {
    
    static ArrayList<String> comments = new ArrayList<>();
    static ArrayList<String> teams = new ArrayList<>();
    static ArrayList<String> confs = new ArrayList<>();
    static ArrayList<String> phrases = new ArrayList<>();
    static ArrayList<String> actions = new ArrayList<>();
    static ArrayList<String> styles = new ArrayList<>();
    static ArrayList[] lists = {comments, teams, confs, phrases, actions, styles};
    static String[] types = {"comment", "team", "conf", "phrase", "action", "style"};
    

    public static void main(String[] args) {
        run();
    }
    
    public static void run() {

        lists = setLists();
        
        while (true) {

            boolean exception = false; // used to try again if TwitterException thrown

            do {
//                try {
                    String comment = getComment(comments);

                    // test
                    System.out.println(comment);

                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CFBTwitterBot.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // run
//                    tweet(comment);
//                    exception = false;
//                } catch (TwitterException e) {
//                    e.printStackTrace();
//                    exception = true;
//                }

            } while (exception); // try again if exception thrown

//            try {
//                TimeUnit.HOURS.sleep(4);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(CFBTwitterBot.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }
    
    public static ArrayList[] setLists() {
        for (int i=0; i<6; i++) {
            try {
                Scanner teamNameScanner = new Scanner(new File("target/classes/" + types[i] + ".txt"));
                while (teamNameScanner.hasNext()) {
                    lists[i].add(teamNameScanner.nextLine());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CFBTwitterBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return lists;
    }
    
    public static String getComment(ArrayList<String> list) {
        Random random = new Random();
        
        return addWords(list.get(random.nextInt(list.size())));
    }
    
    public static String addWords(String incComment) {


        Random random = new Random();

        String comment = incComment
            .replaceAll("#team", getWord("team"))
            .replaceAll("#conf", getWord("conf"))
            .replaceAll("#phrase", getWord("phrase"))
            .replaceAll("#action", getWord("action"))
            .replaceAll("#style", getWord("style"))
            .replaceAll("#num", Integer.toString(random.nextInt(48) + 2));

        /*
        String[] words = incComment.split(" ");

        StringBuilder builder = new StringBuilder();

        for (int i=0; i<words.length; i++) {
            String w = words[i];
            if (w.startsWith("#")) {
                String poundRemoved = w.substring(1);
                String punc = "";
                char lastChar = poundRemoved.charAt(poundRemoved.length() - 1);
                if(!Character.isLetter(lastChar)) {
                    punc = Character.toString(lastChar);
                }
                String type = poundRemoved.replaceAll("\\p{P}", "");
                if (type.equals("num")) {
                    Random random = new Random();
                    words[i] = Integer.toString(random.nextInt(48) + 2);
                }
                else {
                    words[i] = getWord(type) + punc;
                }
                if (i == 0) {
                    words[i] = words[i].substring(0, 1).toUpperCase() +
                            words[i].substring(1);
                }
            }
            builder.append(w);
        }
        
        String comment = String.join(" ", words);
        */
        
        return comment;
    }
    
    public static String getWord(String type) {
        Random random = new Random();
        
        ArrayList<String> list;
        switch (type) {
            case "team":
                list = teams;
                break;
            case "conf":
                list = confs;
                break;
            case "phrase":
                list = phrases;
                break;
            case "action":
                list = actions;
                break;
            case "style":
                list = styles;
                break;
            default:
                list = phrases;
                break;
        }
        
        return list.get(random.nextInt(list.size()));
    }

    public static void tweet(String comment) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        Status status;
        status = twitter.updateStatus(comment);
        System.out.println(status);
    }
}
