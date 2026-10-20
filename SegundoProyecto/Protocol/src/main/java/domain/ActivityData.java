package domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ActivityData {
    private List<LocalDate> days;
    private List<String> hours;
    private Map<String, Map<LocalDate,String>> cells;

    public ActivityData(List<LocalDate> days, List<String> hours, Map<String, Map<LocalDate, String>> cells) {
        this.days = days;
        this.hours = hours;
        this.cells = cells;
    }

    public List<LocalDate> getDays() {
        return days;
    }

    public List<String> getHours() {
        return hours;
    }

   public String getCell(String hour, LocalDate day){
       Map<LocalDate,String> row = cells.get(hour);
       if (row == null) return "";
       String value = row.get(day);
       return value == null ? "" : value; // que agarre el valor, si es nulo, devuelve "" si no, devuelve el valor
   }
}
