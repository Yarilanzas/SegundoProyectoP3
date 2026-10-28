package IIProyectoP3.presentation.alertas;

import domain.Resource;
import javax.swing.DefaultListModel;
import java.util.List;

public class ResourceListModel {
    private final DefaultListModel<String> listModel;

    public ResourceListModel() {
        this.listModel = new DefaultListModel<>();
    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

    public void setResources(List<Resource> resources) {
        listModel.clear();
        if (resources != null) {
            for (Resource res : resources) {
                listModel.addElement(res.getDescription() + " (" + res.getId() + ")");
            }
        }
    }

    public void clear() {
        listModel.clear();
    }
}
