package cc.ixcc.novelthree.jsReader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by newbiechen on 17-5-20.
 */
@Entity
public class BookRecordBean {
    //所属的书的id
    @Id
    private String bookId;
    //阅读到了第几章
    private int chapter;
    //当前的页码
    private int pagePos;
    //当前的页码
    private long playTime;
    @Generated(hash = 528614075)
    public BookRecordBean(String bookId, int chapter, int pagePos, long playTime) {
        this.bookId = bookId;
        this.chapter = chapter;
        this.pagePos = pagePos;
        this.playTime = playTime;
    }
    @Generated(hash = 398068002)
    public BookRecordBean() {
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public int getChapter() {
        return this.chapter;
    }
    public void setChapter(int chapter) {
        this.chapter = chapter;
    }
    public int getPagePos() {
        return this.pagePos;
    }
    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }
    public long getPlayTime() {
        return this.playTime;
    }
    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }
}
