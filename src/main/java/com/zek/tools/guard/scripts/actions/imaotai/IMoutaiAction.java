package com.zek.tools.guard.scripts.actions.imaotai;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.zek.tools.guard.core.action.TaskAction;
import com.zek.tools.guard.core.context.TaskContext;
import com.zek.tools.guard.core.ex.ActionException;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.*;

/**
 * @author chenp
 * @date 2024/2/17
 */
public class IMoutaiAction implements TaskAction {

    private String name = "i茅台";
    private String RESERVE_RULE = "0";
    private String mt_r = "clips_OlU6TmFRag5rCXwbNAQ/Tz1SKlN8THcecBp/";
    private Map<String, String> ITEM_MAP = new HashMap<>();
    private String[] ITEM_CODES = { "10941", "10942" };
    private String AES_KEY = "qbhajinldepmucsonaaaccgypwuvcjaa";
    private String AES_IV = "2018534749963515";
    private AESEncrypt encrypt;

    private String mt_version;
    private List<MoutaiShop> shopList = new ArrayList<>();
    private String currentSessionId;
    private TaskContext context;

    public IMoutaiAction() {
        this.ITEM_MAP.put("10941", "53%vol 500ml贵州茅台酒（甲辰龙年）");
        this.ITEM_MAP.put("10942", "53%vol 375ml×2贵州茅台酒（甲辰龙年）");
        this.ITEM_MAP.put("10056", "53%vol 500ml茅台1935");
        this.ITEM_MAP.put("2478", "53%vol 500ml贵州茅台酒（珍品）");
        this.encrypt = new AESEncrypt(AES_KEY, AES_IV);

        // Initialize other attributes
    }


    private Map<String, String> createHeaders(String user_id, String token, String lat, String lng, String mtDeviceId) {
        Map<String, String> headers = new HashMap<>();
        String header_context = "MT-Lat: 28.499562\n" +
                "MT-K: 1675213490331\n" +
                "MT-Lng: 102.182324\n" +
                "Host: app.moutai519.com.cn\n" +
                "MT-User-Tag: 0\n" +
                "Accept: */*\n" +
                "MT-Network-Type: WIFI\n" +
                "MT-Token: 1\n" +
                "MT-Info: 028e7f96f6369cafe1d105579c5b9377\n" +
                "MT-Device-ID: 2F2075D0-B66C-4287-A903-DBFF6358342A\n" +
                "MT-Bundle-ID: com.moutai.mall\n" +
                "Accept-Language: en-CN;q=1, zh-Hans-CN;q=0.9\n" +
                "MT-Request-ID: 167560018873318465\n" +
                "MT-APP-Version: 1.3.7\n" +
                "User-Agent: iOS;16.3;Apple;?unrecognized?\n" +
                "MT-R: clips_OlU6TmFRag5rCXwbNAQ/Tz1SKlN8THcecBp/HGhHdw==\n" +
                "Content-Length: 93\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Connection: keep-alive\n" +
                "Content-Type: application/json\n" +
                "userId: 2\n";

        String[] lines = header_context.strip().split("\n");
        for (String line : lines) {
            String[] temp_l = line.split(": ");
            headers.put(temp_l[0], temp_l[1]);
        }
        headers.put("userId", user_id);
        headers.put("MT-Token", token);
        headers.put("MT-Lat", lat);
        headers.put("MT-Lng", lng);
        headers.put("MT-Device-ID", mtDeviceId);
        headers.put("MT-Request-ID", String.valueOf(System.currentTimeMillis()));
        headers.put("MT-APP-Version", mt_version);
        headers.put("current_session_id", this.currentSessionId);
        return headers;
    }

    /**
     * 初始化app版本
     */
    private void initAppVersion() {

        String url = "https://itunes.apple.com/cn/lookup?id=1600482450";
        String text = HttpUtil.get(url);
        com.alibaba.fastjson2.JSONObject json = com.alibaba.fastjson2.JSONObject.parseObject(text);
        String version = json.getJSONArray("results").getJSONObject(0).getString("version");
        this.mt_version = version;

    }

