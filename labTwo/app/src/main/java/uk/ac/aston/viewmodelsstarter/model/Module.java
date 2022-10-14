package uk.ac.aston.viewmodelsstarter.model;

public class Module {

    private String code;
    private String title;
    private int credits;
    private String description;
    private String learningOutcomes;
    private static int moduleCount;
    private static final Integer lock = 0;

    public Module() {
        synchronized (lock) {
            moduleCount++;
            code = "CSF" + moduleCount;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLearningOutcomes() {
        return learningOutcomes;
    }

    public void setLearningOutcomes(String learningOutcomes) {
        this.learningOutcomes = learningOutcomes;
    }
}
