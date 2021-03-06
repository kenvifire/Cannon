import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Semaphore;


/**
 * Created by kenvi on 15/11/11.
 */
public class Main {

    private static Map<String,User> users = new HashMap<String, User>();

    private static StratergyEnum currentStragergy;
    private static User currentUser;
    static {
        User kenvi = new User("kenvi","*****",StratergyEnum.NORMAL);
        users.put("kenvi", kenvi);

        User ph13175496035 = new User("ph13175496035","***", StratergyEnum.NORMAL);
        users.put("ph13175496035",ph13175496035);

        User zh13148455976 = new User("zh13148455976","*****", StratergyEnum.NORMAL);
        users.put("zh13148455976",zh13148455976);


    }
    private String name;
//    public static void main(String[] args) throws Exception{
//        Test test = new Test();
//        test.setName("111");
//        System.out.println(test.getName());
//
//        Test test2 = new Test();
//        test2.setName("222");
//        System.out.println(test2.getName());
//
////        Class.forName("com.mysql.jdbc.Driver");
////        final String USER = "test";
////        final String PASS = "test";
////        System.out.println("Connecting to database...");
////        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true", USER, PASS);
////        Statement st = conn.createStatement();
////
////        while(true) {
////            ResultSet rs = st.executeQuery("select * from userToken where isValid = 0 limit 1;");
////            if (rs.next()) {
////                String name = rs.getString("name");
////                String code = rs.getString("code");
////                login(name, code, conn);
////                Thread.sleep(1000);
////            }
////        }
//
//
//
//    }
    //http://fzwjt.com/Login/Register?backurl=http%3A%2F%2Ffzwjt.com%2FCourse%2FPlay%3FcId%3D209%26vId%3D2054%26uid%3D29733
    private static Connection connection = null;
    public static void main(String args[]) throws Exception {
        init();
        //dumpCourseInfo();
       //currentUser = users.get("Yjx1318810");
        //currentUser = users.get("zhangyang067");
       //currentUser = users.get("ashen92637");
        //currentUser = users.get("zhangdalei");
       //currentUser = users.get("gaofei01");
        //currentUser = users.get("kenvi");
        //currentUser = users.get("pho15872398212");
         //currentUser = users.get("akkfafa");
        //currentUser = users.get("kafka");
        currentUser = users.get("ph13175496035");
       // currentUser = users.get("zh13148455976");

        currentStragergy = currentUser.getStratergyEnum();
        finishCourse();


    }

    public static void init() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        final String USER = "test";
        final String PASS = "test";
        System.out.println("Connecting to database...");
        connection  = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true", USER, PASS);

        System.out.println("db connected.");
    }



    public static void changePass(String username, String passCode) {


    }


    public static void login(String username, String passCode,Connection connection) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpContext httpContext = new BasicHttpContext();
        CookieStore cookieStore = new BasicCookieStore();
