package entity;

/**
 * 活动的实体类.
 */
public class Activity {
    private int activityId;
    private int dragonGroupId;
    private String name;
    private String content;
    private String startTime;
    private String overTime;

    public Activity() {
    }

    public Activity(int activityId, int dragonGroupId, String name, String content, String startTime, String overTime) {
        this.name = name;
        this.content = content;
        this.startTime = startTime;
        this.overTime = overTime;
        this.dragonGroupId = dragonGroupId;
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getOverTime() {
        return overTime;
    }

    public int getDragonGroupId() {
        return dragonGroupId;
    }

    public void setDragonGroupId(int dragonGroupId) {
        this.dragonGroupId = dragonGroupId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }
}
