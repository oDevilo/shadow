package org.devil.shadow.support;

import javax.sql.DataSource;

/**
 * Created by devil on 2017/8/7.
 */
public class Shard {
    private String id;
    private DataSource dataSource;
    private String description;

    public Shard() {}

    public Shard(String id, DataSource dataSource, String description) {
        this.id = id;
        this.dataSource = dataSource;
        this.description = description;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Shard{" +
                "id='" + id + '\'' +
                ", dataSource=" + dataSource +
                ", description='" + description + '\'' +
                '}';
    }
}
