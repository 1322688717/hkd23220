package cc.ixcc.novelthree.http;

import cc.ixcc.novelthree.BuildConfig;

public class AllApi {
    //测试版
    public static final String HOST = BuildConfig.host;
    public static final String appinit = "common/appinit";
    public static final String autologin = "auth/auto_register";
    //public static final String getbindinfo = "get_bind_info";
    public static final String setcopyright = "user/copyright";
    public static final String bindthird = "bind_third";
    public static final String unbindthird = "unbind_third";
    public static final String bookchapter = "bookstores/chapter";
    public static final String chapterinfo = "bookstores/read";
    public static final String bookinfo = "bookstores/anime_info";
    public static final String getbookmore = "bookstores/get_anime_by_channel";
    public static final String bookchannel = "bookstores/channel";
    public static final String stackroombook = "bookstores/channel_data";
    public static final String bookclass = "bookstores/get_book_class";
    public static final String getbooks = "bookstores/get_books";
    public static final String addshelve = "bookstores/add_to_shelve";
    public static final String removeshelve = "bookstores/remove_from_shelve";
    public static final String bookshelf = "bookstores/shelf";
    public static final String readhistory = "bookstores/read_history";
    public static final String addreadhistory = "bookstores/add_read_history";
    public static final String emptyreadhistory = "bookstores/empty_read_history";
    public static final String deletereadhistory = "bookstores/delete_read_history";
    public static final String changeguess = "bookstores/change_guess";
    public static final String removelike = "bookstores/remove_like";
    public static final String updatereadtime = "bookstores/count_read_time";
    public static final String paychapter = "bookstores/pay_chapter";
    public static final String activitynotice = "bookstores/activity_notice";
    public static final String message = "message/index";
    public static final String deletemessage = "message/delete";
    public static final String readmessage = "message/reading";
    public static final String getreadcount = "message/get_unread_total";
    public static final String searchinfo = "search/base_info";
    public static final String search = "search/index";
    public static final String commentlist = "comment/list";
    public static final String commentdetail = "comment/detail";
    public static final String commentadd = "comment/add";
    public static final String commentlike = "comment/do_likes";
    public static final String cancellike = "comment/cancel_likes";
    public static final String clearsearch = "search/clear";
    public static final String welfarebaseinfo = "welfare/base_info";
    public static final String todaysigninfo = "welfare/today_sign_info";
    public static final String sign = "welfare/sign";
    public static final String userinfo = "user_info";
    public static final String myinfo = "user/base_info";
    public static final String googlePayCoin = "user/google_payforcoin_callback";
    public static final String googlePayVip = "user/google_payforvip_callback";
    public static final String qiniutoken = "common/getQiniuToken";
    public static final String saveavatar = "save_avatar";
    public static final String savesign = "save_sign";
    public static final String savesex = "save_gender";
    public static final String savenickname = "save_nickname";
    public static final String wallet = "withdraw/base_info";
    public static final String withdraw = "withdraw/at_once";
    public static final String withdrawlogs = "withdraw/logs";
    public static final String withdrawdetail = "withdraw/detail";
    public static final String chargereqconfig = "charge/charge_req_config";
    public static final String coincreateorder = "charge/create_order";
    public static final String hobby = "user/hobby";
    public static final String savehobby = "user/save_hobby";
    public static final String vipinfo = "vip/base_info";
    public static final String vipcreateorder = "vip/create_order";
    public static final String chuanyinvipinfo = "vip/chuanyin_base_info";
    public static final String chuanyincreateorder = "vip/chuanyin_create_order";
    public static final String rewordPackage = "bookstores/writer_reward_package";//打赏套餐
    public static final String commitReword = "bookstores/writer_reward";//打赏套餐
    public static final String writerapply = "user/get_writer_apply";
    public static final String bindemail="user/bindemail";//绑定邮箱
    public static final String cdk="cdk/exchange";//第三方统计收入信息失败
    public static final String addonlinesecs = "user/add_online_sec";
    public static final String crashinfo = "user/crash_info";

    public static final String pay_nicorn_config="charge/charge_nicorn_config";//传音支付配置
    public static final String paynicorn="charge/nicorn_pay";//传音H5支付

}
