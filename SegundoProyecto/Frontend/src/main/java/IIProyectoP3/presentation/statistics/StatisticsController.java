package IIProyectoP3.presentation.statistics;

import IIProyectoP3.logic.ReservationQueryService;

import java.time.LocalDate;

import IIProyectoP3.logic.ServiceProxy;
import domain.CategoryStatistics;




public class StatisticsController {
    private StatisticsView view;
    private StatisticsModel model;
    //private final ReservationQueryService queryService = new ReservationQueryService();

    public StatisticsController(StatisticsView view, StatisticsModel model) {
        this.view = view;
        this.model = model;

        view.setController(this);
        view.setModel(model);
    }

    public void cargarRecursos(LocalDate desde, LocalDate hasta){
        try{
            model.setCategoryStats(ServiceProxy.instance().getResourceStatistics(desde,hasta));
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void cargarActividades(LocalDate desde, LocalDate hasta){
        try{
            model.setActivityStats(ServiceProxy.instance().getActivityStatistics(desde,hasta));
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}