package cc.ixcc.novelthree.jsReader.model.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cc.ixcc.novelthree.jsReader.model.bean.BookHelpfulBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BOOK_HELPFUL_BEAN".
*/
public class BookHelpfulBeanDao extends AbstractDao<BookHelpfulBean, String> {

    public static final String TABLENAME = "BOOK_HELPFUL_BEAN";

    /**
     * Properties of entity BookHelpfulBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, String.class, "_id", true, "_ID");
        public final static Property Total = new Property(1, int.class, "total", false, "TOTAL");
        public final static Property No = new Property(2, int.class, "no", false, "NO");
        public final static Property Yes = new Property(3, int.class, "yes", false, "YES");
    }


    public BookHelpfulBeanDao(DaoConfig config) {
        super(config);
    }
    
    public BookHelpfulBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BOOK_HELPFUL_BEAN\" (" + //
                "\"_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: _id
                "\"TOTAL\" INTEGER NOT NULL ," + // 1: total
                "\"NO\" INTEGER NOT NULL ," + // 2: no
                "\"YES\" INTEGER NOT NULL );"); // 3: yes
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BOOK_HELPFUL_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BookHelpfulBean entity) {
        stmt.clearBindings();
 
        String _id = entity.get_id();
        if (_id != null) {
            stmt.bindString(1, _id);
        }
        stmt.bindLong(2, entity.getTotal());
        stmt.bindLong(3, entity.getNo());
        stmt.bindLong(4, entity.getYes());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BookHelpfulBean entity) {
        stmt.clearBindings();
 
        String _id = entity.get_id();
        if (_id != null) {
            stmt.bindString(1, _id);
        }
        stmt.bindLong(2, entity.getTotal());
        stmt.bindLong(3, entity.getNo());
        stmt.bindLong(4, entity.getYes());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public BookHelpfulBean readEntity(Cursor cursor, int offset) {
        BookHelpfulBean entity = new BookHelpfulBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // _id
            cursor.getInt(offset + 1), // total
            cursor.getInt(offset + 2), // no
            cursor.getInt(offset + 3) // yes
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BookHelpfulBean entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTotal(cursor.getInt(offset + 1));
        entity.setNo(cursor.getInt(offset + 2));
        entity.setYes(cursor.getInt(offset + 3));
     }
    
    @Override
    protected final String updateKeyAfterInsert(BookHelpfulBean entity, long rowId) {
        return entity.get_id();
    }
    
    @Override
    public String getKey(BookHelpfulBean entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BookHelpfulBean entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
