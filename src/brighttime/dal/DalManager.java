package brighttime.dal;

import brighttime.be.Task;
import brighttime.dal.dao.interfaces.ITaskDAO;
import brighttime.dal.dao.mockDAO.MockTaskDAO;
import brighttime.gui.controller.RootController;
import java.util.List;

/**
 *
 * @author rado
 */
public class DalManager implements DalFacade {

    private ITaskDAO mockTaskDAO;

    public DalManager() {
        mockTaskDAO = new MockTaskDAO();

    }

    @Override
    public List<Task> loadTasks() {
        return mockTaskDAO.loadTasks();
    }

}
