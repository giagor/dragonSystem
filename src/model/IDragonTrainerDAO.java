package model;

import entity.DragonTrainer;

import java.util.List;

public interface IDragonTrainerDAO {
    /**
     * 通过具体信息来保存驯龙高手.
     * */
    int save(int dragonGroupId,String name,String username,String password);

    /**
     * 通过id来删除驯龙高手.
     * */
    int delete(int id);

    /**
     * 通过具体信息来更新驯龙高手.
     * */
    int update(int id,int dragonGroupId,String name,String password);

    /**
     * 通过id来得到驯龙高手.
     * */
    DragonTrainer get(int id);

    /**
     * 通过用户名+密码来得到驯龙高手.
     * */
    DragonTrainer get(String username,String password);

    /**
     * 得到所有的驯龙高手.
     * */
    List<DragonTrainer> getList();

}
