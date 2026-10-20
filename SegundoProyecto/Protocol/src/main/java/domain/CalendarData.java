package domain;

import java.util.List;
import java.util.Map;

public class CalendarData {
    private List<Resource> resources;
    private List<String> hours;
    private Map<String, Map<String,String>> cells;

    public CalendarData(List<String> hours, List<Resource> resources, Map<String, Map<String, String>> cells) {
        this.hours = hours;
        this.resources = resources;
        this.cells = cells;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public List<String> getHours() {
        return hours;
    }

    public String getCell(String hour, String resourceId){
        Map<String,String> row = cells.get(hour);
        if (row == null) return "";
        String value = row.get(resourceId);
        return value == null ? "" : value; // que agarre el valor, si es nulo, devuelve "" si no, devuelve el valor
    }
}
