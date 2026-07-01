package com.smartx.rfidreader.core.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile EventDao _eventDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `rfid_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deviceId` TEXT NOT NULL, `eventType` TEXT NOT NULL, `inventory_name` TEXT NOT NULL, `tagsJson` TEXT NOT NULL, `savedAt` TEXT NOT NULL, `gpsLat` REAL NOT NULL, `gpsLng` REAL NOT NULL, `hasGps` INTEGER NOT NULL, `txPower` INTEGER NOT NULL, `session` INTEGER NOT NULL, `rssiFilter` INTEGER NOT NULL, `prefixesJson` TEXT NOT NULL, `isSynced` INTEGER NOT NULL, `syncedAt` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0fbf10caf58b78942d9a58a81fec4f2f')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `rfid_events`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsRfidEvents = new HashMap<String, TableInfo.Column>(15);
        _columnsRfidEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("deviceId", new TableInfo.Column("deviceId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("eventType", new TableInfo.Column("eventType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("inventory_name", new TableInfo.Column("inventory_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("tagsJson", new TableInfo.Column("tagsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("savedAt", new TableInfo.Column("savedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("gpsLat", new TableInfo.Column("gpsLat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("gpsLng", new TableInfo.Column("gpsLng", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("hasGps", new TableInfo.Column("hasGps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("txPower", new TableInfo.Column("txPower", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("session", new TableInfo.Column("session", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("rssiFilter", new TableInfo.Column("rssiFilter", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("prefixesJson", new TableInfo.Column("prefixesJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("syncedAt", new TableInfo.Column("syncedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRfidEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRfidEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRfidEvents = new TableInfo("rfid_events", _columnsRfidEvents, _foreignKeysRfidEvents, _indicesRfidEvents);
        final TableInfo _existingRfidEvents = TableInfo.read(db, "rfid_events");
        if (!_infoRfidEvents.equals(_existingRfidEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "rfid_events(com.smartx.rfidreader.core.db.EventEntity).\n"
                  + " Expected:\n" + _infoRfidEvents + "\n"
                  + " Found:\n" + _existingRfidEvents);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0fbf10caf58b78942d9a58a81fec4f2f", "7d10af80993d48883e0d9460fedb62d9");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "rfid_events");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `rfid_events`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(EventDao.class, EventDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public EventDao eventDao() {
    if (_eventDao != null) {
      return _eventDao;
    } else {
      synchronized(this) {
        if(_eventDao == null) {
          _eventDao = new EventDao_Impl(this);
        }
        return _eventDao;
      }
    }
  }
}
