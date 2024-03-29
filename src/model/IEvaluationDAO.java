package model;

import entity.Evaluation;

import java.util.List;

public interface IEvaluationDAO {
    /**
     * 保存一条评价.
     */
    int save(int activityId, int foreignerId, int rank, String content, String evaluateTime);

    /**
     * 删除一条评价(外邦人使用).
     *
     * @param foreignerId 外邦人Id
     * @param evaluationId "评价"Id
     * */
    int delete(int foreignerId,int evaluationId);

    /**
     * 删除一条评价(龙妈使用).
     * */
    int delete(int evaluationId);

    /**
     * 更新一条评价.
     *
     * @param evaluateTime "评价"的Id
     * @param rank "评价"的等级
     * @param evaluateTime 评价的时间
     * */
    int update(int evaluationId,int rank,String content,String evaluateTime);

    /**
     * 通过评价的Id+外邦人Id得到实例.
     * */
    Evaluation getByEvaluationId(int foreignerId, int evaluationId);

    /**
     * 通过外邦人Id+活动Id得到实例.
     * */
    Evaluation getByActivityId(int foreignerId, int activityId);

    /**
     * 通过外邦人的Id找到评价.
     * */
    List<Evaluation> getList(int foreignerId);

    /**
     * 得到所有的"评价"列表.
     * */
    List<Evaluation> getList();
}
