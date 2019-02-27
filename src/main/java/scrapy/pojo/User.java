package scrapy.pojo;

public class User {
    private int id;
    private String type;
    private String nickName; //微博名称
    private String sex;   //性别
    private String concernNum; //关注数
    private String fansNum;  //粉丝数
    private String weiboNum; //发微博数
    private String address;  //地址
    private String jianjie; //简介
    private String label; //标签



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getConcernNum() {
        return concernNum;
    }

    public void setConcernNum(String concernNum) {
        this.concernNum = concernNum;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }

    public String getWeiboNum() { return weiboNum; }

    public void setWeiboNum(String weiboNum) {
        this.weiboNum = weiboNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString(){
        return "{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex + '\'' +
                ", concernNum=" + concernNum + '\'' +
                ", fansNum=" + fansNum + '\'' +
                ", weiboNum=" + weiboNum + '\'' +
                ", address=" + address + '\'' +
                ", jianjie=" + jianjie + '\'' +
                ", label=" + label + '\'';
    }
}
