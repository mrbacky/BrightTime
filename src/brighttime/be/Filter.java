package brighttime.be;

import java.time.LocalDate;

/**
 * This class is a Filter class. It is used for methods related to filtering
 * tasks.
 *
 * @author annem
 */
public class Filter {

    private Project project;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * The constructor.
     *
     * @param project
     * @param startDate
     * @param endDate
     */
    public Filter(Project project, LocalDate startDate, LocalDate endDate) {
        this.project = project;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