//        BasicClientCookie basicCookie = new BasicClientCookie("VIMAGE","CAF47FF65D06F1BAB823D9261D9A2DE3");
//        basicCookie.setDomain("www.fzwjt.com");
//        basicCookie.setPath("/");
//        cookieStore.addCookie(basicCookie);
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        if(passCode != null) {
            System.out.println("changing pass");
            HttpPost passPost = new HttpPost("http://www.fzwjt.com/Login/ForgetPasswordThrid?code=" + passCode);
            List<NameValuePair> passParams = new ArrayList<NameValuePair>();
            passParams.add(new BasicNameValuePair("UserName", username));
            passParams.add(new BasicNameValuePair("ConfirmPassword", "123456"));
            passParams.add(new BasicNameValuePair("NewPassword", "123456"));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(passParams, Consts.UTF_8);
            passPost.setEntity(formEntity);

            CloseableHttpResponse repse = httpclient.execute(passPost, httpContext);
            dump(repse.getEntity());
        }


        HttpPost httpPost = new HttpPost("http://www.fzwjt.com/Login");


        HttpGet vimage = new HttpGet("http://www.fzwjt.com/Login/ValidateImage");
        CloseableHttpResponse response = httpclient.execute(vimage, httpContext);
        HttpEntity entity = response.getEntity();
        String uuid = UUID.randomUUID().toString();
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/"+uuid +".png");
        entity.writeTo(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        response.getStatusLine().getStatusCode();


        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("UserName", username));
        formparams.add(new BasicNameValuePair("Password", "123456"));

        System.out.print("请输入验证码:");
        String code = null;
        Semaphore semaphore = new Semaphore(1);
        Drawing frame = new Drawing(uuid,semaphore);

        semaphore.acquire();
        semaphore.release();
        code = frame.getCode();
        frame.close();

        formparams.add(new BasicNameValuePair("ValidatorImage", code));

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

        httpPost.setEntity(formEntity);

        CloseableHttpResponse postResp = httpclient.execute(httpPost, httpContext);

        HttpEntity postEntity = postResp.getEntity();

        dump(postEntity);

        boolean vote = false;
        do {
            HttpPost votePost = new HttpPost("http://www.fzwjt.com/Activity2015/AddVote");
            List<NameValuePair> voteParams = new ArrayList<NameValuePair>();
            voteParams.add(new BasicNameValuePair("userId", "5414"));
            UrlEncodedFormEntity voteEntity = new UrlEncodedFormEntity(voteParams, Consts.UTF_8);
            votePost.setEntity(voteEntity);


            CloseableHttpResponse voteResp = httpclient.execute(votePost, httpContext);
            HttpEntity voteRespEntity = voteResp.getEntity();

            BufferedReader voteBr = new BufferedReader(new InputStreamReader(voteRespEntity.getContent()));

            String line = null;
            while ((line = voteBr.readLine()) != null) {
                System.out.println(line);
                if(line.contains("v")) vote=true;
                else vote = false;
            }
            long mill = (long)Math.floor(Math.random()*300);
            Thread.sleep(mill);

        }while (vote);


        //logout

        HttpGet logout = new HttpGet("http://www.fzwjt.com/Login/Logout");
        httpclient.execute(logout,httpContext);

        String updateSql  = "update userToken set isValid = 1 where name = \"%s\";";

        Statement st = connection.createStatement();
        st.execute(String.format(updateSql,username));

    }

    public static void vote() {
        synchronized (Main.class) {

        }
    }

    public static void logout() {

    }

    public static void addRedisRecord(){

    }

    public static void PostHDStudyLog() {

    }

    public static void saveVideoInfo(VideoInfo videoInfo) throws Exception {
        PreparedStatement pstmt = connection.prepareStatement("insert into courseInfo(catalogType,parentId,courseId,videoId,status) VALUES (?,?,?,?,0)");
        pstmt.setInt(1, videoInfo.getCatalogType());
        pstmt.setInt(2, videoInfo.getParentId());
        pstmt.setInt(3, videoInfo.getCourseId());
        pstmt.setInt(4, videoInfo.getVideoId());

        pstmt.execute();
    }

    public static void dump(HttpEntity entity) throws IOException{
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));

        while((line = br.readLine()) != null) {
            System.out.println(line);
        }

        br.close();

    }
    private static class Test{
        private String name = null;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }


    }

    public static void dumpCourseInfo() throws  Exception{
        int catalogType = 3;
        for (int i=8;i <=18; i++) {
            System.out.println("start to process:" + i );
            parsePageInfo(catalogType, i);
            System.out.println("finished  process:" + i);
        }
    }

    public static void parsePageInfo(int catelogType, int parentId) throws Exception{
        int pages = getPage(catelogType, parentId);

        for (int i=1; i<= pages; i++) {
            System.out.println("\t start to process page:" + i);
            List<Integer> courseIdList = getCourseList(catelogType, parentId, i);
            for (Integer courseId : courseIdList) {
                List<VideoInfo> videoInfoList = parserCoursePage(catelogType,parentId, courseId);

                for(VideoInfo videoInfo : videoInfoList) {
                    saveVideoInfo(videoInfo);
                }
            }
            System.out.println("\tfinish page:" + i);
        }

    }

    public static List<Integer> getCourseList(int catelogType, int parentId, int pageId) {
        String pageUrl = "http://fzwjt.com/Course?catalogType=" + catelogType + "&parentId=" + parentId +"&filterCount=0&page=" + pageId;
        System.out.println(String.format("\t\tget course list for:parentId[%s],pageId[%s]",parentId,pageId));
        List<Integer> courseIdList = new ArrayList<Integer>();
        try {
            Document document = Jsoup.connect(pageUrl).timeout(10000).get();

            Elements pageList = document.select(".videoUl a");

            for (Element element : pageList) {
                String url = element.attr("href");
                Integer courseId = Integer.valueOf(url.substring(url.lastIndexOf("/")+1, url.length()));
                courseIdList.add(courseId);
            }


            return courseIdList;
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("error loading ..." + pageUrl);
            throw new RuntimeException(e.getMessage() + pageUrl);
        }
    }

    public static int getPage(int catelogType, int parentId) {
        String categoryUrl = "http://fzwjt.com/Course?catalogType=" + catelogType+ "&parentId=" + parentId + "&filterCount=0";
        try {
            Document document = Jsoup.connect(categoryUrl).timeout(10000).get();

            Elements pageList = document.select(".PageBar41 a");
            if(pageList.size()>2) {
                return pageList.size() - 2;
            }else return 1;

        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("error loading ..." + categoryUrl);
            throw new RuntimeException(e.getMessage() + categoryUrl);
        }


    }

    public static List<VideoInfo> parserCoursePage(int catelogType, int parentId, int courseId) {
        String courseUrl = "http://fzwjt.com/Course/Detail/" + courseId;
        System.out.println("\t\t\t parsing course:" + courseId);
        List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();

        try {

            Document document = null;
            int retryCount = 0;
            do{
                try {
                    document = Jsoup.connect(courseUrl).timeout(10000).get();
                    break;
                }catch (Exception e) {
                    if(retryCount < 3) {
                        System.out.println("exception occurred, retrying");
                    }else {
                        throw  e;
                    }
                }
                retryCount++;
            }while(retryCount < 3);

            if (document == null) {
                System.out.println(String.format("%s,%s,%s, not saved",catelogType,parentId,courseId));
                return Collections.emptyList();
            }

            Elements videoList = document.select(".courseListL li .add");

            for (Element element : videoList) {
                VideoInfo videoInfo = new VideoInfo();
                videoInfo.setCatalogType(catelogType);
                videoInfo.setParentId(parentId);
                videoInfo.setCourseId(courseId);
                videoInfo.setVideoId(Integer.valueOf(element.attr("data-videoid")));
                videoInfoList.add(videoInfo);
            }
            return videoInfoList;
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("error loading ..." + courseId);
            throw new RuntimeException(e.getMessage() + courseId);
        }


    }

    public static void addRecord(HttpClient httpClient, HttpContext httpContext,
                                 int courseId, int videoId){

        isOpen(httpClient,httpContext);

        int count =currentStragergy.getRedisCount();
        do {
         addUserRedis(httpClient, httpContext,videoId);
        }while(count-->0);
         postStudy(httpClient,httpContext,courseId,videoId);
       count = currentStragergy.getRedisCount();
        do {
           addUserRedis(httpClient, httpContext,videoId);
        }while(count-->0);

        try{
            Thread.sleep(200);
        }catch (Exception e) {

        }

        postHDStudy(httpClient,httpContext,courseId,videoId);


    }
    public static void addUserRedis(HttpClient httpClient, HttpContext httpContext, int videoId){
        try {
            System.out.println("add User Reids");
            HttpPost votePost = new HttpPost("http://fzwjt.com/Course/AddUserRedis");
            List<NameValuePair> voteParams = new ArrayList<NameValuePair>();
            voteParams.add(new BasicNameValuePair("vid", videoId + ""));
            UrlEncodedFormEntity voteEntity = new UrlEncodedFormEntity(voteParams, Consts.UTF_8);
            votePost.setEntity(voteEntity);
            HttpResponse response = httpClient.execute(votePost, httpContext);
            dump(response.getEntity());
            Thread.sleep(currentStragergy.getSleepTime());
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void postHDStudy(HttpClient httpClient, HttpContext httpContext,
                                   int courseId, int videId){
        System.out.println("post HD study");
        try {

            HttpPost votePost = new HttpPost("http://fzwjt.com/Course/PostHDStudy");
            List<NameValuePair> voteParams = new ArrayList<NameValuePair>();
            voteParams.add(new BasicNameValuePair("cid", courseId +""));
            voteParams.add(new BasicNameValuePair("vid", videId+""));
            Random random = new Random(10);
            voteParams.add(new BasicNameValuePair("sdt", 60 + random.nextInt(10) +""));
            UrlEncodedFormEntity voteEntity = new UrlEncodedFormEntity(voteParams, Consts.UTF_8);
            votePost.setEntity(voteEntity);
            HttpResponse response = httpClient.execute(votePost, httpContext);
            dump(response.getEntity());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postStudy(HttpClient httpClient, HttpContext httpContext,
                                   int courseId, int videId){
        System.out.println("post  study");
        try {

            HttpPost votePost = new HttpPost("http://fzwjt.com/Course/PostStudy");
            List<NameValuePair> voteParams = new ArrayList<NameValuePair>();
            voteParams.add(new BasicNameValuePair("cid", courseId +""));
            voteParams.add(new BasicNameValuePair("vid", videId+""));
            voteParams.add(new BasicNameValuePair("sp", 10+""));
            Random random = new Random(5);
            voteParams.add(new BasicNameValuePair("lt", 20 + random.nextInt(5) +""));
            UrlEncodedFormEntity voteEntity = new UrlEncodedFormEntity(voteParams, Consts.UTF_8);
            votePost.setEntity(voteEntity);
            HttpResponse resp =  httpClient.execute(votePost, httpContext);
            dump(resp.getEntity());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void finishCourse()  throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpContext httpContext = new BasicHttpContext();
        CookieStore cookieStore = new BasicCookieStore();
//        BasicClientCookie basicCookie = new BasicClientCookie("VIMAGE","CAF47FF65D06F1BAB823D9261D9A2DE3");
//        basicCookie.setDomain("www.fzwjt.com");
//        basicCookie.setPath("/");
//        cookieStore.addCookie(basicCookie);
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        HttpPost httpPost = new HttpPost("http://www.fzwjt.com/Login");


        HttpGet vimage = new HttpGet("http://www.fzwjt.com/Login/ValidateImage");
        CloseableHttpResponse response = httpclient.execute(vimage, httpContext);
        HttpEntity entity = response.getEntity();
        String uuid = UUID.randomUUID().toString();
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/"+uuid +".png");
        entity.writeTo(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        response.getStatusLine().getStatusCode();


        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        //formparams.add(new BasicNameValuePair("UserName", "zhuzhishu"));
        //formparams.add(new BasicNameValuePair("UserName", "zhangbei"));
        formparams.add(new BasicNameValuePair("UserName", currentUser.getUserName()));
        formparams.add(new BasicNameValuePair("Password", currentUser.getPasswd()));

        System.out.print("请输入验证码:");
        String code = null;
        Semaphore semaphore = new Semaphore(1);
        Drawing frame = new Drawing(uuid,semaphore);

        semaphore.acquire();
        semaphore.release();
        code = frame.getCode();
        frame.close();

        formparams.add(new BasicNameValuePair("ValidatorImage", code));

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

        httpPost.setEntity(formEntity);

        CloseableHttpResponse postResp = httpclient.execute(httpPost, httpContext);

        HttpEntity postEntity = postResp.getEntity();

        dump(postEntity);

//        HttpGet index = new HttpGet("http://fzwjt.com/");
//        HttpResponse indexResp = httpclient.execute(index,httpContext);
//        dump(indexResp.getEntity());

       // postHDStudy(httpclient,httpContext,352,3641);

//
        Statement st = connection.createStatement();
        while(true) {
            ResultSet rs = st.executeQuery("select * from courseInfo_" + currentUser.getUserName() + " where status = 0 order by null limit 1;");
            if (rs.next()) {
                int id = rs.getInt("id");
                int courseId = rs.getInt("courseId");
                int videoId = rs.getInt("videoId");
                System.out.println("executing["+courseId +"-" + videoId+"] for" + currentUser.getUserName());
                addRecord(httpclient, httpContext, courseId, videoId);

                st.execute("update courseInfo_" + currentUser.getUserName() + " set status=1 where id =" + id );
            }else {
                break;
            }
        }

    }
    public static void isOpen(HttpClient httpClient, HttpContext httpContext){
        try {

            HttpGet get = new HttpGet("http://fzwjt.com/Course/CheckUserIsOpen");
            HttpResponse response = httpClient.execute(get, httpContext);
            System.out.println("open status");
            dump(response.getEntity());
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
