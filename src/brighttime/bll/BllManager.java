package brighttime.bll;

import brighttime.be.Task;
import brighttime.bll.util.DurationConverter;
import brighttime.dal.DalFacade;
import brighttime.dal.DalManager;
import brighttime.dal.dao.interfaces.ITaskDAO;
import brighttime.dal.dao.mockDAO.MockTaskDAO;
import java.util.List;

/**
 *
 * @author rado
 */
public class BllManager implements BllFacade {

    private DalFacade dalManager = new DalManager();
    private DurationConverter durationConverter;

    public BllManager() {
        dalManager = new DalManager();
        durationConverter = new DurationConverter();

    }

    @Override
    public List<Task> loadTasks() {
        return dalManager.loadTasks();
    }

    @Override
    public String convertDuration(int duration) {
        return durationConverter.sec_To_Format(duration);
    }

    @Override
    public int convertDuration(String duration) {
        return durationConverter.format_To_Sec(duration);
    }

}
