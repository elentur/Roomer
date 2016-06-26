package com.projecttango.DataStructure;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by roberto on 25.06.16.
 */
interface PointDAO extends Dao<Point, String> {
    public int create(Point data) throws SQLException;
    public List<Point> queryForAll() throws SQLException;
}
