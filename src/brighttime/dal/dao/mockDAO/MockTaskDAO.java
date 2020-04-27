package brighttime.dal.dao.mockDAO;

import brighttime.be.Task;
import brighttime.dal.dao.interfaces.ITaskDAO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rado
 */
public class MockTaskDAO implements ITaskDAO {

    public MockTaskDAO() {

    }

    @Override
    public List<Task> loadTasks() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1, "create UI", "BrightTime", "Grumsen Development", 9819121));
        taskList.add(new Task(2, "tidy up apartmen", "Home", "Me", 65111726));
        taskList.add(new Task(3, "code all night", "BrightTime", "Grumsen Development", 15654));
        taskList.add(new Task(4, "write email", "PROJECT X", "CLIENT X", 15654));
        taskList.add(new Task(5, "create DB", "BrightTime", "Grumsen Development", 26281));
        taskList.add(new Task(6, "buy newspapers", "Care+", "Housing Assocation", 15654));
        taskList.add(new Task(7, "build project 74", "project 74", "Mike Wazowski", 15654));
        taskList.add(new Task(8, "Hold my bear", "lets drink", "alcohol", 15654));
        taskList.add(new Task(9, "Do homework", "exam", "easv", 125654));
        taskList.add(new Task(10, "do dishes", "Home", "Me", 457501));
        taskList.add(new Task(11, "do laundry", "Home", "Me", 154654));
        taskList.add(new Task(12, "buy cat", "Home", "Me", 152654));
        taskList.add(new Task(13, "barber visit", "Me", "Rado", 477878));
        taskList.add(new Task(14, "buy groceries", "Food", "Me", 45752752));
        taskList.add(new Task(15, "tidy up emails", "PC work", "Me", 4578));
        taskList.add(new Task(16, "Buy new car", "Me", "Rado", 3725755));

        return taskList;
    }

}
