package com.projecttango.DataStructure;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import org.rajawali3d.math.vector.Vector3;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by
 * roberto on 25.06.16.
 */
public class Point2PointDAOImpl extends BaseDaoImpl<Point2Point, String> implements Point2PointDAO{
    public Point2PointDAOImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Point2Point.class);
    }

    @Override
    public List<Point2Point> queryForAll() throws SQLException {

        List<Point2Point> list = super.queryForAll();

        for (Point2Point p2p : list){
            p2p.setPosition();
            //List neighbours = Point2Point.getNeighbours(p);
        }

        return list;
    }
}
