package com.connorcode.universaltick;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Util {
    public static void checkVersion() throws IOException {
        URL url = new URL("https://version.connorcode.com/UniversalTick/status?code=o3anajoP2i2XAHUgB9G7");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "text/plain");
        InputStream responseStream = connection.getInputStream();

        Scanner scanner = new Scanner(responseStream, StandardCharsets.UTF_8.name());
        String version = scanner.useDelimiter("\\A").next().split(",")[0];

        if (version.equals(UniversalTick.VERSION)) return;
        LogManager.getLogManager().getLogger("UniversalTick")
                .log(Level.WARNING, String.format("Version Outdated! (%s > %s)", version, UniversalTick.VERSION));
    }
}
