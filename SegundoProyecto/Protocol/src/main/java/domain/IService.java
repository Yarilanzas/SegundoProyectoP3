package domain;

import java.time.LocalDate;
import java.util.List;

public interface IService {

    //login
    User login(String id, String password) throws Exception;
    void logout() throws Exception;
    void changePassword(String actual,String nueva, String confirmacion) throws Exception;

    //employees
    List<Employee> employeeFindAll() throws Exception;
    Employee employeeFindbyId(String id) throws Exception;
    List<Employee> employeeFindbyName(String name) throws Exception;
    void employeeSave(Employee employee) throws Exception;
    void employeeDelete(String id) throws Exception;

    //category
    List<Category> categoryFindAll() throws Exception;
    Category categoryFindbyId (String id) throws Exception;
    List<Category> categoryFindbyDesc(String descripction) throws Exception;
    void categorySave(Category category) throws Exception;
    void categoryDelete(String id) throws Exception;

    //resources
    List<Resource> resourcesFindAll() throws Exception;
    List<Resource> resourceFindbyDesc(String description) throws Exception;
    List<Resource> resourceFindbyCategory(String category) throws Exception;
    void resourceSave(Resource resource) throws Exception;
    void resourceUpdate(Resource resource) throws Exception;
    void resourceDelete(String id) throws Exception;

    //reservations
    Reservation reservationSave(Reservation reservation, List<Category> categories) throws Exception;
    List<Reservation> reservationFindAll() throws Exception;
    ReservaExtraccion reservationExtraer(String frase) throws Exception;
    void reservationCancel(String id) throws Exception;
    void reservationDelete(String id) throws Exception;

    //query
    CalendarData getCalendar(LocalDate fecha, Category category) throws Exception;
    ActivityData getHorarioSemanal(LocalDate fecharef) throws Exception;
    List<CategoryStatistics> getResourceStatistics(LocalDate desde, LocalDate hasta) throws Exception;
    List<CategoryStatistics> getActivityStatistics(LocalDate desde, LocalDate hasta) throws Exception;

    public void stop();
}

