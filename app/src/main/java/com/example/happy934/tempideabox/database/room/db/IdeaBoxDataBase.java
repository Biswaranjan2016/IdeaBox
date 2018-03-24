package com.example.happy934.tempideabox.database.room.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.happy934.tempideabox.database.room.Audio;
import com.example.happy934.tempideabox.database.room.Graphical;
import com.example.happy934.tempideabox.database.room.Tags;
import com.example.happy934.tempideabox.database.room.Textual;
import com.example.happy934.tempideabox.database.room.TextualTag;
import com.example.happy934.tempideabox.database.room.dao.AudioDao;
import com.example.happy934.tempideabox.database.room.dao.GraphicalDao;
import com.example.happy934.tempideabox.database.room.dao.TagsDao;
import com.example.happy934.tempideabox.database.room.dao.TextualDao;
import com.example.happy934.tempideabox.database.room.dao.TextualTagDao;

/**
 * Created by happy on 23/3/18.
 */
@Database(entities = {Textual.class, Audio.class, Graphical.class, Tags.class, TextualTag.class}, version = 6)
public abstract class IdeaBoxDataBase extends RoomDatabase{
    public abstract TextualDao textualDao();
    public abstract AudioDao audioDao();
    public abstract GraphicalDao graphicalDao();
    public abstract TagsDao tagsDao();
    public abstract TextualTagDao textualTagDao();
}

