package com.projecttango.DataStructure;

import android.util.Log;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import org.rajawali3d.math.vector.Vector3;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by
 * roberto on 25.06.16.
 */
public class PointDAOImpl extends BaseDaoImpl<Point, String> implements PointDAO{
    public PointDAOImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Point.class);
    }

    @Override
    public int create(Point data) throws SQLException {
        Vector3 v = data.getPosition();

        data.setX(v.x);
        data.setY(v.y);
        data.setZ(v.z);

        Dao<Point2Point, Integer> point2PointDao = null;

        try {
            DatabaseHelper helper = DatabaseHelper.getInstance();
            point2PointDao = helper.getPoint2PointDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*for(Point n : data.getNeighbours().keySet()){
            try {
                point2PointDao.create(new Point2Point(data, n));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/

        return super.create(data);
    }

    @Override
    public List<Point> queryForAll() throws SQLException {

        List<Point> list = super.queryForAll();

        for (Point p : list){
            p.setPosition();
            //List neighbours = Point2Point.getNeighbours(p);
        }

        return list;
    }
}
