package entity;

import base.Person;

import java.util.List;

public class DragonTrainer extends Person {
    private int dragonTrainerId;
    private int dragonGroupId;

    public DragonTrainer() {
    }

    public DragonTrainer(int dragonTrainerId, int dragonGroupId, String name, String username, String password) {
        super(name, username, password);
        this.dragonTrainerId = dragonTrainerId;
        this.dragonGroupId = dragonGroupId;
    }

    public int getDragonTrainerId() {
        return dragonTrainerId;
    }

    public void setDragonTrainerId(int dragonTrainerId) {
        this.dragonTrainerId = dragonTrainerId;
    }

    public int getDragonGroupId() {
        return dragonGroupId;
    }

    public void setDragonGroupId(int dragonGroupId) {
        this.dragonGroupId = dragonGroupId;
    }
}
