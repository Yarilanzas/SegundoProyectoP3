package domain;
import java.io.Serializable;

public class DetailReservation implements Serializable{
    private Category requestedCategory;
    private Resource assignedResource;

    public DetailReservation(Category requestedCategory, Resource assignedResource) {
        this.requestedCategory = requestedCategory;
        this.assignedResource = assignedResource;
    }
    public DetailReservation() {
        this.requestedCategory = null;
        this.assignedResource = null;
    }

    public Category getRequestedCategory() {
        return requestedCategory;
    }

    public Resource getAssignedResource() {
        return assignedResource;
    }
}