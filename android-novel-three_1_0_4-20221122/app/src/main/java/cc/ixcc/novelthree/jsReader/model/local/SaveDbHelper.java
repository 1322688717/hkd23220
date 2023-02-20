package cc.ixcc.novelthree.jsReader.model.local;


import cc.ixcc.novelthree.jsReader.model.bean.AuthorBean;
import cc.ixcc.novelthree.jsReader.model.bean.BookCommentBean;
import cc.ixcc.novelthree.jsReader.model.bean.BookHelpfulBean;
import cc.ixcc.novelthree.jsReader.model.bean.BookHelpsBean;
import cc.ixcc.novelthree.jsReader.model.bean.BookReviewBean;
import cc.ixcc.novelthree.jsReader.model.bean.DownloadTaskBean;
import cc.ixcc.novelthree.jsReader.model.bean.ReviewBookBean;

import java.util.List;

/**
 * Created by newbiechen on 17-4-28.
 */

public interface SaveDbHelper {
    void saveBookComments(List<BookCommentBean> beans);
    void saveBookHelps(List<BookHelpsBean> beans);
    void saveBookReviews(List<BookReviewBean> beans);
    void saveAuthors(List<AuthorBean> beans);
    void saveBooks(List<ReviewBookBean> beans);
    void saveBookHelpfuls(List<BookHelpfulBean> beans);

//    void saveBookSortPackage(BookSortPackage bean);
//    void saveBillboardPackage(BillboardPackage bean);
    /*************DownloadTask*********************/
    void saveDownloadTask(DownloadTaskBean bean);
}
