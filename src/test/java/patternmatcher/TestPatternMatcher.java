package patternmatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestPatternMatcher {

    public static void main(String[] args)  throws IOException {
        test1();
        test2();
    }
    
    static void addFiles(List<File> files, File dir) {
        for (File f : dir.listFiles()) {
            if (f.isDirectory())
                addFiles(files, f);
            else if (f.getName().equals("README"))
                files.add(f);
        }
    }
    
    static void test2() throws IOException {

        List<File> files = new ArrayList<>();
        addFiles(files, new File("/usr/share/doc"));

        List<String> lines = new ArrayList<>();
        for (File f : files) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String line;
                while ((line = in.readLine()) != null)
                    lines.add(line);
            } catch (FileNotFoundException _) {}
        }
        files.clear();
        System.out.println("lines: " + lines.size());
        
        IPatternMatcher matcher = new PatternMatcher();
        Random rnd = new Random();
        int added = 0;
        for (String line : lines) {
            if (line.length() > 20) {
                matcher.addPattern("x" + line.substring(rnd.nextInt(8), 12 + rnd.nextInt(8)));
                ++added;
            }
        }
        System.out.println("patterns: " + added);

        // warmup
        int found = 0;
        for (String line : lines) {
            String result = matcher.checkText(line);
            if (result != null)
                ++found;
        }
        System.out.println("warmup found: " + found);

        // test run
        long start = System.currentTimeMillis();
        found = 0;
        int all = 0;
        for (int i = 0; i < 30; ++i) {
            for (String line : lines) {
                ++all;
                String result = matcher.checkText(line);
                if (result != null)
                    ++found;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("all: " + all);
        System.out.println("found: " + found);
        System.out.println(String.format("duration: %d ms", end - start));
    }
    
    static void test1() {
        IPatternMatcher patternMatcher = new PatternMatcher();

        // add patterns here (real test has 25K patterns)
        patternMatcher.addPattern("iPhone");
        patternMatcher.addPattern("youtube.com/watch");
        patternMatcher.addPattern("profil");
        patternMatcher.addPattern("Заходи, сюда");

        // Check texts against patterns here (real test has 1 million texts)
        String p1 = patternMatcher.checkText("What type of iPhone do you have?");
        String p2 = patternMatcher.checkText("Hi, take a look here: https://www.youtube.com/watch?v=RC_6skf1-t");
        String p3 = patternMatcher.checkText("Salut est-ce que tu peux aimer ma photo de profil?");
        String p4 = patternMatcher.checkText("Привет! У нас сегодня акция. Заходи, сюда узнаешь больше!");
        String p5 = patternMatcher.checkText("Yanıtını Beğen Hediye Yolluyor Dene Gör :)");

        // this line prints: "p1: iPhone p2: youtube.com/watch p3: profil p4: Заходи, сюда p5: null"
        System.out.printf("p1: %s p2: %s p3: %s p4: %s p5: %s\n", p1, p2, p3, p4, p5);
    }
}
