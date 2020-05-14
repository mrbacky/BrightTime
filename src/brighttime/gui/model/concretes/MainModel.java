package brighttime.gui.model.concretes;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.bll.BllException;
import brighttime.bll.BllFacade;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;

/**
 *
 * @author rado
 */
public class MainModel implements IMainModel {

    private final BllFacade bllManager;
    private final ObservableList<Client> clientList = FXCollections.observableArrayList();
    private final ObservableList<Project> projectList = FXCollections.observableArrayList();
    public final ObservableMap<LocalDate, List<Task>> taskMap = FXCollections.observableHashMap();

    private final ObservableMap<LocalDate, List<Node>> taskItems = FXCollections.observableHashMap();

    private final ObservableList<Node> nodeList = FXCollections.observableArrayList();

    public MainModel(BllFacade bllManager) {
        this.bllManager = bllManager;
//        setupTaskMapListener();
    }

    @Override
    public ObservableList<Node> getNodeList() {
        return nodeList;
    }

    @Override
    public ObservableMap<LocalDate, List<Node>> getTaskItemMap() {
        return taskItems;
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
    public void addTask(Task task) throws ModelException {
//        taskList.add(task);
        try {
            
            Task newTask = bllManager.createTask(task);
            List<Task> taskList = taskMap.get(newTask.getCreationTime().toLocalDate());
            if (taskList == null) {
                taskList = new ArrayList<>();
                taskList.add(newTask);
                taskMap.put(newTask.getCreationTime().toLocalDate(), taskList);
            } else {
//                i // O(n*m) = O(n*n)
                taskList.add(newTask);

            }

        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }

    }

    @Override
    public ObservableMap<LocalDate, List<Task>> getTasks() {
        return taskMap;
    }

    @Override
    public void loadTasks() throws ModelException {
        try {
            
            Map<LocalDate, List<Task>> allTasks = bllManager.Tasks();
            taskMap.clear();
            taskMap.putAll(allTasks);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

//    private void setupTaskMapListener() {
//        taskMap.addListener((MapChangeListener.Change<? extends LocalDate, ? extends List<Task>> change) -> {
//            
//        });
//        
//    }
}
