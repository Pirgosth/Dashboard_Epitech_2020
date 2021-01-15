package io.github.dashboard.utils;

import com.google.gson.Gson;
import io.github.dashboard.models.widgets.UserWidget;
import io.github.dashboard.models.widgets.WidgetCacheResult;
import io.github.dashboard.models.widgets.WidgetGridParameters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WidgetUtils {

    public static List<UserWidget> getUserWidgets(int userId) {
        List<UserWidget> userWidgets = new ArrayList<>();
        try {
            ResultSet rs = SqlUtils.executeSelect("SELECT * FROM saved_widgets WHERE user_id = ?;", Collections.singletonList(userId));
            if (rs.first()) {
                do {
                    int savedWidgetId = rs.getInt("id");
                    int widgetId = rs.getInt("widget_id");
                    UserWidget currentWidget = getUserWidgetFromSavedId(savedWidgetId, widgetId, rs.getString("parameters"), rs.getString("url_parameters"));
                    if (currentWidget != null) {
                        userWidgets.add(currentWidget);
                    }
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userWidgets;
    }

    public static int getWidgetTypeIdFromName(String widgetName) {
        ResultSet widgetResult;

        try {
            widgetResult = SqlUtils.executeSelect("SELECT id FROM widgets WHERE name = ?;", Collections.singletonList(widgetName));
            if (!widgetResult.first()) {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        int widgetTypeId;

        try {
            widgetTypeId = widgetResult.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return widgetTypeId;
    }

    private static UserWidget getUserWidgetFromSavedId(int widgetId, int savedWidgetId, String jsonGridParameters, String url_parameters) {
        Gson gson = new Gson();
        UserWidget userWidget = new UserWidget();
        try {
            ResultSet rs = SqlUtils.executeSelect("SELECT name, route FROM widgets WHERE id = ?;", Collections.singletonList(savedWidgetId));
            if (!rs.first()) {
                return null;
            }
            userWidget.id = widgetId;
            userWidget.name = rs.getString("name");
            userWidget.route = rs.getString("route");
            userWidget.gridParameters = gson.fromJson(jsonGridParameters, WidgetGridParameters.class);
            userWidget.url_parameters = url_parameters;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return userWidget;
    }

    public static WidgetCacheResult getWidgetCachedData(String widgetName, String parameters) {

        int widgetTypeId = getWidgetTypeIdFromName(widgetName);
        if(widgetTypeId < 0){
            return null;
        }

        ResultSet cacheResult;

        try {
            cacheResult = SqlUtils.executeSelect("SELECT response, expiration_date FROM widget_cache WHERE parameters = ? AND widget_id = ? ORDER BY expiration_date DESC;", Arrays.asList(parameters, widgetTypeId));
            if (!cacheResult.first()) {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        Timestamp currentDate = new Timestamp(System.currentTimeMillis());

        Timestamp expirationDate;

        try {
            expirationDate = cacheResult.getTimestamp("expiration_date");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        //Cache has expired
        if (currentDate.after(expirationDate)) {
            return new WidgetCacheResult(false, null);
        }

        String response;
        try {
            response = cacheResult.getString("response");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return new WidgetCacheResult(true, response);

    }

    public static void updateWidgetCache(String widgetName, String parameters, String response, int expireInMs) {

        int widgetTypeId = getWidgetTypeIdFromName(widgetName);
        if(widgetTypeId < 0){
            return;
        }

        try {
            SqlUtils.executeDelete("DELETE FROM widget_cache WHERE parameters = ? AND widget_id = ?;", Arrays.asList(parameters, widgetTypeId));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            SqlUtils.executeInsert("INSERT INTO widget_cache(widget_id, parameters, response, expiration_date) VALUES(?, ?, ?, ?);", Arrays.asList(widgetTypeId, parameters, response, new Timestamp(System.currentTimeMillis() + expireInMs)));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