    private long getDayTime() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long dayTime = calendar.getTimeInMillis();
        return dayTime;
    }
    private void initCurrentSessionId() {

        String my_url = "https://static.moutai519.com.cn/mt-backend/xhr/front/mall/index/session/get/" + getDayTime();
        // Replace this line with actual HTTP request handling to get the response JSON
        String text = HttpUtil.get(my_url);
        String current_session_id = JSONObject.parseObject(text).getJSONObject("data").getString("sessionId");
        this.currentSessionId = current_session_id;

    }



    private String getBuyShopId(String province, String itemCode, String city) {
        String url = String.format("https://static.moutai519.com.cn/mt-backend/xhr/front/mall/shop/list/slim/v3/%s/%s/%s/%d", this.currentSessionId, province, itemCode, getDayTime());
        String text = HttpUtil.get(url);
        JSONArray shops = JSONObject.parseObject(text).getJSONObject("data").getJSONArray("shops");
        System.currentTimeMillis();
        // 先寻找城市所有门店
        Set<String> myCityShopIds = new HashSet<>();
        for (MoutaiShop shop : shopList) {
            if (shop.getCityName().equals(city)) {
                myCityShopIds.add(shop.getShopId());
            }
        }
        List<MoutaiItemDetail> wantDetails = new ArrayList<>();
        for (int i = 0; i < shops.size(); i++) {
            JSONObject item = shops.getJSONObject(i);
            String shopId = item.getString("shopId");
            if (myCityShopIds.contains(shopId)) {
                JSONArray subItems = item.getJSONArray("items");
                for (int j = 0; j < subItems.size(); j++) {
                    JSONObject subItem = subItems.getJSONObject(j);
                    MoutaiItemDetail detail = subItem.to(MoutaiItemDetail.class);
                    detail.setShopId(shopId);
                    if (detail.getItemId().equalsIgnoreCase(itemCode)) {
                        wantDetails.add(detail);
                    }
                }
            }
        }
        // 取最大数量的门店
        Collections.sort(wantDetails, (o1, o2) -> o2.getInventory() - o1.getInventory());
        if (wantDetails.size() > 0) {
            return wantDetails.get(0).getShopId();
        }
        else {
            return "";
        }

    }

    private void reservation(String userId, String shopId, String itemId, Map<String, String> headers) throws Exception{
        Map<String,Object> params = new HashMap<>();

        params.put("sessionId", this.currentSessionId);
        params.put("userId", userId);
        params.put("shopId", shopId);
        JSONObject item = new JSONObject();
        item.put("itemId", itemId);
        item.put("count", 1);
        JSONArray itemInfoList = new JSONArray();
        itemInfoList.add(item);
        params.put("itemInfoList", itemInfoList);
        params.put("actParam", encrypt.aes_encrypt(JSONObject.toJSONString(params)));

        params.remove("userId");
        HttpRequest request = HttpUtil.createPost("https://app.moutai519.com.cn/xhr/front/mall/reservation/add");
        request.headerMap(headers,true);
        request.body(JSONObject.toJSONString(params));
        String responseBody = request.execute().body();
        JSONObject respObject = JSONObject.parseObject(responseBody);
        //{"code":4021,"message":"申购已结束，请明天再来"}
        context.appendLog(String.format("申购[%s]:%s" , ITEM_MAP.get(itemId), respObject.getString("message")));

    }

    private void getUserEnergyAward(String token, String mtDeviceId, Map<String, String> headers) {
        HttpRequest request = HttpUtil.createPost("https://h5.moutai519.com.cn/game/isolationPage/getUserEnergyAward");
        request.headerMap(headers,true);
        request.body("{}");
        Map<String, String> cookie = new HashMap<>();
        //   "MT-Device-ID-Wap": self.headers["MT-Device-ID"],
        //            "MT-Token-Wap": self.headers["MT-Token"],
        //            "YX_SUPPORT_WEBP": "1",

        List<HttpCookie> cookies = new ArrayList<>();
        cookies.add(new HttpCookie("MT-Device-ID-Wap", mtDeviceId));
        cookies.add(new HttpCookie("MT-Token-Wap", token));
        cookies.add(new HttpCookie("YX_SUPPORT_WEBP", "1"));
        request.cookie(cookies);
        String responseBody = request.execute().body();
        JSONObject respObject = JSONObject.parseObject(responseBody);
        context.appendLog("小茅运:" +respObject.getString("message"));
    }

    public void initShops(String lat, String lng) throws IOException {
        // 获取门店的资源地址
        String url = "https://static.moutai519.com.cn/mt-backend/xhr/front/mall/resource/get";
        String mtR = "clips_OlU6TmFRag5rCXwbNAQ/Tz1SKlN8THcecBp/HGhHdw==";
        String mtRequestId = System.currentTimeMillis() + "" + (int)(Math.random() * (999999999 - 1111111) + 1111111);
        String mtDeviceId = System.currentTimeMillis() + "" + (int)(Math.random() * (999999999 - 1111111) + 1111111);
        HttpRequest request = HttpUtil.createGet(url);
        request.header("X-Requested-With", "XMLHttpRequest")
                .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0_1 like Mac OS X)")
                .header("Referer", "https://h5.moutai519.com.cn/gux/game/main?appConfig=2_1_2")
                .header("Client-User-Agent", "iOS;16.0.1;Apple;iPhone 14 ProMax")
                .header("MT-R", mtR)
                .header("Origin", "https://h5.moutai519.com.cn")
                .header("MT-APP-Version", mt_version)
                .header("MT-Request-ID", mtRequestId)
                .header("Accept-Language", "zh-CN,zh-Hans;q=1")
                .header("MT-Device-ID", mtDeviceId)
                .header("Accept", "application/json, text/javascript, */*; q=0.01")
                .header("mt-lng", lng)
                .header("mt-lat", lat);
        HttpResponse response = request.execute();
        String responseBody = response.body();
        JSONObject mtshops = JSONObject.parseObject(responseBody).getJSONObject("data").getJSONObject("mtshops_pc");
        String storeUrl = mtshops.getString("url");
        // 获取门店列表
        HttpRequest shopsRequest = HttpUtil.createGet(storeUrl);
        String shopsData = shopsRequest.execute().body();
        JSONObject shopObjects = JSONObject.parseObject(shopsData);
        shopObjects.forEach((k, v) -> {
            MoutaiShop mtShop = JSONObject.parseObject(JSONObject.toJSONString(v), MoutaiShop.class);
            this.shopList.add(mtShop);
        });
        context.appendLog("门店列表获取成功，门店数:" + shopList.size());

    }




    /**
     * 执行动作
     *
     * @param context 任务上下文
     */
    @Override
    public void execute(TaskContext context) {
        try {
            this.context = context;
            // 读取配置
            String city = context.getString("city", "珠海市");
            String lat = context.getString("lat", "22.239936");
            String lng = context.getString("lng", "113.546405");
            String mobile = context.getString("mobile", "13928043230");
            String province = context.getString("province", "广东省");
            String token = context.getString("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTcwOTU2MDI0MCwidXNlcklkIjoxMTQ0ODU3ODE5LCJkZXZpY2VJZCI6IjJGMjA3NUQwLUI2NkMtNDI4Ny1BOTAzLURCRkY2MzU4MzQyQSIsImlhdCI6MTcwNjk2ODI0MH0.y3c0guxRnicwzrrMtj7Sw8_sc3OrvkU9MWP6PGwtFbY");
            String userid = context.getString("userid", "1144857819");
            String deviceId = context.getString("deviceId", "2F2075D0-B66C-4287-A903-DBFF6358342A");
            String[] itemCodes = context.getString("itemCodes", "10941,10942").split(",");
            // 读取ios的版本号
            this.initAppVersion();
            // 读取所有的门店
            this.initShops(lat, lng);
            // 初始化当前的sessionid
            this.initCurrentSessionId();
            // 初始化headers
            Map<String, String> headers = this.createHeaders(userid, token, lat, lng,deviceId);
            // 采购商品
            for(String itemCode: itemCodes) {
                String shopId = this.getBuyShopId(province, itemCode, city);
                this.reservation(userid, shopId, itemCode, headers);
            }
            // 小茅运
            this.getUserEnergyAward(token, deviceId, headers);
        }
        catch (Exception e) {
            throw new ActionException(e);
        }

    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    @Override
    public String name() {
        return "imoutai";
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    @Override
    public String description() {
        return "i茅台抢购";
    }
}
