package com.labpro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class SettingsManager {
    private final String settingsFileName = "settings.json";
    private final Path settingsPath;
    private Gson gson;

    public SettingsManager() {
        this.settingsPath = Paths.get(settingsFileName);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Settings loadSettings() {
        if (Files.notExists(settingsPath)) {
            Settings defaultSettings = new Settings();
            //savesettings
            return defaultSettings;
        }
        try {
            String jsonContent = new String(Files.readAllBytes(settingsPath));
            return gson.fromJson(jsonContent, Settings.class);
        } catch (IOException e) {
            System.err.println("Error loading settings dari: " + settingsFileName + ": " + e.getMessage());
            return new Settings();
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing settings JSON dari " + settingsFileName + ": " + e.getMessage());
            return new Settings();
        }
    }

    public void saveSettings(Settings settings) {
        try {
            String jsonContent =  gson.toJson(settings);
            Files.write(settingsPath, jsonContent.getBytes());
        } catch (IOException e) {
            System.err.println("Error saving settings ke " + settingsFileName + ": " + e.getMessage());
        }
    }
}
