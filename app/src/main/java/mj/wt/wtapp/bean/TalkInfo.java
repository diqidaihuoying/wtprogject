package mj.wt.wtapp.bean;

import java.util.List;

/**
 * Created by wantao on 2017/3/7.
 */

public class TalkInfo {

    /**
     * code : 200
     * data : {"username":"LoL无极剑圣开山鼻祖万涛","items":[{"msg_from":"宇宙疯狂输出全场Carry肖雨","msg_to":"LoL无极剑圣开山鼻祖万涛","msg_content":"LoL我只服万涛"},{"msg_from":"宇宙疯狂输出全场Carry肖雨","msg_to":"LoL无极剑圣开山鼻祖万涛","msg_content":"三个坑货，不牛逼不行"},{"msg_from":"银河系最怂比 永远不上 肖亮","msg_to":"宇宙疯狂输出全场Carry肖雨","msg_content":"这比万涛，老要我琴女上，自己捡人头"},{"msg_from":"肖顺","msg_to":"LoL无极剑圣开山鼻祖万涛","msg_content":"老子赏金天下第一，Timor天下第一"},{"msg_from":"LoL无极剑圣开山鼻祖万涛","msg_to":"肖顺","msg_content":"你的Timor就是对面的"},{"msg_from":"肖栋","msg_to":"肖顺","msg_content":"不是很会玩寒冰，下路怂一点"},{"msg_from":"银河系最怂比 永远不上 肖亮","msg_to":"肖顺","msg_content":"接下来有请我们的肖勇哥出场"},{"msg_from":"LoL肖勇","msg_to":"LoL无极剑圣开山鼻祖万涛","msg_content":"一朵菊花先到 随后枪出如龙"},{"msg_from":"肖栋","msg_to":"LoL肖勇","msg_content":"这把指望万涛带起，你大野别送人头"},{"msg_from":"LoL无极剑圣开山鼻祖万涛","msg_to":"肖栋","msg_content":"神预测:Adc全场输出最低"},{"msg_from":"宇宙疯狂输出全场Carry肖雨","msg_to":"LoL无极剑圣开山鼻祖万涛","msg_content":"你老夸奖他搞嘛"}]}
     * message : OK
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * username : LoL无极剑圣开山鼻祖万涛
         * items : [{"msg_from":"宇宙疯狂输出全场Carry肖雨","msg_to":"LoL无极剑圣开山鼻祖万涛","msg_content":"LoL我只服万涛"},{"msg_from":"宇宙疯狂输出全场Carry肖雨","msg_to":"LoL无极剑圣开山鼻祖万涛","msg_content":"三个坑货，不牛逼不行"},{"msg_from":"银河系最怂比 永远不上 肖亮","msg_to":"宇宙疯狂输出全场Carry肖雨","msg_content":"这比万涛，老要我琴女上，自己捡人头"},{"msg_from":"肖顺","msg_to":"LoL无极剑圣开山鼻祖万涛","msg_content":"老子赏金天下第一，Timor天下第一"},{"msg_from":"LoL无极剑圣开山鼻祖万涛","msg_to":"肖顺","msg_content":"你的Timor就是对面的"},{"msg_from":"肖栋","msg_to":"肖顺","msg_content":"不是很会玩寒冰，下路怂一点"},{"msg_from":"银河系最怂比 永远不上 肖亮","msg_to":"肖顺","msg_content":"接下来有请我们的肖勇哥出场"},{"msg_from":"LoL肖勇","msg_to":"LoL无极剑圣开山鼻祖万涛","msg_content":"一朵菊花先到 随后枪出如龙"},{"msg_from":"肖栋","msg_to":"LoL肖勇","msg_content":"这把指望万涛带起，你大野别送人头"},{"msg_from":"LoL无极剑圣开山鼻祖万涛","msg_to":"肖栋","msg_content":"神预测:Adc全场输出最低"},{"msg_from":"宇宙疯狂输出全场Carry肖雨","msg_to":"LoL无极剑圣开山鼻祖万涛","msg_content":"你老夸奖他搞嘛"}]
         */

        private String username;
        private List<ItemsBean> items;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * msg_from : 宇宙疯狂输出全场Carry肖雨
             * msg_to : LoL无极剑圣开山鼻祖万涛
             * msg_content : LoL我只服万涛
             */

            private String msg_from;
            private String msg_to;
            private String msg_content;

            public String getMsg_from() {
                return msg_from;
            }

            public void setMsg_from(String msg_from) {
                this.msg_from = msg_from;
            }

            public String getMsg_to() {
                return msg_to;
            }

            public void setMsg_to(String msg_to) {
                this.msg_to = msg_to;
            }

            public String getMsg_content() {
                return msg_content;
            }

            public void setMsg_content(String msg_content) {
                this.msg_content = msg_content;
            }
        }
    }
}
