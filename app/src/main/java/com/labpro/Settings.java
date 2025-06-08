package com.labpro;

import java.util.List;
import java.util.ArrayList;

public class Settings {
    private DataFormat  dataStoreFormat;
    private String dataStoreLocation;
    private List<String> loadedPlugins;


    public Settings() {
        this.dataStoreFormat = DataFormat.JSON;
        this.dataStoreLocation = "src/main/resources/Data";
        this.loadedPlugins = new ArrayList<>();

    }

    public DataFormat getDataStoreFormat() {
        return dataStoreFormat;
    }
    public void setDataStoreFormat(DataFormat dataStoreFormat) {this.dataStoreFormat = dataStoreFormat;}

    public String getDataStoreLocation() {
        return dataStoreLocation;
    }
    public void setDataStoreLocation(String dataStoreLocation) {this.dataStoreLocation = dataStoreLocation;}

    public List<String> getLoadedPlugins() {
        return loadedPlugins;
    }
    public void setLoadedPlugins(List<String> loadedPlugins) {
        this.loadedPlugins = loadedPlugins;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "dataStoreFormat=" + dataStoreFormat + // Enum akan otomatis dikonversi ke string
                ", dataStoreLocation='" + dataStoreLocation + '\'' +
                ", loadedPlugins=" + loadedPlugins +
                '}';
    }


}
