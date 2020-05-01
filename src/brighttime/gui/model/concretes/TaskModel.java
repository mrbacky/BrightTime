package brighttime.gui.model.concretes;

import brighttime.be.Task;
import brighttime.bll.BllFacade;
import brighttime.bll.BllManager;
import brighttime.bll.BllException;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.ITaskModel;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author rado
 */
public class TaskModel implements ITaskModel {

    private final BllFacade bllManager;
    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    public TaskModel() throws IOException {
        bllManager = new BllManager();
    }

    @Override
    public Task addTask(Task task) throws ModelException {
        try {
            return bllManager.createTask(task);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadTasks() {
        List<Task> allTasks = bllManager.getTasks();
        taskList.addAll(allTasks);
    }

    @Override
    public ObservableList<Task> getTasks() {
        return taskList;
    }

}
