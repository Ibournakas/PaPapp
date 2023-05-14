package com.example.papapp.Announcement_Services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnnouncementFetcher {
    private static final int MAX_PAGES = 3;
    public static List<Announcements> fetchAnnouncements(int page) throws IOException {



        String baseUrl = "https://www.ceid.upatras.gr/el/announcement";
        String url = baseUrl + "?page=" + page;
        Document doc = Jsoup.connect(url).get();

        List<Announcements> announcements = new ArrayList<>();
        Elements elements = doc.select("article.node-announcement");

        for (Element element : elements) {
            String date = element.select("div.submitted-date").text().trim().replace('\n', ' ');
            String title = element.select("h2").text().trim();
            String author = element.select("span.username").text().trim();
            String category = element.select("div.field-name-field-announcement-category").text().trim().substring(10);
            String content = element.select("div.content").text().trim();

            Announcements announcement = new Announcements(date, title, category, author, content);
            announcements.add(announcement);
        }

        return announcements;
    }
}

