package IIProyectoP3.logic;

import IIProyectoP3.data.UserDAO;
import domain.Employee;
import domain.User;

import java.util.List;

public class EmployeeService {

    private final UserDAO userDAO;

    public EmployeeService(){
        this.userDAO = new UserDAO();
    }

    public List<Employee> findAll() throws Exception{
       return userDAO.findAllEmployees();
    }

    public Employee findById(String id) throws Exception{
       try {
           User user = userDAO.read(id);
           if (user instanceof Employee){
               return (Employee) user ;
           }
       } catch (Exception ignored) {

       }
       return null;
    }

    public List<Employee> findByName(String name) throws Exception{
        return userDAO.findEmployeebyName(name);
    }

    public void save(Employee employee) throws Exception{
        Employee exist = null;
        if (employee.getId() != null && !employee.getId().trim().isEmpty()){
            exist = findById(employee.getId());
        }

        if (exist != null){
            employee.setPassword(exist.getPassword());
            userDAO.update(employee);
        } else {
            String nuevo = generateId(findAll());
            employee.setId(nuevo);
            employee.setPassword(employee.getId());

            userDAO.create(employee);
        }

    }

    public void delete(String id) throws Exception{
       Employee exist = findById(id);
       if (exist != null){
           userDAO.delete(id);
       } else {
           throw new Exception("El funcionario no existe");
       }
    }

    private Employee findByIdIn(List<Employee> list, String id){
        for (Employee e : list){
            if (e.getId().equals(id)){
                return e;
            }
        }
        return null;
    }

    private String generateId(List<Employee> emp){
        int max = 0;
        for (Employee e : emp){
            String num = e.getId().replace("FUN-","");
            try{
                int valor = Integer.parseInt(num);
                if (valor > max){
                    max = valor;
                }
            } catch (NumberFormatException ignored) {}
        }
        int numNuevo = max +1;
        String numeroText = String.valueOf(numNuevo);
        while (numeroText.length() <3){
            numeroText = "0" + numeroText;
        }
        return "FUN-" + numeroText;
    }
}