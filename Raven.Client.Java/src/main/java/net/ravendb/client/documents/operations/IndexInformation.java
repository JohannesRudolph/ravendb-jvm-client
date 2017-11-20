package net.ravendb.client.documents.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.ravendb.client.documents.indexes.IndexLockMode;
import net.ravendb.client.documents.indexes.IndexPriority;
import net.ravendb.client.documents.indexes.IndexState;
import net.ravendb.client.documents.indexes.IndexType;

import java.util.Date;

public class IndexInformation {
    private long etag;
    private String name;

    private boolean isStale;
    private IndexState state;
    private IndexLockMode lockMode;
    private IndexPriority priority;
    private IndexType type;
    private Date lastIndexingTime;

    public long getEtag() {
        return etag;
    }

    public void setEtag(long etag) {
        this.etag = etag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsStale() {
        return isStale;
    }

    public void setIsStale(boolean stale) {
        this.isStale = stale;
    }

    public IndexState getState() {
        return state;
    }

    public void setState(IndexState state) {
        this.state = state;
    }

    public IndexLockMode getLockMode() {
        return lockMode;
    }

    public void setLockMode(IndexLockMode lockMode) {
        this.lockMode = lockMode;
    }

    public IndexPriority getPriority() {
        return priority;
    }

    public void setPriority(IndexPriority priority) {
        this.priority = priority;
    }

    public IndexType getType() {
        return type;
    }

    public void setType(IndexType type) {
        this.type = type;
    }

    public Date getLastIndexingTime() {
        return lastIndexingTime;
    }

    public void setLastIndexingTime(Date lastIndexingTime) {
        this.lastIndexingTime = lastIndexingTime;
    }
}
