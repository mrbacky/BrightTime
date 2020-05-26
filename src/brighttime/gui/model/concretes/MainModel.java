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
import brighttime.gui.util.InputValidator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
    private final InputValidator inputValidator;
    private final ObservableList<Client> clientList = FXCollections.observableArrayList();
    private final ObservableList<Project> projectList = FXCollections.observableArrayList();

    private final ObservableMap<LocalDate, List<TaskConcrete1>> taskMap = FXCollections.observableHashMap();
    private final ObservableList<TaskConcrete2> taskList = FXCollections.observableArrayList();
    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private User user;
    private MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener;

    public MainModel(BllFacade bllManager) {
        this.bllManager = bllManager;
        this.inputValidator = new InputValidator();
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

        //Created the task in the project "LEGO":
        Thread t = new Thread(() -> {
            try {
                long startTimeT = System.currentTimeMillis();
                System.out.println("Time Thread log " + (System.currentTimeMillis() - startTimeT) + "--------------------------------------------");

                startTimeT = System.currentTimeMillis();
                TaskConcrete1 freshTask = bllManager.createTask(task);
                System.out.println("Time Thread createTask " + (System.currentTimeMillis() - startTimeT) + "--------------------------------------------");

                Platform.runLater(() -> { // Puts this on EventQueue to be handled by FX Application Thread
                    long startTime = System.currentTimeMillis();
                    if (freshTask.getTaskEntryList() == null) {
                        List<TaskEntry> entryList = new ArrayList();
                        freshTask.setTaskEntryList(entryList);
                    }
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
                    System.out.println("Time runLater GUI elements " + (System.currentTimeMillis() - startTime) + "--------------------------------------------");

                });
            } catch (BllException ex) {
                Logger.getLogger(MainModel.class.getName()).log(Level.SEVERE, null, ex); // TODO
            }
        }
        );
        t.setDaemon(true);
        t.start();

    }

    @Override
    public ObservableMap<LocalDate, List<TaskConcrete1>> getTasks() {
        return taskMap;
    }

    @Override
    public void loadTasks(User user) throws ModelException {
        try {
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
    public void addTaskMapListener(MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener) {
        if (this.taskMapListener != null) {
            taskMap.removeListener(this.taskMapListener);
        }

        this.taskMapListener = taskMapListener;
        taskMap.addListener(taskMapListener);
    }

    @Override
    public int checkUsernameAvailability(String username) throws ModelException {
        try {

            return bllManager.checkUsernameAvailability(username);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void createUser(User user) throws ModelException {
        if (!inputValidator.usernameCheck(user.getUsername())) {
            throw new ModelException("The username is invalid. Please try another username.");
        }
        if (!inputValidator.passwordCheck(user.getPassword())) {
            throw new ModelException("The password is invalid. Please enter another password.");
        }
        if (checkUsernameAvailability(user.getUsername()) == 0) {
            try {
                User newUser = bllManager.createUser(user);
                userList.add(newUser);
            } catch (BllException ex) {
                throw new ModelException(ex.getMessage());
            }
        } else {
            throw new ModelException("Someone already has this username. Please try another username.");
        }
    }

    @Override
    public User updateUserDetails(User updatedUser) throws ModelException {
        if (!inputValidator.usernameCheck(user.getUsername())) {
            throw new ModelException("The username is invalid. Please try another username.");
        }
        int result = checkUsernameAvailability(updatedUser.getUsername());
        if (result == 0 || result == updatedUser.getId()) {
            try {
                return bllManager.updateUserDetails(updatedUser);
            } catch (BllException ex) {
                throw new ModelException(ex.getMessage());
            }
        } else {
            throw new ModelException("Someone already has this username. Please try another username.");
        }

    }

    @Override
    public void deleteUser(User selectedUser) throws ModelException {
        //TODO: EventLog
        //TODO: Should there be a try catch on each method?
        try {
            if (bllManager.hasTask(selectedUser)) {
                User deletedUser = bllManager.inactivateUser(selectedUser);
                userList.remove(deletedUser);
            } else {
                try {
                    User deletedUser = bllManager.deleteUser(selectedUser);
                    userList.remove(deletedUser);
                } catch (BllException ex) {
                    throw new ModelException(ex.getMessage());
                }
            }
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void updateClient(Client selectedClient) throws ModelException {
        try {
            Client updatedClient = bllManager.updateClient(selectedClient);
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
