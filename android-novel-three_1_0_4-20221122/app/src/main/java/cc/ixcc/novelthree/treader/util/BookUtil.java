package cc.ixcc.novelthree.treader.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import cc.ixcc.novelthree.bean.ChapterBean;
import cc.ixcc.novelthree.http.AllApi;
import cc.ixcc.novelthree.http.HttpCallback;
import cc.ixcc.novelthree.http.HttpClient;
import cc.ixcc.novelthree.treader.bean.Cache;
import cc.ixcc.novelthree.treader.db.BookList;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class BookUtil {
    private static final String cachedPath = Environment.getExternalStorageDirectory() + "/treader/";
    //存储的字符数
    public static final int cachedSize = 30000;
//    protected final ArrayList<WeakReference<char[]>> myArray = new ArrayList<>();

    protected final ArrayList<Cache> myArray = new ArrayList<>();
    //目录
//    private List<BookCatalogue> directoryList = new ArrayList<>();
    private List<ChapterBean.ChaptersBean> directoryList = new ArrayList<>();

    private String m_strCharsetName;
    private String bookName;
    private String bookPath;
    private long bookLen;
    private long position;
    private BookList bookList;

    public BookUtil() {
        File file = new File(cachedPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    boolean isFirst;

    public synchronized void openBook(BookList bookList, boolean isFirst) throws IOException {
        this.isFirst = isFirst;
        this.bookList = bookList;
        //如果当前缓存不是要打开的书本就缓存书本同时删除缓存

        if (bookPath == null || !bookPath.equals(bookList.getBookpath())) {
//            cleanCacheFile();
//            this.bookPath = bookList.getBookpath();
//            bookName = FileUtils.getFileName(bookPath);
            List list = new ArrayList();
            list.add(bookList);
            DataSupport.saveAll(list);
            cacheBook();
        }
    }

    private void cleanCacheFile() {
        File file = new File(cachedPath);
        if (!file.exists()) {
            file.mkdir();
        } else {
            File[] files = file.listFiles();
            if (files == null) return;
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    public int next(boolean back) {
        position += 1;
        if (position > bookLen) {
            position = bookLen;
            return -1;
        }
        char result = current();
        if (back) {
            position -= 1;
        }
        return result;
    }

    public char[] nextLine() {
        if (position >= bookLen) {
            return null;
        }
        String line = "";
        while (position < bookLen) {
            int word = next(false);
            if (word == -1) {
                break;
            }
            char wordChar = (char) word;
            if ((wordChar + "").equals("\r") && (((char) next(true)) + "").equals("\n")) {
                next(false);
                break;
            }
            line += wordChar;
        }
        return line.toCharArray();
    }

    public char[] preLine() {
        if (position <= 0) {
            return null;
        }
        String line = "";
        while (position >= 0) {
            int word = pre(false);
            if (word == -1) {
                break;
            }
            char wordChar = (char) word;
            if ((wordChar + "").equals("\n") && (((char) pre(true)) + "").equals("\r")) {
                pre(false);
//                line = "\r\n" + line;
                break;
            }
            line = wordChar + line;
        }
        return line.toCharArray();
    }

    public char current() {
//        int pos = (int) (position % cachedSize);
//        int cachePos = (int) (position / cachedSize);
        int cachePos = 0;
        int pos = 0;
        int len = 0;
        for (int i = 0; i < myArray.size(); i++) {
            long size = myArray.get(i).getSize();
            if (size + len - 1 >= position) {
                cachePos = i;
                pos = (int) (position - len);
                break;
            }
            len += size;
        }

        char[] charArray = block(cachePos);
        return charArray[pos];
//        char[] block = myArray.get(0).getData().get();
//        return block[]0;
    }

    public int pre(boolean back) {
        position -= 1;
        if (position < 0) {
            position = 0;
            return -1;
        }
        char result = current();
        if (back) {
            position += 1;
        }
        return result;
    }

    public long getPosition() {
        return position;
    }

    public void setPostition(long position) {
        this.position = position;
    }

    public void getChapterList(int anime_id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("anime_id", anime_id);

        HttpClient.getInstance().post(AllApi.bookchapter, AllApi.bookchapter)
                .isMultipart(true)
                .params("anime_id", anime_id)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        directoryList.clear();
                        ChapterBean resultBean = new Gson().fromJson(info[0], ChapterBean.class);
                        directoryList.addAll(resultBean.getChapters());
                        PageFactory.getInstance().refresh();
                    }
                });
    }

    //缓存书本
    private void cacheBook() throws IOException {
//        if (TextUtils.isEmpty(bookList.getCharset())) {
//            m_strCharsetName = FileUtils.getCharset(bookPath);
//            if (m_strCharsetName == null) {
//                m_strCharsetName = "utf-8";
//            }
//            ContentValues values = new ContentValues();
//            values.put("charset",m_strCharsetName);
//            DataSupport.update(BookList.class,values,bookList.getId());
//        }else{
//            m_strCharsetName = bookList.getCharset();
//        }
//        m_strCharsetName = bookList.getCharset();
//        File file = new File(bookPath);
//        InputStreamReader reader = new InputStreamReader(new FileInputStream(file),m_strCharsetName);
        int index = 0;
        bookLen = 0;
//        directoryList.clear();
        myArray.clear();
        Log.e("Book", "myArray clear");
//        while (true){
        char[] buf = new char[cachedSize];
//            int result = reader.read(buf);
//            if (result == -1){
//                reader.close();
//                break;
//            }

//        String bufStr = stripHtml("上一页测试数据" + bookList.getInfo() + "当前页测试数据" + bookList.getInfo() + "下一页测试数据" + bookList.getInfo());
        String bufStr = stripHtml(bookList.getInfo());
//            bufStr = bufStr.replaceAll("\r\n","\r\n\u3000\u3000");
//            bufStr = bufStr.replaceAll("\u3000\u3000+[ ]*","\u3000\u3000");
//            bufStr = bufStr.replaceAll("\r\n+\\s*","\r\n\u3000\u3000");
//            bufStr = bufStr.replaceAll("\r\n[ {0,}]","\r\n\u3000\u3000");
//            bufStr = bufStr.replaceAll(" ","");
//            bufStr = bufStr.replaceAll("\u0000","");
        buf = bufStr.toCharArray();
        bookLen += buf.length;

        Cache cache = new Cache();
        cache.setSize(buf.length);
        cache.setData(buf);

//            bookLen += result;
        myArray.add(cache);
        Log.e("Book", "myArray add: " + myArray.size());
//            myArray.add(new WeakReference<char[]>(buf));
//            myArray.set(index,);
//            try {
//                File cacheBook = new File(fileName(index));
//                if (!cacheBook.exists()){
//                    cacheBook.createNewFile();
//                }
//                final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName(index)), "UTF-16LE");
//                writer.write(buf);
//                writer.close();
//            } catch (IOException e) {
//                throw new RuntimeException("Error during writing " + fileName(index));
//            }
//            index ++;
//        }

        if (isFirst) {
            new Thread() {
                @Override
                public void run() {
                    getChapterList(bookList.getAnid());
                }
            }.start();
        }
    }

    public static String stripHtml(String content) {
        if (TextUtils.isEmpty(content)) return content;
        // <p>段落替换为换行
        content = content.replaceAll("<p.*?>", "\r\n");
        content = content.replaceAll("\n", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        // 还原HTML
        // content = HTMLDecoder.decode(content);
        //&ldquo;&quot;&nbsp;
        content = content.replaceAll("&.dquo;", "\"");
        content = content.replaceAll("&nbsp;", " ");
        return content;
    }
//
//    //缓存书本
//    private void cacheBook() throws IOException {
//        if (TextUtils.isEmpty(bookList.getCharset())) {
//            m_strCharsetName = FileUtils.getCharset(bookPath);
//            if (m_strCharsetName == null) {
//                m_strCharsetName = "utf-8";
//            }
//            ContentValues values = new ContentValues();
//            values.put("charset",m_strCharsetName);
//            DataSupport.update(BookList.class,values,bookList.getId());
//        }else{
//            m_strCharsetName = bookList.getCharset();
//        }
//        m_strCharsetName = bookList.getCharset();
//        File file = new File(bookPath);
//        InputStreamReader reader = new InputStreamReader(new FileInputStream(file),m_strCharsetName);
//        int index = 0;
//        bookLen = 0;
//        directoryList.clear();
//        myArray.clear();
//        while (true){
//            char[] buf = new char[cachedSize];
//            int result = reader.read(buf);
//            if (result == -1){
//                reader.close();
//                break;
//            }
//
//            String bufStr = new String(buf);
////            bufStr = bufStr.replaceAll("\r\n","\r\n\u3000\u3000");
////            bufStr = bufStr.replaceAll("\u3000\u3000+[ ]*","\u3000\u3000");
//            bufStr = bufStr.replaceAll("\r\n+\\s*","\r\n\u3000\u3000");
////            bufStr = bufStr.replaceAll("\r\n[ {0,}]","\r\n\u3000\u3000");
////            bufStr = bufStr.replaceAll(" ","");
//            bufStr = bufStr.replaceAll("\u0000","");
//            buf = bufStr.toCharArray();
//            bookLen += buf.length;
//
//            Cache cache = new Cache();
//            cache.setSize(buf.length);
//            cache.setData(new WeakReference<char[]>(buf));
//
////            bookLen += result;
//            myArray.add(cache);
////            myArray.add(new WeakReference<char[]>(buf));
////            myArray.set(index,);
//            try {
//                File cacheBook = new File(fileName(index));
//                if (!cacheBook.exists()){
//                    cacheBook.createNewFile();
//                }
//                final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName(index)), "UTF-16LE");
//                writer.write(buf);
//                writer.close();
//            } catch (IOException e) {
//                throw new RuntimeException("Error during writing " + fileName(index));
//            }
//            index ++;
//        }
//
//        new Thread(){
//            @Override
//            public void run() {
//                getChapter();
//            }
//        }.start();
//    }

    //获取章节
//    public synchronized void getChapter() {
//        try {
//            long size = 0;
//            for (int i = 0; i < myArray.size(); i++) {
//                char[] buf = block(i);
//                String bufStr = new String(buf);
//                String[] paragraphs = bufStr.split("\r\n");
//                for (int j = 0; j < paragraphs.length; j++) {
//                    String str = paragraphs[j];
////                    if (str.length() <= 30 && (str.matches(".*第.{1,8}章.*") || str.matches(".*第.{1,8}节.*"))) {
//                    BookCatalogue bookCatalogue = new BookCatalogue();
//                    bookCatalogue.setBookCatalogueStartPos(size);
////                        bookCatalogue.setBookCatalogue(str);
//                    bookCatalogue.setBookCatalogue("测试章节");
//                    bookCatalogue.setBookpath(bookPath);
//                    directoryList.add(bookCatalogue);
////                    }
//                    if (str.contains("\u3000\u3000")) {
//                        size += str.length() + 2;
//                    } else if (str.contains("\u3000")) {
//                        size += str.length() + 1;
//                    } else {
//                        size += str.length();
//                    }
//            }
//        }
//    }catch(
//    Exception e)
//
//    {
//        e.printStackTrace();
//    }
//}

    public List<ChapterBean.ChaptersBean> getDirectoryList() {
        return directoryList;
    }

    public long getBookLen() {
        return bookLen;
    }

    protected String fileName(int index) {
        return cachedPath + bookName + index;
    }

    //获取书本缓存
    public char[] block(int index) {
        Log.e("Book", "block: " + myArray.size() + "___index" + index);
        if (myArray.size() == 0) {
            return new char[1];
        }
//        Log.e("Book", "block: " + myArray.size() + "___index" + index);
        char[] block = myArray.get(index).getData();
        if (block == null) {
            try {
//                Log.e("Book2", "block: " + myArray.size() + "___index" + index + "___length" + block.length);
                File file = new File(fileName(index));
                int size = (int) file.length();
                if (size < 0) {
                    throw new RuntimeException("Error during reading " + fileName(index));
                }
                block = new char[size / 2];
                InputStreamReader reader =
                        new InputStreamReader(
                                new FileInputStream(file),
                                "UTF-16LE"
                        );
                if (reader.read(block) != block.length) {
                    throw new RuntimeException("Error during reading " + fileName(index));
                }
                reader.close();
            } catch (IOException e) {
//                throw new RuntimeException("Error during reading " + fileName(index));
            }
            block = new char[0];
            Cache cache = myArray.get(index);
            cache.setData(block);
//            myArray.set(index, new WeakReference<char[]>(block));
        }
        return block;
    }

}
