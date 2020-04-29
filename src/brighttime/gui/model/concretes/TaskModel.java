package brighttime.gui.model.concretes;

import brighttime.be.Task;
import brighttime.bll.BllFacade;
import brighttime.bll.BllManager;
import brighttime.gui.model.interfaces.ITaskModel;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author rado
 */
public class TaskModel implements ITaskModel {

    private BllFacade bllManager;
    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    public TaskModel() {
        bllManager = new BllManager();
    }

    @Override
    public void loadTasks() {
        List<Task> allTasks = bllManager.loadTasks();
        taskList.addAll(allTasks);
        
    }

    /**
     *
     * @return
     */
    @Override
    public ObservableList<Task> getTasks() {
        return taskList;
    }

}
