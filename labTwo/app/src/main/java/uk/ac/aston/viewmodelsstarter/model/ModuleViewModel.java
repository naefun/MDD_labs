package uk.ac.aston.viewmodelsstarter.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import uk.ac.aston.viewmodelsstarter.MyApplication;

public class ModuleViewModel extends ViewModel {
    private MutableLiveData<Module> currentModule;

    public ModuleViewModel() {
        super();
        currentModule = new MutableLiveData<Module>();
        setModuleData(); // only for dummy data
    }

    public LiveData<Module> getCurrentModule() {
        return currentModule;
    }

    private void setModuleData() {
        currentModule.setValue(DataSource.getInstance(MyApplication.getAppContext()).getRealModule());
    }

    public void setCurrentModuleTitle(String newTitle) {
        Module m = currentModule.getValue();
        m.setTitle(newTitle);
        currentModule.setValue(m);
    }
}
