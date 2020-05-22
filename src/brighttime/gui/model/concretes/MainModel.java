package brighttime.gui.model.concretes;

import brighttime.be.Client;
import brighttime.be.EventLog;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    private final ObservableList<Client> clientList = FXCollections.observableArrayList();
    private final ObservableList<Project> projectList = FXCollections.observableArrayList();
    private final ObservableMap<LocalDate, List<TaskConcrete1>> taskMap = FXCollections.observableHashMap();
    private final ObservableList<TaskConcrete2> taskList = FXCollections.observableArrayList();
    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private User user;
    private MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener;

    public MainModel(BllFacade bllManager) {
        this.bllManager = bllManager;
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
    public void addClient(Client client) throws ModelException {
        //TODO: EventLog: Same try catch for two BLL methods?
        try {
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Created the client: " + client.getName() + ", " + client.getHourlyRate() + " DKK/hour.",
                    user.getUsername()));
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
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Loaded all clients.",
                    user.getUsername()));
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
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Created the project for the client \"" + project.getClient().getName() + "\": "
                    + project.getName() + ", " + project.getHourlyRate() + " DKK/hour.",
                    user.getUsername()));
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
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Loaded all projects for the client \"" + client.getName() + "\".",
                    user.getUsername()));
            List<Project> allProjects = bllManager.getProjects(client);
            projectList.clear();
            projectList.addAll(allProjects);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadAllProjects() throws ModelException {
        try {
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Loaded all projects.",
                    user.getUsername()));
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
    public void addTask(TaskConcrete1 task) throws ModelException {
        try {
            //Created the task in the project "LEGO":
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Created the task in the project \"" + task.getProject().getName() + "\": "
                    + task.getDescription() + ".",
                    user.getUsername()));

            TaskConcrete1 freshTask = bllManager.createTask(task);
            List<TaskEntry> entryList = new ArrayList();
            freshTask.setTaskEntryList(entryList);
            System.out.println("date " + freshTask.getCreationTime().toLocalDate());
            List<TaskConcrete1> taskList = taskMap.get(freshTask.getCreationTime().toLocalDate());
            if (taskList == null) {
                taskList = new ArrayList<>();
                taskList.add(freshTask);
                taskMap.put(freshTask.getCreationTime().toLocalDate(), taskList);
            } else {
                taskList.add(0, freshTask);
                taskMap.remove(freshTask.getCreationTime().toLocalDate());
                taskMap.put(freshTask.getCreationTime().toLocalDate(), taskList);

            }
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }

    }

    @Override
    public ObservableMap<LocalDate, List<TaskConcrete1>> getTasks() {
        return taskMap;
    }

    @Override
    public void loadTasks(User user) throws ModelException {
        try {
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Loaded all tasks for the Time Tracker.",
                    user.getUsername()));
            Map<LocalDate, List<TaskConcrete1>> allTasks = bllManager.getAllTasksWithEntries(user);
//            if(taskMapListener!=null)
            //  temp removal
            taskMap.removeListener(taskMapListener);
            taskMap.clear();
            taskMap.putAll(allTasks);
//            if(taskMapListener!=null)
            taskMap.addListener(taskMapListener);
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
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Loaded all tasks for the Overview.",
                    user.getUsername()));
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
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Loaded all filtered tasks for the Overview.",
                    user.getUsername()));
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
    public void loadUsers() throws ModelException {
        try {
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Loaded all users.",
                    user.getUsername()));
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
    public void addTaskMapListener(MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener) {
        if (this.taskMapListener != null) {
            taskMap.removeListener(this.taskMapListener);
        }

        this.taskMapListener = taskMapListener;
        taskMap.addListener(taskMapListener);
    }

    @Override
    public boolean checkUsernameAvailability(String username) throws ModelException {
        try {
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Checked the availability of the username: " + username + ".",
                    user.getUsername()));
            return bllManager.checkUsernameAvailability(username);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public User createUser(User user) throws ModelException {
        try {
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Created the user: " + user.getUsername() + ".",
                    user.getUsername()));
            return bllManager.createUser(user);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

}
