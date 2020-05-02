package brighttime.gui.model.concretes;

import brighttime.be.Task;
import brighttime.bll.BllFacade;
import brighttime.gui.model.interfaces.IMainModel;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author rado
 */
public class MainModel implements IMainModel {

    private BllFacade bllManager;
    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    
    public MainModel(BllFacade bllManager) {
        this.bllManager = bllManager;
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
