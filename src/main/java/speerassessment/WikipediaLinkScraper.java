package speerassessment;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikipediaLinkScraper {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a Wikipedia link: ");
        String link = scanner.nextLine();
        if (!isValidWikiLink(link)) {
            System.err.println("Error: Invalid Wikipedia link");
            System.exit(1);
        }

        System.out.print("Enter an integer between 1 to 20: ");
        int n;
        while (true) {
            System.out.println("Enter an integer between 1 to 20:");
            try {
                n = Integer.parseInt(scanner.nextLine());
                if (n >= 1 && n <= 20) {
                    break;
                } else {
                    System.out.println("Error: Integer must be between 1 to 20. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Not a valid integer. Please try again.");
            }
        }

        Set<String> links = new HashSet<>();
        links.add(link);
        for (int i = 0; i < n; i++) {
            Set<String> newLinks = new HashSet<>();
            for (String currentLink : links) {
                Set<String> scrapedLinks = scrapeLinks(currentLink);
                newLinks.addAll(scrapedLinks);
            }
            links.addAll(newLinks);
        }

        System.out.println("Found " + links.size() + " unique links:");
        for (String linkFound : links) {
            System.out.println(linkFound);
        }
    }

    private static boolean isValidWikiLink(String link) {
        try {
            URL url = new URL(link);
            String host = url.getHost();
            return host.endsWith(".wikipedia.org") && url.getPath().startsWith("/wiki/");
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private static Set<String> scrapeLinks(String link) {
        Set<String> links = new HashSet<>();
        try (Scanner scanner = new Scanner(new URL(link).openStream())) {
            Pattern pattern = Pattern.compile("href=\"/wiki/([^\"]+)\"");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String scrapedLink = "https://en.wikipedia.org/wiki/" + matcher.group(1);
                    links.add(scrapedLink);
                }
            }
        } catch (Exception e) {
            // Do nothing
        }
        return links;
    }
}