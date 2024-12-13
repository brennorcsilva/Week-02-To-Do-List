public class Task {
    private String id;
    private String description;
    private boolean completed;

    public Task(String id, String description, boolean completed) { 
        this.id = id;
        this.description = description;
        this.completed = completed;
    }

    public String getId(){
        return id;
    }

    public String getDescription(){
        return description;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void complete(){
        this.completed = true;
    }

    public void uncomplete(){
        this.completed = false;
    }
}
