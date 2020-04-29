package brighttime.gui.model;

import brighttime.be.Task;
import brighttime.gui.model.concretes.TaskModel;
import brighttime.gui.model.interfaces.ITaskModel;
import java.io.IOException;
import javafx.collections.ObservableList;

/**
 *
 * @author rado
 */
public class ModelManager implements ModelFacade {

    private ITaskModel taskModel;

    public ModelManager() throws IOException {
        taskModel = new TaskModel();
    }

    @Override
    public void loadTasks() {
        taskModel.loadTasks();
    }

    @Override
    public ObservableList<Task> getTasks() {
        return taskModel.getTasks();

    }

}