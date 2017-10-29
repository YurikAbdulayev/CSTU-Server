package com.cstu.service;

import com.cstu.json.objects.ScheduleData;
import com.cstu.json.objects.ScheduleInfo;
import com.cstu.json.objects.ScheduleItem;
import com.cstu.utils.C;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParserServiceImpl implements ParserService {
//Jsoup.connect(url).requestBody("group=%CC%CF%C7-1704&sdate=01.09.2017&edate=29.10.2017&n=700").post()

    @Override
    public String getSchedule(String group, String sdate, String edate) {
        String url = "http://195.95.232.162:8082/cgi-bin/timetable.cgi";
        String test = "group=" + C.toWin1251(group)            +//CC%CF%C7-1704
                "&sdate=" + sdate           +//01.09.2017
                "&edate=" + edate           +//29.10.2017
                "&n=700";
        try {
            List<ScheduleItem> scheduleItems = new ArrayList<>();
            Document doc = Jsoup
                    .connect(url)
                    .requestBody(test)
                    .post();
            Elements days = doc.getElementsByClass("col-md-6");
            for (Element day : days) {
                try {
                    scheduleItems.add(getScheduleItem(day));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new Gson().toJson(scheduleItems);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "[]";
    }

    private ScheduleItem getScheduleItem(Element day) {
        Element table = day.getElementsByTag("table").first();
        return new ScheduleItem(
                day.getElementsByTag("h4").first().text(),
                day.getElementsByTag("small").first().text(),
                getScheduleData(table)
        );
    }

    private List<ScheduleData> getScheduleData(Element table) {
        List<ScheduleData> scheduleData = new ArrayList<>();
        for (Element tr : table.getElementsByTag("tr")) {
            Elements td = tr.getElementsByTag("td");
            String item = td.get(1).text();
            if (!item.isEmpty()) {
                scheduleData.add(new ScheduleData(
                        td.first().text(),
                        parseLesson(item)
                ));
            }
        }
        return scheduleData;
    }

    private ScheduleInfo parseLesson(String item) {
        boolean gr1 = item.toLowerCase().contains("(підгр. 1)");
        boolean gr2 = item.toLowerCase().contains("(підгр. 2)");
        if (gr1 && gr2) {
            return new ScheduleInfo("", "", "", "", "",
                    item);
        } else {
            return this.parseStrLesson(item);
        }
    }

    private ScheduleInfo parseStrLesson(String lesson) {
        boolean checked = false;
        String lessonStr = lesson;
        String lessonType = null;
        for (String v : C.lessonTypes) {
            if (lesson.contains(v) && !checked) {
                lessonType = v;
                lesson = lesson.replace(v, "").trim();
                checked = true;
            }
        }
        String roomNumberWithDetails = lesson.substring(lesson.indexOf(" а.") + 1).trim();
        String lessonRoom;
        String anotherDetails;
        if (roomNumberWithDetails.contains("Спорткомплекс")) {
            lessonRoom = roomNumberWithDetails
                    .substring(0, roomNumberWithDetails.indexOf("кс") + 2).trim();
            anotherDetails = getAnotherDetails(roomNumberWithDetails, lessonRoom);
        } else {
            lessonRoom = getLessonRoom(roomNumberWithDetails + " ");
            anotherDetails = getAnotherDetails(roomNumberWithDetails, lessonRoom);
        }

        String lessonNameWithTeacher = lesson.replace(roomNumberWithDetails, "").trim();

        String lessonName = null;
        String teacher = null;
        checked = false;
        for (String rang : C.teacherRangs) {
            if (lessonNameWithTeacher.contains(rang) && !checked) {
                teacher = lessonNameWithTeacher.substring(lessonNameWithTeacher.indexOf(rang)).trim();
                lessonName = lessonNameWithTeacher.replace(teacher, "").trim();
                checked = true;
            }
        }

        return new ScheduleInfo(
                lessonType,
                lessonName,
                teacher,
                lessonRoom,
                anotherDetails,
                lessonStr
        );
    }

    private String getAnotherDetails(String roomNumberWithDetails, String lessonRoom) {
        return Objects.equals(lessonRoom, roomNumberWithDetails) ? "" :
                roomNumberWithDetails.replace(lessonRoom, "").trim();
    }

    private String getLessonRoom(String roomNumberWithDetails) {
        String pattern = "\\d\\s";
        int endIndex = 0;
        Matcher matcher = Pattern.compile(pattern).matcher(roomNumberWithDetails);
        if(matcher.find()){
            endIndex = matcher.end();
        }
        return roomNumberWithDetails.substring(0, endIndex).trim();
    }
}
