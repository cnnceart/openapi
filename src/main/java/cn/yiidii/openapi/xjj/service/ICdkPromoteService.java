package cn.yiidii.openapi.xjj.service;

public interface ICdkPromoteService {
    /**
     * 添加cdk邀请记录
     * @param cdk           cdk
     * @param promoteCdk     推广的cdk
     * @return
     */
    int addCdkPromote(String cdk, String promoteCdk);
}
