package com.smartx.rfidreader.core.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class EventDao_Impl implements EventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EventEntity> __insertionAdapterOfEventEntity;

  private final EntityDeletionOrUpdateAdapter<EventEntity> __deletionAdapterOfEventEntity;

  private final EntityDeletionOrUpdateAdapter<EventEntity> __updateAdapterOfEventEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public EventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEventEntity = new EntityInsertionAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `rfid_events` (`id`,`deviceId`,`eventType`,`inventory_name`,`tagsJson`,`savedAt`,`gpsLat`,`gpsLng`,`hasGps`,`txPower`,`session`,`rssiFilter`,`prefixesJson`,`isSynced`,`syncedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDeviceId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDeviceId());
        }
        if (entity.getEventType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEventType());
        }
        if (entity.getInventoryName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getInventoryName());
        }
        if (entity.getTagsJson() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTagsJson());
        }
        if (entity.getSavedAt() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getSavedAt());
        }
        statement.bindDouble(7, entity.getGpsLat());
        statement.bindDouble(8, entity.getGpsLng());
        final int _tmp = entity.getHasGps() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getTxPower());
        statement.bindLong(11, entity.getSession());
        statement.bindLong(12, entity.getRssiFilter());
        if (entity.getPrefixesJson() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getPrefixesJson());
        }
        final int _tmp_1 = entity.isSynced() ? 1 : 0;
        statement.bindLong(14, _tmp_1);
        if (entity.getSyncedAt() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getSyncedAt());
        }
      }
    };
    this.__deletionAdapterOfEventEntity = new EntityDeletionOrUpdateAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rfid_events` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfEventEntity = new EntityDeletionOrUpdateAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `rfid_events` SET `id` = ?,`deviceId` = ?,`eventType` = ?,`inventory_name` = ?,`tagsJson` = ?,`savedAt` = ?,`gpsLat` = ?,`gpsLng` = ?,`hasGps` = ?,`txPower` = ?,`session` = ?,`rssiFilter` = ?,`prefixesJson` = ?,`isSynced` = ?,`syncedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDeviceId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDeviceId());
        }
        if (entity.getEventType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEventType());
        }
        if (entity.getInventoryName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getInventoryName());
        }
        if (entity.getTagsJson() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTagsJson());
        }
        if (entity.getSavedAt() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getSavedAt());
        }
        statement.bindDouble(7, entity.getGpsLat());
        statement.bindDouble(8, entity.getGpsLng());
        final int _tmp = entity.getHasGps() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getTxPower());
        statement.bindLong(11, entity.getSession());
        statement.bindLong(12, entity.getRssiFilter());
        if (entity.getPrefixesJson() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getPrefixesJson());
        }
        final int _tmp_1 = entity.isSynced() ? 1 : 0;
        statement.bindLong(14, _tmp_1);
        if (entity.getSyncedAt() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getSyncedAt());
        }
        statement.bindLong(16, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM rfid_events";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final EventEntity event, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfEventEntity.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final EventEntity event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final EventEntity event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EventEntity>> allFlow() {
    final String _sql = "SELECT * FROM rfid_events ORDER BY savedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rfid_events"}, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfInventoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "inventory_name");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfSavedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "savedAt");
          final int _cursorIndexOfGpsLat = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsLat");
          final int _cursorIndexOfGpsLng = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsLng");
          final int _cursorIndexOfHasGps = CursorUtil.getColumnIndexOrThrow(_cursor, "hasGps");
          final int _cursorIndexOfTxPower = CursorUtil.getColumnIndexOrThrow(_cursor, "txPower");
          final int _cursorIndexOfSession = CursorUtil.getColumnIndexOrThrow(_cursor, "session");
          final int _cursorIndexOfRssiFilter = CursorUtil.getColumnIndexOrThrow(_cursor, "rssiFilter");
          final int _cursorIndexOfPrefixesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "prefixesJson");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final String _tmpInventoryName;
            if (_cursor.isNull(_cursorIndexOfInventoryName)) {
              _tmpInventoryName = null;
            } else {
              _tmpInventoryName = _cursor.getString(_cursorIndexOfInventoryName);
            }
            final String _tmpTagsJson;
            if (_cursor.isNull(_cursorIndexOfTagsJson)) {
              _tmpTagsJson = null;
            } else {
              _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            }
            final String _tmpSavedAt;
            if (_cursor.isNull(_cursorIndexOfSavedAt)) {
              _tmpSavedAt = null;
            } else {
              _tmpSavedAt = _cursor.getString(_cursorIndexOfSavedAt);
            }
            final double _tmpGpsLat;
            _tmpGpsLat = _cursor.getDouble(_cursorIndexOfGpsLat);
            final double _tmpGpsLng;
            _tmpGpsLng = _cursor.getDouble(_cursorIndexOfGpsLng);
            final boolean _tmpHasGps;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfHasGps);
            _tmpHasGps = _tmp != 0;
            final int _tmpTxPower;
            _tmpTxPower = _cursor.getInt(_cursorIndexOfTxPower);
            final int _tmpSession;
            _tmpSession = _cursor.getInt(_cursorIndexOfSession);
            final int _tmpRssiFilter;
            _tmpRssiFilter = _cursor.getInt(_cursorIndexOfRssiFilter);
            final String _tmpPrefixesJson;
            if (_cursor.isNull(_cursorIndexOfPrefixesJson)) {
              _tmpPrefixesJson = null;
            } else {
              _tmpPrefixesJson = _cursor.getString(_cursorIndexOfPrefixesJson);
            }
            final boolean _tmpIsSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_1 != 0;
            final String _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getString(_cursorIndexOfSyncedAt);
            }
            _item = new EventEntity(_tmpId,_tmpDeviceId,_tmpEventType,_tmpInventoryName,_tmpTagsJson,_tmpSavedAt,_tmpGpsLat,_tmpGpsLng,_tmpHasGps,_tmpTxPower,_tmpSession,_tmpRssiFilter,_tmpPrefixesJson,_tmpIsSynced,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object pending(final Continuation<? super List<EventEntity>> $completion) {
    final String _sql = "SELECT * FROM rfid_events WHERE isSynced = 0 ORDER BY savedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfInventoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "inventory_name");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfSavedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "savedAt");
          final int _cursorIndexOfGpsLat = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsLat");
          final int _cursorIndexOfGpsLng = CursorUtil.getColumnIndexOrThrow(_cursor, "gpsLng");
          final int _cursorIndexOfHasGps = CursorUtil.getColumnIndexOrThrow(_cursor, "hasGps");
          final int _cursorIndexOfTxPower = CursorUtil.getColumnIndexOrThrow(_cursor, "txPower");
          final int _cursorIndexOfSession = CursorUtil.getColumnIndexOrThrow(_cursor, "session");
          final int _cursorIndexOfRssiFilter = CursorUtil.getColumnIndexOrThrow(_cursor, "rssiFilter");
          final int _cursorIndexOfPrefixesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "prefixesJson");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final String _tmpInventoryName;
            if (_cursor.isNull(_cursorIndexOfInventoryName)) {
              _tmpInventoryName = null;
            } else {
              _tmpInventoryName = _cursor.getString(_cursorIndexOfInventoryName);
            }
            final String _tmpTagsJson;
            if (_cursor.isNull(_cursorIndexOfTagsJson)) {
              _tmpTagsJson = null;
            } else {
              _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            }
            final String _tmpSavedAt;
            if (_cursor.isNull(_cursorIndexOfSavedAt)) {
              _tmpSavedAt = null;
            } else {
              _tmpSavedAt = _cursor.getString(_cursorIndexOfSavedAt);
            }
            final double _tmpGpsLat;
            _tmpGpsLat = _cursor.getDouble(_cursorIndexOfGpsLat);
            final double _tmpGpsLng;
            _tmpGpsLng = _cursor.getDouble(_cursorIndexOfGpsLng);
            final boolean _tmpHasGps;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfHasGps);
            _tmpHasGps = _tmp != 0;
            final int _tmpTxPower;
            _tmpTxPower = _cursor.getInt(_cursorIndexOfTxPower);
            final int _tmpSession;
            _tmpSession = _cursor.getInt(_cursorIndexOfSession);
            final int _tmpRssiFilter;
            _tmpRssiFilter = _cursor.getInt(_cursorIndexOfRssiFilter);
            final String _tmpPrefixesJson;
            if (_cursor.isNull(_cursorIndexOfPrefixesJson)) {
              _tmpPrefixesJson = null;
            } else {
              _tmpPrefixesJson = _cursor.getString(_cursorIndexOfPrefixesJson);
            }
            final boolean _tmpIsSynced;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_1 != 0;
            final String _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getString(_cursorIndexOfSyncedAt);
            }
            _item = new EventEntity(_tmpId,_tmpDeviceId,_tmpEventType,_tmpInventoryName,_tmpTagsJson,_tmpSavedAt,_tmpGpsLat,_tmpGpsLng,_tmpHasGps,_tmpTxPower,_tmpSession,_tmpRssiFilter,_tmpPrefixesJson,_tmpIsSynced,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> pendingCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM rfid_events WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rfid_events"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> totalCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM rfid_events";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rfid_events"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
