

package brighttime.gui.model;

import brighttime.bll.BllFacade;
import brighttime.bll.BllManager;
import brighttime.dal.DalFacade;
import brighttime.dal.DalManager;
import brighttime.dal.dao.interfaces.IProjectDAO;
import brighttime.gui.model.concretes.MainModel;
import brighttime.gui.model.concretes.TaskModel;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.model.interfaces.IProjectModel;
import brighttime.gui.model.interfaces.ITaskModel;
import java.io.IOException;

/**
 * 
 * @author rado
 * @inspiration Greg
 */
public class ModelCreator {
    
    private static ModelCreator instance;
    private final BllFacade bllManager;

    public ModelCreator() throws IOException {
        bllManager = new BllManager(new DalManager());
    }
    
    public static synchronized ModelCreator getInstance() throws IOException {
        if (instance == null) {
            instance = new ModelCreator();
        }
        return instance;
    }
    
    public ITaskModel createTaskModel() throws IOException{
        return new TaskModel(bllManager);
    }
    
    public IMainModel createMainModel(){
        return new MainModel(bllManager);
    }
    
}
