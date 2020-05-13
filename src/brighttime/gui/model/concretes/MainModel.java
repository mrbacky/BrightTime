package brighttime.gui.model.concretes;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.bll.BllException;
import brighttime.bll.BllFacade;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 *
 * @author rado
 */
public class MainModel implements IMainModel {

    private final BllFacade bllManager;
    private final ObservableList<Client> clientList = FXCollections.observableArrayList();
    private final ObservableList<Project> projectList = FXCollections.observableArrayList();
    private final ObservableMap<LocalDate, List<TaskConcrete1>> taskMap = FXCollections.observableHashMap();
    private final ObservableList<TaskConcrete2> taskList = FXCollections.observableArrayList();

    public MainModel(BllFacade bllManager) {
        this.bllManager = bllManager;
    }

    @Override
    public Client addClient(Client client) throws ModelException {
        try {
            return bllManager.createClient(client);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadClients() throws ModelException {
        try {
            List<Client> allClients = bllManager.getClients();
            clientList.clear();
            clientList.addAll(allClients);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public ObservableList<Client> getClientList() {
        return clientList;
    }

    @Override
    public Project addProject(Project project) throws ModelException {
        try {
            return bllManager.createProject(project);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadProjects(Client client) throws ModelException {
        try {
            List<Project> allProjects = bllManager.getProjects(client);
            projectList.clear();
            projectList.addAll(allProjects);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public ObservableList<Project> getProjectList() {
        return projectList;
    }

    @Override
    public void addTask(TaskConcrete1 task) throws ModelException {
//        taskList.add(task);
        try {
            bllManager.createTask(task);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }

    }

    @Override
    public ObservableMap<LocalDate, List<TaskConcrete1>> getTasks() {
        return taskMap;
    }

    @Override
    public void loadTasks() throws ModelException {
        try {
            Map<LocalDate, List<TaskConcrete1>> allTasks = bllManager.Tasks();
            taskMap.clear();
            taskMap.putAll(allTasks);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public ObservableList<TaskConcrete2> getTaskList() {
        return taskList;
    }

    @Override
    public void getAllTasks() throws ModelException {
        try {
            List<TaskConcrete2> allTasks = bllManager.getAllTasks();
            for (TaskConcrete2 task : allTasks) {
                task.setTotalCostString(bllManager.formatCost(task.getTotalCost()));
                task.setTotalDurationString(bllManager.formatDuration(task.getTotalDurationSeconds()));
            }
            taskList.clear();
            taskList.addAll(allTasks);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void getAllTasksFiltered(Filter filter) throws ModelException {
        try {
            List<TaskConcrete2> temp = bllManager.getAllTasksFiltered(filter);
            for (TaskConcrete2 task : temp) {
                task.setTotalCostString(bllManager.formatCost(task.getTotalCost()));
                task.setTotalDurationString(bllManager.formatDuration(task.getTotalDurationSeconds()));
            }
            taskList.clear();
            taskList.addAll(temp);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

}
