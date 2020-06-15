package brighttime.gui.model.concretes;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.be.TaskEntry;
import brighttime.be.User;
import brighttime.bll.BllException;
import brighttime.bll.BllFacade;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 *
 * @author rado
 */
public class MainModel implements IMainModel {

    private final BllFacade bllManager;
    private User user;

    private final ObservableList<Client> clientList = FXCollections.observableArrayList();
    private final ObservableList<Project> projectList = FXCollections.observableArrayList();
    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private final ObservableList<TaskConcrete2> taskList = FXCollections.observableArrayList();
    private final ObservableMap<LocalDate, List<TaskConcrete1>> taskMap = FXCollections.observableHashMap();
    private MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener;

    public MainModel(BllFacade bllManager) {
        this.bllManager = bllManager;
    }

    /**
     * @param taskMapListener Setting up "Singleton" for listener so there is
     * always one instance of listener attached to task map.
     */
    @Override
    public void addTaskMapListener(MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener) {
        if (this.taskMapListener != null) {
            taskMap.removeListener(this.taskMapListener);
        }
        this.taskMapListener = taskMapListener;
        taskMap.addListener(taskMapListener);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void loadTasks(Filter filter) throws ModelException {
        try {
            Map<LocalDate, List<TaskConcrete1>> allTasks = bllManager.getAllTasksWithEntries(filter);
            //  Temp removal of listener becouse putAll is not one action. It is loop of put commands onto task map.
            taskMap.removeListener(taskMapListener);
            taskMap.clear();
            taskMap.putAll(allTasks);
            taskMap.addListener(taskMapListener);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadOverviewTasks() throws ModelException {
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
    public void loadOverviewTasksFiltered(Filter filter) throws ModelException {
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

    @Override
    public ObservableMap<LocalDate, List<TaskConcrete1>> getTaskMap() {
        return taskMap;
    }

    @Override
    public ObservableList<TaskConcrete2> getOverviewTaskList() {
        return taskList;
    }

    @Override
    public void addTask(TaskConcrete1 task) throws ModelException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<TaskConcrete1> future = executorService.submit(() -> bllManager.createTask(task));
        executorService.shutdown();
        try {
            addLocally(future.get());
        } catch (InterruptedException | ExecutionException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    private void addLocally(TaskConcrete1 freshTask) {
        if (freshTask.getTaskEntryList() == null) {
            List<TaskEntry> entryList = new ArrayList();
            freshTask.setTaskEntryList(entryList);
        }
        List<TaskConcrete1> taskList = taskMap.get(freshTask.getModifiedDateTime().toLocalDate());
        if (taskList == null) {
            taskList = new ArrayList<>();
            taskList.add(freshTask);
            taskMap.put(freshTask.getModifiedDateTime().toLocalDate(), taskList);
        } else {
            taskList.add(0, freshTask);
            taskMap.remove(freshTask.getModifiedDateTime().toLocalDate());
            taskMap.put(freshTask.getModifiedDateTime().toLocalDate(), taskList);
        }
    }

    @Override
    public void addClient(Client client) throws ModelException {
        try {
            Client newClient = bllManager.createClient(client);
            clientList.add(newClient);
            Comparator<Client> byName = Comparator.comparing(Client::getName);
            FXCollections.sort(clientList, byName);
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
    public void addProject(Project project) throws ModelException {
        try {
            Project newProject = bllManager.createProject(project);
            projectList.add(newProject);
            Comparator<Project> byName = Comparator.comparing(Project::getName);
            FXCollections.sort(projectList, byName);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadProjects(Client client) throws ModelException {
        try {
            List<Project> allProjects = bllManager.getProjectsForAClient(client);
            projectList.clear();
            projectList.addAll(allProjects);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadAllProjects() throws ModelException {
        try {
            List<Project> allProjects = bllManager.getAllProjects();
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
    public void loadUsers() throws ModelException {
        try {
            List<User> allUsers = bllManager.getUsers();
            userList.clear();
            userList.addAll(allUsers);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public ObservableList<User> getUserList() {
        return userList;
    }

    @Override
    public void createUser(User user) throws ModelException {
        try {
            User newUser = bllManager.createUser(user);
            userList.add(newUser);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void updateUserDetails(User updatedUser) throws ModelException {
        try {
            bllManager.updateUserDetails(updatedUser);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void deleteUser(User selectedUser) throws ModelException {
        try {
            User deletedUser = bllManager.deleteUser(selectedUser);
            userList.remove(deletedUser);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void updateClient(Client selectedClient) throws ModelException {
        try {
            bllManager.updateClient(selectedClient);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void deleteClient(Client selectedClient) throws ModelException {
        try {
            Client deletedClient = bllManager.deleteClient(selectedClient);
            clientList.remove(deletedClient);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

}
