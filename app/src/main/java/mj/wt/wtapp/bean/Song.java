package mj.wt.wtapp.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by wantao on 2017/3/9.
 */

@Entity
public class Song {
    @Id
    private Long id;
    public String song;
    public String singer;
    public String path;
    public int duration;
    public long size;
    public long progress;
    public long getProgress() {
        return this.progress;
    }
    public void setProgress(long progress) {
        this.progress = progress;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public int getDuration() {
        return this.duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getSinger() {
        return this.singer;
    }
    public void setSinger(String singer) {
        this.singer = singer;
    }
    public String getSong() {
        return this.song;
    }
    public void setSong(String song) {
        this.song = song;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 157028327)
    public Song(Long id, String song, String singer, String path, int duration,
            long size, long progress) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.path = path;
        this.duration = duration;
        this.size = size;
        this.progress = progress;
    }
    @Generated(hash = 87031450)
    public Song() {
    }
}
