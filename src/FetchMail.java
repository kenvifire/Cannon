/**
 * Created by kenvi on 15/11/12.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class FetchMail {
    public static final Pattern namePattern = Pattern.compile("<b>([^,]*),</b><");
    public static final Pattern codePattern = Pattern.compile("code=([^\"]*)</a>");

    public static void main(String[] args) throws  Exception{
        String protocol = "pop3";
        boolean isSSL = true;
        String host = "pop.163.com";
        int port = 995;
        String username = "io_java@163.com";
        String password = "cternrpsdrnlroem";

        Properties props = new Properties();
        props.put("mail.pop3.ssl.enable", isSSL);
        props.put("mail.pop3.host", host);
        props.put("mail.pop3.port", port);

        Session session = Session.getDefaultInstance(props);

        Class.forName("com.mysql.jdbc.Driver");
        final String USER = "test";
        final String PASS = "test";
        System.out.println("Connecting to database...");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true", USER, PASS);
        Statement st = conn.createStatement();
        String sql = "insert into userToken values(\"%s\",\"%s\",\"%s\");";
        String querySql = "select count(*)cnt from userToken where name=\"%s\";";

        Store store = null;
        Folder folder = null;
        try {
            store = session.getStore(protocol);
            store.connect(username, password);

            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            int size = folder.getMessageCount();
            for(int i=1;i<=size;i++) {
                String name = null;
                String code = null;
                Message message = folder.getMessage(i);


                String from = message.getFrom()[0].toString();
                String subject = message.getSubject();
                Date date = message.getSentDate();
                BufferedReader br = new BufferedReader(new InputStreamReader(message.getInputStream()));
                String line;
                StringBuffer content = new StringBuffer();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                }


                Matcher nameMatcher = namePattern.matcher(content);
                if (nameMatcher.find()) {
                    name = nameMatcher.group(1);
                }

                Matcher codeMatcher = codePattern.matcher(content);
                if (codeMatcher.find()) {
                    code = codeMatcher.group(1);
                }

//                ResultSet rs = st.executeQuery(querySql);
//                boolean hasValue = false;
//                if(rs.next()) {
//                   hasValue = rs.getInt("cnt")>1;
//                }

                if(name != null && code != null) {
                    st.execute(String.format(sql,name,code,"0"));
                }
            }

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (folder != null) {
                    folder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        System.out.println("接收完毕！");
    }
}
