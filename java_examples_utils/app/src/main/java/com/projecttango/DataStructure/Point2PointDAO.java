package com.projecttango.DataStructure;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by roberto on 25.06.16.
 */
interface Point2PointDAO extends Dao<Point2Point, String> {
    public List<Point2Point> queryForAll() throws SQLException;
}
