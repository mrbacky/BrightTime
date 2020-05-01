package brighttime.gui.model.interfaces;

import brighttime.be.Task;
import brighttime.gui.model.ModelException;
import javafx.collections.ObservableList;

/**
 *
 * @author rados
 */
public interface ITaskModel {

    /**
     * Adds a new task.
     *
     * @param task The new task.
     * @return The created task from the database.
     * @throws ModelException
     */
    Task addTask(Task task) throws ModelException;

    void loadTasks();

    ObservableList<Task> getTasks();

}
